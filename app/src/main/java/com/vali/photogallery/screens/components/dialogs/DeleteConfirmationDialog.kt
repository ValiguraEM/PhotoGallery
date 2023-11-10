package com.vali.photogallery.screens.components.dialogs

import androidx.compose.material.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.vali.photogallery.R

@Composable
fun DeleteConfirmationDialog(onDismiss: () -> Unit, onConfirmDelete: () -> Unit) {
    AlertDialog(
        backgroundColor = MaterialTheme.colorScheme.surface,
        shape = MaterialTheme.shapes.medium,
        onDismissRequest = onDismiss,
        title = {
            Text(
                stringResource(R.string.dialog_delete_image),
                style = MaterialTheme.typography.labelLarge
            )
        },
        text = {
            Text(
                stringResource(R.string.dialog_delete_text),
                style = MaterialTheme.typography.bodyLarge
            )
        },
        confirmButton = {
            Button(onClick = onConfirmDelete) {
                Text(
                    stringResource(R.string.dialog_delete),
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        },
        dismissButton = {
            Button(onClick = onDismiss) {
                Text(
                    stringResource(R.string.dialog_cancel),
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        }
    )
}