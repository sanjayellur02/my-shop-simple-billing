package com.grocery.billing.ui.products

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.grocery.billing.data.csv.CsvImportAnalysis
import com.grocery.billing.data.csv.CsvImportAnalyzer
import com.grocery.billing.data.csv.CsvParser
import com.grocery.billing.data.entity.Product
import com.grocery.billing.data.repository.ProductRepository
import com.grocery.billing.util.Dates
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
                productRepository.insertAll(
                    loaded.analysis.valid.map {
                        Product(
                            id = it.id,
                            name = it.name,
                            sellingPricePaise = it.sellingPricePaise,
                            unit = it.unit,
                            barcode = it.barcode,
                            createdAt = now,
                            updatedAt = now
                        )
                    }
                )
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
