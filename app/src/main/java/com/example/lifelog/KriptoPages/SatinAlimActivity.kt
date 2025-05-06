package com.example.lifelog.KriptoPages

import android.app.Activity
import android.database.sqlite.SQLiteConstraintException
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.lifelog.ApiKeys.Keys
import com.example.lifelog.R
import com.example.lifelog.database.Crypto
import com.example.lifelog.database.CryptoDao
import com.example.lifelog.database.Database
import com.example.lifelog.databinding.ActivitySatinAlimBinding
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import com.google.gson.JsonParser
import com.google.gson.JsonPrimitive
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.DecimalFormat

class SatinAlimActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySatinAlimBinding
    var gelenfiyat: String?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_satin_alim)
        binding= ActivitySatinAlimBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val vt= Database(this)
        val gelenCrypto=intent.getSerializableExtra("Crypto") as Crypto//Diğer sayfadan gelen veri

        binding.GelenCoinLong.text=gelenCrypto.CryptoName
        binding.GelenCoinshort.text=gelenCrypto.Cryptoshort

        //Fiyat bilgisi alma
        fetchCryptoPrice(gelenCrypto.Cryptoshort) { price ->
            if (price != null) {
                gelenfiyat=formatNumber(price)
                binding.GuncelFiyat.text=gelenfiyat
        }
    }
        binding.AmountOfCoin.addTextChangedListener(object: TextWatcher{//Edittexte yazılan veriyi anlık olarak alma
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
                    try {
                        val yazilan=p0.toString()
                        val gelenFiyat= gelenfiyat!!.toDouble()
                        val guncel=yazilan.toDouble()*gelenFiyat
                        binding.GuncelTutar.text=formatNumber(guncel)
                        binding.GuncelAdet.text=p0
                        binding.AmountOfUsdt.text.clear()
                    }catch (e: NullPointerException){
                        Log.e("Fiyat alınamadı","Fiyat alınamadı")

                    }

                }catch (e:NumberFormatException){
                    if(binding.AmountOfUsdt.text.isNullOrEmpty()&&binding.AmountOfCoin.text.isNullOrEmpty()){
                        binding.GuncelAdet.text=""
                        binding.GuncelTutar.text=""
                    }

                }
            }
        })
        binding.AmountOfUsdt.addTextChangedListener(object : TextWatcher{
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
                    val yazilan=p0.toString()
                    val gelenFiyat= gelenfiyat!!.toDouble()
                    val Toplam=yazilan.toDouble()/gelenFiyat
                    val son="%.2f".format(Toplam)
                    binding.GuncelAdet.text=son
                    binding.GuncelTutar.text=p0
                    binding.AmountOfCoin.text.clear()
                }catch (e:NumberFormatException){
                    if(binding.AmountOfUsdt.text.isEmpty()&&binding.AmountOfCoin.text.isEmpty()){
                        binding.GuncelAdet.text=""
                        binding.GuncelTutar.text=""
                    }

                }
            }
        })
        binding.KriptoBuyButton.setOnClickListener {
            val adet=binding.GuncelAdet.text.toString()
            val tutar=binding.GuncelTutar.text.toString()
            try {//Eğer eklenen kripto yoksa vt'ye eklenecek
                if(adet.isEmpty()||tutar.isEmpty()){
                    Toast.makeText(this,"Fiyat Bilgisi Alınamadı", Toast.LENGTH_SHORT).show()
                }else{
                    CryptoDao().AddCrypto(vt,gelenCrypto.CryptoName,gelenCrypto.Cryptoshort,tutar,adet)
                    Toast.makeText(this,"Eklendi", Toast.LENGTH_SHORT).show()
                    binding.AmountOfCoin.text.clear()
                    binding.AmountOfUsdt.text.clear()
                }

            }catch (e:SQLiteConstraintException){//Eğer zaten varsa güncellenecek
                if(adet.isEmpty()||tutar.isEmpty()){
                    Toast.makeText(this,"Fiyat Bilgisi Alınamadı", Toast.LENGTH_SHORT).show()
                }else{
                    CryptoDao().UpdateCryptoUSDT(vt,gelenCrypto.CryptoName,tutar.toDouble(),adet.toDouble())
                    Toast.makeText(this,"Eklendi", Toast.LENGTH_SHORT).show()
                    binding.AmountOfCoin.text.clear()
                    binding.AmountOfUsdt.text.clear()
                }
            }
        }
        binding.backbutton.setOnClickListener {
            finish()
        }


}
fun fetchCryptoPrice(symbol: String, callback: (Double?) -> Unit) {
    // Coroutine başlatıyoruz
    CoroutineScope(Dispatchers.Main).launch {
        val price = getCryptoPrice(symbol)
        Log.e("Fiyat",price.toString())
        // Callback ile fiyatı geri gönderiyoruz
        callback(price)
    }
}

suspend fun getCryptoPrice(symbol: String): Double? {
    val apiUrl = "https://api.api-ninjas.com/v1/cryptoprice?symbol=${symbol}USDT"
    val client = OkHttpClient()

    val request = Request.Builder()
        .url(apiUrl)
        .addHeader("X-Api-Key", Keys().getKriptoKey())
        .build()
    return withContext(Dispatchers.IO) {
        try {
            val response: Response = client.newCall(request).execute()

            if (response.isSuccessful) {
                val responseBody = response.body?.string()

                val jsonObject = JsonParser.parseString(responseBody).asJsonObject

                val priceJson: JsonPrimitive? = jsonObject.getAsJsonPrimitive("price")

                priceJson?.asDouble
            } else {
                Log.e("Error:", "Fiyat bilgisi gelmedi ${response.code} ${response.message}")
                null
            }
        } catch (e: Exception) {
            Log.e("Exception:", e.message ?: "Unknown error")
            null
        }
    }
}}
fun formatNumber(number: Double): String {
    val formatter = DecimalFormat("0.##########") // En fazla 10 basamak gösterir, gereksiz sıfır koymaz
    return formatter.format(number)
}
