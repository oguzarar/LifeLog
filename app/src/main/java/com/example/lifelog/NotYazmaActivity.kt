package com.example.lifelog

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.lifelog.database.Database
import com.example.lifelog.database.Notesdao
import com.example.lifelog.databinding.ActivityNotYazmaBinding
import com.example.lifelog.pages.NotesFragment

class NotYazmaActivity : AppCompatActivity() {

    private lateinit var binding: ActivityNotYazmaBinding
    private lateinit var editTextNote: EditText
    private lateinit var editTextBaslik: EditText
    private lateinit var buttonNotKaydet: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityNotYazmaBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val vt = Database(this@NotYazmaActivity)

        editTextNote = findViewById(R.id.EditTextMultiLineNotYazma)
        editTextBaslik = findViewById(R.id.editTextBaslikGiris)
        buttonNotKaydet = findViewById(R.id.buttonNotKaydet)

        binding.imageViewNotlarGeri.setOnClickListener{
            finish()        //not yazma ekranında iken geri dönme tuşuna tıklanılması halinde notlarım ekranına geri dönüş.
        }
        //Kaydet butonuna basılması ile verinin kayıt edilmesi.
        binding.buttonNotKaydet.setOnClickListener{
            val gelenNot = editTextNote.text.toString()
            val gelenBaslik = editTextBaslik.text.toString()
            //kullanıcının not ve başlık kısmına veri girişi yapmaması durumunu kontrol etme
            if(gelenNot.isNotEmpty() && gelenBaslik.isNotEmpty()){
                Toast.makeText(this@NotYazmaActivity, "Not Kaydedildi.", Toast.LENGTH_SHORT).show()
                Notesdao().notEkle(vt, gelenNot, gelenBaslik)
                finish()
            }else{
                Toast.makeText(this@NotYazmaActivity, "Not girişi yapınız.", Toast.LENGTH_SHORT).show()
            }




        }

    }
}