package com.example.lifelog.FitnessPages

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.lifelog.R

class AktiviteSecAdapter (private val context: Context, private var egzersizListesi: List<Exercises>) : RecyclerView.Adapter<AktiviteSecAdapter.CardViewTasarimNesneleriniTutucu>(){

    inner class CardViewTasarimNesneleriniTutucu(view: View) : RecyclerView.ViewHolder(view){

        val cardViewAktivite: CardView
        val aktiviteName: TextView

        init {
            cardViewAktivite = view.findViewById(R.id.cardViewAktivite)
            aktiviteName = view.findViewById(R.id.cardViewAktiviteNameTextView)
        }

    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CardViewTasarimNesneleriniTutucu {
        val binding = LayoutInflater.from(context).inflate(R.layout.card_view_aktivite, parent, false)
        return CardViewTasarimNesneleriniTutucu(binding)
    }

    override fun getItemCount(): Int {
        return egzersizListesi.size
    }

    override fun onBindViewHolder(holder: CardViewTasarimNesneleriniTutucu, position: Int) {

        val egzersiz = egzersizListesi[position]

        holder.aktiviteName.text = egzersiz.exerciseName

        val kaloriTextView = holder.itemView.findViewById<TextView>(R.id.cardViewAktiviteKaloriTextView)
        kaloriTextView.text = "${egzersiz.caloriesPerHour} kcal/saat"

        holder.cardViewAktivite.setOnClickListener{
            Toast.makeText(context, "${egzersiz.exerciseName} seçildi.", Toast.LENGTH_SHORT).show()

            val intent = Intent()

            intent.putExtra("egzersiz ismi", egzersiz.exerciseName)
            intent.putExtra("egzersiz kcal", egzersiz.caloriesPerHour)

            (context as Activity).setResult(Activity.RESULT_OK, intent)
            (context as Activity).finish()

        }




    }


}