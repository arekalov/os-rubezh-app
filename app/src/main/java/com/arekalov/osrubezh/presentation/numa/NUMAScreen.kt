package com.arekalov.osrubezh.presentation.numa

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
fun NUMAScreen(
    viewModel: NUMAViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    
    NUMAContent(
        uiState = uiState,
        onFrequencyChange = viewModel::onFrequencyChange,
        onL1L2TimeChange = viewModel::onL1L2TimeChange,
        onLocalNumaTimeChange = viewModel::onLocalNumaTimeChange,
        onOtherNumaTimeChange = viewModel::onOtherNumaTimeChange,
        onRegistersCountChange = viewModel::onRegistersCountChange,
        onL1L2CountChange = viewModel::onL1L2CountChange,
        onLocalNumaCountChange = viewModel::onLocalNumaCountChange,
        onOtherNumaCountChange = viewModel::onOtherNumaCountChange,
        onCalculate = viewModel::calculate,
        onToggleDetails = viewModel::toggleDetails
    )
}

@Composable
fun NUMAContent(
    uiState: NUMAUiState,
    onFrequencyChange: (String) -> Unit,
    onL1L2TimeChange: (String) -> Unit,
    onLocalNumaTimeChange: (String) -> Unit,
    onOtherNumaTimeChange: (String) -> Unit,
    onRegistersCountChange: (String) -> Unit,
    onL1L2CountChange: (String) -> Unit,
    onLocalNumaCountChange: (String) -> Unit,
    onOtherNumaCountChange: (String) -> Unit,
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
                text = "NUMA",
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.title1
            )
        }
        
        item {
            Text(
                text = "Частота процессора",
                style = MaterialTheme.typography.caption1,
                color = MaterialTheme.colors.onSurface.copy(alpha = 0.7f)
            )
        }
        
        item {
            NumericInputField(
                value = uiState.frequencyGHz,
                onValueChange = onFrequencyChange,
                label = "{A} Частота (ГГц)",
                enabled = !uiState.isLoading
            )
        }
        
        item {
            Text(
                text = "Времена обращения (нс)",
                style = MaterialTheme.typography.caption1,
                color = MaterialTheme.colors.onSurface.copy(alpha = 0.7f),
                modifier = Modifier.padding(top = 4.dp)
            )
        }
        
        item {
            NumericInputField(
                value = uiState.l1l2TimeNs,
                onValueChange = onL1L2TimeChange,
                label = "{B} L1-L2 кеш (нс)",
                enabled = !uiState.isLoading
            )
        }
        
        item {
            NumericInputField(
                value = uiState.localNumaTimeNs,
                onValueChange = onLocalNumaTimeChange,
                label = "{C} Локальная NUMA (нс)",
                enabled = !uiState.isLoading
            )
        }
        
        item {
            NumericInputField(
                value = uiState.otherNumaTimeNs,
                onValueChange = onOtherNumaTimeChange,
                label = "{D} Другие NUMA (нс)",
                enabled = !uiState.isLoading
            )
        }
        
        item {
            Text(
                text = "Количество команд",
                style = MaterialTheme.typography.caption1,
                color = MaterialTheme.colors.onSurface.copy(alpha = 0.7f),
                modifier = Modifier.padding(top = 4.dp)
            )
        }
        
        item {
            NumericInputField(
                value = uiState.registersCount,
                onValueChange = onRegistersCountChange,
                label = "{E} К регистрам",
                enabled = !uiState.isLoading
            )
        }
        
        item {
            NumericInputField(
                value = uiState.l1l2Count,
                onValueChange = onL1L2CountChange,
                label = "{F} К L1-L2",
                enabled = !uiState.isLoading
            )
        }
        
        item {
            NumericInputField(
                value = uiState.localNumaCount,
                onValueChange = onLocalNumaCountChange,
                label = "{G} К Local NUMA",
                enabled = !uiState.isLoading
            )
        }
        
        item {
            NumericInputField(
                value = uiState.otherNumaCount,
                onValueChange = onOtherNumaCountChange,
                label = "{H} К Other NUMA",
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
                    title = "Ответ",
                    content = "${NumberFormatter.format(result.answer)} нс"
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
fun NUMAScreenPreview() {
    OsrubezhTheme {
        NUMAContent(
            uiState = NUMAUiState(),
            onFrequencyChange = {},
            onL1L2TimeChange = {},
            onLocalNumaTimeChange = {},
            onOtherNumaTimeChange = {},
            onRegistersCountChange = {},
            onL1L2CountChange = {},
            onLocalNumaCountChange = {},
            onOtherNumaCountChange = {},
            onCalculate = {},
            onToggleDetails = {}
        )
    }
}
