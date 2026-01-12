package com.arekalov.osrubezh.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.wear.compose.material.MaterialTheme
import androidx.wear.compose.material.Text
import com.arekalov.osrubezh.presentation.theme.OsrubezhTheme

@Composable
fun NumericInputField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    modifier: Modifier = Modifier,
    enabled: Boolean = true
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .background(MaterialTheme.colors.surface)
            .padding(horizontal = 12.dp, vertical = 8.dp)
    ) {
        if (value.isEmpty()) {`
            Text(
                text = label,
                style = MaterialTheme.typography.body2,
                color = MaterialTheme.colors.onSurface.copy(alpha = 0.5f),
                modifier = Modifier.align(Alignment.CenterStart)
            )
        }
        
        BasicTextField(
            value = value,
            onValueChange = { newValue ->
                if (newValue.isEmpty() || newValue.all { it.isDigit() }) {
                    onValueChange(newValue)
                }
            },
            enabled = enabled,
            textStyle = TextStyle(
                color = MaterialTheme.colors.onSurface,
                fontSize = 14.sp,
                textAlign = TextAlign.Start
            ),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number
            ),
            singleLine = true,
            cursorBrush = SolidColor(MaterialTheme.colors.primary),
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.CenterStart)
        )
    }
}

@Preview(device = Devices.WEAR_OS_SMALL_ROUND, showSystemUi = true)
@Composable
fun NumericInputFieldPreview() {
    OsrubezhTheme {
        Box(modifier = Modifier.padding(16.dp)) {
            NumericInputField(
                value = "",
                onValueChange = {},
                label = "Введите число"
            )
        }
    }
}

@Preview(device = Devices.WEAR_OS_SMALL_ROUND, showSystemUi = true)
@Composable
fun NumericInputFieldFilledPreview() {
    OsrubezhTheme {
        Box(modifier = Modifier.padding(16.dp)) {
            NumericInputField(
                value = "123",
                onValueChange = {},
                label = "Введите число"
            )
        }
    }
}
