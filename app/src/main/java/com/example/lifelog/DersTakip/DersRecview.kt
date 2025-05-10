package com.example.lifelog.DersTakip

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.lifelog.R
import com.example.lifelog.database.Database
import com.example.lifelog.database.TaskDaos.derstakip.DersTakip
import com.example.lifelog.database.TaskDaos.derstakip.DersTakipDao

class DersRecview(private var mContext: Context, val DersLists: MutableList<DersTakip>):
    RecyclerView.Adapter<DersRecview.RecviewNesneTutucu>(){
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecviewNesneTutucu {
        //XML ile RecyclerView bağlantısı kuruldu
        val design= LayoutInflater.from(mContext).inflate(R.layout.ders_rec_view,parent,false)
        return RecviewNesneTutucu(design)
    }

    override fun onBindViewHolder(holder: DersRecview.RecviewNesneTutucu, position: Int) {
        val ders=DersLists[position]

        val vt= Database(mContext)//Veritabanı bağlantısı yapıldı

        holder.DersAdiText.text=ders.dersAdi
        holder.SinavTarihi.text=ders.dersTarih
        holder.SinavSaati.text=ders.dersSaat
        holder.SinavSilme.setOnClickListener {
            if (holder.adapterPosition != RecyclerView.NO_POSITION) {
                DersTakipDao().DeleteTask(vt, ders.ders_id!!.toInt())
                DersLists.removeAt(holder.adapterPosition)
                notifyItemRemoved(holder.adapterPosition)
                Toast.makeText(mContext, "Silindi", Toast.LENGTH_SHORT).show()
            }

        }

    }

    override fun getItemCount(): Int {
        return DersLists.size
    }
    inner class RecviewNesneTutucu(view: View): RecyclerView.ViewHolder(view){
        var DersCardview: CardView
        var DersAdiText: TextView
        var SinavTarihi: TextView
        var SinavSaati: TextView
        var SinavSilme: ImageView

        init{
            DersCardview=view.findViewById(R.id.dersCardview)
            DersAdiText=view.findViewById(R.id.DersAdi)
            SinavSaati=view.findViewById(R.id.SinavSaati)
            SinavTarihi=view.findViewById(R.id.SinavTarihi)
            SinavSilme=view.findViewById(R.id.sinavSilme)
        }
    }
}