package com.guri.gurifirestore

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
                    if (isLoading) {
                        ProgressIndicator()
                    } else {
                        Column {
                            NoteInput(state = state)

                            NoteList(
                                modifier = Modifier.padding(innerPadding),
                                notes = notes
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun NoteInput(state: UiState) {
    OutlinedTextField(
        value = state.title,
        onValueChange = { newName ->

        },
        label = { Text(text = "Title") },
        placeholder = { Text(text = "") },
        modifier = Modifier.fillMaxWidth(),
    )
    Spacer(modifier = Modifier.height(4.dp))
    OutlinedTextField(
        value = state.content,
        onValueChange = { content ->

        },
        label = { Text(text = "Content") },
        placeholder = { Text(text = "") },
        modifier = Modifier.fillMaxWidth(),
    )
    Spacer(modifier = Modifier.height(4.dp))

}

@Composable
private fun ProgressIndicator() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text(text = "Loading...")
    }
}

@Composable
fun NoteList(modifier: Modifier, notes: List<Note>) {
    LazyColumn(modifier = modifier) {
        items(notes) { note ->
            Text(text = note.title)
            Text(text = note.content)
        }
    }
}