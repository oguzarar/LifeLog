package com.example.lifelog.database

import android.content.ContentValues

class CryptoDao {
    fun AddCrypto(vt: Database,CryptoLongName: String,CryptoShortName: String,AmountOfUSDT: String,AmountOfCrypto: String){
        val db=vt.writableDatabase
        val values= ContentValues()

        values.put("CryptoLongName",CryptoLongName)
        values.put("CryptoShortName",CryptoShortName)
        values.put("AmountOfCrypto",AmountOfCrypto)
        values.put("AmountOfUSDT",AmountOfUSDT)
        db.insertOrThrow("CryptoDB",null,values)
        db.close()
    }

}