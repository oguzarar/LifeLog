package com.example.lifelog.PluginPages

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.lifelog.KaloriTakip.YemekListeleActivity
import com.example.lifelog.R
import com.example.lifelog.databinding.ActivityKaloriBinding

class KaloriActivity : AppCompatActivity() {
    private lateinit var binding: ActivityKaloriBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_kalori)
        binding= ActivityKaloriBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.button.setOnClickListener {
            val gecis= Intent(this@KaloriActivity, YemekListeleActivity::class.java)
            startActivity(gecis)
        }
    }

}