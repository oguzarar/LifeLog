package com.example.lifelog.DovizTakip

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
import com.example.lifelog.database.Doviz
import com.example.lifelog.R

class DovizListeleRecview(
    private var mContext: Context,
    private var CurrenciesList: ArrayList<Doviz>
) : RecyclerView.Adapter<DovizListeleRecview.DovizNesneTutucu>(), Filterable {

    private var filteredList: ArrayList<Doviz> = ArrayList(CurrenciesList)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DovizNesneTutucu {
        val design = LayoutInflater.from(mContext).inflate(R.layout.doviz_listleme_recview, parent, false)
        return DovizNesneTutucu(design)
    }

    override fun onBindViewHolder(holder: DovizNesneTutucu, position: Int) {
        val currency = filteredList[position]
        holder.dovizLongName.text = currency.DovizName
        holder.dovizShortName.text = currency.Dovizshort
        holder.dovizCardview.setOnClickListener {
            val gecis = Intent(mContext, SatinAlimPageActivity::class.java)
            gecis.putExtra("Doviz", currency)
            mContext.startActivity(gecis)
        }
    }

    override fun getItemCount(): Int {
        return filteredList.size
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(query: CharSequence?): FilterResults {
                val charString = query?.toString()?.lowercase()?.trim() ?: ""
                val result = if (charString.isEmpty()) {
                    CurrenciesList
                } else {
                    CurrenciesList.filter {
                        it.DovizName.lowercase().contains(charString) ||
                                it.Dovizshort.lowercase().contains(charString)
                    }
                }

                val filterResults = FilterResults()
                filterResults.values = ArrayList(result)
                return filterResults
            }

            override fun publishResults(query: CharSequence?, results: FilterResults) {
                filteredList = results.values as ArrayList<Doviz>
                notifyDataSetChanged()
            }
        }
    }

    inner class DovizNesneTutucu(view: View) : RecyclerView.ViewHolder(view) {
        var dovizCardview: CardView = view.findViewById(R.id.DovizCardview)
        var dovizLongName: TextView = view.findViewById(R.id.DovizLongName)
        var dovizShortName: TextView = view.findViewById(R.id.DovizShortName)
    }
}
