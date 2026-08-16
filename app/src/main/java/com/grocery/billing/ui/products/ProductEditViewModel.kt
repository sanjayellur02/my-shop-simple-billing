package com.grocery.billing.ui.products

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.grocery.billing.data.repository.ProductRepository
import com.grocery.billing.money.Money
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class ProductEditUiState(
    val originalId: String? = null,
    val id: String = "",
    val name: String = "",
    val priceText: String = "",
    val unit: String = "",
    val barcode: String = "",
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

    fun save() {
        val s = _state.value
        val priceText = s.priceText.trim()
        if (priceText.isNotEmpty() && Money.parseRupeesToPaise(priceText) == null) {
            _state.update { it.copy(error = "Please enter a valid price.") }
            return
        }
        val pricePaise = if (priceText.isEmpty()) 0L else Money.parseRupeesToPaise(priceText)!!
        viewModelScope.launch {
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
                _state.update { it.copy(saved = true) }
            } else {
                _state.update { it.copy(error = error) }
            }
        }
    }
}
