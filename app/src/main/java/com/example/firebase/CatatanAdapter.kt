package com.example.firebase

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView


class CatatanAdapter(private var notes: MutableList<Catatan> = mutableListOf(), private val db: NoteFirestoreHelper) :
    RecyclerView.Adapter<CatatanAdapter.NoteViewHolder>() {

    // Kelas ViewHolder untuk merepresentasikan tampilan setiap item catatan
    class NoteViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        // Komponen tampilan dalam item catatan
        val titleTextView: TextView = itemView.findViewById(R.id.titleTextView)
        val contentTextView: TextView = itemView.findViewById(R.id.contentTextView)
        val updateButton: ImageView = itemView.findViewById(R.id.updateButton)
        val deleteButton: ImageView = itemView.findViewById(R.id.deleteButton)
    }

    // Metode dipanggil ketika ViewHolder baru dibuat
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        // Menginflate tata letak item catatan
        val view = LayoutInflater.from(parent.context).inflate(R.layout.catatan_item, parent, false)
        return NoteViewHolder(view)
    }

    // Metode untuk mendapatkan jumlah total item catatan
    override fun getItemCount(): Int = notes.size

    // Metode untuk menghubungkan data catatan dengan tampilan ViewHolder
    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        // Mendapatkan objek catatan pada posisi tertentu
        val note = notes[position]
        // Menetapkan teks judul dan konten pada tampilan
        holder.titleTextView.text = note.title
        holder.contentTextView.text = note.content

        // Menetapkan tindakan klik pada tombol pembaruan (update)
        holder.updateButton.setOnClickListener{
            // Membuat intent untuk membuka UpdateNoteActivity dengan mengirimkan ID catatan
            val intent = Intent(holder.itemView.context, UpdateCatatanActivity::class.java).apply {
                putExtra("note_id", note.id)
            }
            // Menjalankan intent
            holder.itemView.context.startActivity(intent)
        }

        // Menetapkan tindakan klik pada tombol penghapusan
        holder.deleteButton.setOnClickListener{
            // Menampilkan dialog konfirmasi sebelum menghapus catatan
            showDeleteConfirmationDialog(holder.itemView.context, note)
        }
    }

    // Metode untuk menampilkan dialog konfirmasi penghapusan catatan
    private fun showDeleteConfirmationDialog(context: Context, note:Catatan) {
        val builder = AlertDialog.Builder(context)
        builder.setTitle("Konfirmasi Hapus")
        builder.setMessage("Apakah Anda yakin ingin menghapus catatan ini?")

        // Menetapkan tindakan jika pengguna menekan tombol "Ya"
        builder.setPositiveButton("Ya") { _, _ ->
            val index = notes.indexOfFirst { it.id == note.id }
            if (index != -1) {
                db.deleteNote(note.id)
                notes.removeAt(index)
                notifyItemRemoved(index)
                Toast.makeText(context, "Catatan dihapus", Toast.LENGTH_SHORT).show()
            }
        }

        // Menetapkan tindakan jika pengguna memilih untuk tidak menghapus
        builder.setNegativeButton("Tidak") { _, _ ->
            // Tidak melakukan apa-apa
        }

        // Membuat dan menampilkan dialog
        val dialog = builder.create()
        dialog.show()
    }

    // Metode untuk menyegarkan data set catatan
    fun refreshData(newNotes: List<Catatan>) {
        notes = newNotes.toMutableList()
        notifyDataSetChanged()
    }
}