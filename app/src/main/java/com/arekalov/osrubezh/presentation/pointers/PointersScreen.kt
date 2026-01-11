package com.arekalov.osrubezh.presentation.pointers

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

@Composable
fun PointersScreen(
    viewModel: PointersViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    
    PointersContent(
        uiState = uiState,
        onDirectPointersChange = viewModel::onDirectPointersChange,
        onPointerSizeBitsChange = viewModel::onPointerSizeBitsChange,
        onBlockSizeBytesChange = viewModel::onBlockSizeBytesChange,
        onFileSizeBytesChange = viewModel::onFileSizeBytesChange,
        onCalculate = viewModel::calculate,
        onToggleDetails = viewModel::toggleDetails
    )
}

@Composable
fun PointersContent(
    uiState: PointersUiState,
    onDirectPointersChange: (String) -> Unit,
    onPointerSizeBitsChange: (String) -> Unit,
    onBlockSizeBytesChange: (String) -> Unit,
    onFileSizeBytesChange: (String) -> Unit,
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
                text = "Указатели",
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.title1
            )
        }
        
        item {
            Text(
                text = "Параметры inode",
                style = MaterialTheme.typography.caption1,
                color = MaterialTheme.colors.onSurface.copy(alpha = 0.7f)
            )
        }
        
        item {
            NumericInputField(
                value = uiState.directPointers,
                onValueChange = onDirectPointersChange,
                label = "{A} Прямых указателей",
                enabled = !uiState.isLoading
            )
        }
        
        item {
            NumericInputField(
                value = uiState.pointerSizeBits,
                onValueChange = onPointerSizeBitsChange,
                label = "{B} Размер указателя (бит)",
                enabled = !uiState.isLoading
            )
        }
        
        item {
            Text(
                text = "Параметры файловой системы",
                style = MaterialTheme.typography.caption1,
                color = MaterialTheme.colors.onSurface.copy(alpha = 0.7f),
                modifier = Modifier.padding(top = 4.dp)
            )
        }
        
        item {
            NumericInputField(
                value = uiState.blockSizeBytes,
                onValueChange = onBlockSizeBytesChange,
                label = "{C} Размер блока (байт)",
                enabled = !uiState.isLoading
            )
        }
        
        item {
            NumericInputField(
                value = uiState.fileSizeBytes,
                onValueChange = onFileSizeBytesChange,
                label = "{D} Размер файла (байт)",
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
                    content = "${result.totalBlocks} блоков"
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
                        content = buildString {
                            append("## Детальные расчеты\n\n")
                            append("### Указателей в блоке\n")
                            append("```\n")
                            append("ppp = (${result.blockSizeBytes} * 8) / ${result.pointerSizeBits}\n")
                            append("    = ${result.pointersPerBlock}\n")
                            append("```\n\n")
                            
                            append("### Системные блоки (метаданные)\n")
                            append("- Размер: **${result.systemBytes} байт**\n")
                            append("- Блоков: **${result.systemBlocks}**\n\n")
                            
                            append("### Файловые блоки (данные)\n")
                            append("- Размер: **${result.fileBytes} байт**\n")
                            append("- Блоков: **${result.fileBlocks}**\n\n")
                            
                            append("### Итого\n")
                            append("```\n")
                            append("Всего байт:  ${result.totalBytes}\n")
                            append("Всего блоков: ${result.totalBlocks}\n")
                            append("             (${result.systemBlocks} системных + ${result.fileBlocks} файловых)\n")
                            append("```")
                        }
                    )
                }
            }
        }
    }
}

@Preview(device = Devices.WEAR_OS_SMALL_ROUND, showSystemUi = true)
@Composable
fun PointersScreenPreview() {
    OsrubezhTheme {
        PointersContent(
            uiState = PointersUiState(),
            onDirectPointersChange = {},
            onPointerSizeBitsChange = {},
            onBlockSizeBytesChange = {},
            onFileSizeBytesChange = {},
            onCalculate = {},
            onToggleDetails = {}
        )
    }
}
