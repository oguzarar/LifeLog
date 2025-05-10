package com.example.lifelog.DovizTakip

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.lifelog.ApiKeys.Keys.Companion.dovizApiKeys2
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import org.json.JSONObject

class Livedata : ViewModel(){

    private val _price = MutableLiveData<Double?>()
    val price: LiveData<Double?> get() = _price

    private var fetchJob: Job? = null

    fun startFetching(symbol: String) {
        fetchJob?.cancel() // varsa iptal et
        fetchJob = viewModelScope.launch {
            while (isActive) {
                fetchCurrencyRate(symbol,"TRY") { currentPrice ->
                    _price.value = currentPrice
                }
                delay(2000)
            }
        }
    }
    fun stopFetching() {
        fetchJob?.cancel() // İşlemi durdur
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
        val apiUrl = "https://api.freecurrencyapi.com/v1/latest?apikey=${dovizApiKeys2}&base_currency=${from}&currencies=${to}"
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
                Log.e("Exception:", e.message ?: "Hata")
                null
            }
        }
    }
}