package com.life.totally.great.presentation.components.text

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.life.totally.great.R

@Composable
fun WhiteButtonText(
    modifier: Modifier = Modifier,
    @StringRes labelId: Int = R.string.search
) {
    Text(
        text = stringResource(labelId),
        fontSize = 17.sp,
        color = Color.White,
        modifier = modifier.padding(horizontal = 30.dp, vertical = 6.dp)
    )
}

@Preview(showBackground = true)
@Composable
fun TitleLabel(
    modifier: Modifier = Modifier,
    text: String = "Some text"
) {
    Text(
        text = text,
        style = MaterialTheme.typography.headlineMedium
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