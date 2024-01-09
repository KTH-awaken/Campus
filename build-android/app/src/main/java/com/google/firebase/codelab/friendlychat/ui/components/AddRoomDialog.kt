package com.google.firebase.codelab.friendlychat.ui.components
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.input.KeyboardType

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddRoomDialog(
    onDismissRequest: () -> Unit,
    onConfirm: (String, Double) -> Unit,
) {
    var roomName by remember { mutableStateOf("") }
    var roomSize by remember { mutableStateOf("") }

    AlertDialog(
        title = {
            Text(text = "Add room")
        },
        text = {
            Column {
                OutlinedTextField(
                    value = roomName,
                    onValueChange = { roomName = it },
                    label = { Text("Room name") },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = roomSize,
                    onValueChange = { roomSize = it },
                    label = { Text("Room size") },
                    keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        onDismissRequest = {
            onDismissRequest()
        },
        confirmButton = {
            TextButton(
                onClick = {
                    val size = roomSize.toDoubleOrNull()
                    if (roomName.isNotBlank() && size != null) {
                        onConfirm(roomName, size)
                    }
                }
            ) {
                Text("Confirm")
            }
        },
        dismissButton = {
            TextButton(
                onClick = {
                    onDismissRequest()
                }
            ) {
                Text("Dismiss")
            }
        },
    )
}
