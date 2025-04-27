package com.example.lifelog.database

import android.content.ContentValues

class DersTakipdao {
    fun dersEkle(vt: Database,dersadi: String,sinavTarih: String,dersSaat: String){
        val db=vt.writableDatabase
        val content= ContentValues()
        content.put("DersAdi",dersadi)
        content.put("SinavTarih",sinavTarih)
        content.put("SinavSaat",dersSaat)

        db.insertOrThrow("DersTakip",null,content)
        db.close()
    }
    fun getAllDers(vt: Database): ArrayList<DersTakip> {
        var gelenders= ArrayList<DersTakip>()
        val db = vt.writableDatabase
        val cursor = db.rawQuery("SELECT * FROM DersTakip", null)
        while (cursor.moveToNext()) {
            val ders = DersTakip(
                cursor.getString(cursor.getColumnIndex("DersAdi")),
                cursor.getString(cursor.getColumnIndex("SinavTarih")),
                cursor.getString(cursor.getColumnIndex("SinavSaat")))
            gelenders.add(ders)
        }
        db.close()
        return gelenders
    }
}