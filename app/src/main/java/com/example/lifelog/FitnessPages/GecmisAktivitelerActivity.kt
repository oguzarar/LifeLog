package com.example.lifelog.FitnessPages

import android.os.Bundle
import android.provider.ContactsContract.Data
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.lifelog.R
import com.example.lifelog.database.Database
import com.example.lifelog.databinding.ActivityGecmisAktivitelerBinding
import com.example.lifelog.databinding.ActivityMainBinding

class GecmisAktivitelerActivity : AppCompatActivity() {

    private lateinit var binding: ActivityGecmisAktivitelerBinding
    private lateinit var gecmisAktivitelerAdapter: GecmisAktivitelerAdapter
    private lateinit var aktiviteListesi: List<AktiviteModel>
    private lateinit var vt: Database

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityGecmisAktivitelerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        vt = Database(this)
        aktiviteListesi = AktiviteTakipdao().tumAktiviteleriGetir(vt)

        gecmisAktivitelerAdapter = GecmisAktivitelerAdapter(this@GecmisAktivitelerActivity, aktiviteListesi)
        binding.gecmisAktivitelerRecyclerView.layoutManager = LinearLayoutManager(this@GecmisAktivitelerActivity)
        binding.gecmisAktivitelerRecyclerView.adapter = gecmisAktivitelerAdapter

        binding.gecmisAktivitelerGeriTusu.setOnClickListener{
            finish()
        }

    }
}