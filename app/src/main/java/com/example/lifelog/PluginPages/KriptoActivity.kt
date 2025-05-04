package com.example.lifelog.PluginPages

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Adapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.lifelog.ApiKeys.Keys
import com.example.lifelog.KriptoPages.AllCryptoActivity
import com.example.lifelog.KriptoPages.ListeleRecView
import com.example.lifelog.R
import com.example.lifelog.database.CryptoDB
import com.example.lifelog.database.CryptoDao
import com.example.lifelog.database.CryptoUpdate
import com.example.lifelog.database.Database
import com.example.lifelog.databinding.ActivityKriptoBinding
import com.google.gson.JsonParser
import com.google.gson.JsonPrimitive
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import org.json.JSONObject
import java.io.IOException

class KriptoActivity : AppCompatActivity() {
    private lateinit var binding: ActivityKriptoBinding
    private lateinit var adapter: ListeleRecView
    private lateinit var CrpytoLists: ArrayList<CryptoDB>
    private lateinit var CryptoList2: List<CryptoUpdate>
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

        //Eklenmiş kripto paralın fiyatını güncelleme kısmı
        CryptoList2= CryptoDao().getCryptoName(vt)//Veritabaınından kripto kısa isimelri alındı
        for(i in CryptoList2){//Döngü içinde listelenmiş kriptoların fiyatları alındı
            fetchCryptoPrice(i.CryptoShortame){price->
                val guncel= price?.times(i.CoinAmount.toDouble())//güncel fiyatlar coin miktarı ile çarpıldı
                CryptoDao().guncelleCrypto(vt,i.CryptoShortame,guncel.toString())//Güncel fiyatlar VT'ye eklendi.
            }
        }

        //Toplam miktar vt'den çekildi
        val getir=CryptoDao().GetTotalAmount(vt)
        val son="%.2f".format(getir.toDouble())
        binding.ToplamBakiyeBilgiText.text=son

        //Eklenen kriptolar recview ile ekrana verildi.
        binding.kriptoRv.setHasFixedSize(true)
        binding.kriptoRv.layoutManager= LinearLayoutManager(this)
        CrpytoLists= CryptoDao().GetCrypto(vt)
        adapter= ListeleRecView(this,CrpytoLists)
        binding.kriptoRv.adapter=adapter
    }

    //Yeni kritp eklendiğinde sayfa güncellenecek
    override fun onResume() {
        super.onResume()
        val vt= Database(this)
        CrpytoLists.clear()
        CrpytoLists.addAll(CryptoDao().GetCrypto(vt))
        adapter.notifyDataSetChanged()
        val getir=CryptoDao().GetTotalAmount(vt)
        val son="%.2f".format(getir.toDouble())
        binding.ToplamBakiyeBilgiText.text=son
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
}

