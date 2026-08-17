package com.grocery.billing.ui.products

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.grocery.billing.data.csv.CsvImportAnalysis
import com.grocery.billing.data.csv.CsvImportAnalyzer
import com.grocery.billing.data.csv.CsvParser
import com.grocery.billing.data.entity.Product
import com.grocery.billing.data.entity.ProductPriceOption
import com.grocery.billing.data.repository.ProductRepository
import com.grocery.billing.util.Dates
import com.grocery.billing.util.SkuGenerator
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed interface CsvImportState {
    data object Idle : CsvImportState
    data class Loaded(val analysis: CsvImportAnalysis, val fileName: String) : CsvImportState
    data object Imported : CsvImportState
    data class Failed(val message: String) : CsvImportState
}

class CsvImportViewModel(
    private val productRepository: ProductRepository
) : ViewModel() {

    private val _state = MutableStateFlow<CsvImportState>(CsvImportState.Idle)
    val state: StateFlow<CsvImportState> = _state.asStateFlow()

    fun analyzeText(text: String, fileName: String) {
        viewModelScope.launch {
            try {
                val rows = CsvParser.parse(text)
                val existingIds = productRepository.getAll().map { it.id }.toSet()
                val analysis = CsvImportAnalyzer.analyze(rows, existingIds)
                _state.value = CsvImportState.Loaded(analysis, fileName)
            } catch (e: Exception) {
                _state.value = CsvImportState.Failed("Could not read the file.")
            }
        }
    }

    fun import() {
        viewModelScope.launch {
            val loaded = _state.value as? CsvImportState.Loaded ?: return@launch
            try {
                val now = Dates.isoTimestamp()
                val existingSkus = productRepository.getAll().mapNotNull { it.sku }.toSet()
                val existingBarcodes = productRepository.getAll().mapNotNull { it.barcode }.toSet()
                val usedSkus = existingSkus.toMutableSet()
                val usedBarcodes = existingBarcodes.toMutableSet()

                val products = mutableListOf<Product>()
                val extras = mutableListOf<ProductPriceOption>()
                for (row in loaded.analysis.valid) {
                    val sku = if (row.sku != null && row.sku.isNotEmpty()) {
                        row.sku
                    } else {
                        productRepository.generateUniqueSku(row.name, usedSkus).also { usedSkus += it }
                    }

                    val barcode = if (row.barcode != null && row.barcode.isNotEmpty()) {
                        row.barcode
                    } else {
                        productRepository.generateUniqueBarcode(usedBarcodes).also { usedBarcodes += it }
                    }

                    products += Product(
                        id = row.id,
                        name = row.name,
                        sellingPricePaise = row.sellingPricePaise,
                        unit = row.unit,
                        barcode = barcode.ifEmpty { null },
                        sku = sku.ifEmpty { null },
                        createdAt = now,
                        updatedAt = now
                    )
                    for (opt in row.extraOptions) {
                        extras += ProductPriceOption(
                            productId = row.id,
                            sellingPricePaise = opt.sellingPricePaise,
                            unit = opt.unit
                        )
                    }
                }
                productRepository.insertAll(products, extras)
                _state.value = CsvImportState.Imported
            } catch (e: Exception) {
                _state.value = CsvImportState.Failed("Import failed: ${e.message}")
            }
        }
    }

    fun reset() {
        _state.value = CsvImportState.Idle
    }
}
