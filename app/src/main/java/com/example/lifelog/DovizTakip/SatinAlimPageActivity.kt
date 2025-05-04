package com.example.lifelog.DovizTakip

import android.database.sqlite.SQLiteConstraintException
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.lifelog.ApiKeys.Keys
import com.example.lifelog.R
import com.example.lifelog.database.Database
import com.example.lifelog.database.Doviz
import com.example.lifelog.database.DovizDao
import com.example.lifelog.databinding.ActivitySatinAlimPageBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import org.json.JSONObject

class SatinAlimPageActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySatinAlimPageBinding
    var gelenfiyat: String?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_satin_alim_page)
        binding= ActivitySatinAlimPageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val vt= Database(this)

        val gelen=intent.getSerializableExtra("Doviz") as Doviz

        fetchCurrencyRate(gelen.Dovizshort,"TRY") {price->
            if (price != null) {
                gelenfiyat=price.toString()
                binding.GuncelFiyat.text=gelenfiyat
            }
        }
        binding.GelenDovizLong.text=gelen.DovizName
        binding.GelenDovizshort.text=gelen.Dovizshort

        binding.AmountOfMoney.addTextChangedListener(object : TextWatcher{
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
                        binding.GuncelTutar.text=guncel.toString()
                    }catch (e: NullPointerException){
                        Log.e("Fiyat alınamadı","Fiyat alınamadı")

                    }
                }catch (e:NumberFormatException){
                    if(binding.AmountOfMoney.text.isNullOrEmpty()){
                        binding.GuncelTutar.text=""
                    }
                }
            }
        })

        binding.DovizBuyButton.setOnClickListener {
            val DovizLongName=gelen.DovizName
            val DovizShortName=gelen.Dovizshort
            val DovizAmount=binding.AmountOfMoney.text.toString()
            val DovizTRYTutar=binding.GuncelTutar.text.toString()
            if(DovizAmount.isEmpty()&&DovizTRYTutar.isEmpty()){
                Toast.makeText(this,"Fiyat Bilgisi Alınamadı",Toast.LENGTH_SHORT).show()
            }else{
                try {
                    DovizDao().AddDoviz(vt,DovizLongName,DovizShortName,DovizAmount,DovizTRYTutar)
                }catch (e: SQLiteConstraintException){
                    DovizDao().UpadteCryptoUSDT(vt,DovizLongName,DovizAmount.toDouble(),DovizTRYTutar.toDouble())
                }
                binding.AmountOfMoney.text.clear()
                binding.GuncelTutar.text=""
                Toast.makeText(this,"Eklendi",Toast.LENGTH_SHORT).show()
            }
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
