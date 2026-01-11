package com.arekalov.osrubezh.presentation.raid

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.arekalov.osrubezh.domain.usecase.CalculateRAIDUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RAIDViewModel @Inject constructor(
    private val calculateRAIDUseCase: CalculateRAIDUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(RAIDUiState())
    val uiState: StateFlow<RAIDUiState> = _uiState.asStateFlow()

    fun onRaidLevelChange(value: String) {
        _uiState.update { it.copy(raidLevel = value) }
    }

    fun onDiskCountChange(value: String) {
        _uiState.update { it.copy(diskCount = value) }
    }

    fun onDiskSizeChange(value: String) {
        _uiState.update { it.copy(diskSizeGB = value) }
    }

    fun onBlockSizeChange(value: String) {
        _uiState.update { it.copy(blockSize = value) }
    }

    fun onSuSizeChange(value: String) {
        _uiState.update { it.copy(suSize = value) }
    }

    fun onBlockReadChange(value: String) {
        _uiState.update { it.copy(blockRead = value) }
    }

    fun onBlockWriteChange(value: String) {
        _uiState.update { it.copy(blockWrite = value) }
    }

    fun onSuCalcChange(value: String) {
        _uiState.update { it.copy(suCalc = value) }
    }

    fun toggleDetails() {
        _uiState.update { it.copy(showDetails = !it.showDetails) }
    }

    fun calculate() {
        val state = _uiState.value
        
        if (state.raidLevel.isEmpty() || state.diskCount.isEmpty() ||
            state.diskSizeGB.isEmpty() || state.blockSize.isEmpty() ||
            state.suSize.isEmpty() || state.blockRead.isEmpty() ||
            state.blockWrite.isEmpty() || state.suCalc.isEmpty()
        ) {
            _uiState.update { it.copy(error = "Заполните все поля") }
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }

            try {
                val result = calculateRAIDUseCase(
                    raidLevel = state.raidLevel.toInt(),
                    diskCount = state.diskCount.toInt(),
                    diskSizeGB = state.diskSizeGB.toInt(),
                    blockSize = state.blockSize.toInt(),
                    suSize = state.suSize.toInt(),
                    blockRead = state.blockRead.toInt(),
                    blockWrite = state.blockWrite.toInt(),
                    suCalc = state.suCalc.toInt()
                )

                result.fold(
                    onSuccess = { raidResult ->
                        _uiState.update {
                            it.copy(
                                result = raidResult,
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
