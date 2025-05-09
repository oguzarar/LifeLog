package com.example.lifelog.PluginPages

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.work.OneTimeWorkRequestBuilder
import com.example.lifelog.KaloriTakip.GelenYemekRecView
import com.example.lifelog.KaloriTakip.Kalori
import com.example.lifelog.KaloriTakip.ListeleRecview1
import com.example.lifelog.KaloriTakip.YemekListeleActivity
import com.example.lifelog.R
import com.example.lifelog.database.Database
import com.example.lifelog.database.KaloriDao
import com.example.lifelog.databinding.ActivityKaloriBinding
import java.util.Calendar
import androidx.work.*
import com.example.lifelog.KaloriTakip.GecmisKaloriActivity
import com.example.lifelog.PluginPages.DovizActivity
import java.util.TimeZone
import java.util.concurrent.TimeUnit

class KaloriActivity : AppCompatActivity() {
    private lateinit var binding: ActivityKaloriBinding
    private lateinit var yemeklist: ArrayList<Kalori>
    private lateinit var adapter: GelenYemekRecView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_kalori)
        binding= ActivityKaloriBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.GecmisButton.setOnClickListener {
            val gecis= Intent(this@KaloriActivity, GecmisKaloriActivity::class.java)
            startActivity(gecis)
        }

        val vt= Database(this)
        KaloriDao().kaloriSifirlama(vt)
        binding.Toplamkalroitext.text= "%.2f".format(KaloriDao().GetTotalcalorie(vt).first)
        binding.ToplamprtoeinText.text= "%.2f".format(KaloriDao().GetTotalcalorie(vt).second)


        binding.Button.setOnClickListener {
            val gecis= Intent(this@KaloriActivity, YemekListeleActivity::class.java)
            startActivity(gecis)
        }
        yemeklist= ArrayList<Kalori>()


        yemeklist=KaloriDao().getAllYemek(vt)

        binding.MainYemekListeleRecview.setHasFixedSize(true)
        binding.MainYemekListeleRecview.layoutManager= LinearLayoutManager(this)
        adapter= GelenYemekRecView(this,yemeklist)
        binding.MainYemekListeleRecview.adapter=adapter

        binding.backbutton.setOnClickListener {
            finish()
        }

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

