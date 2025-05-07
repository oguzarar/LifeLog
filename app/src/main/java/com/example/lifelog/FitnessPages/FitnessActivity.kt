package com.example.lifelog.FitnessPages

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.SystemClock
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.transition.Visibility
import com.example.lifelog.R
import com.example.lifelog.database.Database
import com.example.lifelog.databinding.ActivityFitnessBinding
import com.example.lifelog.databinding.ActivityMainBinding
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class FitnessActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFitnessBinding
    private lateinit var aktiviteSecLauncher: ActivityResultLauncher<Intent>
    private lateinit var vt: Database

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityFitnessBinding.inflate(layoutInflater)
        setContentView(binding.root)

        vt = Database(this@FitnessActivity)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        aktiviteSecLauncher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val data = result.data
                val egzersizIsmi = data?.getStringExtra("egzersiz ismi")
                val egzersizKcal = data?.getIntExtra("egzersiz kcal", 0)

                if (egzersizIsmi != null && egzersizKcal != null){
                    binding.secilenAktiviteTextView.text = egzersizIsmi
                    binding.secilenAktiviteKalori.text = "${egzersizKcal} kcal/saat"
                }

                binding.eklenenAktiviteCard.visibility = View.VISIBLE
                binding.kronometreCard.visibility = View.VISIBLE
                binding.buttonAktiviteBitir.visibility = View.VISIBLE
                binding.baslatDurdurButtonKronometre.visibility = View.VISIBLE

            }
        }

        binding.aktiviteEkleCard.setOnClickListener{

            val intent = Intent(this@FitnessActivity, AktiviteSecActivity::class.java)
            aktiviteSecLauncher.launch(intent)

        }

        binding.gecmisAktivitelerCard.setOnClickListener{
            val intent = Intent(this@FitnessActivity, GecmisAktivitelerActivity::class.java)
            startActivity(intent)
        }

        var isRunning = false
        var timeWhenStopped: Long = 0
        var aktiviteBaslatildiMi = false

        binding.baslatDurdurButtonKronometre.setOnClickListener {
            if (!isRunning) {
                binding.chronometer.base = SystemClock.elapsedRealtime() + timeWhenStopped
                binding.chronometer.start()
                isRunning = true
                binding.baslatDurdurButtonKronometre.text = "Durdur"
                binding.aktiviteEkleCard.isClickable = false
                binding.aktiviteEkleCard.alpha = 0.5f
            } else {
                timeWhenStopped = binding.chronometer.base - SystemClock.elapsedRealtime()
                binding.chronometer.stop()
                isRunning = false
                binding.baslatDurdurButtonKronometre.text = "Başlat"
            }
            aktiviteBaslatildiMi = true
        }

        var sureMiliSaniye = SystemClock.elapsedRealtime() - binding.chronometer.base
        var sureSaniye = (sureMiliSaniye / 1000).toInt()

        binding.buttonAktiviteBitir.setOnClickListener {

            val dialogView = LayoutInflater.from(this@FitnessActivity).inflate(R.layout.aktivite_dialog, null)

            val egzersizIsmi = binding.secilenAktiviteTextView.text.toString()
            val kaloriText = binding.secilenAktiviteKalori.text.toString()
            val kalori = kaloriText.substringBefore(" ").toDoubleOrNull() ?: 0

            sureMiliSaniye = SystemClock.elapsedRealtime() - binding.chronometer.base
            sureSaniye = (sureMiliSaniye / 1000).toInt()


            val dakika = sureSaniye / 60
            val saniye = sureSaniye % 60

            val tarih = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault()).format(Date())

            val harcananKalori = (kalori.toDouble() / 3600) * sureSaniye

            dialogView.findViewById<TextView>(R.id.dialogEgzersizIsmi).text = "Egzersiz: $egzersizIsmi"
            dialogView.findViewById<TextView>(R.id.dialogHarcananKalori).text = "Harcanan Kalori: %.2f kcal".format(harcananKalori)
            dialogView.findViewById<TextView>(R.id.dialogEgzersizSuresi).text = "Geçen Süre: $dakika dakika $saniye saniye"
            dialogView.findViewById<TextView>(R.id.dialogEgzersizTarihi).text = "Tarih: $tarih"

            val alertDialog = AlertDialog.Builder(this@FitnessActivity)
                .setView(dialogView)
                .setPositiveButton("Bitir") { _, _ ->
                    val aktivite = AktiviteModel(
                        aktiviteAdi = egzersizIsmi,
                        harcananKalori = harcananKalori,
                        aktiviteSuresi = sureSaniye.toString(),
                        aktiviteTarihi = tarih
                    )
                    AktiviteTakipdao().aktiviteKaydet(vt, aktivite.aktiviteAdi, aktivite.harcananKalori, aktivite.aktiviteSuresi)

                    Toast.makeText(this, "Aktivite kaydedildi!", Toast.LENGTH_SHORT).show()

                    binding.baslatDurdurButtonKronometre.text = "Başlat"

                    binding.aktiviteEkleCard.isClickable = true
                    binding.aktiviteEkleCard.alpha = 1.0f
                    binding.chronometer.stop()
                    binding.chronometer.base = SystemClock.elapsedRealtime()
                    isRunning = false
                    binding.eklenenAktiviteCard.visibility = View.GONE
                    binding.kronometreCard.visibility = View.GONE
                    binding.buttonAktiviteBitir.visibility = View.GONE
                    binding.baslatDurdurButtonKronometre.visibility = View.GONE

                    aktiviteBaslatildiMi = false
                }

                .create()

            alertDialog.show()
        }

        binding.fizikselAktiviteSayfasiGeriTusu.setOnClickListener{

            if(aktiviteBaslatildiMi == true){
                AlertDialog.Builder(this)
                    .setTitle("Kronometre Çalışıyor")
                    .setMessage("Bitirilmemiş bir aktiviteniz mevcut. Sayfadan çıkmak istediğinize emin misiniz?")
                    .setPositiveButton("Evet"){ _, _ ->
                        finish()
                    }
                    .setNegativeButton("Hayır", null)
                    .show()
            }
            else{
                finish()
            }

        }



    }
}