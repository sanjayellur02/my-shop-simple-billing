package com.grocery.billing.ui.products

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.grocery.billing.data.entity.ProductPriceOption
import com.grocery.billing.data.repository.ProductRepository
import com.grocery.billing.money.Money
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class ExtraPriceDraft(
    val key: Long,
    val priceText: String,
    val unit: String
)

data class ProductEditUiState(
    val originalId: String? = null,
    val id: String = "",
    val name: String = "",
    val priceText: String = "",
    val unit: String = "",
    val barcode: String = "",
    val extraPrices: List<ExtraPriceDraft> = emptyList(),
    val error: String? = null,
    val isEditing: Boolean = false,
    val saved: Boolean = false
)

class ProductEditViewModel(
    private val productRepository: ProductRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _state = MutableStateFlow(ProductEditUiState())
    val state: StateFlow<ProductEditUiState> = _state.asStateFlow()

    private val productId = savedStateHandle.get<String>("productId")
    private var extraKey = 0L

    init {
        viewModelScope.launch {
            val existing = productId?.let { productRepository.getById(it) }
            _state.value = if (existing != null) {
                ProductEditUiState(
                    originalId = existing.id,
                    id = existing.id,
                    name = existing.name,
                    priceText = Money.paiseToNumber(existing.sellingPricePaise),
                    unit = existing.unit,
                    barcode = existing.barcode ?: "",
                    extraPrices = productRepository.getExtraPrices(existing.id).map {
                        ExtraPriceDraft(
                            key = ++extraKey,
                            priceText = Money.paiseToNumber(it.sellingPricePaise),
                            unit = it.unit
                        )
                    },
                    isEditing = true
                )
            } else {
                ProductEditUiState()
            }
        }
    }

    fun onIdChange(value: String) {
        _state.update { it.copy(id = value, error = null) }
    }

    fun onNameChange(value: String) {
        _state.update { it.copy(name = value, error = null) }
    }

    fun onPriceChange(value: String) {
        val cleaned = value.filter { it.isDigit() || it == '.' }
        _state.update { it.copy(priceText = cleaned, error = null) }
    }

    fun onUnitChange(value: String) {
        _state.update { it.copy(unit = value, error = null) }
    }

    fun onBarcodeChange(value: String) {
        _state.update { it.copy(barcode = value, error = null) }
    }

    fun onExtraPriceChange(key: Long, value: String) {
        val cleaned = value.filter { it.isDigit() || it == '.' }
        _state.update {
            it.copy(
                extraPrices = it.extraPrices.map { d ->
                    if (d.key == key) d.copy(priceText = cleaned) else d
                },
                error = null
            )
        }
    }

    fun onExtraUnitChange(key: Long, value: String) {
        _state.update {
            it.copy(
                extraPrices = it.extraPrices.map { d ->
                    if (d.key == key) d.copy(unit = value) else d
                },
                error = null
            )
        }
    }

    fun addExtraPrice() {
        _state.update { it.copy(extraPrices = it.extraPrices + ExtraPriceDraft(key = ++extraKey, priceText = "", unit = "")) }
    }

    fun removeExtraPrice(key: Long) {
        _state.update { it.copy(extraPrices = it.extraPrices.filterNot { d -> d.key == key }) }
    }

    fun save() {
        val s = _state.value
        val priceText = s.priceText.trim()
        if (priceText.isNotEmpty() && Money.parseRupeesToPaise(priceText) == null) {
            _state.update { it.copy(error = "Please enter a valid price.") }
            return
        }
        val pricePaise = if (priceText.isEmpty()) 0L else Money.parseRupeesToPaise(priceText)!!
        val extras = mutableListOf<ProductPriceOption>()
        for (d in s.extraPrices) {
            val p = d.priceText.trim()
            if (p.isEmpty()) continue
            val paise = Money.parseRupeesToPaise(p)
            if (paise == null) {
                _state.update { it.copy(error = "Please enter a valid extra price.") }
                return
            }
            extras.add(ProductPriceOption(productId = s.id, sellingPricePaise = paise, unit = d.unit.trim()))
        }
        viewModelScope.launch {
            productRepository.replaceExtraPrices(s.originalId ?: s.id, emptyList())
            val error = if (s.isEditing) {
                productRepository.update(
                    currentId = s.originalId ?: "",
                    newId = s.id,
                    name = s.name,
                    sellingPricePaise = pricePaise,
                    unit = s.unit,
                    barcode = s.barcode
                )
            } else {
                productRepository.add(
                    id = s.id,
                    name = s.name,
                    sellingPricePaise = pricePaise,
                    unit = s.unit,
                    barcode = s.barcode
                )
            }
            if (error == null) {
                if (extras.isNotEmpty()) {
                    productRepository.replaceExtraPrices(s.id, extras)
                }
                _state.update { it.copy(saved = true) }
            } else {
                _state.update { it.copy(error = error) }
            }
        }
    }
}
