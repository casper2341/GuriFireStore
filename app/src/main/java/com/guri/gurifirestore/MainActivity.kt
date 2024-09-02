package com.guri.gurifirestore

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.FirebaseApp
import com.google.firebase.firestore.FirebaseFirestore
import com.guri.gurifirestore.model.Note
import com.guri.gurifirestore.ui.theme.GuriFireStoreTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            GuriFireStoreTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    val viewModel : NoteViewModel = ViewModelProvider(this)[NoteViewModel::class.java]
                    FirebaseApp.initializeApp(this)

                    val notes = viewModel.state.notes
                    val isLoading = viewModel.state.isLoading
                    if (isLoading) {
                        ProgressIndicator()
                    } else {
                        NoteList(modifier = Modifier.padding(innerPadding), notes = notes)
                    }
                }
            }
        }
    }

    @Composable
    private fun ProgressIndicator() {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text(text = "Loading...")
        }
    }

    @Composable
    fun NoteList(modifier : Modifier, notes: List<Note>) {
        LazyColumn(modifier = modifier) {
            items(notes) { note ->
                Text(text = note.title)
                Text(text = note.content)
            }
        }
    }
}