package com.life.totally.great.presentation.components.buttons

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.unit.dp
import com.life.totally.great.R
import com.life.totally.great.presentation.components.text.WhiteButtonText
import com.life.totally.great.presentation.ui.theme.Purple40
import com.life.totally.great.presentation.ui.theme.PurpleGrey40


@Composable
fun VioletButton(
    modifier: Modifier = Modifier,
    @StringRes labelId: Int = R.string.search,
    actionDelegate: () -> Unit = {}
) {
    StyledButton(
        modifier = modifier,
        labelId = labelId,
        onClickAction = actionDelegate,
        containerColor = Purple40,
        contentColor = White,
        disabledContainerColor = PurpleGrey40,
        disabledContentColor = White
    )
}

@Composable
fun StyledButton(
    modifier: Modifier = Modifier,
    @StringRes labelId: Int,
    onClickAction: () -> Unit,
    containerColor: Color,
    contentColor: Color,
    disabledContainerColor: Color,
    disabledContentColor: Color
) {
    Button(
        onClick = onClickAction, // Simplified onClick
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(23.dp),
        colors = ButtonColors( // Use ButtonDefaults.buttonColors for better theming integration
            containerColor = containerColor,
            contentColor = contentColor,
            disabledContainerColor = disabledContainerColor,
            disabledContentColor = disabledContentColor
        )
    ) {
        WhiteButtonText(labelId = labelId) // Assuming WhiteText handles text styling
    }
}