package com.example.lifelog.PluginPages

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.lifelog.ApiKeys.Keys
import com.example.lifelog.KriptoPages.AllCryptoActivity
import com.example.lifelog.R
import com.example.lifelog.databinding.ActivityKriptoBinding
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import org.json.JSONObject
import java.io.IOException

class KriptoActivity : AppCompatActivity() {
    private lateinit var binding: ActivityKriptoBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_kripto)
        binding= ActivityKriptoBinding.inflate(layoutInflater)
        setContentView(binding.root)


        binding.button.setOnClickListener {
            val gecis= Intent(this@KriptoActivity, AllCryptoActivity::class.java)
            startActivity(gecis)
        }







    }
}
fun getCryptoPrice(symbol: String) {
    val client = OkHttpClient()

    val request = Request.Builder()
        .url("https://api.api-ninjas.com/v1/cryptoprice?symbol=$symbol"+"USD")
        .addHeader("X-Api-Key", Keys().getKriptoKey())
        .build()

    Thread {
        try {
            val response: Response = client.newCall(request).execute()
            if (response.isSuccessful) {
                val body = response.body?.string()
                val json= JSONObject(body.toString())
                val price=json.getString("price")

                Log.e("Sonuç: ","$price")
            } else {
                Log.e("Hata:,","${response.code} ${response.message}")
            }
        } catch (e: IOException) {
            Log.e("İstek atılamadı:","${e.message}")
        }
    }.start()
}