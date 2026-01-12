package com.arekalov.osrubezh.presentation.fcfs

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.arekalov.osrubezh.domain.usecase.CalculateFCFSUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FCFSViewModel @Inject constructor(
    private val calculateFCFSUseCase: CalculateFCFSUseCase
) : ViewModel() {
    
    private val _uiState = MutableStateFlow(FCFSUiState())
    val uiState: StateFlow<FCFSUiState> = _uiState.asStateFlow()
    
    fun onCpuTimeChange(value: String) {
        _uiState.update { it.copy(cpuTime = value, error = null) }
    }
    
    fun onIoTimeChange(value: String) {
        _uiState.update { it.copy(ioTime = value, error = null) }
    }
    
    fun onTotalWorkTimeChange(value: String) {
        _uiState.update { it.copy(totalWorkTime = value, error = null) }
    }
    
    fun onProcessCountChange(value: String) {
        _uiState.update { it.copy(processCount = value, error = null) }
    }
    
    fun toggleDetails() {
        _uiState.update { it.copy(showDetails = !it.showDetails) }
    }
    
    fun calculate() {
        val state = _uiState.value
        
        val cpuTime = state.cpuTime.toIntOrNull()
        val ioTime = state.ioTime.toIntOrNull()
        val totalWorkTime = state.totalWorkTime.toIntOrNull()
        val processCount = state.processCount.toIntOrNull()
        
        if (cpuTime == null || cpuTime <= 0) {
            _uiState.update { it.copy(error = "Некорректное значение: Время вычислений") }
            return
        }
        
        if (ioTime == null || ioTime < 0) {
            _uiState.update { it.copy(error = "Некорректное значение: Время ввода-вывода") }
            return
        }
        
        if (totalWorkTime == null || totalWorkTime <= 0) {
            _uiState.update { it.copy(error = "Некорректное значение: Всего времени работы") }
            return
        }
        
        if (processCount == null || processCount <= 0) {
            _uiState.update { it.copy(error = "Некорректное значение: Количество процессов") }
            return
        }
        
        _uiState.update { it.copy(isLoading = true, error = null) }
        
        viewModelScope.launch {
            calculateFCFSUseCase(
                cpuTime = cpuTime,
                ioTime = ioTime,
                totalWorkTime = totalWorkTime,
                processCount = processCount
            ).fold(
                onSuccess = { result ->
                    _uiState.update { 
                        it.copy(
                            result = result,
                            isLoading = false,
                            error = null
                        )
                    }
                },
                onFailure = { error ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            error = error.message ?: "Ошибка вычисления"
                        )
                    }
                }
            )
        }
    }
}
