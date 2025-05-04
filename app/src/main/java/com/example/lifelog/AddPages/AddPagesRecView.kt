package com.example.lifelog.AddPages


import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.lifelog.FitnessPages.FitnessActivity
import com.example.lifelog.R
import com.example.lifelog.database.Plugins
import com.example.lifelog.GoldPages.AltinActivity
import com.example.lifelog.PluginPages.DersNotTakipActivity
import com.example.lifelog.PluginPages.DersTakipActivity
import com.example.lifelog.PluginPages.DovizActivity
import com.example.lifelog.PluginPages.KaloriActivity
import com.example.lifelog.PluginPages.KriptoActivity

class AddPagesRecView(private var mContext: Context,private val PluginList: MutableList<Plugins>):
    RecyclerView.Adapter<AddPagesRecView.CardviewnesneTutucu>(){

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

    override fun onBindViewHolder(holder: CardviewnesneTutucu, position: Int){

        val plugin=PluginList[position]
        holder.PluginText.text=plugin.Plugin_name
        holder.PluginSatirCard.setOnClickListener {
            val activity = holder.itemView.context as AppCompatActivity
            when(plugin.Plugin_name){
                "Kripto Takip Et"->{
                    val gecis= Intent(mContext, KriptoActivity::class.java)
                    mContext.startActivity(gecis)
                }
                "Altın Takip Et"->{
                    val gecis= Intent(mContext, AltinActivity::class.java)
                    mContext.startActivity(gecis)
                }
                "Döviz Takip Et"->{
                    val gecis= Intent(mContext, DovizActivity::class.java)
                    mContext.startActivity(gecis)
                }
                "Aktivite Takip Et"->{
                    val gecis= Intent(mContext, FitnessActivity::class.java)
                    mContext.startActivity(gecis)
                }
                "Ders Takip Et"->{
                    val gecis= Intent(mContext, DersTakipActivity::class.java)
                    mContext.startActivity(gecis)
                }
                "Kalori Takip Et"->{
                    val gecis= Intent(mContext, KaloriActivity::class.java)
                    mContext.startActivity(gecis)
                }
                "Ders Not Takip Et"->{
                    val gecis= Intent(mContext, DersNotTakipActivity::class.java)
                    mContext.startActivity(gecis)
                }

            }

        }

    }

    override fun getItemCount(): Int {
        return PluginList.size

    }


}