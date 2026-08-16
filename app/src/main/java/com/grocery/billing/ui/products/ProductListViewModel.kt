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
import kotlinx.coroutines.launch

class ProductListViewModel(
    private val productRepository: ProductRepository
) : ViewModel() {

    val products: StateFlow<List<Product>> = productRepository.products
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    fun delete(product: Product) {
        viewModelScope.launch {
            try {
                productRepository.delete(product)
            } catch (e: Exception) {
                _error.value = "Could not delete product."
            }
        }
    }

    fun clearError() {
        _error.value = null
    }
}
