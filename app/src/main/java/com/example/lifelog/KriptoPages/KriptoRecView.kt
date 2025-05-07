package com.example.lifelog.KriptoPages

import android.app.Activity
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
import com.example.lifelog.database.Dao.Crypto.Crypto


class KriptoRecView(private var mContext: Context, var CryptoList: List<Crypto>) :
    RecyclerView.Adapter<KriptoRecView.KriptoNesneTutucu>(), Filterable {

    private var filteredList = CryptoList.toMutableList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): KriptoNesneTutucu {
        val design = LayoutInflater.from(mContext).inflate(R.layout.cardview_kripto_tasarim_rv, parent, false)
        return KriptoNesneTutucu(design)
    }

    override fun onBindViewHolder(holder: KriptoNesneTutucu, position: Int) {
        val crypto = filteredList[position] // Burada filteredList kullanıyoruz
        holder.KriptoShortText.text = crypto.Cryptoshort
        holder.KriptoLongText.text = crypto.CryptoName
        holder.KriptoCardview.setOnClickListener {
            val gecis = Intent(mContext, SatinAlimActivity::class.java) // Sayfaya geçiş
            gecis.putExtra("Crypto", crypto) // Veri aktarımı
            mContext.startActivity(gecis)
            if (mContext is Activity) {
                (mContext as Activity).finish()
            }
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
                    CryptoList
                } else {
                    CryptoList.filter {
                        it.CryptoName.lowercase().contains(filterString) || it.Cryptoshort.lowercase().contains(filterString)
                    }
                }
                val filterResults = FilterResults()
                filterResults.values = resultList
                return filterResults
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                filteredList = (results?.values as? List<Crypto>)?.toMutableList() ?: mutableListOf()
                notifyDataSetChanged()
            }
        }
    }

    inner class KriptoNesneTutucu(view: View) : RecyclerView.ViewHolder(view) {
        var KriptoCardview: CardView = view.findViewById(R.id.KriptoCardview)
        var KriptoLongText: TextView = view.findViewById(R.id.CryptoLongName)
        var KriptoShortText: TextView = view.findViewById(R.id.CryptoShortName)
    }
}
