package com.example.lifelog.pages

import android.database.sqlite.SQLiteConstraintException
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.example.lifelog.AddPages.AddPagesRecView
import com.example.lifelog.R
import com.example.lifelog.database.AddPagesDao
import com.example.lifelog.database.Database
import com.example.lifelog.database.Plugins
import com.example.lifelog.databinding.FragmentAddPagesBinding

class AddPagesFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding= FragmentAddPagesBinding.inflate(inflater,container,false)


        //Seçilen Eklenti Veritabaına ekleniyor
        binding.kriptotakipet.setOnClickListener {
            var kriptoText=binding.kriptotext.text
            PluginAdd(kriptoText.toString())
        }

        binding.altintakipet.setOnClickListener {
            var AltinTakip=binding.altintext.text
            PluginAdd(AltinTakip.toString())
        }

        binding.doviztakipet.setOnClickListener {
            var DovizTakip=binding.doviztext.text
            PluginAdd(DovizTakip.toString())
        }

        binding.futboltakipet.setOnClickListener {
            var FutbolTakip=binding.fitnessText.text
            PluginAdd(FutbolTakip.toString())
        }

        binding.derstakipet.setOnClickListener {
            var DersTakip=binding.derstakiptext.text
            PluginAdd(DersTakip.toString())
        }

        binding.kaloritakipet.setOnClickListener {
            var KaloriTakip=binding.kaloritext.text
            PluginAdd(KaloriTakip.toString())
        }


        return binding.root
    }
    fun PluginAdd(PluginName: String){
        val vt = Database(requireContext())//Nesne oluşturuldu.
        val alert= AlertDialog.Builder(requireContext())//Uyarı çıkması için
        alert.setTitle("Emin misiniz?")
        alert.setMessage("$PluginName için Ana Ekrana eklenecek Emin misiniz?")
        alert.setPositiveButton("EVET") {dialog,which->
            try {
                AddPagesDao().PluginAdd(vt,PluginName)//Seçilen ek veritabanına kayıt ediliyor
                Toast.makeText(requireContext(),"Eklendi", Toast.LENGTH_LONG).show()
            }catch (e:SQLiteConstraintException){//Eğer zaten var ise eklenmiyecek
                Toast.makeText(requireContext(),"Bu eklenti zaten mevcut", Toast.LENGTH_LONG).show()
            }
        }
        alert.setNegativeButton("HAYIR"){ dialog, which ->//Hayır durumunda birşey yapılmıyor.
        }
        val dialog=alert.create()
        dialog.show()

    }
}