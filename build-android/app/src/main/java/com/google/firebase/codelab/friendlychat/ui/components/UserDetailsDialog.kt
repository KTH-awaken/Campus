package com.google.firebase.codelab.friendlychat.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import com.google.firebase.codelab.friendlychat.model.User

@Composable
fun UserDetailsDialog(user: User, onDismissRequest: () -> Unit) {
    val customCardColors = CardDefaults.cardColors(
        containerColor = MaterialTheme.colorScheme.onBackground,
    )
    AlertDialog(
        onDismissRequest = onDismissRequest,
        confirmButton = {
            TextButton(onClick = onDismissRequest) {
                Text("OK")
            }
        },
        text = {
            Column {
                Text("Email: ${user.email ?: "Unknown"}")
                Text("Room: ${user.room ?: "Unknown"}")
                Text("Last Online: ${user.lastOnline ?: "Unknown"}")
                // Add more details as needed
            }
        },
        title = {
            Text(text = user.displayName ?: "Unknown")
        },
        containerColor = MaterialTheme.colorScheme.onBackground

    )
}