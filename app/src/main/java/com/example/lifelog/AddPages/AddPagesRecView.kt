package com.example.lifelog.AddPages

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.lifelog.R
import com.example.lifelog.database.Plugins

class AddPagesRecView(private var mContext: Context,private val PluginList: MutableList<Plugins>):
    RecyclerView.Adapter<AddPagesRecView.CardviewnesneTutucu>() {

    inner class CardviewnesneTutucu(view: View): RecyclerView.ViewHolder(view){
        var PluginSatirCard: CardView
        var PluginText: TextView


        init{
            PluginSatirCard=view.findViewById(R.id.PluginCardView)
            PluginText=view.findViewById(R.id.PluginText)

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardviewnesneTutucu {
        val design= LayoutInflater.from(mContext).inflate(R.layout.add_pages_items_rv,parent,false)
        return CardviewnesneTutucu(design)
    }

    override fun onBindViewHolder(holder: CardviewnesneTutucu, position: Int) {
        val plugin=PluginList[position]
        holder.PluginText.text=plugin.Plugin_name

    }

    override fun getItemCount(): Int {
        return PluginList.size

    }


}