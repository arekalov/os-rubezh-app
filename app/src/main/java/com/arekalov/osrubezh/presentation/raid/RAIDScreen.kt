package com.arekalov.osrubezh.presentation.raid

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
fun RAIDScreen(
    viewModel: RAIDViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    
    RAIDContent(
        uiState = uiState,
        onRaidLevelChange = viewModel::onRaidLevelChange,
        onDiskCountChange = viewModel::onDiskCountChange,
        onDiskSizeChange = viewModel::onDiskSizeChange,
        onBlockSizeChange = viewModel::onBlockSizeChange,
        onSuSizeChange = viewModel::onSuSizeChange,
        onBlockReadChange = viewModel::onBlockReadChange,
        onBlockWriteChange = viewModel::onBlockWriteChange,
        onSuCalcChange = viewModel::onSuCalcChange,
        onCalculate = viewModel::calculate,
        onToggleDetails = viewModel::toggleDetails
    )
}

@Composable
fun RAIDContent(
    uiState: RAIDUiState,
    onRaidLevelChange: (String) -> Unit,
    onDiskCountChange: (String) -> Unit,
    onDiskSizeChange: (String) -> Unit,
    onBlockSizeChange: (String) -> Unit,
    onSuSizeChange: (String) -> Unit,
    onBlockReadChange: (String) -> Unit,
    onBlockWriteChange: (String) -> Unit,
    onSuCalcChange: (String) -> Unit,
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
                text = "RAID",
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.title1
            )
        }
        
        item {
            NumericInputField(
                value = uiState.raidLevel,
                onValueChange = onRaidLevelChange,
                label = "{A} Уровень RAID",
                enabled = !uiState.isLoading
            )
        }
        
        item {
            NumericInputField(
                value = uiState.diskCount,
                onValueChange = onDiskCountChange,
                label = "{B} Кол-во дисков",
                enabled = !uiState.isLoading
            )
        }
        
        item {
            NumericInputField(
                value = uiState.diskSizeGB,
                onValueChange = onDiskSizeChange,
                label = "{C} Размер диска (ГБ)",
                enabled = !uiState.isLoading
            )
        }
        
        item {
            NumericInputField(
                value = uiState.blockSize,
                onValueChange = onBlockSizeChange,
                label = "{D} Размер блока (байт)",
                enabled = !uiState.isLoading
            )
        }
        
        item {
            Text(
                text = "Скорость операций",
                style = MaterialTheme.typography.caption1,
                color = MaterialTheme.colors.onSurface.copy(alpha = 0.7f),
                modifier = Modifier.padding(top = 4.dp)
            )
        }
        
        item {
            NumericInputField(
                value = uiState.suSize,
                onValueChange = onSuSizeChange,
                label = "{E} Страйп-unit (блоков)",
                enabled = !uiState.isLoading
            )
        }
        
        item {
            NumericInputField(
                value = uiState.blockRead,
                onValueChange = onBlockReadChange,
                label = "{F} Чтение блока (мкс)",
                enabled = !uiState.isLoading
            )
        }
        
        item {
            NumericInputField(
                value = uiState.blockWrite,
                onValueChange = onBlockWriteChange,
                label = "{G} Запись блока (мкс)",
                enabled = !uiState.isLoading
            )
        }
        
        item {
            NumericInputField(
                value = uiState.suCalc,
                onValueChange = onSuCalcChange,
                label = "{H} Вычисление SU (мкс)",
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
                    title = "Время восстановления",
                    content = String.format("%.7f мин", result.answerMinutes)
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
fun RAIDScreenPreview() {
    OsrubezhTheme {
        RAIDContent(
            uiState = RAIDUiState(),
            onRaidLevelChange = {},
            onDiskCountChange = {},
            onDiskSizeChange = {},
            onBlockSizeChange = {},
            onSuSizeChange = {},
            onBlockReadChange = {},
            onBlockWriteChange = {},
            onSuCalcChange = {},
            onCalculate = {},
            onToggleDetails = {}
        )
    }
}
