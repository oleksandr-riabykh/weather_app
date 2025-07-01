package com.life.totally.great.presentation.components.text

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Preview(showBackground = true)
@Composable
fun TitleLabel(
    modifier: Modifier = Modifier,
    text: String = "Some text"
) {
    Text(
        text = text,
        style = MaterialTheme.typography.headlineSmall
    )
}

@Composable
fun GrayStringText(
    text: String,
    modifier: Modifier = Modifier
) {
    Text(
        text = text,
        fontSize = 17.sp,
        color = Color.DarkGray,
        modifier = modifier.padding(horizontal = 4.dp, vertical = 6.dp)
    )
}