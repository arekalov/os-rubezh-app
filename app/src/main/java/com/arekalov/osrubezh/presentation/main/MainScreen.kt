package com.arekalov.osrubezh.presentation.main

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.wear.compose.material.Chip
import androidx.wear.compose.material.ChipDefaults
import androidx.wear.compose.material.ScalingLazyColumn
import androidx.wear.compose.material.Text
import androidx.wear.compose.material.items
import com.arekalov.osrubezh.presentation.theme.OsrubezhTheme

data class MenuItem(
    val title: String,
    val route: String
)

@Composable
fun MainScreen(
    onNavigate: (String) -> Unit
) {
    val menuItems = listOf(
        MenuItem("RAID", "raid"),
        MenuItem("Указатели / Stripe Units", "pointers"),
        MenuItem("NUMA", "numa")
    )

    ScalingLazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(
            top = 32.dp,
            start = 16.dp,
            end = 16.dp,
            bottom = 32.dp
        ),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        item {
            Text(
                text = "Решаторы",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp),
                textAlign = TextAlign.Center,
                style = androidx.wear.compose.material.MaterialTheme.typography.title1
            )
        }
        
        items(menuItems) { item ->
            Chip(
                onClick = { onNavigate(item.route) },
                label = {
                    Text(
                        text = item.title,
                        textAlign = TextAlign.Center,
                        maxLines = 2
                    )
                },
                modifier = Modifier.fillMaxWidth(),
                colors = ChipDefaults.primaryChipColors()
            )
        }
    }
}

@Preview(device = Devices.WEAR_OS_SMALL_ROUND, showSystemUi = true)
@Composable
fun MainScreenPreview() {
    OsrubezhTheme {
        MainScreen(onNavigate = {})
    }
}

@Preview(device = Devices.WEAR_OS_SQUARE, showSystemUi = true)
@Composable
fun MainScreenPreviewSquare() {
    OsrubezhTheme {
        MainScreen(onNavigate = {})
    }
}
