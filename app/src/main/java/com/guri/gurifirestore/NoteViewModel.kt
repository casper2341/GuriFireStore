package com.guri.gurifirestore

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.FirebaseFirestore
import com.guri.gurifirestore.model.Note
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch

class NoteViewModel : ViewModel() {
    var state by mutableStateOf(UiState(isLoading = false))
        private set
    private var events = MutableSharedFlow<NoteUIEvents>()

    init {
        handleIntent()
        state = state.copy(isLoading = true)
        getAllData()
    }

    private fun getAllData() {
        viewModelScope.launch(Dispatchers.IO) {
            val firestore = FirebaseFirestore.getInstance()
            val notesCollection = firestore.collection("note")

            notesCollection.get()
                .addOnSuccessListener { result ->
                    val notes = result.documents.map { document ->
                        document.toObject(Note::class.java)
                            ?.copy(id = document.id)  // Ensure document ID is stored
                    }.filterNotNull()
                    state = state.copy(isLoading = false, notes = notes)
                }
                .addOnFailureListener { exception ->
                    // Handle error
                }
        }
    }

    fun triggerEvent(intent: NoteUIEvents) {
        viewModelScope.launch {
            events.emit(intent)
        }
    }

    private fun handleIntent() {
        viewModelScope.launch {
            events.collect {
                when (it) {
                    is NoteUIEvents.DeleteNote -> {
                        deleteNote(it.note)
                    }

                    is NoteUIEvents.EditContent -> {
                        state = state.copy(editNote = it.note)
                    }

                    is NoteUIEvents.EditTitle -> {
                        state = state.copy(editNote = it.note)
                    }

                    is NoteUIEvents.NoteContentChanged -> {
                        state = state.copy(content = it.content)
                    }

                    is NoteUIEvents.NoteTitleChanged -> {
                        state = state.copy(title = it.title)
                    }

                    is NoteUIEvents.SaveNote -> {
                        addNote(note = it.note)
                    }

                    is NoteUIEvents.UpdateNote -> {
                        println("gurdeep update ${it.note.id}")
                        state = state.copy(
                            showEditDialog = true,
                            editNote = Note(
                                id = it.note.id,
                                title = it.note.title,
                                content = it.note.content
                            )
                        )
                        println("gurdeep note ${state.editNote?.id}")
                    }

                    NoteUIEvents.DismissDialog -> {
                        state = state.copy(showEditDialog = false)
                    }

                    is NoteUIEvents.EditNote -> {
                        state = state.copy(showEditDialog = false)
                        println("gurdeep edit $it")
                        updateNote(note = it.note)
                    }
                }
            }
        }
    }

    private fun addNote(note: Note) {
        viewModelScope.launch(Dispatchers.IO) {
            val firestore = FirebaseFirestore.getInstance()
            val notesCollection = firestore.collection("note")

            notesCollection.add(note).addOnSuccessListener { documentReference ->
                getAllData()
            }
        }
    }

    private fun updateNote(note: Note) {
        viewModelScope.launch(Dispatchers.IO) {
            val firestore = FirebaseFirestore.getInstance()
            val notesCollection = firestore.collection("note")

            if (note.id?.isNotEmpty() == true) {  // Ensure the note ID is not empty
                notesCollection.document(note.id)
                    .set(note)
                    .addOnSuccessListener {
                        getAllData()  // Refresh notes after update
                    }.addOnFailureListener {
                        println("gurdeep $it")
                        //state = state.copy(isLoading = true)
                        //getAllData()
                    }
            } else {
                println("gurdeep else")
            }
        }
    }

    private fun deleteNote(note: Note) {
        viewModelScope.launch {
            val firestore = FirebaseFirestore.getInstance()
            val notesCollection = firestore.collection("note")

            if (note.id?.isNotEmpty() == true) {  // Ensure the note ID is not empty
                notesCollection.document(note.id)
                    .delete()
                getAllData()
            }
        }
    }
}