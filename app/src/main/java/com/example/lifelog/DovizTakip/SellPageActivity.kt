package com.example.lifelog.DovizTakip

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.lifelog.ApiKeys.Keys
import com.example.lifelog.ApiKeys.Keys.Companion.dovizApiKeys2
import com.example.lifelog.PluginPages.DovizActivity
import com.example.lifelog.R
import com.example.lifelog.database.AssetsDao.Doviz.DovizDao
import com.example.lifelog.database.Database
import com.example.lifelog.database.AssetsDao.Doviz.DovizDB
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
    private lateinit var viewModel: Livedata
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sell_page)
        binding= ActivitySellPageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val gelenDoviz=intent.getSerializableExtra("Doviz") as DovizDB //Ana sayfadan gelen veri alındı

        //Alınan veriler, TextView'lere yerleştirildi
        binding.SellGelenDovizLong.text=gelenDoviz.DovizLongName
        binding.SellGelenDovizshort.text=gelenDoviz.DovizShortName
        binding.GuncelSahiplik.text=gelenDoviz.DovizMiktari

        viewModel = ViewModelProvider(this).get(Livedata::class.java)
        viewModel.startFetching(gelenDoviz.DovizShortName)
        viewModel.price.observe(this) { price ->
            if(price!=null){
                binding.progressBar.visibility=View.GONE
                gelenfiyat=formatNumber4(price)
                binding.SellGuncelFiyat.text=gelenfiyat
                Log.e("Fiyat","Güncellendi")
            }else{
                binding.progressBar.visibility=View.VISIBLE
            }
        }

        //EditTexte yazılan verileri anlık olarak alma kısmı
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
        val vt= Database(this)//Veritabanı bağlantısı yapıldı.


        binding.DovizSellButton.setOnClickListener {
            //Girilen değerler alındı
            val girilenAmount=binding.SellAmountOfMoney.text.toString()
            val totalAmount=binding.GuncelTutar.text.toString()
            //Eğer fiyat bilgisi alınmamışsa işlem yapılmayacak
            if(girilenAmount.isEmpty()&&totalAmount.isEmpty()){
                Toast.makeText(this,"Fİyat bilgisi alınamadı",Toast.LENGTH_SHORT).show()
            }else{
                //Eğer girilen veri sahip olunan miktardan fazla ise işlem yapılmaaycak
                if(girilenAmount.toDouble()>gelenDoviz.DovizMiktari.toDouble()){
                    Toast.makeText(this,"Yetersiz Bakiye",Toast.LENGTH_SHORT).show()
                }else{
                    //Eğer herşey doğru ise satış işlemi tamamlanıyor.
                    val doviz= DovizDB(gelenDoviz.DovizLongName,gelenDoviz.DovizShortName,girilenAmount,totalAmount)
                    DovizDao().sellAsset(vt,doviz)
                    Toast.makeText(this,"Satıldı", Toast.LENGTH_SHORT).show()
                    //Satış sayfasından döviz sayfasına geçiş yapılıyor.
                    val gecis= Intent(this@SellPageActivity, DovizActivity::class.java)
                    startActivity(gecis)
                    finish()
                }
            }
        }
        //Geri butonu
        binding.backbutton.setOnClickListener {
            finish()
        }
    }
    override fun onDestroy() {
        super.onDestroy()
        viewModel.stopFetching()
    }
}






