package com.arekalov.osrubezh.presentation.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.wear.compose.material.Button
import androidx.wear.compose.material.ButtonDefaults
import androidx.wear.compose.material.Text
import com.arekalov.osrubezh.presentation.theme.OsrubezhTheme

@Composable
fun CalculatorButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true
) {
    Button(
        onClick = onClick,
        modifier = modifier
            .fillMaxWidth()
            .height(40.dp),
        enabled = enabled,
        colors = ButtonDefaults.primaryButtonColors()
    ) {
        Text(
            text = text,
            textAlign = TextAlign.Center
        )
    }
}

@Preview(device = Devices.WEAR_OS_SMALL_ROUND, showSystemUi = true)
@Composable
fun CalculatorButtonPreview() {
    OsrubezhTheme {
        CalculatorButton(
            text = "Вычислить",
            onClick = {}
        )
    }
}
