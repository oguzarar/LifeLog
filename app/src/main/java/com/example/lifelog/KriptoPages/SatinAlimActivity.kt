package com.example.lifelog.KriptoPages

import android.database.sqlite.SQLiteConstraintException
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.lifelog.R
import com.example.lifelog.database.AssetsDao.Crypto.Crypto
import com.example.lifelog.database.AssetsDao.Crypto.CryptoDB
import com.example.lifelog.database.AssetsDao.Crypto.CryptoDao
import com.example.lifelog.database.Database
import com.example.lifelog.databinding.ActivitySatinAlimBinding
import com.example.lifelog.duzenleme.duzenleme.Companion.formatNumber

class SatinAlimActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySatinAlimBinding
    private lateinit var viewModel: LiveData
    var gelenfiyat: String?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_satin_alim)
        binding= ActivitySatinAlimBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val vt= Database(this)
        val gelenCrypto=intent.getSerializableExtra("Crypto") as Crypto//Diğer sayfadan gelen veri

        binding.GelenCoinLong.text=gelenCrypto.CryptoName
        binding.GelenCoinshort.text=gelenCrypto.Cryptoshort



        viewModel = ViewModelProvider(this).get(LiveData::class.java)
        viewModel.startFetching(gelenCrypto.Cryptoshort)

        viewModel.price.observe(this) { price ->
            // UI'yi güncelle
            if(price!=null){
                binding.progressBar.visibility= View.GONE
                gelenfiyat=price.toString()
                binding.GuncelFiyat.text=price.toString()
            }else{
                binding.progressBar.visibility= View.VISIBLE
            }

            Log.e("Fiyat bilgisi","Güncellendi")
        }


        binding.AmountOfCoin.addTextChangedListener(object: TextWatcher{//Edittexte yazılan veriyi anlık olarak alma
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
                        binding.GuncelTutar.text=formatNumber(guncel)
                        binding.GuncelAdet.text=p0
                        binding.AmountOfUsdt.text.clear()
                    }catch (e: NullPointerException){
                        Log.e("Fiyat alınamadı","Fiyat alınamadı")

                    }

                }catch (e:NumberFormatException){
                    if(binding.AmountOfUsdt.text.isNullOrEmpty()&&binding.AmountOfCoin.text.isNullOrEmpty()){
                        binding.GuncelAdet.text=""
                        binding.GuncelTutar.text=""
                    }

                }
            }
        })
        binding.AmountOfUsdt.addTextChangedListener(object : TextWatcher{
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
                    val yazilan=p0.toString()
                    val gelenFiyat= gelenfiyat!!.toDouble()
                    val Toplam=yazilan.toDouble()/gelenFiyat
                    val son="%.2f".format(Toplam)
                    binding.GuncelAdet.text=son
                    binding.GuncelTutar.text=p0
                    binding.AmountOfCoin.text.clear()
                }catch (e:NumberFormatException){
                    if(binding.AmountOfUsdt.text.isEmpty()&&binding.AmountOfCoin.text.isEmpty()){
                        binding.GuncelAdet.text=""
                        binding.GuncelTutar.text=""
                    }

                }
            }
        })
        binding.KriptoBuyButton.setOnClickListener {
            val adet=binding.GuncelAdet.text.toString()
            val tutar=binding.GuncelTutar.text.toString()
            val crypto= CryptoDB(gelenCrypto.CryptoName,gelenCrypto.Cryptoshort,adet,tutar)
            try {//Eğer eklenen kripto yoksa vt'ye eklenecek
                if(adet.isEmpty()||tutar.isEmpty()){
                    Toast.makeText(this,"Fiyat Bilgisi Alınamadı", Toast.LENGTH_SHORT).show()
                }else{
                    CryptoDao().addAsset(vt,crypto)
                    Toast.makeText(this,"Eklendi", Toast.LENGTH_SHORT).show()
                    binding.AmountOfCoin.text.clear()
                    binding.AmountOfUsdt.text.clear()
                }

            }catch (e:SQLiteConstraintException){//Eğer zaten varsa güncellenecek
                if(adet.isEmpty()||tutar.isEmpty()){
                    Toast.makeText(this,"Fiyat Bilgisi Alınamadı", Toast.LENGTH_SHORT).show()
                }else{
                    CryptoDao().updateAsset(vt,crypto)
                    Toast.makeText(this,"Eklendi", Toast.LENGTH_SHORT).show()
                    binding.AmountOfCoin.text.clear()
                    binding.AmountOfUsdt.text.clear()
                }
            }
        }
        binding.backbutton.setOnClickListener {
            finish()
        }


    }
    override fun onDestroy() {
        super.onDestroy()
        viewModel.stopFetching() // Activity kapandığında stopFetching çağırarak işlemi sonlandırabiliriz
    }

}



