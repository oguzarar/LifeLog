package com.example.lifelog.KaloriTakip

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.lifelog.R

import com.example.lifelog.KaloriTakip.KaloriEkleActivity

class ListeleRecview1(private var mContext: Context, val Yemeklists: ArrayList<Kalori>) :
    RecyclerView.Adapter<ListeleRecview1.YemekNesneTutucu>(), Filterable {

    private var filteredList = Yemeklists.toMutableList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): YemekNesneTutucu {
        val design = LayoutInflater.from(mContext).inflate(R.layout.yemek_list_recview, parent, false)
        return YemekNesneTutucu(design)
    }

    override fun onBindViewHolder(holder: YemekNesneTutucu, position: Int) {
        val yemek = filteredList[position] // Burada filteredList kullanıyoruz
        holder.Yemekismi.text = yemek.isim
        holder.kalorimiktari.text = yemek.kalori
        holder.proteinmiktari.text = yemek.protein
        holder.YemekCardview.setOnClickListener {
            val gecis = Intent(mContext, KaloriEkleActivity::class.java)
            gecis.putExtra("Yemek", yemek) // Yemek verisini geçiyoruz
            mContext.startActivity(gecis)
        }
    }

    override fun getItemCount(): Int {
        return filteredList.size // filteredList'i kullanıyoruz
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val filterString = constraint?.toString()?.lowercase()?.trim() ?: ""
                val resultList = if (filterString.isEmpty()) {
                    Yemeklists
                } else {
                    Yemeklists.filter {
                        it.isim.lowercase().contains(filterString) || it.kalori.lowercase().contains(filterString) || it.protein.lowercase().contains(filterString)
                    }
                }
                val filterResults = FilterResults()
                filterResults.values = resultList
                return filterResults
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                filteredList = (results?.values as? List<Kalori>)?.toMutableList() ?: mutableListOf()
                notifyDataSetChanged()
            }
        }
    }

    inner class YemekNesneTutucu(view: View) : RecyclerView.ViewHolder(view) {
        val YemekCardview: CardView = view.findViewById(R.id.KaloriCardview)
        val Yemekismi: TextView = view.findViewById(R.id.Yemekismi)
        val kalorimiktari: TextView = view.findViewById(R.id.kalorimiktariText)
        val proteinmiktari: TextView = view.findViewById(R.id.proteinMiktaritext)
    }
}
