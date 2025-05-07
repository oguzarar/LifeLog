package com.example.lifelog.PluginPages

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.lifelog.ApiKeys.Keys
import com.example.lifelog.DovizTakip.DovizListeleActivity
import com.example.lifelog.DovizTakip.MainPageDovizListeleRecView
import com.example.lifelog.R
import com.example.lifelog.database.Dao.Doviz.DovizDao
import com.example.lifelog.database.Database
import com.example.lifelog.database.Dao.Doviz.DovizDB
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
    private lateinit var DovizLists: ArrayList<DovizDB>
    private lateinit var DovizUpdate: ArrayList<DovizDB>
    private lateinit var adapter: MainPageDovizListeleRecView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_doviz)
        binding= ActivityDovizBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val vt= Database(this)

        binding.button.setOnClickListener {
            val gecis= Intent(this@DovizActivity, DovizListeleActivity::class.java)
            startActivity(gecis)
        }
        DovizUpdate= ArrayList<DovizDB>()
        DovizUpdate=DovizDao().getAllAssets(vt)



        DovizLists= ArrayList<DovizDB>()
        DovizLists= DovizDao().getAllAssets(vt)
        binding.MainPageDovizRecView.setHasFixedSize(true)
        binding.MainPageDovizRecView.layoutManager= LinearLayoutManager(this)
        adapter= MainPageDovizListeleRecView(this,DovizLists)
        binding.MainPageDovizRecView.adapter=adapter

        binding.backbutton.setOnClickListener {
            finish()
        }

    }

    override fun onResume() {
        super.onResume()
        val vt= Database(this)
        DovizLists.clear()
        DovizLists.addAll(DovizDao().getAllAssets(vt))
        adapter.notifyDataSetChanged()
        val getir= DovizDao().getTotalAmount(vt)
        val son="%.2f".format(getir.toDouble())
        binding.ToplamBakiyeBilgiText.text=son
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
