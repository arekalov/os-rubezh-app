package com.arekalov.osrubezh.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.wear.compose.material.MaterialTheme
import androidx.wear.compose.material.Text
import com.arekalov.osrubezh.presentation.theme.OsrubezhTheme

@Composable
fun ResultCard(
    title: String,
    content: String,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .background(MaterialTheme.colors.surface)
            .padding(12.dp)
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.title3,
            color = MaterialTheme.colors.primary,
            modifier = Modifier.padding(bottom = 4.dp)
        )
        Text(
            text = content,
            style = MaterialTheme.typography.body2,
            color = MaterialTheme.colors.onSurface
        )
    }
}

@Preview(device = Devices.WEAR_OS_SMALL_ROUND, showSystemUi = true)
@Composable
fun ResultCardPreview() {
    OsrubezhTheme {
        ResultCard(
            title = "Результат",
            content = "42",
            modifier = Modifier.padding(16.dp)
        )
    }
}

@Preview(device = Devices.WEAR_OS_SMALL_ROUND, showSystemUi = true)
@Composable
fun ResultCardLongPreview() {
    OsrubezhTheme {
        ResultCard(
            title = "Подробный результат",
            content = "Значение: 100\nВремя: 2 мс\nСкорость: 50 оп/с",
            modifier = Modifier.padding(16.dp)
        )
    }
}
