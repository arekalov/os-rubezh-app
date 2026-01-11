package com.arekalov.osrubezh.presentation.pointers

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.arekalov.osrubezh.domain.usecase.CalculatePointersUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PointersViewModel @Inject constructor(
    private val calculatePointersUseCase: CalculatePointersUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(PointersUiState())
    val uiState: StateFlow<PointersUiState> = _uiState.asStateFlow()

    fun onDirectPointersChange(value: String) {
        _uiState.update { it.copy(directPointers = value) }
    }

    fun onPointerSizeBitsChange(value: String) {
        _uiState.update { it.copy(pointerSizeBits = value) }
    }

    fun onBlockSizeBytesChange(value: String) {
        _uiState.update { it.copy(blockSizeBytes = value) }
    }

    fun onFileSizeBytesChange(value: String) {
        _uiState.update { it.copy(fileSizeBytes = value) }
    }

    fun toggleDetails() {
        _uiState.update { it.copy(showDetails = !it.showDetails) }
    }

    fun calculate() {
        val state = _uiState.value
        
        if (state.directPointers.isEmpty() || state.pointerSizeBits.isEmpty() ||
            state.blockSizeBytes.isEmpty() || state.fileSizeBytes.isEmpty()
        ) {
            _uiState.update { it.copy(error = "Заполните все поля") }
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }

            try {
                val result = calculatePointersUseCase(
                    directPointers = state.directPointers.toInt(),
                    pointerSizeBits = state.pointerSizeBits.toInt(),
                    blockSizeBytes = state.blockSizeBytes.toInt(),
                    fileSizeBytes = state.fileSizeBytes.toLong()
                )

                result.fold(
                    onSuccess = { pointersResult ->
                        _uiState.update {
                            it.copy(
                                result = pointersResult,
                                isLoading = false,
                                error = null
                            )
                        }
                    },
                    onFailure = { exception ->
                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                error = exception.message ?: "Ошибка вычисления"
                            )
                        }
                    }
                )
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        error = "Неверный формат данных"
                    )
                }
            }
        }
    }
}
