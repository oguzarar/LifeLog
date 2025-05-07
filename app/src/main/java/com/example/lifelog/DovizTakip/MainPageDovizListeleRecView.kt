package com.example.lifelog.DovizTakip

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.lifelog.R
import com.example.lifelog.database.Dao.Doviz.DovizDB
import com.example.lifelog.duzenleme.duzenleme.Companion.formatNumber2


class MainPageDovizListeleRecView(private var mContext: Context, val DovizList: ArrayList<DovizDB>):
RecyclerView.Adapter<MainPageDovizListeleRecView.DovizNesneTutucu>(){
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): DovizNesneTutucu {
        val design= LayoutInflater.from(mContext).inflate(R.layout.main_page_doviz_listele_rec_view,parent,false)
        return DovizNesneTutucu(design)
    }

    override fun onBindViewHolder(
        holder: DovizNesneTutucu,
        position: Int
    ) {
        val Doviz= DovizList[position]
        holder.DovizLongName.text=Doviz.DovizLongName
        holder.DovizShortName.text=Doviz.DovizShortName
        holder.Tutar.text=formatNumber2(Doviz.DovizMiktariTRY.toDouble())
        holder.DovizCardView.setOnClickListener {
            val gecis= Intent(mContext, SellPageActivity::class.java)
            gecis.putExtra("Doviz", Doviz)
            mContext.startActivity(gecis)
        }

    }

    override fun getItemCount(): Int {
        return DovizList.size
    }

    inner class DovizNesneTutucu(view: View): RecyclerView.ViewHolder(view){
        val DovizCardView: CardView
        val DovizLongName: TextView
        val DovizShortName: TextView
        val Tutar: TextView

        init{
            DovizCardView=view.findViewById(R.id.MainDovizCardview)
            DovizLongName=view.findViewById(R.id.MainDovizLongName)
            DovizShortName=view.findViewById(R.id.MainDovizShortName)
            Tutar=view.findViewById(R.id.DovizfiyatGoster)
        }
    }
}