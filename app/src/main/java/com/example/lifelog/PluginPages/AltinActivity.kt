package com.example.lifelog.PluginPages

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.widget.EditText
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.lifelog.R
import com.example.lifelog.database.Database
import com.example.lifelog.database.Goldsdao
import com.example.lifelog.databinding.ActivityAltinBinding
import com.google.android.material.floatingactionbutton.FloatingActionButton

class AltinActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAltinBinding
    private lateinit var fab: FloatingActionButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityAltinBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val vt = Database(this@AltinActivity)

        fab = findViewById(R.id.fabAltinEkle)

        val ilkListe = Goldsdao().fetchAllGold(vt)
        Log.e("AltinActivity", "Açılışta çekilen altınlar: $ilkListe")

        //recyclerView ile adapter sınıfının bağlanması
        binding.altinRecyclerView.layoutManager = LinearLayoutManager(this)
        binding.altinRecyclerView.adapter = AltinAdapter(this, ilkListe)

        //fab butonuna basılması durumunda altın ekleme diyaloğunun açılması
        fab.setOnClickListener{
            //altın ekleme diyaloğunun tasarımı
            val dialogView = LayoutInflater.from(this@AltinActivity).inflate(R.layout.dialog_altin_ekle, null)

            //edittext, radiogrup erişimi
            val altinMiktarGirisiEditText = dialogView.findViewById<EditText>(R.id.editTextAltınMiktarGirisi)
            val radioGrup = dialogView.findViewById<RadioGroup>(R.id.altinRadioGrup)
            //alertdialogun başlık görünüm gibi özelliklerinin ayarlanması
            val builder = AlertDialog.Builder(this@AltinActivity)
            builder.setTitle("Altın Ekle")
                .setView(dialogView)

            builder.setPositiveButton("Ekle"){_, _ ->

                val altinMiktarText = altinMiktarGirisiEditText.text.toString().trim()
                val secilenRadioButtonId = radioGrup.checkedRadioButtonId

                //altın miktarı girilmemesi durumunun kontrolü ve kullanıcıya mesaj verilmesi
                if (altinMiktarText.isEmpty()) {
                    Toast.makeText(this, "Lütfen miktar giriniz.", Toast.LENGTH_SHORT).show()
                    return@setPositiveButton
                }

                val altinMiktar = altinMiktarText.toIntOrNull()
                if (altinMiktar == null || altinMiktar <= 0) {
                    Toast.makeText(this, "Geçerli bir miktar giriniz.", Toast.LENGTH_SHORT).show()
                    return@setPositiveButton
                }

                //altın türünün seçilmemesi durumunun kontrol edilmesi ve kullanıcıya mesaj verilmesi
                if (secilenRadioButtonId == -1) {
                    Toast.makeText(this, "Lütfen altın türü seçiniz.", Toast.LENGTH_SHORT).show()
                    return@setPositiveButton
                }

                //seçilen radiobuttondan altın türünün alınması
                val secilenRadioButton = dialogView.findViewById<RadioButton>(secilenRadioButtonId)
                val altinTuru = secilenRadioButton.text.toString()

                //Kullanıcıya eklenen verinin bilgisini ekranda gösterme
                Toast.makeText(this@AltinActivity, "Altın Türü: $altinTuru,  Eklenen Altın Miktarı: $altinMiktar Adet", Toast.LENGTH_SHORT).show()
                //kullanıcın girdiği verilerin veritabanına kayıt edilmesi
                Goldsdao().addGold(vt, altinTuru, altinMiktar)
                //kayıt sonrası Log kaydı ile kontrol etme
                val guncelListe = Goldsdao().fetchAllGold(vt)
                Log.e("AltinActivity", "Ekleme sonrası veritabanı: $guncelListe")

                binding.altinRecyclerView.adapter = AltinAdapter(this@AltinActivity, guncelListe)

            }
                .setNegativeButton("İptal", null)
                .create()

                .show()
        }

    }
}