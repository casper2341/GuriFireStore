package com.guri.gurifirestore

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.FirebaseFirestore
import com.guri.gurifirestore.model.Note

class NoteViewModel : ViewModel() {
    var state by mutableStateOf(UiState(isLoading = false))
        private set

    init {
        state = state.copy(isLoading = true)
        val firestore = FirebaseFirestore.getInstance()
        val notesCollection = firestore.collection("note")

        notesCollection.get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    val note = document.toObject(Note::class.java)
                    state = state.copy(isLoading = false, notes = state.notes + note)
                }
            }
            .addOnFailureListener { exception ->
                // Handle error
            }
    }
}