package com.example.lifelog.database.AssetsDao.Doviz

import android.content.ContentValues
import com.example.lifelog.database.AssetsDao.DatabaseDao
import com.example.lifelog.database.Database

class DovizDao: DatabaseDao<DovizDB> {
    override fun addAsset(vt: Database, doviz: DovizDB) {
        val db=vt.writableDatabase
        val content= ContentValues()
        content.put("DovizLongName",doviz.DovizLongName)
        content.put("DovizShortName",doviz.DovizShortName)
        content.put("DovizAmount",doviz.DovizMiktari)
        content.put("DovizMiktariTRY",doviz.DovizMiktariTRY)
        db.insertOrThrow("Doviz",null,content)
        db.close()
    }

    override fun getAllAssets(vt: Database): ArrayList<DovizDB> {
        val gelenDoviz= ArrayList<DovizDB>()
        val db=vt.writableDatabase
        val cursor=db.rawQuery("SELECT * FROM Doviz",null)
        while(cursor.moveToNext()){
            val Doviz= DovizDB(cursor.getString(cursor.getColumnIndexOrThrow("DovizLongName")),
                cursor.getString(cursor.getColumnIndexOrThrow("DovizShortName")),
                cursor.getString(cursor.getColumnIndexOrThrow("DovizAmount")),
                cursor.getString(cursor.getColumnIndexOrThrow("DovizMiktariTRY")))
            gelenDoviz.add(Doviz)
        }
        db.close()
        return gelenDoviz
    }

    override fun getTotalAmount(vt: Database) : Double{
        val db = vt.writableDatabase
        var toplam = 0.0
        val cursor = db.rawQuery("SELECT DovizMiktariTRY FROM Doviz", null)
        while (cursor.moveToNext()) {
            val amount = cursor.getDouble(cursor.getColumnIndexOrThrow("DovizMiktariTRY"))
            toplam += amount
        }
        cursor.close()
        db.close()
        return toplam
    }

    override fun sellAsset(vt: Database, doviz: DovizDB) {
        val db = vt.writableDatabase
        val cursor = db.rawQuery("SELECT DovizAmount, DovizMiktariTRY FROM Doviz WHERE DovizLongName = ?", arrayOf(doviz.DovizLongName))

        if (cursor.moveToFirst()) {
            val mevcutTutar = cursor.getString(cursor.getColumnIndexOrThrow("DovizAmount")).toDouble()
            val mevcutTRY = cursor.getString(cursor.getColumnIndexOrThrow("DovizMiktariTRY")).toDouble()
            val yeniTutar = mevcutTutar - doviz.DovizMiktari.toDouble()
            val yeniTry = mevcutTRY - doviz.DovizMiktariTRY.toDouble()

            if (yeniTutar < 0.1 && yeniTry < 0.1) {
                // Eğer tamamen sattıysan kaydı sil
                db.delete("Doviz", "DovizLongName = ?", arrayOf(doviz.DovizLongName))
            } else {
                // Eğer bakiye kalıyorsa güncelle
                val contentValues = ContentValues().apply {
                    put("DovizAmount", yeniTutar.toString())
                    put("DovizMiktariTRY", yeniTry.toString())
                }
                db.update("Doviz", contentValues, "DovizLongName = ?", arrayOf(doviz.DovizLongName))
            }
        }
    }

    override fun updateAsset(vt: Database, doviz: DovizDB) {
        val db = vt.writableDatabase
        val cursor = db.rawQuery("SELECT DovizAmount, DovizMiktariTRY FROM Doviz WHERE DovizLongName = ?", arrayOf(doviz.DovizLongName))

        if (cursor.moveToFirst()) {
            val mevcutTutar = cursor.getString(cursor.getColumnIndexOrThrow("DovizAmount")).toDouble()
            val mevcutTRY = cursor.getString(cursor.getColumnIndexOrThrow("DovizMiktariTRY")).toDouble()

            val yeniTutar = mevcutTutar + doviz.DovizMiktari.toDouble()
            val yeniTRY = mevcutTRY + doviz.DovizMiktariTRY.toDouble()

            val contentValues = ContentValues().apply {
                put("DovizAmount", yeniTutar.toString())
                put("DovizMiktariTRY", yeniTRY.toString())
            }

            db.update("Doviz", contentValues, "DovizLongName = ?", arrayOf(doviz.DovizLongName))
        }

        cursor.close()
        db.close()
    }

    override fun updatePrice(vt: Database,doviz: DovizDB) {
        val db=vt.writableDatabase

        val values = ContentValues()

        values.put("DovizMiktariTRY", doviz.DovizMiktariTRY)

        db.update("Doviz", values, "DovizShortName = ?", arrayOf(doviz.DovizShortName))

        db.close()

    }

}