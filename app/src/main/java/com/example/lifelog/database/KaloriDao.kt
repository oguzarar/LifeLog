package com.example.lifelog.database

import android.content.ContentValues
import com.example.lifelog.KaloriTakip.Kalori

class KaloriDao {
    fun Addyemek(vt: Database, Yemek_ismi: String, Yemek_Turu: String, Yemek_kalori: String, Yemek_Protein: String){
        val db=vt.writableDatabase
        val content= ContentValues()
        content.put("yemek_ismi",Yemek_ismi)
        content.put("yemek_turu",Yemek_Turu)
        content.put("yemek_kalori",Yemek_kalori)
        content.put("yemek_protein",Yemek_Protein)
        db.insertOrThrow("Kalori",null,content)
        db.close()
    }
    fun getAllYemek(vt: Database): ArrayList<Kalori>{
        val gelenYemek= ArrayList<Kalori>()
        val db=vt.writableDatabase
        val cursor = db.rawQuery("SELECT * FROM Kalori", null)
        while (cursor.moveToNext()) {
            val yemek = Kalori(
                cursor.getInt(cursor.getColumnIndex("yemek_id")),
                cursor.getString(cursor.getColumnIndex("yemek_ismi")),
                cursor.getString(cursor.getColumnIndex("yemek_turu")),
                cursor.getString(cursor.getColumnIndex("yemek_kalori")),
                cursor.getString(cursor.getColumnIndex("yemek_protein")))
            gelenYemek.add(yemek)
        }
        db.close()
        return gelenYemek
    }
    fun GetTotalcalorie(vt: Database): Pair<Double, Double> {
        val db = vt.writableDatabase
        var toplamKalori=0.0
        var toplamProtein=0.0
        val cursor = db.rawQuery("SELECT yemek_kalori,yemek_protein FROM Kalori", null)
        while (cursor.moveToNext()) {
            var amountKalori = cursor.getString(cursor.getColumnIndex("yemek_kalori"))
            var amountProtein=cursor.getString(cursor.getColumnIndex("yemek_protein"))
            toplamKalori += amountKalori.toDouble()
            toplamProtein+=amountProtein.toDouble()
        }

        cursor.close()
        db.close()
        return Pair(toplamKalori,toplamProtein)
    }
    fun UpadteKalori(vt: Database, Yemek_ismi: String, Yemek_kalori: Double, Yemek_Protein: Double) {
        val db = vt.writableDatabase
        val cursor = db.rawQuery("SELECT yemek_kalori, yemek_protein FROM Kalori WHERE yemek_ismi = ?", arrayOf(Yemek_ismi))

        if (cursor.moveToFirst()) {
            val mevcutKalori = cursor.getString(cursor.getColumnIndex("yemek_kalori")).toDouble()
            val mevcutProtein = cursor.getString(cursor.getColumnIndex("yemek_protein")).toDouble()

            val yeniKalori = mevcutKalori+Yemek_kalori
            val yeniProtein = mevcutProtein+Yemek_Protein

            val contentValues = ContentValues().apply {
                put("yemek_kalori", yeniKalori.toString())
                put("yemek_protein", yeniProtein.toString())
            }

            db.update("Kalori", contentValues, "yemek_ismi = ?", arrayOf(Yemek_ismi))
        }

        cursor.close()
        db.close()
    }
}