package com.example.lifelog.pages

import android.database.sqlite.SQLiteConstraintException
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.example.lifelog.R
import com.example.lifelog.database.AddPagesDao
import com.example.lifelog.database.Database
import com.example.lifelog.databinding.FragmentAddPagesBinding

class AddPagesFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding= FragmentAddPagesBinding.inflate(inflater,container,false)
        val vt = Database(requireContext())

        binding.KriptoButton.setOnClickListener {
            PluginAdd("Kripto Takip","Kripto")
        }
        binding.AltinTakipButton.setOnClickListener {
            PluginAdd("Altın Takip","Altin")
        }
        binding.DovizTakipButton.setOnClickListener {
            PluginAdd("Döviz Takip","Doviz")
        }
        binding.DersNotButton.setOnClickListener {
            PluginAdd("Ders Not Takibi","DersNot")
        }
        binding.FutbolTakipButton.setOnClickListener {
            PluginAdd("Futbol Takip","Futbol")
        }
        binding.KaloriSayaciButton.setOnClickListener {
            PluginAdd("Kalori Sayacı","Kalori")
        }
        binding.DersTakipButton.setOnClickListener {
            PluginAdd("Ders takip","DersTakip")
        }

        return binding.root
    }
    fun PluginAdd(Mesaj:String,PluginName: String){
        val vt = Database(requireContext())
        val alert= AlertDialog.Builder(requireContext())
        alert.setTitle("Emin misiniz?")
        alert.setMessage("$Mesaj için Ana Ekrana eklenecek Emin misiniz?")
        alert.setPositiveButton("EVET") {dialog,which->
            try {
                AddPagesDao().PluginAdd(vt,PluginName)
            }catch (e:SQLiteConstraintException){
                Toast.makeText(requireContext(),"Bu eklenti zaten mevcut", Toast.LENGTH_LONG).show()
            }
        }
        alert.setNegativeButton("HAYIR"){ dialog, which ->
        }
        val dialog=alert.create()
        dialog.show()

    }
}