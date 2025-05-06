package com.example.lifelog.GoldPages

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.widget.EditText
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.lifelog.R
import com.example.lifelog.database.Database
import com.example.lifelog.databinding.ActivityAltinBinding
import com.google.android.material.floatingactionbutton.FloatingActionButton
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class AltinActivity : AppCompatActivity(), AltinAdapter.onAltinMiktariDegistiListener {

    val BASE_URL_GOLD = "https://api.metalpriceapi.com/v1/"

    private lateinit var binding: ActivityAltinBinding
    private lateinit var fab: FloatingActionButton
    lateinit var altinAdapter: AltinAdapter
    private var gramGoldPrice: Double = 0.0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityAltinBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val vt = Database(this@AltinActivity)

        fab = findViewById(R.id.fabAltinEkle)

        val ilkListe = Goldsdao().fetchAllGold(vt)
        Log.e("AltinActivity", "Açılışta çekilen altınlar: $ilkListe")

        //recyclerView ile adapter sınıfının bağlanması
        altinAdapter = AltinAdapter(this@AltinActivity, ilkListe, gramGoldPrice, this)
        binding.altinRecyclerView.layoutManager = LinearLayoutManager(this@AltinActivity)
        binding.altinRecyclerView.adapter = altinAdapter


        //fab butonuna basılması durumunda altın ekleme diyaloğunun açılması
        fab.setOnClickListener{
            //altın ekleme diyaloğunun tasarımı
            val dialogView = LayoutInflater.from(this@AltinActivity).inflate(R.layout.dialog_altin_ekle, null)

            //edittext, radiogrup erişimi
            val altinMiktarGirisiEditText = dialogView.findViewById<EditText>(R.id.editTextAltınMiktarGirisi)
            val radioGrup = dialogView.findViewById<RadioGroup>(R.id.altinRadioGrup)
            //alertdialogun başlık görünüm gibi özelliklerinin ayarlanması
            val builder = AlertDialog.Builder(this@AltinActivity)
            builder.setTitle("Altın Ekle")
                .setView(dialogView)

            builder.setPositiveButton("Ekle"){_, _ ->

                val altinMiktarText = altinMiktarGirisiEditText.text.toString().trim()
                val secilenRadioButtonId = radioGrup.checkedRadioButtonId

                //altın miktarı girilmemesi durumunun kontrolü ve kullanıcıya mesaj verilmesi
                if (altinMiktarText.isEmpty()) {
                    Toast.makeText(this, "Lütfen miktar giriniz.", Toast.LENGTH_SHORT).show()
                    return@setPositiveButton
                }

                val altinMiktar = altinMiktarText.toIntOrNull()
                if (altinMiktar == null || altinMiktar <= 0) {
                    Toast.makeText(this, "Geçerli bir miktar giriniz.", Toast.LENGTH_SHORT).show()
                    return@setPositiveButton
                }

                //altın türünün seçilmemesi durumunun kontrol edilmesi ve kullanıcıya mesaj verilmesi
                if (secilenRadioButtonId == -1) {
                    Toast.makeText(this, "Lütfen altın türü seçiniz.", Toast.LENGTH_SHORT).show()
                    return@setPositiveButton
                }



                //seçilen radiobuttondan altın türünün alınması
                val secilenRadioButton = dialogView.findViewById<RadioButton>(secilenRadioButtonId)
                val altinTuru = secilenRadioButton.text.toString()

                val mevcutAltin = Goldsdao().getGoldByType(vt, altinTuru)

                //Girilen altın türü kontrol edilir, eğer ki mevcutsa adet güncellenir ve yeni card oluşturulmamış olur. Mevcut olmayan altın etklenirse card oluşturulur.
                if (mevcutAltin != null) {
                    val yeniMiktar = mevcutAltin.goldAmount + altinMiktar
                    Goldsdao().updateGoldAmount(vt, mevcutAltin.goldId, yeniMiktar)
                    Toast.makeText(this, "$altinTuru altın güncellendi. Eklenen Adet: $altinMiktar", Toast.LENGTH_SHORT).show()
                } else {
                    Goldsdao().addGold(vt, altinTuru, altinMiktar)
                    Toast.makeText(this, "$altinTuru atlın eklendi. Eklenen Adet: $altinMiktar", Toast.LENGTH_SHORT).show()
                }
                onResume()
            }
                .setNegativeButton("İptal", null)
                .create()

                .show()
        }
        loadGoldData()

    }
    //altın ekleme, eksiltme durumlarını anında güncellemek için onResume metodu
    override fun onResume() {
        super.onResume()

        val guncelListe = Goldsdao().fetchAllGold(Database(this))
        altinAdapter.updateList(guncelListe)

        val toplamAltinTutari = altinAdapter.toplamAltinTutariHesaplama()
        binding.textViewTumAtlinVarlik.text = "Toplam Altın Varlığınız: %.2f TL".format(toplamAltinTutari)

    }

    override fun onAltinMiktariDegisti(yeniToplam: Double) {
        binding.textViewTumAtlinVarlik.text = "Toplam Altın Varlığınız: %.2f TL".format(yeniToplam)
    }


    //Api ile çekilen verilen activity içerisine yüklenmesini sağlayacak metod
    private fun loadGoldData(){
        //retrofit nesnesinin oluşturulması
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL_GOLD)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        //servis oluşturulması
        val service = retrofit.create(GoldAPI::class.java)
        val call = service.getGoldPrice()

        call.enqueue(object: Callback<GoldModel>{

            override fun onResponse(p0: Call<GoldModel>, response: Response<GoldModel>) {
                //apiden gelen cevabı başarılı şekilde alabilirsek bu kodlama çalışır.
                if (response.isSuccessful){
                    response.body()?.let { goldModel ->
                        val onsPrice = goldModel.rates.tryXau
                        gramGoldPrice = onsPrice / 31.1035

                        Log.e("GoldAPI", "Gram Altın Fiyatı: $gramGoldPrice")

                        val guncelListe = Goldsdao().fetchAllGold(Database(this@AltinActivity))

                        altinAdapter = AltinAdapter(this@AltinActivity, guncelListe, gramGoldPrice, this@AltinActivity)
                        binding.altinRecyclerView.layoutManager = LinearLayoutManager(this@AltinActivity)
                        binding.altinRecyclerView.adapter = altinAdapter
                        //toplam altın tutarı yüklenir.
                        val toplamAltinTutari = altinAdapter.toplamAltinTutariHesaplama()
                        binding.textViewTumAtlinVarlik.text = "Toplam Altın Varlığınız: %.2f TL".format(toplamAltinTutari)
                    }
                }
                else{
                    Log.e("Altin API", "Cevap Basarisiz: ${response.code()}")
                }
            }

            override fun onFailure(p0: Call<GoldModel>, p1: Throwable) {
                Log.e("Altin API: ", "Hata: ${p1.message}")
            }

        })

    }



}