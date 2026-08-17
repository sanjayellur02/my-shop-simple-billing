package com.grocery.billing.ui.products

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.grocery.billing.data.entity.Product
import com.grocery.billing.data.repository.ProductRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ProductListViewModel(
    private val productRepository: ProductRepository
) : ViewModel() {

    val products: StateFlow<List<Product>> = productRepository.products
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    private val _selectionMode = MutableStateFlow(false)
    val selectionMode: StateFlow<Boolean> = _selectionMode.asStateFlow()

    private val _selected = MutableStateFlow<Set<String>>(emptySet())
    val selected: StateFlow<Set<String>> = _selected.asStateFlow()

    fun delete(product: Product) {
        viewModelScope.launch {
            try {
                productRepository.delete(product)
            } catch (e: Exception) {
                _error.value = "Could not delete product."
            }
        }
    }

    fun enterSelectionMode() {
        _selectionMode.value = true
        _selected.value = emptySet()
    }

    fun exitSelectionMode() {
        _selectionMode.value = false
        _selected.value = emptySet()
    }

    fun toggleSelected(id: String) {
        _selected.update { sel -> if (id in sel) sel - id else sel + id }
    }

    fun selectAll() {
        _selected.value = products.value.map { it.id }.toSet()
    }

    fun clearSelection() {
        _selected.value = emptySet()
    }

    fun deleteSelected() {
        viewModelScope.launch {
            val ids = _selected.value
            if (ids.isEmpty()) return@launch
            try {
                for (id in ids) {
                    val product = productRepository.getById(id) ?: continue
                    productRepository.delete(product)
                }
                exitSelectionMode()
            } catch (e: Exception) {
                _error.value = "Could not delete some products."
            }
        }
    }

    fun clearError() {
        _error.value = null
    }
}
