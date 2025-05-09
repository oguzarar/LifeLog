package com.example.lifelog.database

import android.content.ContentValues
import android.util.Log
import com.example.lifelog.KaloriTakip.Kalori
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale


class KaloriDao {
    fun Addyemek(vt: Database, kalori: Kalori){
        val db=vt.writableDatabase
        val content= ContentValues()
        content.put("yemek_ismi",kalori.isim)
        content.put("yemek_turu",kalori.kategori)
        content.put("yemek_kalori",kalori.kalori)
        content.put("yemek_protein",kalori.protein)
        db.insertOrThrow("Kalori",null,content)
        db.close()
    }
    fun getAllYemek(vt: Database): ArrayList<Kalori>{
        val gelenYemek= ArrayList<Kalori>()
        val db=vt.writableDatabase
        val cursor = db.rawQuery("SELECT * FROM Kalori", null)
        while (cursor.moveToNext()) {
            val yemek = Kalori(
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
    fun kaloriSifirlama(vt: Database) {
        val db = vt.writableDatabase
        val content = ContentValues()
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.DAY_OF_YEAR, -1) // 1 gün geri git
        val formatter = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
        val formattedDate = formatter.format(calendar.time)

        val gecmisCursor = db.rawQuery("SELECT * FROM GecmisKalori ORDER BY KaloriTarih DESC LIMIT 1", null)
        var kaloriTarih: String? = null

        if (gecmisCursor == null || !gecmisCursor.moveToFirst()) {
            // Yeni veriyi GecmisKalori tablosuna ekle
            content.put("KaloriTarih", formattedDate)
            content.put("kalori", "0")
            content.put("protein", "0")
            db.insertOrThrow("GecmisKalori", null, content)

            // Kalori tablosunu temizle
            db.execSQL("DELETE FROM Kalori")
            Log.e("1","if bloğu çalıştı")
        } else {
            // Geçmiş veriyi al
            kaloriTarih = gecmisCursor.getString(gecmisCursor.getColumnIndexOrThrow("KaloriTarih"))

            // Eğer bugün tarih, geçmiş tarihten daha büyükse verileri sıfırla
            if (formattedDate != kaloriTarih) {
                var toplamKalori = 0.0
                var toplamProtein = 0.0
                val kaloriCursor = db.rawQuery("SELECT yemek_kalori, yemek_protein FROM Kalori", null)

                while (kaloriCursor.moveToNext()) {
                    val amountKalori = kaloriCursor.getString(kaloriCursor.getColumnIndex("yemek_kalori"))
                    val amountProtein = kaloriCursor.getString(kaloriCursor.getColumnIndex("yemek_protein"))
                    toplamKalori += amountKalori.toDouble()
                    toplamProtein += amountProtein.toDouble()
                }
                kaloriCursor.close()

                // Yeni veriyi GecmisKalori tablosuna ekle
                content.put("KaloriTarih", formattedDate)
                content.put("kalori", toplamKalori)
                content.put("protein", toplamProtein)
                db.insertOrThrow("GecmisKalori", null, content)

                // Kalori tablosunu temizle
                db.execSQL("DELETE FROM Kalori")
                Log.e("2","Else bloğu çalıştı")
            }
        }

        gecmisCursor?.close()
        db.close()
    }

    fun GetHistory(vt: Database): ArrayList<KaloriDB> {
        val db = vt.writableDatabase
        val gelenYemek = ArrayList<KaloriDB>()
        val cursor = db.rawQuery("SELECT * FROM GecmisKalori", null)

        // İlk satırı atla
        if (cursor.moveToFirst()) {
            // İlk satırı atlamak için hiçbir şey yapma
        }

        // Geri kalan satırları al
        while (cursor.moveToNext()) {
            val yemek = KaloriDB(
                cursor.getString(cursor.getColumnIndex("kalori")),
                cursor.getString(cursor.getColumnIndex("protein")),
                cursor.getString(cursor.getColumnIndex("KaloriTarih"))
            )
            gelenYemek.add(yemek)
        }

        cursor.close()
        db.close()
        return gelenYemek
    }
}