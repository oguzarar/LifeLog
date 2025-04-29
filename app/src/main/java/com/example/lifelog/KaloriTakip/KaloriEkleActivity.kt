package com.example.lifelog.KaloriTakip

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.lifelog.R
import com.example.lifelog.databinding.ActivityKaloriEkleBinding

class KaloriEkleActivity : AppCompatActivity() {
    private lateinit var binding: ActivityKaloriEkleBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_kalori_ekle)
        binding= ActivityKaloriEkleBinding.inflate(layoutInflater)
        setContentView(binding.root)



    }
}