package com.example.lifelog.KaloriTakip

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.lifelog.R
import com.example.lifelog.database.KaloriDB

class GecmisKaloriRecview(private var mContext: Context,var Yemeklist: ArrayList<KaloriDB>):
    RecyclerView.Adapter<GecmisKaloriRecview.Gecmis>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): GecmisKaloriRecview.Gecmis {
        val design= LayoutInflater.from(mContext).inflate(R.layout.gecmis_kalori_recview,parent,false)
        return Gecmis(design)
    }

    override fun onBindViewHolder(
        holder: GecmisKaloriRecview.Gecmis,
        position: Int
    ) {
        val yemek=Yemeklist[position]
        holder.Tarih.text=yemek.tarih
        holder.kaloriMiktari.text=yemek.kalori
        holder.proteinmiktari.text=yemek.protein

    }


    override fun getItemCount(): Int {
        return Yemeklist.size
    }

    inner class Gecmis(view: View): RecyclerView.ViewHolder(view){
        val Tarih: TextView
        val kaloriMiktari: TextView
        val proteinmiktari: TextView

        init{
            Tarih=view.findViewById(R.id.Tarih)
            kaloriMiktari=view.findViewById(R.id.GecmiskalorimiktariText2)
            proteinmiktari=view.findViewById(R.id.GecmisproteinMiktaritext2)
        }
    }
}