package com.example.lifelog.PluginPages

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.lifelog.ApiKeys.Keys.Companion.kriptoApiKeys

import com.example.lifelog.KriptoPages.AllCryptoActivity
import com.example.lifelog.KriptoPages.ListeleRecView
import com.example.lifelog.R
import com.example.lifelog.database.AssetsDao.Crypto.CryptoDB
import com.example.lifelog.database.AssetsDao.Crypto.CryptoDao
import com.example.lifelog.database.Database
import com.example.lifelog.databinding.ActivityKriptoBinding
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

class KriptoActivity : AppCompatActivity() {
    private lateinit var binding: ActivityKriptoBinding
    private lateinit var adapter: ListeleRecView
    private lateinit var CrpytoLists: ArrayList<CryptoDB>
    private lateinit var CryptoList2: List<CryptoDB>
    private var job: Job? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_kripto)
        binding= ActivityKriptoBinding.inflate(layoutInflater)
        setContentView(binding.root)


        binding.button.setOnClickListener {//Kripto satın alım sayfasına geçiş içi
            val gecis= Intent(this@KriptoActivity, AllCryptoActivity::class.java)
            startActivity(gecis)
        }
        val vt= Database(this)


        //Toplam miktar vt'den çekildi
        val getir= CryptoDao().getTotalAmount(vt)
        val son="%.2f".format(getir.toDouble())
        binding.ToplamBakiyeBilgiText.text=son

        //Eklenen kriptolar recview ile ekrana verildi.
        binding.kriptoRv.setHasFixedSize(true)
        binding.kriptoRv.layoutManager= LinearLayoutManager(this)
        CrpytoLists= CryptoDao().getAllAssets(vt)
        adapter= ListeleRecView(this,CrpytoLists)
        binding.kriptoRv.adapter=adapter

        binding.KriptoBack.setOnClickListener {
            finish()
        }
    }


    override fun onResume() {
        super.onResume()

        val vt = Database(this)

        job = CoroutineScope(Dispatchers.Main).launch {
            // Arka planda işlemler
            withContext(Dispatchers.IO) {
                while (isActive) {
                    // Kripto para listelerini güncelleme
                    CrpytoLists.clear()
                    CrpytoLists.addAll(CryptoDao().getAllAssets(vt))

                    // UI'yı güncelleme
                    withContext(Dispatchers.Main) {
                        adapter.notifyDataSetChanged()
                        val getir = CryptoDao().getTotalAmount(vt)
                        val son = "%.2f".format(getir.toDouble())
                        binding.ToplamBakiyeBilgiText.text = son
                    }

                    // Kripto fiyatlarını güncelleme
                    CryptoList2 = CryptoDao().getAllAssets(vt)
                    for (i in CryptoList2) {
                        fetchCryptoPrice(i.CryptoShortName) { price ->
                            if (price != null) {
                                val guncel = price.times(i.AmountOfCrypto.toDouble())
                                val crypto = CryptoDB(i.CryptoLongName, i.CryptoShortName, i.AmountOfCrypto, guncel.toString())
                                CryptoDao().updatePrice(vt, crypto) // Güncel fiyatları VT'ye kaydet
                                Log.e("Fiyat", "Güncellendi") // Fiyat güncellendiğini logla
                            }
                        }
                    }

                    delay(3000) // Her 3 saniyede bir güncelleme yapılacak
                }
            }
        }
    }

    override fun onPause() {
        super.onPause()
        // Sayfa başka bir aktiviteye geçtiğinde veya arka planda olduğunda, işlemi durdur
        job?.cancel()
    }
}

//Apiden fiat bilgisi
fun fetchCryptoPrice(symbol: String, callback: (Double?) -> Unit) {

    CoroutineScope(Dispatchers.Main).launch {
        val price = getCryptoPrice(symbol)
        Log.e("Fiyat",price.toString())

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
                Log.e("Error:", "Fiyat bilgisi gelmedi ${response.code} ${response.message}")
                null
            }
        } catch (e: Exception) {
            Log.e("Exception:", e.message ?: "Unknown error")
            null
        }
    }
}

