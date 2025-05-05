package com.example.lifelog.FitnessPages

import android.content.Context
import android.provider.ContactsContract.Data
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.lifelog.R
import com.example.lifelog.database.Database

class GecmisAktivitelerAdapter(private val context: Context, private val aktiviteListesi: MutableList<AktiviteModel>)
    : RecyclerView.Adapter<GecmisAktivitelerAdapter.GecmisAktivitelerCardTasarimNesneleriniTutucu>(){

        val vt = Database(context)

    inner class GecmisAktivitelerCardTasarimNesneleriniTutucu(view: View) : RecyclerView.ViewHolder(view){

        val cardViewGecmisAktivitelerDetaylari: CardView
        val cardViewTextEgzersizIsmi: TextView
        val cardViewTextHarcananKalori: TextView
        val cardViewTextEgzersizSuresi: TextView
        val cardViewEgzersizTarihSaat: TextView
        val gecmisEgzersizSil_image: ImageView

        init {
            cardViewGecmisAktivitelerDetaylari = view.findViewById(R.id.cardViewGecmisAktivitelerDetaylari)
            cardViewTextEgzersizIsmi = view.findViewById(R.id.cardViewTextEgzersizIsmi)
            cardViewTextHarcananKalori = view.findViewById(R.id.cardViewTextHarcananKalori)
            cardViewTextEgzersizSuresi = view.findViewById(R.id.cardViewTextEgzersizSuresi)
            cardViewEgzersizTarihSaat = view.findViewById(R.id.cardViewEgzersizTarihSaat)
            gecmisEgzersizSil_image = view.findViewById(R.id.gecmisEgzersizSil_image)
        }

    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): GecmisAktivitelerAdapter.GecmisAktivitelerCardTasarimNesneleriniTutucu {
        val binding = LayoutInflater.from(context).inflate(R.layout.card_view_gecmis_aktiviteler_detaylari, parent, false)
        return GecmisAktivitelerCardTasarimNesneleriniTutucu(binding)
    }

    override fun onBindViewHolder(
        holder: GecmisAktivitelerAdapter.GecmisAktivitelerCardTasarimNesneleriniTutucu,
        position: Int
    ) {

        val aktivite = aktiviteListesi[position]

        holder.cardViewTextEgzersizIsmi.text = "Egzersiz:${aktivite.aktiviteAdi}"
        holder.cardViewTextHarcananKalori.text = "Harcanan Kalori: %.2f kcal".format(aktivite.harcananKalori)
        holder.cardViewTextEgzersizSuresi.text = "Süre: ${aktivite.aktiviteSuresi.toInt() / 60} Dakika ${aktivite.aktiviteSuresi.toInt() % 60} Saniye"
        holder.cardViewEgzersizTarihSaat.text = "Tarih:${aktivite.aktiviteTarihi}"

        holder.gecmisEgzersizSil_image.setOnClickListener{

            AlertDialog.Builder(holder.itemView.context)
                .setTitle("Silinsin mi?")
                .setMessage("Bu aktiviteyi silmek istediğinize emin misiniz?")
                .setPositiveButton("Evet") { _, _ ->
                    AktiviteTakipdao().gecmisAktiviteSil(vt, aktivite.aktiviteId)
                    aktiviteListesi.removeAt(position)
                    notifyItemRemoved(position)
                    notifyItemRangeChanged(position, aktiviteListesi.size)
                }
                .setNegativeButton("Hayır", null)
                .show()

        }


    }

    override fun getItemCount(): Int {
        return aktiviteListesi.size
    }

}