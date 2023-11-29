package com.example.firebase

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.firebase.databinding.ActivityAddCatatanBinding
import com.google.firebase.firestore.FirebaseFirestore

class AddCatatanActivity : AppCompatActivity() {

    // Variabel yang ditunda inisialisasinya untuk View Binding dan Firebase Firestore.
    private lateinit var binding: ActivityAddCatatanBinding
    private lateinit var firestore: FirebaseFirestore // Menginisialisasi variabel untuk FirebaseFirestore.

    // Metode yang di-override yang dipanggil ketika aktivitas dibuat.
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Inflating layout menggunakan View Binding dan menetapkannya sebagai tampilan konten.
        binding = ActivityAddCatatanBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Mendapatkan instance FirebaseFirestore.
        firestore = FirebaseFirestore.getInstance()

        // Menyiapkan pendengar klik untuk tombol "Kembali" agar kembali ke layar sebelumnya.
        binding.buttonCencel.setOnClickListener {
            onBackPressed()
        }

        // Menyiapkan pendengar klik untuk tombol "Simpan" agar menyimpan catatan ke Firestore.
        binding.buttonSubmit1.setOnClickListener {

            // Mendapatkan teks dari EditText judul dan konten.
            val title = binding.titleEditText.text.toString()
            val content = binding.contentEditText.text.toString()

            // Memeriksa apakah judul dan konten tidak kosong.
            if (title.isNotEmpty() && content.isNotEmpty()) {

                // Membuat objek catatan dengan judul dan konten yang diambil.
                val Catatan = Catatan(title = title, content = content)

                // Menyimpan catatan ke Firestore.
                saveNoteToFirestore(Catatan)
            } else {

                // Menampilkan pesan kesalahan jika judul atau konten kosong.
                Toast.makeText(this, "Judul dan konten catatan harus diisi", Toast.LENGTH_SHORT).show()
            }
        }
    }

    // Metode untuk menyimpan catatan ke Firestore.
    private fun saveNoteToFirestore(Catatan:Catatan) {
        val noteMap = hashMapOf(
            "title" to Catatan.title,
            "content" to Catatan.content
        )

        // Menambahkan data catatan ke koleksi "notes" di Firestore.
        firestore.collection("notes")
            .add(noteMap)
            .addOnSuccessListener { documentReference ->

                // Mengupdate ID catatan dengan ID yang dihasilkan oleh Firestore.
                val newNote = Catatan.copy(id = documentReference.id)

                // Mengatur dokumen dengan catatan yang diperbarui.
                documentReference.set(newNote).addOnFailureListener {
                    // Menampilkan pesan kesalahan jika gagal menyimpan catatan.
                    Toast.makeText(this, "Gagal menyimpan catatan", Toast.LENGTH_SHORT).show()
                }

                // Mengembalikan hasil ke MainActivity setelah menyimpan catatan.
                returnResultToMainActivity(newNote)

                // Menampilkan pesan sukses dan menutup aktivitas.
                Toast.makeText(this, "Catatan disimpan", Toast.LENGTH_SHORT).show()
                finish()
            }
            .addOnFailureListener { e ->
                // Menampilkan pesan kesalahan jika gagal menyimpan catatan.
                Toast.makeText(this, "Gagal menyimpan catatan", Toast.LENGTH_SHORT).show()
            }
    }

    // Metode untuk mengembalikan hasil ke MainActivity setelah menyimpan catatan.
    private fun returnResultToMainActivity(Catatan: Catatan?) {

        // Membuat intent untuk mengembalikan hasil.
        val resultIntent = Intent()

        // Menambahkan data catatan ke intent jika tidak null.
        if (Catatan != null) {
            resultIntent.putExtra("newCatatan", Catatan)
        }

        // Mengatur hasil OK dan menutup aktivitas.
        setResult(RESULT_OK, resultIntent)
        finish()
    }
}