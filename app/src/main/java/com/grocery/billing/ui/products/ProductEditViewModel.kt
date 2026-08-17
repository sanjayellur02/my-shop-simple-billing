package com.grocery.billing.ui.products

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.grocery.billing.data.entity.ProductPriceOption
import com.grocery.billing.data.repository.DraftRepository
import com.grocery.billing.data.repository.ProductRepository
import com.grocery.billing.money.Money
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
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
    val saved: Boolean = false,
    val draftRestored: Boolean = false
)

class ProductEditViewModel(
    private val productRepository: ProductRepository,
    private val draftRepository: DraftRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _state = MutableStateFlow(ProductEditUiState())
    val state: StateFlow<ProductEditUiState> = _state.asStateFlow()

    private val productId = savedStateHandle.get<String>("productId")
    private var extraKey = 0L
    private var autoSaveJob: Job? = null

    companion object {
        private const val DRAFT_KEY_PREFIX = "product_edit:"
        private const val AUTO_SAVE_DEBOUNCE_MS = 800L
    }

    init {
        viewModelScope.launch {
            val draftKey = DRAFT_KEY_PREFIX + (productId ?: "new")
            val draft = draftRepository.get(draftKey)

            val existing = productId?.let { productRepository.getById(it) }

            if (draft != null) {
                val restored = restoreDraft(draft.data)
                if (restored != null) {
                    extraKey = restored.extraPrices.maxOfOrNull { it.key }?.plus(1) ?: 0L
                    _state.value = restored.copy(draftRestored = true)
                    return@launch
                }
            }

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
        scheduleAutoSave()
    }

    fun onNameChange(value: String) {
        _state.update { it.copy(name = value, error = null) }
        scheduleAutoSave()
    }

    fun onPriceChange(value: String) {
        val cleaned = value.filter { it.isDigit() || it == '.' }
        _state.update { it.copy(priceText = cleaned, error = null) }
        scheduleAutoSave()
    }

    fun onUnitChange(value: String) {
        _state.update { it.copy(unit = value, error = null) }
        scheduleAutoSave()
    }

    fun onBarcodeChange(value: String) {
        _state.update { it.copy(barcode = value, error = null) }
        scheduleAutoSave()
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
        scheduleAutoSave()
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
        scheduleAutoSave()
    }

    fun addExtraPrice() {
        _state.update { it.copy(extraPrices = it.extraPrices + ExtraPriceDraft(key = ++extraKey, priceText = "", unit = "")) }
        scheduleAutoSave()
    }

    fun removeExtraPrice(key: Long) {
        _state.update { it.copy(extraPrices = it.extraPrices.filterNot { d -> d.key == key }) }
        scheduleAutoSave()
    }

    fun clearDraftError() {
        _state.update { it.copy(draftRestored = false) }
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
                deleteDraft()
                _state.update { it.copy(saved = true) }
            } else {
                _state.update { it.copy(error = error) }
            }
        }
    }

    private fun scheduleAutoSave() {
        autoSaveJob?.cancel()
        autoSaveJob = viewModelScope.launch {
            delay(AUTO_SAVE_DEBOUNCE_MS)
            saveDraft()
        }
    }

    private suspend fun saveDraft() {
        val s = _state.value
        if (s.id.isEmpty() && s.name.isEmpty() && s.priceText.isEmpty() && s.extraPrices.isEmpty()) return
        val draftKey = DRAFT_KEY_PREFIX + (productId ?: "new")
        val json = serializeDraft(s)
        draftRepository.save(draftKey, json)
    }

    private suspend fun deleteDraft() {
        val draftKey = DRAFT_KEY_PREFIX + (productId ?: "new")
        draftRepository.delete(draftKey)
    }

    private fun serializeDraft(s: ProductEditUiState): String {
        val sb = StringBuilder()
        sb.appendLine(s.originalId ?: "")
        sb.appendLine(s.id)
        sb.appendLine(s.name)
        sb.appendLine(s.priceText)
        sb.appendLine(s.unit)
        sb.appendLine(s.barcode)
        sb.appendLine(s.extraPrices.size)
        for (ep in s.extraPrices) {
            sb.appendLine(ep.priceText)
            sb.appendLine(ep.unit)
        }
        return sb.toString()
    }

    private fun restoreDraft(data: String): ProductEditUiState? {
        return try {
            val lines = data.lines()
            var idx = 0
            fun next(): String = lines[idx++]

            val originalId = next().ifEmpty { null }
            val id = next()
            val name = next()
            val priceText = next()
            val unit = next()
            val barcode = next()
            val extraCount = next().toIntOrNull() ?: 0
            val extras = mutableListOf<ExtraPriceDraft>()
            for (i in 0 until extraCount) {
                extras.add(ExtraPriceDraft(key = i.toLong(), priceText = next(), unit = next()))
            }
            ProductEditUiState(
                originalId = originalId,
                id = id,
                name = name,
                priceText = priceText,
                unit = unit,
                barcode = barcode,
                extraPrices = extras,
                isEditing = originalId != null
            )
        } catch (_: Exception) {
            null
        }
    }
}
