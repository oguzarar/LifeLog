package com.example.lifelog.PluginPages

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.lifelog.R
import com.example.lifelog.database.Golds
import org.w3c.dom.Text
//RecyclerView de altınları listelemek için adapter sınıfı oluşturma
class AltinAdapter(private val context: Context, private var goldsList: List<Golds>) : RecyclerView.Adapter<AltinAdapter.CardViewTasarimNesneleriniTutucu>() {
    //itemların görsel öğelerine erişim için viewHolder sınıfı
    inner class CardViewTasarimNesneleriniTutucu(view: View) : RecyclerView.ViewHolder(view){

        //RecyclerViewdeki öğelerin viewleri
        val cardViewAltin: CardView
        val altinTuru: TextView
        val altinMiktar: TextView
        val altinTutari: TextView

        //Elemanlara layouttan erişiyoruz
        init {
            cardViewAltin = view.findViewById(R.id.cardViewAltin)
            altinTuru = view.findViewById(R.id.textViewAltinTuru)
            altinMiktar = view.findViewById(R.id.textViewAltinMiktar)
            altinTutari = view.findViewById(R.id.textViewAltinTutar)
        }

    }

    //itemlerin layout oluşturulurken kullanılacak ViewHolderlarını döndürme
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardViewTasarimNesneleriniTutucu {
        val binding = LayoutInflater.from(context).inflate(R.layout.card_view_altin_tasarimi, parent, false)
        return CardViewTasarimNesneleriniTutucu(binding)
    }

    //Listedeki eleman adedini döndürme
    override fun getItemCount(): Int {
        return goldsList.size
    }

    //tıklanabilir cardView oluşturma
    override fun onBindViewHolder(holder: CardViewTasarimNesneleriniTutucu, position: Int) {

        val gold = goldsList[position]

        holder.altinTuru.text = "${gold.goldType}"
        holder.altinMiktar.text = "${gold.goldAmount}"

        holder.cardViewAltin.setOnClickListener{

            val intent = Intent(context, AltinDetayActivity::class.java)
            intent.putExtra("gold", gold)
            context.startActivity(intent)

        }

    }


}