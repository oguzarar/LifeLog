package com.example.lifelog.FitnessPages

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.lifelog.R

class GecmisAktivitelerAdapter(private val context: Context, private val aktiviteListesi: List<AktiviteModel>)
    : RecyclerView.Adapter<GecmisAktivitelerAdapter.GecmisAktivitelerCardTasarimNesneleriniTutucu>(){

    inner class GecmisAktivitelerCardTasarimNesneleriniTutucu(view: View) : RecyclerView.ViewHolder(view){

        val cardViewGecmisAktivitelerDetaylari: CardView
        val cardViewTextEgzersizIsmi: TextView
        val cardViewTextHarcananKalori: TextView
        val cardViewTextEgzersizSuresi: TextView
        val cardViewEgzersizTarihSaat: TextView

        init {
            cardViewGecmisAktivitelerDetaylari = view.findViewById(R.id.cardViewGecmisAktivitelerDetaylari)
            cardViewTextEgzersizIsmi = view.findViewById(R.id.cardViewTextEgzersizIsmi)
            cardViewTextHarcananKalori = view.findViewById(R.id.cardViewTextHarcananKalori)
            cardViewTextEgzersizSuresi = view.findViewById(R.id.cardViewTextEgzersizSuresi)
            cardViewEgzersizTarihSaat = view.findViewById(R.id.cardViewEgzersizTarihSaat)
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
        holder.cardViewTextHarcananKalori.text = "Kalori:${aktivite.harcananKalori} kcal"
        holder.cardViewTextEgzersizSuresi.text = "Süre:${aktivite.aktiviteSuresi} Dakika"
        holder.cardViewEgzersizTarihSaat.text = "Tarih:${aktivite.aktiviteTarihi}"

    }

    override fun getItemCount(): Int {
        return aktiviteListesi.size
    }

}