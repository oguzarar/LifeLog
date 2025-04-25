package com.example.lifelog.KriptoPages

import android.app.Activity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.lifelog.ApiKeys.Keys
import com.example.lifelog.R
import com.example.lifelog.database.Crypto
import com.example.lifelog.databinding.ActivitySatinAlimBinding
import kotlinx.coroutines.delay
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import org.json.JSONObject
import java.io.IOException
import java.util.HashMap
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import com.google.gson.JsonPrimitive
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SatinAlimActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySatinAlimBinding
    var gelenfiyat: String?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_satin_alim)
        binding= ActivitySatinAlimBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val gelenCrypto=intent.getSerializableExtra("Crypto") as Crypto

        binding.GelenCoinLong.text=gelenCrypto.CryptoName
        binding.GelenCoinshort.text=gelenCrypto.Cryptoshort
        fetchCryptoPrice(gelenCrypto.Cryptoshort) { price ->
            if (price != null) {
                gelenfiyat=price.toString()
                binding.GuncelFiyat.text=gelenfiyat
        }
    }
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
                    val gelen=p0.toString()
                    val gelen2= gelenfiyat!!.toDouble()
                    val guncel=gelen.toDouble()*gelen2
                    binding.AmountOfUsdt.setText(guncel.toString())
                }catch (e:NumberFormatException){
                }
            }
        })
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

    // OkHttpClient instance
    val client = OkHttpClient()

    // Request creation
    val request = Request.Builder()
        .url(apiUrl)
        .addHeader("X-Api-Key", Keys().getKriptoKey())
        .build()

    // Arka planda ağ isteğini yapıyoruz
    return withContext(Dispatchers.IO) {
        try {
            // Execute the request
            val response: Response = client.newCall(request).execute()

            // Check the response status code
            if (response.isSuccessful) {
                // Parse the response body
                val responseBody = response.body?.string()

                // Use Gson to parse the response body
                val jsonObject = JsonParser.parseString(responseBody).asJsonObject

                // Get the "price" field from the JSON response
                val priceJson: JsonPrimitive? = jsonObject.getAsJsonPrimitive("price")

                // Return the price as a Double if it's available
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
}}
