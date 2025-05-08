package com.example.lifelog.KaloriTakip

import android.os.Bundle
import android.util.Log
import android.widget.SearchView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.lifelog.R
import com.example.lifelog.databinding.ActivityYemekListeleBinding
import com.example.room.veritabani
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class YemekListeleActivity : AppCompatActivity() {
    private lateinit var binding: ActivityYemekListeleBinding
    private lateinit var dao: Kaloridao
    private lateinit var vt:veritabani
    private lateinit var adapter: ListeleRecview1
    private lateinit var yemekList: ArrayList<Kalori>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_yemek_listele)
        binding= ActivityYemekListeleBinding.inflate(layoutInflater)
        setContentView(binding.root)
        vt= veritabani.veritabaniErisim(this)!! //Veritabanına erişmek için gerekli. Sondaki !! işareti değerin null olup olmadığını kontrol etmeden o değeri al
        dao=vt.getkisilerDao()
        yemekList= ArrayList<Kalori>()

        val job= CoroutineScope(Dispatchers.Main).launch {//Bu kod kisilerdao sınıfında suspend olan tumKisiler fonskiyonun çalışmasını sağlar
           val yemek1 =dao.tumKisiler()
            for(i in yemek1){
                var yeni= Kalori(i.isim,i.kategori,i.kalori,i.protein)
                yemekList.add(yeni)
            }
            binding.KaloriRecview.setHasFixedSize(true)
            binding.KaloriRecview.layoutManager= LinearLayoutManager(this@YemekListeleActivity)
            adapter= ListeleRecview1(this@YemekListeleActivity,yemekList)
            binding.KaloriRecview.adapter=adapter
        }

        binding.searchView2.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                adapter.filter?.filter(newText)
                return true
            }
        })

        binding.backbutton.setOnClickListener {
            finish()
        }






    }

}


