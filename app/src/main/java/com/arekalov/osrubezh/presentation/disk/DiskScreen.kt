package com.arekalov.osrubezh.presentation.disk

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.wear.compose.material.CircularProgressIndicator
import androidx.wear.compose.material.MaterialTheme
import androidx.wear.compose.material.ScalingLazyColumn
import androidx.wear.compose.material.Text
import com.arekalov.osrubezh.presentation.components.CalculatorButton
import com.arekalov.osrubezh.presentation.components.MarkdownCard
import com.arekalov.osrubezh.presentation.components.NumericInputField
import com.arekalov.osrubezh.presentation.components.ResultCard
import com.arekalov.osrubezh.presentation.theme.OsrubezhTheme
import com.arekalov.osrubezh.presentation.utils.NumberFormatter

@Composable
fun DiskScreen(
    viewModel: DiskViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    
    DiskContent(
        uiState = uiState,
        onBytePerSectorChange = viewModel::onBytePerSectorChange,
        onSectorsPerTrackChange = viewModel::onSectorsPerTrackChange,
        onTracksPerSurfaceChange = viewModel::onTracksPerSurfaceChange,
        onSurfacesPerDiskChange = viewModel::onSurfacesPerDiskChange,
        onRecordsCountChange = viewModel::onRecordsCountChange,
        onBytesPerRecordChange = viewModel::onBytesPerRecordChange,
        onCalculate = viewModel::calculate,
        onToggleDetails = viewModel::toggleDetails
    )
}

@Composable
fun DiskContent(
    uiState: DiskUiState,
    onBytePerSectorChange: (String) -> Unit,
    onSectorsPerTrackChange: (String) -> Unit,
    onTracksPerSurfaceChange: (String) -> Unit,
    onSurfacesPerDiskChange: (String) -> Unit,
    onRecordsCountChange: (String) -> Unit,
    onBytesPerRecordChange: (String) -> Unit,
    onCalculate: () -> Unit,
    onToggleDetails: () -> Unit
) {
    ScalingLazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(
            top = 24.dp,
            start = 16.dp,
            end = 16.dp,
            bottom = 24.dp
        ),
        verticalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        item {
            Text(
                text = "Диск",
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.title1
            )
        }
        
        item {
            Text(
                text = "Геометрия диска",
                style = MaterialTheme.typography.caption1,
                color = MaterialTheme.colors.onSurface.copy(alpha = 0.7f)
            )
        }
        
        item {
            NumericInputField(
                value = uiState.bytePerSector,
                onValueChange = onBytePerSectorChange,
                label = "{A} Байт на сектор",
                enabled = !uiState.isLoading
            )
        }
        
        item {
            NumericInputField(
                value = uiState.sectorsPerTrack,
                onValueChange = onSectorsPerTrackChange,
                label = "{B} Секторов на дорожке",
                enabled = !uiState.isLoading
            )
        }
        
        item {
            NumericInputField(
                value = uiState.tracksPerSurface,
                onValueChange = onTracksPerSurfaceChange,
                label = "{C} Дорожек на поверхности",
                enabled = !uiState.isLoading
            )
        }
        
        item {
            NumericInputField(
                value = uiState.surfacesPerDisk,
                onValueChange = onSurfacesPerDiskChange,
                label = "{D} Поверхностей на диске",
                enabled = !uiState.isLoading
            )
        }
        
        item {
            Text(
                text = "Параметры файла",
                style = MaterialTheme.typography.caption1,
                color = MaterialTheme.colors.onSurface.copy(alpha = 0.7f),
                modifier = Modifier.padding(top = 4.dp)
            )
        }
        
        item {
            NumericInputField(
                value = uiState.recordsCount,
                onValueChange = onRecordsCountChange,
                label = "{E} Количество записей",
                enabled = !uiState.isLoading
            )
        }
        
        item {
            NumericInputField(
                value = uiState.bytesPerRecord,
                onValueChange = onBytesPerRecordChange,
                label = "{F} Байт на запись",
                enabled = !uiState.isLoading
            )
        }
        
        item {
            Spacer(modifier = Modifier.height(4.dp))
        }
        
        item {
            if (uiState.isLoading) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(40.dp),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            } else {
                CalculatorButton(
                    text = "Вычислить",
                    onClick = onCalculate
                )
            }
        }
        
        uiState.error?.let { error ->
            item {
                Text(
                    text = error,
                    style = MaterialTheme.typography.caption1,
                    color = MaterialTheme.colors.error,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
        
        uiState.result?.let { result ->
            item {
                Spacer(modifier = Modifier.height(8.dp))
            }
            
            item {
                ResultCard(
                    title = "1. Количество секторов",
                    content = NumberFormatter.format(result.sectorsForFile)
                )
            }
            
            item {
                ResultCard(
                    title = "2. Количество дорожек",
                    content = NumberFormatter.format(result.tracksForFile)
                )
            }
            
            item {
                ResultCard(
                    title = "3. Размер с фрагментацией",
                    content = "${NumberFormatter.format(result.realSizeFile)} байт"
                )
            }
            
            item {
                ResultCard(
                    title = "4. Секторов на последней дорожке",
                    content = NumberFormatter.format(result.sectorsOnLastTrack)
                )
            }
            
            item {
                ResultCard(
                    title = "5. Внутренняя фрагментация",
                    content = "${NumberFormatter.format(result.totalFragmentation)} байт"
                )
            }
            
            item {
                CalculatorButton(
                    text = if (uiState.showDetails) "Скрыть детали" else "Подробнее",
                    onClick = onToggleDetails
                )
            }
            
            if (uiState.showDetails) {
                item {
                    MarkdownCard(
                        content = result.getFormattedDetails()
                    )
                }
            }
        }
    }
}

@Preview(device = Devices.WEAR_OS_SMALL_ROUND, showSystemUi = true)
@Composable
fun DiskScreenPreview() {
    OsrubezhTheme {
        DiskContent(
            uiState = DiskUiState(),
            onBytePerSectorChange = {},
            onSectorsPerTrackChange = {},
            onTracksPerSurfaceChange = {},
            onSurfacesPerDiskChange = {},
            onRecordsCountChange = {},
            onBytesPerRecordChange = {},
            onCalculate = {},
            onToggleDetails = {}
        )
    }
}
