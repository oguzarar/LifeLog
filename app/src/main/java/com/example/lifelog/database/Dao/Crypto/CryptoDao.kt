package com.example.lifelog.database.Dao.Crypto

import android.content.ContentValues
import com.example.lifelog.database.Database

class CryptoDao {
    //Veri ekleme
    fun AddCrypto(vt: Database, CryptoLongName: String, CryptoShortName: String, AmountOfUSDT: String, AmountOfCrypto: String){
        val db=vt.writableDatabase
        val values= ContentValues()

        values.put("CryptoLongName",CryptoLongName)
        values.put("CryptoShortName",CryptoShortName)
        values.put("AmountOfCrypto",AmountOfCrypto)
        values.put("AmountOfUSDT",AmountOfUSDT)
        db.insertOrThrow("CryptoDB",null,values)
        db.close()
    }
    //Toplam miktar alma
    fun GetTotalAmount(vt: Database): Double {
        val db = vt.writableDatabase
        var toplam = 0.0
        val cursor = db.rawQuery("SELECT AmountOfUSDT FROM CryptoDB", null)
        while (cursor.moveToNext()) {
            val amount = cursor.getDouble(cursor.getColumnIndexOrThrow("AmountOfUSDT"))
            toplam += amount
        }
        cursor.close()
        db.close()
        return toplam
    }
    //Eklenen verileri alma
    fun GetCrypto(vt: Database): ArrayList<CryptoDB>{
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
    //Güncelleme
    fun UpdateCryptoUSDT(vt: Database, cryptoLongName: String, eklenecekMiktar: Double, eklenecekCoin:Double): Boolean {
        val db = vt.writableDatabase
        val cursor = db.rawQuery("SELECT AmountOfUSDT, AmountOfCrypto FROM CryptoDB WHERE CryptoLongName = ?", arrayOf(cryptoLongName))
        var basarili = false

        if (cursor.moveToFirst()) {
            val mevcutCoin=cursor.getString(cursor.getColumnIndexOrThrow("AmountOfCrypto")).toDouble()
            val mevcutUSDT = cursor.getString(cursor.getColumnIndexOrThrow("AmountOfUSDT")).toDouble()
            val yeniUSDT = mevcutUSDT + eklenecekMiktar
            val yeniCoin=mevcutCoin+eklenecekCoin

            val contentValues = ContentValues()
            contentValues.put("AmountOfUSDT", yeniUSDT.toString())
            contentValues.put("AmountOfCrypto",yeniCoin.toString())

            val guncellemeSonucu = db.update("CryptoDB", contentValues, "CryptoLongName = ?", arrayOf(cryptoLongName))
            basarili = guncellemeSonucu > 0
        }
        cursor.close()
        db.close()
        return basarili
    }
    //Satma
    fun SellCryptoUSDT(vt: Database, cryptoLongName: String, eklenecekMiktar: Double, eklenecekCoin: Double): Boolean {
        val db = vt.writableDatabase
        val cursor = db.rawQuery("SELECT AmountOfUSDT, AmountOfCrypto FROM CryptoDB WHERE CryptoLongName = ?", arrayOf(cryptoLongName))
        var basarili = false

        if (cursor.moveToFirst()) {
            val mevcutCoin = cursor.getString(cursor.getColumnIndexOrThrow("AmountOfCrypto")).toDouble()
            val mevcutUSDT = cursor.getString(cursor.getColumnIndexOrThrow("AmountOfUSDT")).toDouble()
            val yeniUSDT = mevcutUSDT - eklenecekMiktar
            val yeniCoin = mevcutCoin - eklenecekCoin

            if (yeniUSDT < 0 || yeniCoin < 0) {
                cursor.close()
                db.close()
                return false
            }

            if (yeniUSDT == 0.0 && yeniCoin == 0.0) {
                // Eğer tamamen sattıysan kaydı sil
                db.delete("CryptoDB", "CryptoLongName = ?", arrayOf(cryptoLongName))
                basarili = true
            } else {
                // Eğer bakiye kalıyorsa güncelle
                val contentValues = ContentValues().apply {
                    put("AmountOfUSDT", yeniUSDT.toString())
                    put("AmountOfCrypto", yeniCoin.toString())
                }
                val guncellemeSonucu = db.update("CryptoDB", contentValues, "CryptoLongName = ?", arrayOf(cryptoLongName))
                basarili = guncellemeSonucu > 0
            }
        }
        cursor.close()
        db.close()
        return basarili
    }


    fun guncelleCrypto(vt: Database, CryptoShortName: String, AmountOfUSDT: String) {
        val db = vt.writableDatabase
        val values = ContentValues()

        values.put("AmountOfUSDT", AmountOfUSDT)

        db.update("CryptoDB", values, "CryptoShortName = ?", arrayOf(CryptoShortName))

        db.close()
    }





}