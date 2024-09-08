package com.guri.gurifirestore

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelProvider
import com.guri.gurifirestore.model.Note
import com.guri.gurifirestore.ui.theme.GuriFireStoreTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            GuriFireStoreTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    val viewModel: NoteViewModel =
                        ViewModelProvider(this)[NoteViewModel::class.java]

                    val state = viewModel.state
                    val notes = state.notes
                    val isLoading = state.isLoading
                    val showEditDialog = state.showEditDialog

                    if (showEditDialog) {
                        EditDialog(
                            state = state,
                            triggerEvent = {
                                viewModel.triggerEvent(it)
                            },
                        )
                    }

                    if (isLoading) {
                        ProgressIndicator()
                    } else {
                        Column(modifier = Modifier.padding(innerPadding)) {
                            NoteInput(
                                state = state,
                                onAddClick = {
                                    viewModel.triggerEvent(
                                        NoteUIEvents.SaveNote(
                                            Note(
                                                title = state.title,
                                                content = state.content
                                            )
                                        )
                                    )
                                },
                                onUpdateTitle = {
                                    viewModel.triggerEvent(
                                        NoteUIEvents.NoteTitleChanged(
                                            it
                                        )
                                    )
                                },
                                onUpdateContent = {
                                    viewModel.triggerEvent(
                                        NoteUIEvents.NoteContentChanged(
                                            it
                                        )
                                    )
                                })

                            NoteList(
                                modifier = Modifier.padding(innerPadding),
                                notes = notes,
                                OnUpdateClick = {
                                    viewModel.triggerEvent(NoteUIEvents.UpdateNote(it))
                                },
                                OnDeleteClick = {
                                    viewModel.triggerEvent(NoteUIEvents.DeleteNote(it))
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun NoteInput(
    state: UiState,
    onAddClick: () -> Unit,
    onUpdateTitle: (String) -> Unit,
    onUpdateContent: (String) -> Unit
) {
    OutlinedTextField(
        value = state.title,
        onValueChange = { title ->
            onUpdateTitle(title)
        },
        label = { Text(text = "Title") },
        placeholder = { Text(text = "") },
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
    )
    Spacer(modifier = Modifier.height(4.dp))
    OutlinedTextField(
        value = state.content,
        onValueChange = { content ->
            onUpdateContent(content)
        },
        label = { Text(text = "Content") },
        placeholder = { Text(text = "") },
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
    )
    Spacer(modifier = Modifier.height(4.dp))
    if (state.title.isNotBlank() && state.content.isNotBlank()) {
        AddButton(onClick = onAddClick)
    }
    Spacer(modifier = Modifier.height(4.dp))
}

@Composable
private fun ProgressIndicator() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text(text = "Loading...")
    }
}

@Composable
fun NoteList(
    modifier: Modifier,
    notes: List<Note>,
    OnUpdateClick: (Note) -> Unit,
    OnDeleteClick: (Note) -> Unit
) {
    LazyColumn(modifier = modifier.padding(horizontal = 8.dp)) {
        items(notes) { note ->
            NoteItem(
                note = note,
                onUpdateClick = OnUpdateClick,
                onDeleteClick = OnDeleteClick
            )
        }
    }
}

@Composable
fun NoteItem(
    note: Note,
    onUpdateClick: (Note) -> Unit,
    onDeleteClick: (Note) -> Unit
) {
    var opened by remember { mutableStateOf(false) }
    Column(
        modifier = Modifier
            .padding(vertical = 2.dp)
            .fillMaxWidth()
            .border(width = 1.dp, color = Color.Black, shape = RoundedCornerShape(8.dp))
            .clickable {
                opened = !opened
            }
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = note.title)
            if (opened) {
                Icon(imageVector = Icons.Default.KeyboardArrowDown, contentDescription = "")
            } else {
                Icon(imageVector = Icons.Default.KeyboardArrowUp, contentDescription = "")
            }
        }
        if (opened) {
            Spacer(modifier = Modifier.padding(2.dp))
            Text(text = note.content)
            Spacer(modifier = Modifier.padding(2.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Button(
                    modifier = Modifier.weight(1f),
                    onClick = {
                        onUpdateClick.invoke(note)
                    }) {
                    Text("Update")
                }
                Spacer(modifier = Modifier.weight(1f))
                Button(
                    modifier = Modifier.weight(1f),
                    onClick = {
                        onDeleteClick.invoke(note)
                    }) {
                    Text("Delete")
                }
            }
        }
    }
}

@Composable
fun AddButton(onClick: () -> Unit) {
    Button(
        modifier = Modifier.padding(horizontal = 16.dp),
        onClick = {
            onClick.invoke()
        }) {
        Text("Add")
    }
}