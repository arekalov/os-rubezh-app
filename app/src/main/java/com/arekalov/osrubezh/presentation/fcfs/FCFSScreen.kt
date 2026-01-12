package com.arekalov.osrubezh.presentation.fcfs

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
fun FCFSScreen(
    viewModel: FCFSViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    
    FCFSContent(
        uiState = uiState,
        onCpuTimeChange = viewModel::onCpuTimeChange,
        onIoTimeChange = viewModel::onIoTimeChange,
        onTotalWorkTimeChange = viewModel::onTotalWorkTimeChange,
        onProcessCountChange = viewModel::onProcessCountChange,
        onCalculate = viewModel::calculate,
        onToggleDetails = viewModel::toggleDetails
    )
}

@Composable
fun FCFSContent(
    uiState: FCFSUiState,
    onCpuTimeChange: (String) -> Unit,
    onIoTimeChange: (String) -> Unit,
    onTotalWorkTimeChange: (String) -> Unit,
    onProcessCountChange: (String) -> Unit,
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
                text = "FCFS",
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.title1
            )
        }
        
        item {
            Text(
                text = "Планирование процессов",
                style = MaterialTheme.typography.caption1,
                color = MaterialTheme.colors.onSurface.copy(alpha = 0.7f)
            )
        }
        
        item {
            NumericInputField(
                value = uiState.cpuTime,
                onValueChange = onCpuTimeChange,
                label = "Время вычислений (мс)",
                enabled = !uiState.isLoading
            )
        }
        
        item {
            NumericInputField(
                value = uiState.ioTime,
                onValueChange = onIoTimeChange,
                label = "Время ввода-вывода (мс)",
                enabled = !uiState.isLoading
            )
        }
        
        item {
            NumericInputField(
                value = uiState.totalWorkTime,
                onValueChange = onTotalWorkTimeChange,
                label = "Всего времени работы (мс)",
                enabled = !uiState.isLoading
            )
        }
        
        item {
            NumericInputField(
                value = uiState.processCount,
                onValueChange = onProcessCountChange,
                label = "Количество процессов",
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
                Text(
                    text = "Ответы",
                    style = MaterialTheme.typography.title3,
                    modifier = Modifier.padding(vertical = 4.dp)
                )
            }
            
            item {
                ResultCard(
                    title = "Завершение всех процессов",
                    content = "${NumberFormatter.format(result.maxTotalTime)} мс"
                )
            }
            
            item {
                ResultCard(
                    title = "Завершение первого процесса",
                    content = "${NumberFormatter.format(result.minTotalTime)} мс"
                )
            }
            
            item {
                ResultCard(
                    title = "Макс. ожидание во всех очередях",
                    content = "${NumberFormatter.format(result.maxWaitInAllQueues)} мс"
                )
            }
            
            item {
                ResultCard(
                    title = "Мин. ожидание во всех очередях",
                    content = "${NumberFormatter.format(result.minWaitInAllQueues)} мс"
                )
            }
            
            item {
                ResultCard(
                    title = "Макс. ожидание в очереди готовых",
                    content = "${NumberFormatter.format(result.maxReadyQueueWait)} мс"
                )
            }
            
            item {
                ResultCard(
                    title = "Мин. ожидание в очереди готовых",
                    content = "${NumberFormatter.format(result.minReadyQueueWait)} мс"
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
fun FCFSScreenPreview() {
    OsrubezhTheme {
        FCFSContent(
            uiState = FCFSUiState(),
            onCpuTimeChange = {},
            onIoTimeChange = {},
            onTotalWorkTimeChange = {},
            onProcessCountChange = {},
            onCalculate = {},
            onToggleDetails = {}
        )
    }
}
