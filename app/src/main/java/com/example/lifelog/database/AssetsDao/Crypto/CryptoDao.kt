package com.example.lifelog.database.AssetsDao.Crypto

import android.content.ContentValues
import com.example.lifelog.database.AssetsDao.DatabaseDao
import com.example.lifelog.database.Database

class CryptoDao: DatabaseDao<CryptoDB> {
    override fun addAsset(vt: Database, item: CryptoDB) {
        val db=vt.writableDatabase
        val values= ContentValues()

        values.put("CryptoLongName",item.CryptoLongName)
        values.put("CryptoShortName",item.CryptoShortName)
        values.put("AmountOfCrypto",item.AmountOfCrypto)
        values.put("AmountOfUSDT",item.AmountOfUSDT)
        db.insertOrThrow("CryptoDB",null,values)
        db.close()
    }

    override fun getAllAssets(vt: Database): ArrayList<CryptoDB> {
        var gelenCrypto= ArrayList<CryptoDB>()
        val db=vt.writableDatabase
        val cursor=db.rawQuery("SELECT * FROM CryptoDB",null)
        while(cursor.moveToNext()){
            val gelen= CryptoDB(cursor.getString(cursor.getColumnIndex("CryptoLongName")),
                cursor.getString(cursor.getColumnIndex("CryptoShortName")),
                cursor.getString(cursor.getColumnIndex("AmountOfCrypto")),
                cursor.getString(cursor.getColumnIndex("AmountOfUSDT")))
            gelenCrypto.add(gelen)
        }
        db.close()
        return gelenCrypto
    }

    override fun getTotalAmount(vt: Database): Double {
        val db = vt.writableDatabase
        var toplam = 0.0
        val cursor = db.rawQuery("SELECT AmountOfUSDT FROM CryptoDB", null)
        while (cursor.moveToNext()) {
            val amount = cursor.getDouble(cursor.getColumnIndex("AmountOfUSDT"))
            toplam += amount
        }
        cursor.close()
        db.close()
        return toplam
    }

    override fun sellAsset(vt: Database, item: CryptoDB) {
        val db = vt.writableDatabase
        val cursor = db.rawQuery("SELECT AmountOfUSDT, AmountOfCrypto FROM CryptoDB WHERE CryptoLongName = ?", arrayOf(item.CryptoLongName))


        if (cursor.moveToFirst()) {
            val mevcutCoin = cursor.getString(cursor.getColumnIndex("AmountOfCrypto")).toDouble()
            val mevcutUSDT = cursor.getString(cursor.getColumnIndex("AmountOfUSDT")).toDouble()
            val yeniUSDT = mevcutUSDT - item.AmountOfUSDT.toDouble()
            val yeniCoin = mevcutCoin - item.AmountOfCrypto.toDouble()



            if (yeniUSDT < 0.0001 && yeniCoin < 0.0001) {
                // Eğer tamamen sattıysan kaydı sil
                db.delete("CryptoDB", "CryptoLongName = ?", arrayOf(item.CryptoLongName))
            } else {
                // Eğer bakiye kalıyorsa güncelle
                val contentValues = ContentValues().apply {
                    put("AmountOfUSDT", yeniUSDT.toString())
                    put("AmountOfCrypto", yeniCoin.toString())
                }
                db.update("CryptoDB", contentValues, "CryptoLongName = ?", arrayOf(item.CryptoLongName))
            }
        }
        cursor.close()
        db.close()

    }

    override fun updateAsset(vt: Database, item: CryptoDB) {
        val db = vt.writableDatabase
        val cursor = db.rawQuery("SELECT AmountOfUSDT, AmountOfCrypto FROM CryptoDB WHERE CryptoLongName = ?", arrayOf(item.CryptoLongName))


        if (cursor.moveToFirst()) {
            val mevcutCoin=cursor.getString(cursor.getColumnIndex("AmountOfCrypto")).toDouble()
            val mevcutUSDT = cursor.getString(cursor.getColumnIndex("AmountOfUSDT")).toDouble()
            val yeniUSDT = mevcutUSDT + item.AmountOfUSDT.toDouble()
            val yeniCoin=mevcutCoin + item.AmountOfCrypto.toDouble()

            val contentValues = ContentValues()
            contentValues.put("AmountOfUSDT", yeniUSDT.toString())
            contentValues.put("AmountOfCrypto",yeniCoin.toString())

            db.update("CryptoDB", contentValues, "CryptoLongName = ?", arrayOf(item.CryptoLongName))

        }
        cursor.close()
        db.close()
    }

    override fun updatePrice(vt: Database, item: CryptoDB) {
        val db = vt.writableDatabase
        val values = ContentValues()

        values.put("AmountOfUSDT", item.AmountOfUSDT)

        db.update("CryptoDB", values, "CryptoShortName = ?", arrayOf(item.CryptoShortName))

        db.close()
    }
}