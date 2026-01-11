package com.arekalov.osrubezh.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.wear.compose.material.MaterialTheme
import androidx.wear.compose.material.Text

@Composable
fun MarkdownCard(
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
        val lines = content.lines()
        var inCodeBlock = false
        
        for (line in lines) {
            when {
                line.startsWith("```") -> {
                    inCodeBlock = !inCodeBlock
                }
                inCodeBlock -> {
                    Text(
                        text = line,
                        style = MaterialTheme.typography.caption2.copy(
                            fontFamily = FontFamily.Monospace,
                            fontSize = 10.sp
                        ),
                        color = MaterialTheme.colors.primary.copy(alpha = 0.9f),
                        modifier = Modifier.padding(vertical = 1.dp)
                    )
                }
                line.startsWith("## ") -> {
                    Text(
                        text = line.substring(3),
                        style = MaterialTheme.typography.title3.copy(
                            fontWeight = FontWeight.Bold
                        ),
                        color = MaterialTheme.colors.primary,
                        modifier = Modifier.padding(top = 6.dp, bottom = 3.dp)
                    )
                }
                line.startsWith("### ") -> {
                    Text(
                        text = line.substring(4),
                        style = MaterialTheme.typography.body2.copy(
                            fontWeight = FontWeight.SemiBold
                        ),
                        color = MaterialTheme.colors.onSurface,
                        modifier = Modifier.padding(top = 4.dp, bottom = 2.dp)
                    )
                }
                line.startsWith("- ") -> {
                    val text = line.substring(2)
                    val annotated = buildAnnotatedString {
                        append("• ")
                        
                        var currentIndex = 0
                        var inBold = false
                        var boldStart = 0
                        
                        for (i in text.indices) {
                            if (i < text.length - 1 && text[i] == '*' && text[i + 1] == '*') {
                                if (inBold) {
                                    withStyle(SpanStyle(fontWeight = FontWeight.Bold)) {
                                        append(text.substring(boldStart, i))
                                    }
                                    inBold = false
                                    currentIndex = i + 2
                                } else {
                                    append(text.substring(currentIndex, i))
                                    inBold = true
                                    boldStart = i + 2
                                    currentIndex = i + 2
                                }
                            }
                        }
                        
                        if (currentIndex < text.length) {
                            if (inBold) {
                                withStyle(SpanStyle(fontWeight = FontWeight.Bold)) {
                                    append(text.substring(boldStart))
                                }
                            } else {
                                append(text.substring(currentIndex))
                            }
                        }
                    }
                    
                    Text(
                        text = annotated,
                        style = MaterialTheme.typography.body2.copy(fontSize = 11.sp),
                        color = MaterialTheme.colors.onSurface,
                        modifier = Modifier
                            .padding(top = 1.dp, bottom = 1.dp, start = 4.dp)
                    )
                }
                line.isNotEmpty() -> {
                    Text(
                        text = line,
                        style = MaterialTheme.typography.body2.copy(fontSize = 11.sp),
                        color = MaterialTheme.colors.onSurface,
                        modifier = Modifier.padding(vertical = 1.dp)
                    )
                }
            }
        }
    }
}
