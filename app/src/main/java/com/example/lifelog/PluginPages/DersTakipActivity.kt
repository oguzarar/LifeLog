package com.example.lifelog.PluginPages

import android.content.Intent
import android.os.Bundle
import android.widget.Adapter
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.lifelog.DersTakip.DersRecview
import com.example.lifelog.DersTakip.DersTakip2Activity
import com.example.lifelog.R
import com.example.lifelog.database.Database
import com.example.lifelog.database.DersTakip
import com.example.lifelog.database.DersTakipdao
import com.example.lifelog.databinding.ActivityDersTakipBinding

class DersTakipActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDersTakipBinding
    private lateinit var adapter: DersRecview
    private lateinit var Gelenders: ArrayList<DersTakip>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_ders_takip)
        binding= ActivityDersTakipBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val vt= Database(this)
        Gelenders= DersTakipdao().getAllDers(vt)
        binding.DersRV.setHasFixedSize(true)
        binding.DersRV.layoutManager= LinearLayoutManager(this)
        adapter= DersRecview(this,Gelenders)
        binding.DersRV.adapter=adapter

        binding.fabDersEkle.setOnClickListener {
            val gecis= Intent(this, DersTakip2Activity::class.java)
            startActivity(gecis)
        }

    }

    override fun onResume() {
        super.onResume()
        val vt= Database(this)
        Gelenders.clear()
        Gelenders.addAll(DersTakipdao().getAllDers(vt))
        adapter.notifyDataSetChanged()
    }
}