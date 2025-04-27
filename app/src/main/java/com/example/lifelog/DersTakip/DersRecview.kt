package com.example.lifelog.DersTakip

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.lifelog.R
import com.example.lifelog.database.DersTakip

class DersRecview(private var mContext: Context,val DersLists:List<DersTakip>):
    RecyclerView.Adapter<DersRecview.RecviewNesneTutucu>(){
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecviewNesneTutucu {
        val design= LayoutInflater.from(mContext).inflate(R.layout.ders_rec_view,parent,false)
        return RecviewNesneTutucu(design)
    }

    override fun onBindViewHolder(holder: DersRecview.RecviewNesneTutucu, position: Int) {
        val ders=DersLists[position]
        holder.DersAdiText.text=ders.dersAdi
        holder.SinavTarihi.text=ders.dersTarih
        holder.SinavSaati.text=ders.dersSaat

    }

    override fun getItemCount(): Int {
        return DersLists.size
    }
    inner class RecviewNesneTutucu(view: View): RecyclerView.ViewHolder(view){
        var DersCardview: CardView
        var DersAdiText: TextView
        var SinavTarihi: TextView
        var SinavSaati: TextView

        init{
            DersCardview=view.findViewById(R.id.dersCardview)
            DersAdiText=view.findViewById(R.id.DersAdi)
            SinavSaati=view.findViewById(R.id.SinavSaati)
            SinavTarihi=view.findViewById(R.id.SinavTarihi)
        }
    }
}