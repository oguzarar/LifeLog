package com.example.lifelog.DovizTakip

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.lifelog.ApiKeys.Keys
import com.example.lifelog.PluginPages.DovizActivity
import com.example.lifelog.R
import com.example.lifelog.database.Dao.Doviz.DovizDao
import com.example.lifelog.database.Database
import com.example.lifelog.database.Dao.Doviz.DovizDB
import com.example.lifelog.databinding.ActivitySellPageBinding
import com.example.lifelog.duzenleme.duzenleme.Companion.formatNumber2
import com.example.lifelog.duzenleme.duzenleme.Companion.formatNumber4
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONObject

class SellPageActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySellPageBinding
    var gelenfiyat: String?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sell_page)
        binding= ActivitySellPageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val gelenDoviz=intent.getSerializableExtra("Doviz") as DovizDB


        binding.SellGelenDovizLong.text=gelenDoviz.DovizLongName
        binding.SellGelenDovizshort.text=gelenDoviz.DovizShortName
        binding.GuncelSahiplik.text=gelenDoviz.DovizMiktari

        CurrencyUtil.fetchCurrencyRate(gelenDoviz.DovizShortName,"TRY") { price->
            if(price!=null){
                gelenfiyat=formatNumber4(price)
                binding.SellGuncelFiyat.text=gelenfiyat
            }
        }

        binding.SellAmountOfMoney.addTextChangedListener(object : TextWatcher{
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
                        binding.GuncelTutar.text=formatNumber2(guncel)
                    }catch (e: NullPointerException){
                        Log.e("Fiyat alınamadı","Fiyat alınamadı")

                    }
                }catch (e:NumberFormatException){
                    if(binding.SellAmountOfMoney.text.isNullOrEmpty()){
                        binding.GuncelTutar.text=""
                    }
                }
            }
        })
        val vt= Database(this)
        binding.DovizSellButton.setOnClickListener {
            val girilenAmount=binding.SellAmountOfMoney.text.toString()
            val totalAmount=binding.GuncelTutar.text.toString()
            if(girilenAmount.isEmpty()&&totalAmount.isEmpty()){
                Toast.makeText(this,"Fİyat bilgisi alınamadı",Toast.LENGTH_SHORT).show()
            }else{
                if(girilenAmount.toDouble()>gelenDoviz.DovizMiktari.toDouble()){
                    Toast.makeText(this,"Yetersiz Bakiye",Toast.LENGTH_SHORT).show()
                }else{
                    val doviz= DovizDB(gelenDoviz.DovizLongName,gelenDoviz.DovizShortName,girilenAmount,totalAmount)
                    DovizDao().sellAsset(vt,doviz)
                    Toast.makeText(this,"Satıldı", Toast.LENGTH_SHORT).show()
                    val gecis= Intent(this@SellPageActivity, DovizActivity::class.java)
                    startActivity(gecis)
                    finish()
                }
            }
        }

        binding.backbutton.setOnClickListener {
            finish()
        }
    }
}




object CurrencyUtil {
    fun fetchCurrencyRate(from: String, to: String, callback: (Double?) -> Unit) {
        CoroutineScope(Dispatchers.Main).launch {
            val rate = getCurrencyRate(from, to)
            callback(rate)
        }
    }

    suspend fun getCurrencyRate(from: String, to: String): Double? {
        val apiUrl = "https://api.freecurrencyapi.com/v1/latest?apikey=${Keys().getDovizKey()}&base_currency=$from&currencies=$to"
        val client = OkHttpClient()

        val request = Request.Builder().url(apiUrl).build()

        return withContext(Dispatchers.IO) {
            try {
                val response = client.newCall(request).execute()
                if (response.isSuccessful) {
                    val body = response.body?.string()
                    val json = JSONObject(body)
                    json.getJSONObject("data").getDouble(to)
                } else {
                    Log.e("API", "Error: ${response.code}")
                    null
                }
            } catch (e: Exception) {
                Log.e("API", "Exception: ${e.message}")
                null
            }
        }
    }
}






