package com.arekalov.osrubezh.presentation.numa

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.arekalov.osrubezh.domain.usecase.CalculateNUMAUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NUMAViewModel @Inject constructor(
    private val calculateNUMAUseCase: CalculateNUMAUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(NUMAUiState())
    val uiState: StateFlow<NUMAUiState> = _uiState.asStateFlow()

    fun onFrequencyChange(value: String) {
        _uiState.update { it.copy(frequencyGHz = value) }
    }

    fun onL1L2TimeChange(value: String) {
        _uiState.update { it.copy(l1l2TimeNs = value) }
    }

    fun onLocalNumaTimeChange(value: String) {
        _uiState.update { it.copy(localNumaTimeNs = value) }
    }

    fun onOtherNumaTimeChange(value: String) {
        _uiState.update { it.copy(otherNumaTimeNs = value) }
    }

    fun onRegistersCountChange(value: String) {
        _uiState.update { it.copy(registersCount = value) }
    }

    fun onL1L2CountChange(value: String) {
        _uiState.update { it.copy(l1l2Count = value) }
    }

    fun onLocalNumaCountChange(value: String) {
        _uiState.update { it.copy(localNumaCount = value) }
    }

    fun onOtherNumaCountChange(value: String) {
        _uiState.update { it.copy(otherNumaCount = value) }
    }

    fun toggleDetails() {
        _uiState.update { it.copy(showDetails = !it.showDetails) }
    }

    fun calculate() {
        val state = _uiState.value
        
        if (state.frequencyGHz.isEmpty() || state.l1l2TimeNs.isEmpty() ||
            state.localNumaTimeNs.isEmpty() || state.otherNumaTimeNs.isEmpty() ||
            state.registersCount.isEmpty() || state.l1l2Count.isEmpty() ||
            state.localNumaCount.isEmpty() || state.otherNumaCount.isEmpty()
        ) {
            _uiState.update { it.copy(error = "Заполните все поля") }
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }

            try {
                val result = calculateNUMAUseCase(
                    frequencyGHz = state.frequencyGHz.toDouble(),
                    l1l2TimeNs = state.l1l2TimeNs.toDouble(),
                    localNumaTimeNs = state.localNumaTimeNs.toDouble(),
                    otherNumaTimeNs = state.otherNumaTimeNs.toDouble(),
                    registersCount = state.registersCount.toInt(),
                    l1l2Count = state.l1l2Count.toInt(),
                    localNumaCount = state.localNumaCount.toInt(),
                    otherNumaCount = state.otherNumaCount.toInt()
                )

                result.fold(
                    onSuccess = { numaResult ->
                        _uiState.update {
                            it.copy(
                                result = numaResult,
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
