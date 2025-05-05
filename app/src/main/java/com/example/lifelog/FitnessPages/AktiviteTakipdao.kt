package com.example.lifelog.FitnessPages

import android.content.ContentValues
import com.example.lifelog.database.Database
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class AktiviteTakipdao {

    fun aktiviteKaydet(vt: Database, aktiviteAdi: String, harcananKalori: Double, yapilanSure: String){

        val db = vt.writableDatabase
        val values = ContentValues()

        val formatter = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())
        val yapilanTarihSaat = formatter.format(Date())

        values.put("aktiviteAdi", aktiviteAdi)
        values.put("harcananKalori", harcananKalori)
        values.put("aktiviteSuresi", yapilanSure)
        values.put("aktiviteTarihi", yapilanTarihSaat)


        try {
            db.insertOrThrow("AktiviteTakip", null, values)
        }catch (e: Exception){
            e.printStackTrace()
        }

        db.close()

    }

    fun tumAktiviteleriGetir(vt: Database) : MutableList<AktiviteModel>{

        val db = vt.writableDatabase
        val aktiviteListesi = mutableListOf<AktiviteModel>()
        val cursor = db.rawQuery("SELECT * FROM AktiviteTakip ORDER BY aktiviteId DESC", null)

        while (cursor.moveToNext()){
            val aktivite = AktiviteModel(cursor.getInt(cursor.getColumnIndexOrThrow("aktiviteId"))
                ,cursor.getString(cursor.getColumnIndexOrThrow("aktiviteAdi"))
                ,cursor.getDouble(cursor.getColumnIndexOrThrow("harcananKalori"))
                ,cursor.getString(cursor.getColumnIndexOrThrow("aktiviteSuresi"))
                ,cursor.getString(cursor.getColumnIndexOrThrow("aktiviteTarihi")))

            aktiviteListesi.add(aktivite)
        }

        cursor.close()
        db.close()

        return aktiviteListesi
    }

    fun gecmisAktiviteSil(vt: Database, aktiviteId: Int){

        val db = vt.writableDatabase

        db.delete("AktiviteTakip", "aktiviteId=?", arrayOf(aktiviteId.toString()))
        db.close()

    }

}