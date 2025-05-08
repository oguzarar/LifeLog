package com.example.lifelog.Notes

import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.lifelog.R
import com.example.lifelog.database.Database
import com.example.lifelog.database.Notes
import com.example.lifelog.database.Notesdao
import com.example.lifelog.databinding.ActivityNotuGoruntuleBinding

class NotuGoruntuleActivity : AppCompatActivity() {

    private lateinit var binding: ActivityNotuGoruntuleBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityNotuGoruntuleBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val vt = Database(this@NotuGoruntuleActivity)

        //Notlar sayfasından gönderilen nesneyi yakalama
        val gelenNot = intent.getSerializableExtra("note") as Notes
        //Gelen verileri yükleme
        binding.textViewNotGoruntuleSaat.text = gelenNot.note_time
        binding.textViewNotGoruntuleTarih.text = gelenNot.note_date
        binding.textViewNotGoruntuleBaslik.text = gelenNot.note_title
        binding.textViewNotIcerigi.text = gelenNot.note
        //geri dönme tuşu
        binding.imageViewNotuGoruntuleGeri.setOnClickListener{
            finish()
        }
        //not silme tuşuna basılması durumu
        binding.imageViewNotuSil.setOnClickListener{

            AlertDialog.Builder(this)
                .setTitle("Silinsin Mi?")
                .setMessage("Notu silmek istediğinize emin misiniz?")
                .setPositiveButton("Evet"){ _, _ ->
                    Notesdao().notSil(vt, gelenNot.note_id!!)
                    Toast.makeText(this@NotuGoruntuleActivity, "Not Silindi.", Toast.LENGTH_SHORT).show()
                    finish()
                }
                .setNegativeButton("İptal", null)
                .show()



        }





    }
}