package com.example.lifelog.DovizTakip

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.lifelog.database.Doviz
import com.example.lifelog.R
class DovizListeleRecview(private var mContext: Context,val CurrenciesList: ArrayList<Doviz>):
RecyclerView.Adapter<DovizListeleRecview.DovizNesneTutucu>(){
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): DovizNesneTutucu {
        val design= LayoutInflater.from(mContext).inflate(R.layout.doviz_listleme_recview,parent,false)
        return DovizNesneTutucu(design)
    }

    override fun onBindViewHolder(
        holder: DovizNesneTutucu,
        position: Int
    ) {
        val Currencies=CurrenciesList[position]
        holder.dovizLongName.text=Currencies.DovizName
        holder.dovizShortName.text=Currencies.Dovizshort
        holder.dovizCardview.setOnClickListener {
            val gecis= Intent(mContext, SatinAlimPageActivity::class.java)
            gecis.putExtra("Doviz",Currencies)
            mContext.startActivity(gecis)
        }
    }

    override fun getItemCount(): Int {
        return CurrenciesList.size
    }

    inner class DovizNesneTutucu(view: View): RecyclerView.ViewHolder(view){
        var dovizCardview: CardView
        var dovizLongName: TextView
        var dovizShortName: TextView

        init {
            dovizCardview=view.findViewById(R.id.DovizCardview)
            dovizLongName=view.findViewById(R.id.DovizLongName)
            dovizShortName=view.findViewById(R.id.DovizShortName)
        }
    }

}