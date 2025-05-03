package com.example.lifelog.KaloriTakip


import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.lifelog.R

class GelenYemekRecView(private var mContext: Context, var YemekList: ArrayList<Kalori>):
    RecyclerView.Adapter<GelenYemekRecView.Yemeknesnetutucu>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): Yemeknesnetutucu {
        val design= LayoutInflater.from(mContext).inflate(R.layout.gelen_yemk_rev_view,parent,false)
        return Yemeknesnetutucu(design)
    }

    override fun onBindViewHolder(
        holder: Yemeknesnetutucu,
        position: Int
    ) {
        val yemek=YemekList[position]
        holder.yemekismi.text=yemek.isim
        holder.kaloriMiktari.text=yemek.kalori
        holder.proteinmiktari.text=yemek.protein


    }

    override fun getItemCount(): Int {
        return YemekList.size
    }
    inner class Yemeknesnetutucu(view: View): RecyclerView.ViewHolder(view){
        val yemekismi: TextView
        val kaloriMiktari: TextView
        val proteinmiktari: TextView

        init{
            yemekismi=view.findViewById(R.id.Yemekismi2)
            kaloriMiktari=view.findViewById(R.id.kalorimiktariText2)
            proteinmiktari=view.findViewById(R.id.proteinMiktaritext2)

        }

    }
}