package com.guri.gurifirestore

import com.guri.gurifirestore.model.Note

sealed class NoteUIEvents {
    data class NoteTitleChanged(val title: String) : NoteUIEvents()
    data class NoteContentChanged(val content: String) : NoteUIEvents()
    data class SaveNote(val note: Note) : NoteUIEvents()
    data class DeleteNote(val note: Note) : NoteUIEvents()
    data class UpdateNote(val note: Note) : NoteUIEvents()
    data class EditTitle(val note: Note) : NoteUIEvents()
    data class EditContent(val note: Note) : NoteUIEvents()
    data class EditNote(val note: Note) : NoteUIEvents()
    data object DismissDialog : NoteUIEvents()
}