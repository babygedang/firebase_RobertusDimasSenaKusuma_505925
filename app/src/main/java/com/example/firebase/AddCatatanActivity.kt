package com.example.firebase

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.firebase.databinding.ActivityAddCatatanBinding


class AddCatatanActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddCatatanBinding
    private val noteFirestoreHelper = NoteFirestoreHelper()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddCatatanBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Menetapkan tindakan klik pada tombol kembali
        binding.buttonCencel.setOnClickListener {
            // Panggil onBackPressed() atau finish() untuk menutup aktivitas
            onBackPressed()
        }

        binding.buttonSubmit1.setOnClickListener {
            val title = binding.titleEditText.text.toString()
            val content = binding.contentEditText.text.toString()

            if (title.isNotEmpty() && content.isNotEmpty()) {
                val newNote =Catatan("", "New title", "New content")
                noteFirestoreHelper.insertNote(newNote) { noteId ->
                    // Use the noteId here
                }
                finish()
                Toast.makeText(this, "Catatan disimpan", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Judul dan konten catatan harus diisi", Toast.LENGTH_SHORT).show()
            }
        }
    }
}