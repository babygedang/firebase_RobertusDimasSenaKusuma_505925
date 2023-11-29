package com.example.firebase

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreSettings
import com.google.firebase.firestore.Source

class NoteFirestoreHelper {

    private val firestore = FirebaseFirestore.getInstance().apply {
        firestoreSettings = FirebaseFirestoreSettings.Builder()
            .setPersistenceEnabled(false)
            .build()
    }
    private val notesCollectionRef = firestore.collection("allnotes")


    fun insertNote(note: Catatan, callback: (String) -> Unit) {
        notesCollectionRef.add(note).addOnSuccessListener { documentReference ->
            val noteId = documentReference.id
            callback.invoke(noteId)
        }
    }

    fun getAllNotes(callback: (List<Catatan>) -> Unit) {
        notesCollectionRef.get().addOnSuccessListener { result ->
            val noteList = mutableListOf<Catatan>()
            for (document in result) {
                val note = document.toObject(Catatan::class.java)
                noteList.add(note)
            }
            callback.invoke(noteList)
        }
    }

    fun updateNote(noteId: String, updatedNote:Catatan) {
        val noteRef = notesCollectionRef.document(noteId)
        noteRef.set(updatedNote)
    }

    fun getNoteByID(noteId: String, callback: (Catatan?) -> Unit) {
        val noteRef = notesCollectionRef.document(noteId)
        try {
            noteRef.get(Source.SERVER).addOnSuccessListener { document ->
                if (document.exists()) {
                    val note = document.toObject(Catatan::class.java)
                    callback.invoke(note)
                } else {
                    callback.invoke(null)
                }
            }
        } catch (e: Exception) {
            noteRef.get(Source.CACHE).addOnSuccessListener { document ->
                if (document.exists()) {
                    val note = document.toObject(Catatan::class.java)
                    callback.invoke(note)
                } else {
                    callback.invoke(null)
                }
            }
        }
    }

    fun deleteNote(noteId: String) {
        val noteRef = notesCollectionRef.document(noteId)
        noteRef.delete()
    }
}