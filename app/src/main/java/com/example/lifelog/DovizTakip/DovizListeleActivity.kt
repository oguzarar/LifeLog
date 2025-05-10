package com.example.lifelog.DovizTakip

import android.os.Bundle
import android.widget.SearchView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.lifelog.R
import com.example.lifelog.database.AssetsDao.Doviz.Doviz
import com.example.lifelog.databinding.ActivityDovizEkleBinding

class DovizListeleActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDovizEkleBinding
    private lateinit var CurrencyList: ArrayList<Doviz>
    private lateinit var adapter: DovizListeleRecview
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_doviz_ekle)
        binding= ActivityDovizEkleBinding.inflate(layoutInflater)
        setContentView(binding.root)
        CurrencyList= ArrayList<Doviz>()
        val currencies = mapOf(
            "EUR" to "Euro",
            "USD" to "US Dollar",
            "JPY" to "Japanese Yen",
            "BGN" to "Bulgarian Lev",
            "CZK" to "Czech Republic Koruna",
            "DKK" to "Danish Krone",
            "GBP" to "British Pound Sterling",
            "HUF" to "Hungarian Forint",
            "PLN" to "Polish Zloty",
            "RON" to "Romanian Leu",
            "SEK" to "Swedish Krona",
            "CHF" to "Swiss Franc",
            "ISK" to "Icelandic Króna",
            "NOK" to "Norwegian Krone",
            "HRK" to "Croatian Kuna",
            "RUB" to "Russian Ruble",
            "TRY" to "Turkish Lira",
            "AUD" to "Australian Dollar",
            "BRL" to "Brazilian Real",
            "CAD" to "Canadian Dollar",
            "CNY" to "Chinese Yuan",
            "HKD" to "Hong Kong Dollar",
            "IDR" to "Indonesian Rupiah",
            "ILS" to "Israeli New Sheqel",
            "INR" to "Indian Rupee",
            "KRW" to "South Korean Won",
            "MXN" to "Mexican Peso",
            "MYR" to "Malaysian Ringgit",
            "NZD" to "New Zealand Dollar",
            "PHP" to "Philippine Peso",
            "SGD" to "Singapore Dollar",
            "THB" to "Thai Baht",
            "ZAR" to "South African Rand"
        )
        for(i in currencies){
            var currency= Doviz(i.key,i.value)//Doviz sınıfından bir nesne oluşturuluyor
            CurrencyList.add(currency)// ve bu nesneler ArrayList'e ekleniyor.
        }

        //alınan veriler revcview'e gönderildi
        binding.DovizListeleRecView.setHasFixedSize(true)
        binding.DovizListeleRecView.layoutManager= LinearLayoutManager(this)
        adapter= DovizListeleRecview(this,CurrencyList)
        binding.DovizListeleRecView.adapter=adapter

        //Arama işlmei
        binding.searchview.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }
            override fun onQueryTextChange(newText: String?): Boolean {
                adapter.filter.filter(newText)
                return true
            }
        })

        //Geri butonu
        binding.backbutton.setOnClickListener {
            finish()
        }

    }
    }
