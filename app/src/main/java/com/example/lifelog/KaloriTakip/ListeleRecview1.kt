package com.example.lifelog.KaloriTakip

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.lifelog.R

class ListeleRecview1(private var mContext: Context, val Yemeklists: ArrayList<Kalori>):
        RecyclerView.Adapter<ListeleRecview1.YemekNesneTutucu>(){
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): YemekNesneTutucu {
        val design= LayoutInflater.from(mContext).inflate(R.layout.yemek_list_recview,parent,false)
        return YemekNesneTutucu(design)

    }

    override fun onBindViewHolder(holder: YemekNesneTutucu, position: Int) {
        val Yemek= Yemeklists[position]
        holder.Yemekismi.text=Yemek.isim
        holder.kalorimiktari.text=Yemek.kalori
        holder.proteinmiktari.text=Yemek.protein


    }

    override fun getItemCount(): Int {
        return Yemeklists.size
    }
    inner class YemekNesneTutucu(view: View): RecyclerView.ViewHolder(view){
        val YemekCardview: CardView
        val Yemekismi: TextView
        val kalorimiktari: TextView
        val proteinmiktari: TextView


        init{
            YemekCardview=view.findViewById(R.id.KaloriCardview)
            Yemekismi=view.findViewById(R.id.Yemekismi)
            kalorimiktari=view.findViewById(R.id.kalorimiktariText)
            proteinmiktari=view.findViewById(R.id.proteinMiktaritext)
        }

    }
}