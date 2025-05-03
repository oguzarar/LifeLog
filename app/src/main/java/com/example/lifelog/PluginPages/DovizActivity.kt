package com.example.lifelog.PluginPages

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.lifelog.ApiKeys.Keys
import com.example.lifelog.DovizTakip.DovizListeleActivity
import com.example.lifelog.R
import com.example.lifelog.databinding.ActivityDovizBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import org.json.JSONObject

class DovizActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDovizBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_doviz)
        binding= ActivityDovizBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.button.setOnClickListener {
            val gecis= Intent(this@DovizActivity, DovizListeleActivity::class.java)
            startActivity(gecis)
        }


    }
}
fun fetchCurrencyRate(from: String, to: String, callback: (Double?) -> Unit) {
    // Coroutine başlatıyoruz
    CoroutineScope(Dispatchers.Main).launch {
        val rate = getCurrencyRate(from, to)
        callback(rate)
    }
}

// Gerçek API çağrısını yapacak olan suspend fonksiyonu
suspend fun getCurrencyRate(from: String, to: String): Double? {
    val apiUrl = "https://api.freecurrencyapi.com/v1/latest?apikey=${Keys().getDovizKey()}&base_currency=${from}&currencies=${to}"
    val client = OkHttpClient()

    val request = Request.Builder()
        .url(apiUrl)
        .build()

    return withContext(Dispatchers.IO) {
        try {
            val response: Response = client.newCall(request).execute()

            if (response.isSuccessful) {
                val responseBody = response.body?.string()
                val jsonObject = JSONObject(responseBody)

                // Gelen JSON'dan döviz kuru değerini alıyoruz
                val price = jsonObject.getJSONObject("data").getDouble(to)
                price
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
