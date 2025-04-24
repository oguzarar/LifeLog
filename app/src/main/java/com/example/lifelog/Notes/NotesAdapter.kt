package com.example.lifelog.Notes

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.lifelog.R
import com.example.lifelog.database.Notes

//RecyclerViewde verileri gösterebilmek için Adapter sınıfı
class NotesAdapter(private val context: Context, private val notesList: MutableList<Notes>) : RecyclerView.Adapter<NotesAdapter.CardTasarimTutucu>(){

    inner class CardTasarimTutucu(view: View) : RecyclerView.ViewHolder(view){

        val cardView: CardView
        val textViewNoteTitle: TextView
        val textViewNoteDate: TextView

        //Gelen verilere göre içeriği güncellenecek tasarım nesnelerimize erişim
        init {
            cardView = view.findViewById(R.id.notlarCardView)
            textViewNoteTitle = view.findViewById(R.id.textViewNotBaslik)
            textViewNoteDate = view.findViewById(R.id.textViewNotTarih)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardTasarimTutucu {
        val binding = LayoutInflater.from(context).inflate(R.layout.notlar_card_tasarimi, parent, false)
        return CardTasarimTutucu(binding)
    }
    //Notlar Listesinin uzunluğunu döndürme
    override fun getItemCount(): Int {
        return notesList.size
    }
    //tıklanabilir cardView oluşturma
    override fun onBindViewHolder(holder: CardTasarimTutucu, position: Int) {

        val note = notesList[position]
        holder.textViewNoteTitle.text = note.note_title //ekranda gösterilen verilen değerlerini yükleme
        holder.textViewNoteDate.text = note.note_date

        //cardViewe tıklanılması durumunda not detayına geçiş
        holder.cardView.setOnClickListener{
            Toast.makeText(context, "Not: ${note.note_title}", Toast.LENGTH_SHORT).show()

            val intent = Intent(context, NotuGoruntuleActivity::class.java)
            //note nesnesi notun görüntüleneceği sayfaya aktarılıyor.
            intent.putExtra("note", note)
            context.startActivity(intent)


        }

    }


}