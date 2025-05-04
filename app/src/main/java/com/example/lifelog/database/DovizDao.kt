package com.example.lifelog.database

import android.content.ContentValues

class DovizDao {
    fun AddDoviz(vt: Database,DovizLongName: String,DovizShortName: String,DovizAmount: String,DovizMiktariTRY: String){
        val db=vt.writableDatabase
        val content= ContentValues()
        content.put("DovizLongName",DovizLongName)
        content.put("DovizShortName",DovizShortName)
        content.put("DovizAmount",DovizAmount)
        content.put("DovizMiktariTRY",DovizMiktariTRY)
        db.insertOrThrow("Doviz",null,content)
        db.close()
    }

    fun GetAllDoviz(vt: Database): ArrayList<DovizDB>{
        val gelenDoviz= ArrayList<DovizDB>()
        val db=vt.writableDatabase
        val cursor=db.rawQuery("SELECT * FROM Doviz",null)
        while(cursor.moveToNext()){
            val Doviz= DovizDB(cursor.getString(cursor.getColumnIndex("DovizLongName")),
                               cursor.getString(cursor.getColumnIndex("DovizShortName")),
                               cursor.getString(cursor.getColumnIndex("DovizAmount")),
                               cursor.getString(cursor.getColumnIndex("DovizMiktariTRY")))
            gelenDoviz.add(Doviz)
        }
        db.close()
        return gelenDoviz
    }

    fun GetTotalDovizAmount(vt: Database): Double{
        val db = vt.writableDatabase
        var toplam = 0.0
        val cursor = db.rawQuery("SELECT DovizMiktariTRY FROM Doviz", null)
        while (cursor.moveToNext()) {
            val amount = cursor.getDouble(cursor.getColumnIndex("DovizMiktariTRY"))
            toplam += amount
        }
        cursor.close()
        db.close()
        return toplam

    }
    fun SellCryptoUSDT(vt: Database, dovizLongName: String, cikanMiktar: Double, cikanTRY: Double) {
        val db = vt.writableDatabase
        val cursor = db.rawQuery("SELECT DovizAmount, DovizMiktariTRY FROM Doviz WHERE DovizLongName = ?", arrayOf(dovizLongName))

        if (cursor.moveToFirst()) {
            val mevcutTutar = cursor.getString(cursor.getColumnIndex("DovizAmount")).toDouble()
            val mevcutTRY = cursor.getString(cursor.getColumnIndex("DovizMiktariTRY")).toDouble()
            val yeniTutar = mevcutTutar - cikanMiktar
            val yeniTry = mevcutTRY - cikanTRY

            if (yeniTutar == 0.0 && yeniTry == 0.0) {
                // Eğer tamamen sattıysan kaydı sil
                db.delete("Doviz", "DovizLongName = ?", arrayOf(dovizLongName))
            } else {
                // Eğer bakiye kalıyorsa güncelle
                val contentValues = ContentValues().apply {
                    put("DovizAmount", yeniTutar.toString())
                    put("DovizMiktariTRY", yeniTry.toString())
                }
                db.update("Doviz", contentValues, "DovizLongName = ?", arrayOf(dovizLongName))
            }
        }
        cursor.close()
        db.close()
    }
    fun UpadteCryptoUSDT(vt: Database, dovizLongName: String, alinanMiktar: Double, alinanTRY: Double) {
        val db = vt.writableDatabase
        val cursor = db.rawQuery("SELECT DovizAmount, DovizMiktariTRY FROM Doviz WHERE DovizLongName = ?", arrayOf(dovizLongName))

        if (cursor.moveToFirst()) {
            val mevcutTutar = cursor.getString(cursor.getColumnIndex("DovizAmount")).toDouble()
            val mevcutTRY = cursor.getString(cursor.getColumnIndex("DovizMiktariTRY")).toDouble()

            val yeniTutar = mevcutTutar + alinanMiktar
            val yeniTRY = mevcutTRY + alinanTRY

            val contentValues = ContentValues().apply {
                put("DovizAmount", yeniTutar.toString())
                put("DovizMiktariTRY", yeniTRY.toString())
            }

            db.update("Doviz", contentValues, "DovizLongName = ?", arrayOf(dovizLongName))
        }

        cursor.close()
        db.close()
    }


}