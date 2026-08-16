package com.grocery.billing.ui.billing

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.grocery.billing.billing.BillCalculator
import com.grocery.billing.billing.ProductRanker
import com.grocery.billing.data.entity.Product
import com.grocery.billing.data.entity.SettingsKeys
import com.grocery.billing.data.repository.BillItemDraft
import com.grocery.billing.data.repository.BillRepository
import com.grocery.billing.data.repository.HeldBillItemDraft
import com.grocery.billing.data.repository.HeldBillRepository
import com.grocery.billing.data.repository.ProductRepository
import com.grocery.billing.data.repository.SettingsRepository
import com.grocery.billing.money.Money
import com.grocery.billing.util.BillNumbers
import com.grocery.billing.util.Dates
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.math.BigDecimal

data class DraftItem(
    val key: Long,
    val productId: String?,
    val productName: String,
    val quantity: String,
    val ratePaise: Long,
    val amountPaise: Long
)

data class BillingPriceOption(
    val pricePaise: Long,
    val unit: String
)

data class BillingUiState(
    val billNumber: String = "",
    val billDate: String = "",
    val billTime: String = "",
    val items: List<DraftItem> = emptyList(),
    val subtotalPaise: Long = 0L,
    val discountText: String = "",
    val discountPaise: Long = 0L,
    val totalPaise: Long = 0L,
    val searchQuery: String = "",
    val searchResults: List<Product> = emptyList(),
    val searching: Boolean = false,
    val recentProducts: List<Product> = emptyList(),
    val selectedProduct: Product? = null,
    val quantityText: String = "1",
    val rateText: String = "",
    val showRateEditor: Boolean = false,
    val priceOptions: List<BillingPriceOption> = emptyList(),
    val showPricePicker: Boolean = false,
    val selectedUnit: String = "",
    val allowPriceOverride: Boolean = true,
    val heldCount: Int = 0,
    val scanNonce: Int = 0,
    val saving: Boolean = false,
    val savedBillId: Long? = null,
    val saveError: String? = null,
    val error: String? = null
)

/**
 * Holds the in-progress (draft) bill plus the inline product picker state.
 * Scoped to the activity so the draft and picker survive configuration
 * changes and navigation.
 */
class BillingViewModel(
    private val productRepository: ProductRepository,
    private val billRepository: BillRepository,
    private val heldBillRepository: HeldBillRepository,
    private val settingsRepository: SettingsRepository
) : ViewModel() {

    private val _state = MutableStateFlow(BillingUiState())
    val state: StateFlow<BillingUiState> = _state.asStateFlow()

    private var itemKey = 0L
    private var searchJob: Job? = null

    init {
        viewModelScope.launch {
            heldBillRepository.observeAll().collect { held ->
                _state.update { it.copy(heldCount = held.size) }
            }
        }
        viewModelScope.launch {
            settingsRepository.observe(SettingsKeys.ALLOW_PRICE_OVERRIDE).collect { v ->
                _state.update { it.copy(allowPriceOverride = v.toBoolean()) }
            }
        }
        viewModelScope.launch {
            _state.update { it.copy(recentProducts = productRepository.recentlySold(8)) }
        }
    }

    fun ensureStarted() {
        if (_state.value.billNumber.isNotEmpty()) return
        startNewBill()
    }

    fun startNewBill() {
        viewModelScope.launch {
            val next = BillNumbers.format(billRepository.nextBillNumber())
            _state.update { s ->
                BillingUiState(
                    billNumber = next,
                    billDate = Dates.todayDateString(),
                    billTime = Dates.timeString(),
                    heldCount = s.heldCount,
                    allowPriceOverride = s.allowPriceOverride
                )
            }
        }
    }

    fun discardBill() {
        _state.update { s ->
            BillingUiState(
                heldCount = s.heldCount,
                allowPriceOverride = s.allowPriceOverride
            )
        }
    }

    private fun recalc(s: BillingUiState): BillingUiState {
        val subtotal = s.items.sumOf { it.amountPaise }
        val total = BillCalculator.grandTotalPaise(subtotal, s.discountPaise)
        return s.copy(subtotalPaise = subtotal, totalPaise = total)
    }

    // ---- Product picker ----

    fun onSearchQueryChange(query: String) {
        _state.update {
            it.copy(
                searchQuery = query,
                error = null,
                selectedProduct = null,
                searchResults = emptyList(),
                quantityText = "1",
                rateText = "",
                showRateEditor = false,
                priceOptions = emptyList(),
                showPricePicker = false,
                selectedUnit = ""
            )
        }
        searchJob?.cancel()
        val q = query.trim()
        if (q.isEmpty()) return
        searchJob = viewModelScope.launch {
            delay(120)
            _state.update { it.copy(searching = true) }
            val results = productRepository.search(q)
            _state.update {
                it.copy(searchResults = ProductRanker.rank(q, results), searching = false)
            }
        }
    }

    fun clearSearch() {
        onSearchQueryChange("")
    }

    fun selectProduct(product: Product) {
        viewModelScope.launch {
            val extras = productRepository.getExtraPrices(product.id)
            val options = buildList {
                if (product.sellingPricePaise > 0L) {
                    add(BillingPriceOption(product.sellingPricePaise, product.unit))
                }
                for (e in extras) {
                    if (e.sellingPricePaise > 0L) add(BillingPriceOption(e.sellingPricePaise, e.unit))
                }
            }
            val single = options.singleOrNull()
            _state.update {
                it.copy(
                    selectedProduct = product,
                    searchQuery = product.name,
                    searchResults = emptyList(),
                    searching = false,
                    quantityText = "1",
                    rateText = single?.let { o -> Money.paiseToNumber(o.pricePaise) } ?: "",
                    selectedUnit = single?.unit ?: "",
                    priceOptions = options,
                    showPricePicker = options.size > 1,
                    showRateEditor = options.isEmpty(),
                    error = null
                )
            }
        }
    }

    fun onPriceOptionSelected(option: BillingPriceOption) {
        _state.update {
            it.copy(
                rateText = Money.paiseToNumber(option.pricePaise),
                selectedUnit = option.unit,
                showPricePicker = false,
                showRateEditor = false,
                error = null
            )
        }
    }

    /** Closes the preset-price picker and opens the free-price field. */
    fun showCustomPriceEditor() {
        _state.update {
            it.copy(
                showPricePicker = false,
                showRateEditor = true,
                rateText = "",
                selectedUnit = "",
                error = null
            )
        }
    }

    fun cancelPricePick() {
        _state.update {
            it.copy(
                selectedProduct = null,
                searchQuery = "",
                searchResults = emptyList(),
                searching = false,
                quantityText = "1",
                rateText = "",
                showRateEditor = false,
                priceOptions = emptyList(),
                showPricePicker = false,
                selectedUnit = "",
                error = null
            )
        }
    }

    fun onQuantityChange(value: String) {
        val cleaned = value.filter { it.isDigit() || it == '.' }
        _state.update { it.copy(quantityText = cleaned, error = null) }
    }

    fun onRateChange(value: String) {
        val cleaned = value.filter { it.isDigit() || it == '.' }
        _state.update { it.copy(rateText = cleaned, error = null) }
    }

    fun adjustPickerQuantity(delta: Long) {
        val current = _state.value.quantityText.toBigDecimalOrNull() ?: BigDecimal.ONE
        val next = (current + BigDecimal(delta)).coerceAtLeast(BigDecimal.ONE)
        _state.update {
            it.copy(quantityText = Money.formatQuantity(next.toPlainString()), error = null)
        }
    }

    fun toggleRateEditor() {
        _state.update { it.copy(showRateEditor = !it.showRateEditor, error = null) }
    }

    fun closeRateEditor() {
        _state.update { it.copy(showRateEditor = false, error = null) }
    }

    fun onBarcodeScanned(barcode: String) {
        val q = barcode.trim()
        if (q.isEmpty()) return
        viewModelScope.launch {
            val product = productRepository.findExact(q)
            if (product != null) {
                selectProduct(product)
            } else {
                _state.update {
                    it.copy(
                        searchQuery = q,
                        selectedProduct = null,
                        searchResults = emptyList(),
                        searching = false,
                        quantityText = "1",
                        rateText = "",
                        showRateEditor = false,
                        priceOptions = emptyList(),
                        showPricePicker = false,
                        selectedUnit = "",
                        error = null,
                        scanNonce = it.scanNonce + 1
                    )
                }
            }
        }
    }

    fun addSelectedToBill(): Boolean {
        val s = _state.value
        val product = s.selectedProduct ?: return false

        val quantity = s.quantityText.trim()
        val qtyBd = quantity.toBigDecimalOrNull()
        if (quantity.isEmpty() || qtyBd == null || qtyBd <= BigDecimal.ZERO) {
            _state.update { it.copy(error = "Please enter a valid quantity.") }
            return false
        }

        val rateText = s.rateText.trim()
        val ratePaise: Long = when {
            rateText.isNotEmpty() -> Money.parseRupeesToPaise(rateText)
                ?: run {
                    _state.update { it.copy(error = "Please enter a valid price.") }
                    return false
                }
            product.sellingPricePaise > 0L -> product.sellingPricePaise
            else -> 0L // no price yet - set it later on the pricing screen
        }

        val amount = BillCalculator.itemAmountPaise(quantity, ratePaise)
        val item = DraftItem(
            key = ++itemKey,
            productId = product.id,
            productName = product.name,
            quantity = Money.formatQuantity(quantity),
            ratePaise = ratePaise,
            amountPaise = amount
        )
        _state.update { st ->
            recalc(
                st.copy(
                    items = st.items + item,
                    selectedProduct = null,
                    searchQuery = "",
                    searchResults = emptyList(),
                    quantityText = "1",
                    rateText = "",
                    showRateEditor = false,
                    priceOptions = emptyList(),
                    showPricePicker = false,
                    selectedUnit = "",
                    error = null
                )
            )
        }
        viewModelScope.launch {
            _state.update { it.copy(recentProducts = productRepository.recentlySold(8)) }
        }
        return true
    }

    // ---- Draft bill ----

    fun addItem(productId: String?, productName: String, quantity: String, ratePaise: Long) {
        val amount = BillCalculator.itemAmountPaise(quantity, ratePaise)
        val item = DraftItem(
            key = ++itemKey,
            productId = productId,
            productName = productName,
            quantity = Money.formatQuantity(quantity),
            ratePaise = ratePaise,
            amountPaise = amount
        )
        _state.update { s -> recalc(s.copy(items = s.items + item)) }
    }

    fun updateItem(key: Long, quantity: String, ratePaise: Long) {
        val amount = BillCalculator.itemAmountPaise(quantity, ratePaise)
        _state.update { s ->
            recalc(
                s.copy(
                    items = s.items.map {
                        if (it.key == key) {
                            it.copy(
                                quantity = Money.formatQuantity(quantity),
                                ratePaise = ratePaise,
                                amountPaise = amount
                            )
                        } else it
                    }
                )
            )
        }
    }

    fun adjustQuantity(key: Long, delta: Long) {
        _state.update { s ->
            val items = s.items.map {
                if (it.key == key) {
                    val current = it.quantity.toBigDecimalOrNull() ?: BigDecimal.ONE
                    val next = (current + BigDecimal(delta)).coerceAtLeast(BigDecimal.ONE)
                    val qty = Money.formatQuantity(next.toPlainString())
                    it.copy(
                        quantity = qty,
                        amountPaise = BillCalculator.itemAmountPaise(qty, it.ratePaise)
                    )
                } else it
            }
            recalc(s.copy(items = items))
        }
    }

    fun removeItem(key: Long) {
        _state.update { s ->
            recalc(s.copy(items = s.items.filterNot { it.key == key }))
        }
    }

    fun setDiscountText(text: String) {
        val paise = Money.parseRupeesToPaise(text) ?: 0L
        _state.update { s -> recalc(s.copy(discountText = text, discountPaise = paise)) }
    }

    // ---- Hold / resume ----

    fun holdBill(reference: String) {
        val s = _state.value
        if (s.items.isEmpty()) return
        viewModelScope.launch {
            val id = heldBillRepository.hold(
                reference = reference,
                billNumber = s.billNumber.ifEmpty { BillNumbers.format(billRepository.nextBillNumber()) },
                date = s.billDate.ifEmpty { Dates.todayDateString() },
                time = Dates.timeString(),
                items = s.items.map {
                    HeldBillItemDraft(
                        productId = it.productId,
                        productName = it.productName,
                        quantity = it.quantity,
                        ratePaise = it.ratePaise,
                        amountPaise = it.amountPaise
                    )
                },
                subtotalPaise = s.subtotalPaise,
                discountPaise = s.discountPaise,
                totalPaise = s.totalPaise
            )
            if (id > 0) startNewBill()
        }
    }

    suspend fun resumeHeld(id: Long): Boolean {
        val held = heldBillRepository.getWithItems(id) ?: return false
        val current = _state.value
        val items = held.items.map {
            DraftItem(
                key = ++itemKey,
                productId = it.productId,
                productName = it.productNameSnapshot,
                quantity = it.quantity,
                ratePaise = it.ratePaise,
                amountPaise = it.amountPaise
            )
        }
        _state.value = BillingUiState(
            billNumber = held.bill.billNumber,
            billDate = held.bill.billDate,
            billTime = held.bill.billTime,
            items = items,
            subtotalPaise = held.bill.subtotalPaise,
            discountText = Money.paiseToNumber(held.bill.discountPaise),
            discountPaise = held.bill.discountPaise,
            totalPaise = held.bill.totalPaise,
            heldCount = current.heldCount,
            allowPriceOverride = current.allowPriceOverride
        )
        heldBillRepository.delete(id)
        return true
    }

    suspend fun deleteHeld(id: Long) = heldBillRepository.delete(id)

    // ---- Save ----

    fun saveBill() {
        val s = _state.value
        if (s.saving || s.items.isEmpty()) return
        _state.update { it.copy(saving = true, saveError = null) }
        viewModelScope.launch {
            try {
                val billId = billRepository.saveBill(
                    billNumber = s.billNumber,
                    date = s.billDate.ifEmpty { Dates.todayDateString() },
                    time = s.billTime.ifEmpty { Dates.timeString() },
                    items = s.items.map {
                        BillItemDraft(
                            productId = it.productId,
                            productName = it.productName,
                            quantity = it.quantity,
                            ratePaise = it.ratePaise,
                            amountPaise = it.amountPaise
                        )
                    },
                    subtotalPaise = s.subtotalPaise,
                    discountPaise = s.discountPaise,
                    totalPaise = s.totalPaise
                )
                _state.value = BillingUiState(savedBillId = billId)
            } catch (e: Exception) {
                _state.update { it.copy(saving = false, saveError = e.message) }
            }
        }
    }

    fun consumeSaved() {
        _state.update { it.copy(savedBillId = null) }
    }
}
