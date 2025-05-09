package com.example.lifelog.KaloriTakip

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.lifelog.R
import com.example.lifelog.database.Database
import com.example.lifelog.database.KaloriDB
import com.example.lifelog.database.KaloriDao
import com.example.lifelog.databinding.ActivityGecmisKaloriBinding

class GecmisKaloriActivity : AppCompatActivity() {
    private lateinit var binding: ActivityGecmisKaloriBinding
    private lateinit var yemeklist: ArrayList<KaloriDB>
    private lateinit var adapter: GecmisKaloriRecview
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gecmis_kalori)
        binding= ActivityGecmisKaloriBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val vt= Database(this)
        yemeklist= ArrayList<KaloriDB>()
        yemeklist=KaloriDao().GetHistory(vt)
        binding.gecmisRecview.setHasFixedSize(true)
        binding.gecmisRecview.layoutManager= LinearLayoutManager(this)
        adapter= GecmisKaloriRecview(this,yemeklist)
        binding.gecmisRecview.adapter=adapter

        binding.backbutton3.setOnClickListener {
            finish()
        }


    }
}