package com.example.lifelog.KriptoPages

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.lifelog.ApiKeys.Keys.Companion.kriptoApiKeys
import com.example.lifelog.R
import com.example.lifelog.database.AssetsDao.Crypto.CryptoDB
import com.example.lifelog.database.AssetsDao.Crypto.CryptoDao
import com.example.lifelog.database.Database
import com.example.lifelog.databinding.ActivitySellCryptoBinding
import com.example.lifelog.duzenleme.duzenleme.Companion.formatNumber
import com.google.gson.JsonParser
import com.google.gson.JsonPrimitive
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response

class SellCryptoActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySellCryptoBinding
    private lateinit var CrpytoLists: ArrayList<CryptoDB>
    var gelenfiyat: String?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_sell_crypto)
        binding= ActivitySellCryptoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val vt= Database(this)
        val gelenCrypto= intent.getSerializableExtra("Crypto") as CryptoDB


        binding.GelenCoinLong.text=gelenCrypto.CryptoLongName
        binding.GelenCoinshort.text=gelenCrypto.CryptoShortName
        fetchCryptoPrice(gelenCrypto.CryptoShortName) { price ->
            if (price != null) {
                gelenfiyat=price.toString()
                binding.GuncelFiyat.text=gelenfiyat
            }
        }
        binding.SahipTutar.text=gelenCrypto.AmountOfUSDT
        binding.SahipCrypto.text=gelenCrypto.AmountOfCrypto

        binding.AmountOfCoin.addTextChangedListener(object: TextWatcher{
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
                    val guncel=yazilan.toDouble()*gelenFiyat
                    binding.GuncelTutar.text= formatNumber(guncel)
                    binding.GuncelAdet.text=p0
                    binding.AmountOfUsdt.text.clear()
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
            val AmountUsdt=binding.GuncelTutar.text.toString()
            val AmountCrypto=binding.GuncelAdet.text.toString()
            if(AmountUsdt.isEmpty()||AmountCrypto.isEmpty()){
                Toast.makeText(this,"Fiyat bilgisi alınamadı", Toast.LENGTH_SHORT).show()
            }else{
                if(AmountUsdt.toDouble()<=gelenCrypto.AmountOfUSDT.toDouble()&&AmountCrypto.toDouble()<=gelenCrypto.AmountOfCrypto.toDouble()){
                    val crypto= CryptoDB(gelenCrypto.CryptoLongName,gelenCrypto.CryptoShortName,AmountUsdt,AmountCrypto)
                    CryptoDao().sellAsset(vt,crypto)
                    Toast.makeText(this,"Satıldı", Toast.LENGTH_SHORT).show()
                    binding.AmountOfUsdt.text.clear()
                    binding.AmountOfCoin.text.clear()
                    finish()
                }
                else{
                    Toast.makeText(this,"Bakiye yetersiz", Toast.LENGTH_SHORT).show()
                }
            }
        }
        binding.backbutton.setOnClickListener {
            finish()
        }

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
        .addHeader("X-Api-Key", kriptoApiKeys)
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
                Log.e("Error:", "${response.code} ${response.message}")
                null
            }
        } catch (e: Exception) {
            Log.e("Exception:", e.message ?: "Unknown error")
            null
        }
    }
}




