package com.example.lifelog.KaloriTakip

import android.content.Intent
import android.database.sqlite.SQLiteConstraintException
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.lifelog.R
import com.example.lifelog.database.Database
import com.example.lifelog.database.KaloriDao
import com.example.lifelog.databinding.ActivityKaloriEkleBinding
import com.example.lifelog.duzenleme.duzenleme.Companion.formatNumber2

class KaloriEkleActivity : AppCompatActivity() {
    private lateinit var binding: ActivityKaloriEkleBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_kalori_ekle)
        binding= ActivityKaloriEkleBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val gelenYemek= intent.getSerializableExtra("Yemek") as Kalori
        binding.GelenYemek.text=gelenYemek.isim
        binding.KaloriMiktari.text=gelenYemek.kalori
        binding.proteinMiktari.text=gelenYemek.protein

        binding.editTextText.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(
                p0: CharSequence?,
                p1: Int,
                p2: Int,
                p3: Int
            ) {

            }

            override fun onTextChanged(
                p0: CharSequence?,
                p1: Int,
                p2: Int,
                p3: Int
            ) {

            }

            override fun afterTextChanged(p0: Editable?) {
                try {
                    val gelen=p0.toString()
                    val gelenkalori=(gelenYemek.kalori.toDouble())/100
                    val gelenprotein=(gelenYemek.protein.toDouble())/100
                    binding.GirlenKaloriMiktari.text=formatNumber2((gelen.toDouble()*gelenkalori))
                    binding.girilenproteinmktari.text=formatNumber2((gelen.toDouble()*gelenprotein))
                }catch (e:NumberFormatException){
                    binding.GirlenKaloriMiktari.text=""
                    binding.girilenproteinmktari.text=""

                }
            }
        })
        binding.YemekEkleButton.setOnClickListener {
            val vt= Database(this)

            val yemekismi=gelenYemek.isim
            val yemekTuru=gelenYemek.kategori
            val yemekkalori=binding.GirlenKaloriMiktari.text.toString()
            val yemekprotein=binding.girilenproteinmktari.text.toString()
            if(yemekTuru.isNullOrEmpty()||yemekprotein.isNullOrEmpty()){
                Toast.makeText(this,"Değer giriniz", Toast.LENGTH_SHORT).show()
            }else{
                try {
                    KaloriDao().Addyemek(vt,yemekismi,yemekTuru,yemekkalori,yemekprotein)
                    Toast.makeText(this,"Eklendi", Toast.LENGTH_SHORT).show()
                    binding.editTextText.text.clear()
                    binding.GirlenKaloriMiktari.text=""
                    binding.girilenproteinmktari.text=""
                }catch (e:SQLiteConstraintException){
                    KaloriDao().UpadteKalori(vt,yemekismi,yemekkalori.toDouble(),yemekprotein.toDouble())
                    Toast.makeText(this,"Eklendi", Toast.LENGTH_SHORT).show()
                    binding.editTextText.text.clear()
                    binding.GirlenKaloriMiktari.text=""
                    binding.girilenproteinmktari.text=""
                }

            }
        }

        binding.backbutton.setOnClickListener {
            finish()
        }






    }
}