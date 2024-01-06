@file:OptIn(ExperimentalMaterial3Api::class)

package com.google.firebase.codelab.friendlychat.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.FocusState
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.campus.ui.viewmodels.ChatVM
import com.google.firebase.codelab.friendlychat.R


@Composable
fun CustomBasicTextField(vm: ChatVM) {
    var text by remember { mutableStateOf("") }
    var isFocused by remember { mutableStateOf(false) }
    val focusRequester = remember { FocusRequester() }

    Divider(thickness = 1.dp, color = MaterialTheme.colorScheme.outline)
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 10.dp, bottom = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = { /* Handle attachment */ }) {
            Icon(
                painter = painterResource(id = R.drawable.add_photo_alternate_fill0_wght400_grad0_opsz24),
                contentDescription = "Attachment",
                tint = MaterialTheme.colorScheme.tertiary,
                modifier = Modifier.size(30.dp),
            )
        }

        BasicTextField(
            value = text,
            onValueChange = { text = it },
            singleLine = true,
            keyboardOptions = KeyboardOptions.Default.copy(capitalization = KeyboardCapitalization.Sentences),
            textStyle = TextStyle(
                color = Color.Black,
                fontSize = 12.sp // Adjust font size as needed
            ),
            modifier = Modifier
                .weight(1f)
                .height(35.dp)
                .padding(horizontal = 8.dp)
                .clip(RoundedCornerShape(20.dp))
                .background(
                    color = if (isFocused) MaterialTheme.colorScheme.onTertiary else MaterialTheme.colorScheme.onTertiary,
                    shape = RoundedCornerShape(20.dp)
                )
                .border(
                    width = if (isFocused) 2.dp else 0.dp,
                    color = if (isFocused) MaterialTheme.colorScheme.tertiary else Color.Transparent,
                    shape = RoundedCornerShape(20.dp)
                )
                .focusRequester(focusRequester)
                .onFocusChanged { focusState ->
                    isFocused = focusState.isFocused
                    if (!isFocused && text.isEmpty()) {
                        focusRequester.freeFocus()
                    }
                }
                .imePadding(),
            decorationBox = { innerTextField ->
                Box(
                    contentAlignment = Alignment.CenterStart, // Center text horizontally
                    modifier = Modifier
                        .padding(horizontal = 20.dp)
                ) {
                    if (text.isEmpty() && !isFocused) {
                        Text("Message Campus Flemingsberg", color = MaterialTheme.colorScheme.secondary)
                    }
                    innerTextField()
                }
            }
        )

        if (text!=""){
            IconButton(onClick = {
                if (text.isNotBlank()) {
                    val timeStamp: String = System.currentTimeMillis().toString()
                    vm.sendMessage(text, vm.auth.currentUser!!.displayName!!, vm.auth.currentUser!!.photoUrl.toString(),timeStamp)
                    text = ""
                }
            }) {
                Icon(
                    Icons.Default.Send, contentDescription = "Send",
                    tint = MaterialTheme.colorScheme.tertiary,
                    modifier = Modifier.size(30.dp),
                )
            }
        }
    }
}
