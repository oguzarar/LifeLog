package com.example.lifelog.DovizTakip

import android.database.sqlite.SQLiteConstraintException
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
import com.example.lifelog.KriptoPages.LiveData
import com.example.lifelog.R
import com.example.lifelog.database.AssetsDao.Doviz.DovizDao
import com.example.lifelog.database.Database
import com.example.lifelog.database.AssetsDao.Doviz.Doviz
import com.example.lifelog.database.AssetsDao.Doviz.DovizDB
import com.example.lifelog.databinding.ActivitySatinAlimPageBinding
import com.example.lifelog.duzenleme.duzenleme.Companion.formatNumber2
import com.example.lifelog.duzenleme.duzenleme.Companion.formatNumber4
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
    private lateinit var viewModel: Livedata
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_satin_alim_page)
        binding= ActivitySatinAlimPageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val vt= Database(this)//Veritabanı bağlantısı yapıldı

        val gelen=intent.getSerializableExtra("Doviz") as Doviz//Ana sayfadan gelen veri alındı



        viewModel = ViewModelProvider(this).get(Livedata::class.java)
        viewModel.startFetching(gelen.Dovizshort)
        viewModel.price.observe(this) { price ->
            if (price != null) {
                binding.progressBar.visibility= View.GONE
                gelenfiyat=formatNumber4(price)
                binding.GuncelFiyat.text=gelenfiyat
                Log.e("Fiyat bilgisi","GÜncellendi")
            }else{
                binding.progressBar.visibility= View.VISIBLE
            }

        }
        //Alınan veriler, TextView'lere yerleştirildi
        binding.GelenDovizLong.text=gelen.DovizName
        binding.GelenDovizshort.text=gelen.Dovizshort

        //EditTexte yazılan verileri anlık olarak alma kısmı
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
                        binding.GuncelTutar.text=formatNumber2(guncel)
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
            //Girilen değerler alındı
            val DovizAmount=binding.AmountOfMoney.text.toString()
            val DovizTRYTutar=binding.GuncelTutar.text.toString()
            //Eğer fiyat bilgisi alınmamışsa işlem yapılmayacak
            if(DovizAmount.isEmpty()&&DovizTRYTutar.isEmpty()){
                Toast.makeText(this,"Fiyat Bilgisi Alınamadı",Toast.LENGTH_SHORT).show()
            }else{
                //Verileri veritabanına göndermek için DovizDB'den nesne oluşturuldu
                val doviz= DovizDB(gelen.DovizName,gelen.Dovizshort,binding.AmountOfMoney.text.toString(),binding.GuncelTutar.text.toString())
                try {//eğer alınan döviz veritabanında yoksa ekliyor
                    DovizDao().addAsset(vt,doviz)
                }catch (e: SQLiteConstraintException){//eğer varsa toplam miktara ekleniyor
                    DovizDao().updateAsset(vt,doviz)
                }
                binding.AmountOfMoney.text.clear()
                binding.GuncelTutar.text=""
                Toast.makeText(this,"Eklendi",Toast.LENGTH_SHORT).show()
            }
        }
        //Geri tuşuş
        binding.backbutton.setOnClickListener {
            finish()
        }
    }
    override fun onDestroy() {
        super.onDestroy()
        viewModel.stopFetching() // Activity kapandığında stopFetching çağırarak işlem sonlandırıldı
    }
}

