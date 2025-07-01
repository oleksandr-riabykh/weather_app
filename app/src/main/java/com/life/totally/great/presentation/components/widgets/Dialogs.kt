package com.life.totally.great.presentation.components.widgets

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.life.totally.great.R

@Composable
fun LocationPermissionDialog(
    onGrantClick: () -> Unit, onDismissClick: () -> Unit = {}
) {
    AlertDialog(
        onDismissRequest = onDismissClick,
        title = { Text(stringResource(R.string.location_permission)) },
        text = { Text(stringResource(R.string.this_app_needs_access_to_your_location_to_provide_local_weather_updates)) },
        confirmButton = {
            TextButton(onClick = onGrantClick) {
                Text(stringResource(R.string.grant))
            }
        },
        dismissButton = {
            TextButton(onClick = onDismissClick) {
                Text(stringResource(R.string.cancel))
            }
        })
}

@Composable
fun LocationRationaleDialog(
    onDismissRequest: () -> Unit, onConfirm: () -> Unit
) {
    AlertDialog(onDismissRequest = onDismissRequest, title = {
        Text(text = stringResource(R.string.location_permission_required))
    }, text = {
        Text(
            text = stringResource(R.string.location_rationale_message)
        )
    }, confirmButton = {
        TextButton(onClick = onConfirm) {
            Text(text = stringResource(R.string.allow))
        }
    }, dismissButton = {
        TextButton(onClick = onDismissRequest) {
            Text(text = stringResource(R.string.cancel))
        }
    })
}