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
import com.example.lifelog.databinding.FragmentSettingsBinding

class SettingsFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding= FragmentSettingsBinding.inflate(inflater,container,false)

        val tablolar = listOf(
            "Notes",
            "ToDoList",
            "Plugins",
            "Golds",
            "CryptoDB",
            "DersTakip",
            "Kalori",
            "Doviz",
            "AktiviteTakip",
            "GecmisKalori"
        )

        val vt = Database(requireContext())
        binding.button2.setOnClickListener {
            val alert= AlertDialog.Builder(requireContext())
            alert.setTitle("Emin misiniz?")
            alert.setMessage("Tüm verileri silinecek. Emin misiniz?")
            alert.setPositiveButton("EVET") {dialog,which->
                val db=vt.writableDatabase
                for(i in tablolar){
                    db.execSQL("DELETE FROM $i")
                }
                db.close()
                Toast.makeText(requireContext(),"Tüm veriler silindi", Toast.LENGTH_SHORT).show()

            }
            alert.setNegativeButton("HAYIR"){ dialog, which ->
            }
            val dialog=alert.create()
            dialog.show()
        }


        return binding.root
    }
}