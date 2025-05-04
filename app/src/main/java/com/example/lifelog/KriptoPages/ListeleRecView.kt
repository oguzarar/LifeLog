package com.example.lifelog.KriptoPages

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.lifelog.ApiKeys.Keys
import com.example.lifelog.R
import com.example.lifelog.database.CryptoDB
import com.example.lifelog.database.Database
import com.google.gson.JsonParser
import com.google.gson.JsonPrimitive
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response

class ListeleRecView(private var mContext: Context,val CryptoLists:List<CryptoDB>):
    RecyclerView.Adapter<ListeleRecView.KriptoNesneTutucu>(){
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): KriptoNesneTutucu {
        val design= LayoutInflater.from(mContext).inflate(R.layout.cardview_kripto_tasarim_rv2,parent,false)
        return KriptoNesneTutucu(design)
    }

    override fun onBindViewHolder(
        holder: KriptoNesneTutucu,
        position: Int
    ) {
        val Crypto=CryptoLists[position]

        holder.KriptoLongText.text=Crypto.CryptoLongName
        holder.KriptoShortText.text=Crypto.CryptoShortName

        val gelenUSDT="%.2f".format(Crypto.AmountOfUSDT.toDouble())
        val gelenKripto="%.2f".format(Crypto.AmountOfCrypto.toDouble())
        holder.KriptoTutar.text=gelenUSDT

        holder.KriptoAdet.text=gelenKripto
        holder.KriptoCardview.setOnClickListener {
            val gecis= Intent(mContext, SellCryptoActivity::class.java)//Sayfaya geçiş
            gecis.putExtra("Crypto",Crypto)//Geçilen sayfaya veri aktarımı
            mContext.startActivity(gecis)


        }

    }

    override fun getItemCount(): Int {
        return CryptoLists.size
    }

    inner class KriptoNesneTutucu(view: View): RecyclerView.ViewHolder(view){
        var KriptoCardview: CardView
        var KriptoLongText: TextView
        var KriptoShortText: TextView
        var KriptoAdet: TextView
        var KriptoTutar: TextView


        init{
            KriptoCardview=view.findViewById(R.id.KriptoCardview)
            KriptoLongText=view.findViewById(R.id.CryptoLongName)
            KriptoShortText=view.findViewById(R.id.CryptoShortName)
            KriptoAdet=view.findViewById(R.id.AdetGoster)
            KriptoTutar=view.findViewById(R.id.fiyatGoster)
        }

    }
}



