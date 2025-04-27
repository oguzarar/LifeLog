package com.example.lifelog.KriptoPages

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.lifelog.PluginPages.KriptoActivity
import com.example.lifelog.R
import com.example.lifelog.database.Crypto

class KriptoRecView (private var mContext: Context,val CryptoList: List<Crypto>):
    RecyclerView.Adapter<KriptoRecView.KriptoNesneTutucu>(){
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): KriptoRecView.KriptoNesneTutucu {
       val design= LayoutInflater.from(mContext).inflate(R.layout.cardview_kripto_tasarim_rv,parent,false)
        return KriptoNesneTutucu(design)
    }

    override fun onBindViewHolder(holder: KriptoRecView.KriptoNesneTutucu, position: Int) {
        val Crypto=CryptoList[position]
        holder.KriptoShortText.text=Crypto.Cryptoshort
        holder.KriptoLongText.text=Crypto.CryptoName
        holder.KriptoCardview.setOnClickListener {
            val gecis= Intent(mContext, SatinAlimActivity::class.java)//Sayfaya geçiş
            gecis.putExtra("Crypto",Crypto)//Geçilen sayfaya veri aktarımı
            mContext.startActivity(gecis)
        }

    }

    override fun getItemCount(): Int {
        return CryptoList.size
    }
    inner class KriptoNesneTutucu(view: View): RecyclerView.ViewHolder(view){
        var KriptoCardview: CardView
        var KriptoLongText: TextView
        var KriptoShortText: TextView


        init{
            KriptoCardview=view.findViewById(R.id.KriptoCardview)
            KriptoLongText=view.findViewById(R.id.CryptoLongName)
            KriptoShortText=view.findViewById(R.id.CryptoShortName)
        }

    }
}