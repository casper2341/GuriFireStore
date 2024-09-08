package com.guri.gurifirestore

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.guri.gurifirestore.model.Note

@Composable
fun EditDialog(
    state: UiState,
    modifier: Modifier = Modifier
        .padding(horizontal = 8.dp, vertical = 16.dp)
        .fillMaxWidth(),
    triggerEvent: (NoteUIEvents) -> Unit,
) {
    Dialog(
        onDismissRequest = { triggerEvent(NoteUIEvents.DismissDialog) },
        properties = DialogProperties(
            dismissOnBackPress = true,
            usePlatformDefaultWidth = false,
            dismissOnClickOutside = true
        ),
        content = {
            Column(
                modifier = modifier.background(
                    color = Color.White,
                    shape = RoundedCornerShape(10.dp)
                )
            ) {
                Text(text = "Edit Note", modifier = modifier)
                OutlinedTextField(
                    value = state.editNote?.title!!,
                    onValueChange = { title ->
                        triggerEvent(
                            NoteUIEvents.EditTitle(
                                Note(
                                    id = state.editNote.id,
                                    title = title,
                                    content = state.editNote.content
                                )
                            )
                        )
                    },
                    label = { Text(text = "Title") },
                    placeholder = { Text(text = "") },
                    modifier = modifier,
                )
                OutlinedTextField(
                    value = state.editNote.content,
                    onValueChange = { content ->
                        triggerEvent(
                            NoteUIEvents.EditContent(
                                Note(
                                    id = state.editNote.id,
                                    title = state.editNote.title,
                                    content = content
                                )
                            )
                        )
                    },
                    label = { Text(text = "Content") },
                    placeholder = { Text(text = "") },
                    modifier = modifier,
                )
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.End
                ) {
                    Button(
                        onClick = {
                            println("Gurdeep onclick ${state.editNote}")
                            triggerEvent(
                                NoteUIEvents.EditNote(
                                    Note(
                                        id = state.editNote.id,
                                        title = state.editNote.title,
                                        content = state.editNote.content
                                    )
                                )
                            )
                        }
                    ) {
                        Text(text = "Edit")
                    }
                }
            }
        }
    )
}