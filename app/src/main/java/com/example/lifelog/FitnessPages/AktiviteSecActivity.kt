package com.example.lifelog.FitnessPages

import android.app.Activity
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.lifelog.R
import com.example.lifelog.databinding.ActivityAktiviteSecBinding
import com.example.lifelog.databinding.ActivityMainBinding

class AktiviteSecActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAktiviteSecBinding
    private lateinit var aktiviteSecAdapter: AktiviteSecAdapter

    val egzersizListesi = listOf(
        Exercises("Koşu", 700),
        Exercises("Yüzme", 600),
        Exercises("Bisiklet", 550),
        Exercises("Yürüyüş", 300),
        Exercises("Aerobik", 500),
        Exercises("Sprint Koşusu", 900),
        Exercises("Spin Bisiklet", 700),
        Exercises("CrossFit", 800),
        Exercises("Vücut Ağırlığı Egzersizleri", 450),
        Exercises("Yoga", 250),
        Exercises("Pilates", 300),
        Exercises("Hızlı Yürüyüş", 350),
        Exercises("Kayak", 600),
        Exercises("İp Atlama", 800),
        Exercises("Kickboks", 700),
        Exercises("Ağırlık Kaldırma", 450),
        Exercises("Dans", 500),
        Exercises("Futbol", 600),
        Exercises("Basketbol", 700),
        Exercises("Cimnastik", 450),
        Exercises("Voleybol", 400)
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityAktiviteSecBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        aktiviteSecAdapter = AktiviteSecAdapter(this@AktiviteSecActivity, egzersizListesi)
        binding.aktivitelerRecyclerView.layoutManager = LinearLayoutManager(this@AktiviteSecActivity)
        binding.aktivitelerRecyclerView.adapter = aktiviteSecAdapter

        binding.aktiviteSecmeGeriTusu.setOnClickListener{
            finish()
        }


    }
}