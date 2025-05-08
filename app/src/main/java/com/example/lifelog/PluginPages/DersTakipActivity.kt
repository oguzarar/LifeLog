package com.example.lifelog.PluginPages

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.lifelog.DersTakip.DersRecview
import com.example.lifelog.DersTakip.DersTakip2Activity
import com.example.lifelog.R
import com.example.lifelog.database.Database
import com.example.lifelog.database.TaskDaos.derstakip.DersTakip
import com.example.lifelog.database.TaskDaos.derstakip.DersTakipDao
import com.example.lifelog.databinding.ActivityDersTakipBinding

class DersTakipActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDersTakipBinding
    private lateinit var adapter: DersRecview
    private lateinit var Gelenders: MutableList<DersTakip>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_ders_takip)
        binding= ActivityDersTakipBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val vt= Database(this)
        Gelenders= DersTakipDao().GetAllTask(vt)
        binding.DersRV.setHasFixedSize(true)
        binding.DersRV.layoutManager= LinearLayoutManager(this)
        adapter= DersRecview(this,Gelenders)
        binding.DersRV.adapter=adapter

        binding.fabDersEkle.setOnClickListener {
            val gecis= Intent(this, DersTakip2Activity::class.java)
            startActivity(gecis)
        }
        binding.backbutton.setOnClickListener {
            finish()
        }

    }

    override fun onResume() {
        super.onResume()
        val vt= Database(this)
        Gelenders.clear()
        Gelenders.addAll(DersTakipDao().GetAllTask(vt))
        adapter.notifyDataSetChanged()
    }
}