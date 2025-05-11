package com.example.lifelog.KriptoPages

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.lifelog.ApiKeys.Keys.Companion.kriptoApiKeys
import com.google.gson.JsonParser
import com.google.gson.JsonPrimitive
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

class LiveData : ViewModel() {

    private val client = OkHttpClient()

    private val _price = MutableLiveData<Double?>()
    val price: LiveData<Double?> get() = _price

    private var fetchJob: Job? = null

    fun startFetching(symbol: String) {
        fetchJob?.cancel()
        fetchJob = viewModelScope.launch {
            while (isActive) {
                fetchCryptoPrice(symbol) { currentPrice ->
                    _price.value = currentPrice
                }
                delay(1000)
            }
        }
    }

    fun stopFetching() {
        fetchJob?.cancel()
    }

    private fun fetchCryptoPrice(symbol: String, callback: (Double?) -> Unit) {
        CoroutineScope(Dispatchers.Main).launch {
            val price = getCryptoPrice(symbol.uppercase())
            Log.e("Fiyat", price.toString())
            callback(price)
        }
    }

    suspend fun getCryptoPrice(symbol: String): Double? {
        val apiUrl = "https://api.api-ninjas.com/v1/cryptoprice?symbol=${symbol}USDT"

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
                    Log.e("Error:", "Fiyat bilgisi gelmedi ${response.code} ${response.message}")
                    null
                }
            } catch (e: Exception) {
                Log.e("Exception:", e.message ?: "hata")
                null
            }
        }
    }
}
