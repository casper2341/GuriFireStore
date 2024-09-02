package com.guri.gurifirestore

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
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
import androidx.compose.ui.Modifier
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

                    FirebaseApp.initializeApp(this)
                    val firestore = FirebaseFirestore.getInstance()
                    val notesCollection = firestore.collection("note")

                    val notes = remember { mutableStateListOf<Note>() }

                    SideEffect {
                        notesCollection.get()
                            .addOnSuccessListener { result ->
                                for (document in result) {
                                    val note = document.toObject(Note::class.java)
                                    notes.add(note)
                                }
                            }
                            .addOnFailureListener { exception ->
                                // Handle error
                            }
                    }

                    NoteList(modifier = Modifier.padding(innerPadding), notes = notes)
                }
            }
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