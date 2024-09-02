package com.guri.gurifirestore

import com.guri.gurifirestore.model.Note

data class UiState(
    val title: String = "",
    val content: String = "",
    val notes : List<Note> = emptyList(),
    val isLoading: Boolean = false
)