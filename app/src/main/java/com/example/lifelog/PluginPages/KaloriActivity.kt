package com.example.lifelog.PluginPages

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.lifelog.KaloriTakip.GelenYemekRecView
import com.example.lifelog.KaloriTakip.Kalori
import com.example.lifelog.KaloriTakip.ListeleRecview1
import com.example.lifelog.KaloriTakip.YemekListeleActivity
import com.example.lifelog.R
import com.example.lifelog.database.Database
import com.example.lifelog.database.KaloriDao
import com.example.lifelog.databinding.ActivityKaloriBinding

class KaloriActivity : AppCompatActivity() {
    private lateinit var binding: ActivityKaloriBinding
    private lateinit var yemeklist: ArrayList<Kalori>
    private lateinit var adapter: GelenYemekRecView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_kalori)
        binding= ActivityKaloriBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val vt= Database(this)

        binding.Toplamkalroitext.text= "%.2f".format(KaloriDao().GetTotalcalorie(vt).first)
        binding.ToplamprtoeinText.text= "%.2f".format(KaloriDao().GetTotalcalorie(vt).second)


        binding.button.setOnClickListener {
            val gecis= Intent(this@KaloriActivity, YemekListeleActivity::class.java)
            startActivity(gecis)
        }
        yemeklist= ArrayList<Kalori>()


        yemeklist=KaloriDao().getAllYemek(vt)

        binding.MainYemekListeleRecview.setHasFixedSize(true)
        binding.MainYemekListeleRecview.layoutManager= LinearLayoutManager(this)
        adapter= GelenYemekRecView(this,yemeklist)
        binding.MainYemekListeleRecview.adapter=adapter
    }

    override fun onResume() {
        super.onResume()
        val vt= Database(this)
        yemeklist.clear()
        yemeklist.addAll(KaloriDao().getAllYemek(vt))
        adapter.notifyDataSetChanged()

        binding.Toplamkalroitext.text= "%.2f".format(KaloriDao().GetTotalcalorie(vt).first)
        binding.ToplamprtoeinText.text= "%.2f".format(KaloriDao().GetTotalcalorie(vt).second)

    }

}