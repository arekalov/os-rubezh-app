package com.arekalov.osrubezh.presentation.disk

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.arekalov.osrubezh.domain.usecase.CalculateDiskUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DiskViewModel @Inject constructor(
    private val calculateDiskUseCase: CalculateDiskUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(DiskUiState())
    val uiState: StateFlow<DiskUiState> = _uiState.asStateFlow()

    fun onBytePerSectorChange(value: String) {
        _uiState.update { it.copy(bytePerSector = value) }
    }

    fun onSectorsPerTrackChange(value: String) {
        _uiState.update { it.copy(sectorsPerTrack = value) }
    }

    fun onTracksPerSurfaceChange(value: String) {
        _uiState.update { it.copy(tracksPerSurface = value) }
    }

    fun onSurfacesPerDiskChange(value: String) {
        _uiState.update { it.copy(surfacesPerDisk = value) }
    }

    fun onRecordsCountChange(value: String) {
        _uiState.update { it.copy(recordsCount = value) }
    }

    fun onBytesPerRecordChange(value: String) {
        _uiState.update { it.copy(bytesPerRecord = value) }
    }

    fun toggleDetails() {
        _uiState.update { it.copy(showDetails = !it.showDetails) }
    }

    fun calculate() {
        val state = _uiState.value
        
        if (state.bytePerSector.isEmpty() || state.sectorsPerTrack.isEmpty() ||
            state.tracksPerSurface.isEmpty() || state.surfacesPerDisk.isEmpty() ||
            state.recordsCount.isEmpty() || state.bytesPerRecord.isEmpty()
        ) {
            _uiState.update { it.copy(error = "Заполните все поля") }
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }

            try {
                val result = calculateDiskUseCase(
                    bytePerSector = state.bytePerSector.toInt(),
                    sectorsPerTrack = state.sectorsPerTrack.toInt(),
                    tracksPerSurface = state.tracksPerSurface.toInt(),
                    surfacesPerDisk = state.surfacesPerDisk.toInt(),
                    recordsCount = state.recordsCount.toInt(),
                    bytesPerRecord = state.bytesPerRecord.toInt()
                )

                result.fold(
                    onSuccess = { diskResult ->
                        _uiState.update {
                            it.copy(
                                result = diskResult,
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
