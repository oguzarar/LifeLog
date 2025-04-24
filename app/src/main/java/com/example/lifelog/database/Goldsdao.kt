package com.example.lifelog.database

import android.content.ContentValues
import android.provider.ContactsContract.Data

class Goldsdao {

    //Altın verilerinin veritabanına kayıt edilmesi
    fun addGold(vt: Database, goldType:String, goldAmount:Int){

        val db = vt.writableDatabase    //veritabanımızın yazma işlemini aktif etme
        val values = ContentValues()  //key-value ilişkisi ile veri kaydetme

        values.put("goldType", goldType)
        values.put("goldAmount", goldAmount)

        try {
            db.insertOrThrow("Golds", null, values)
        }catch (e:Exception){
            e.printStackTrace()
        }

        db.close()

    }

    //Altın verilerinin veritabanında güncellenmesi
    fun updateGold(vt: Database, goldType:String, newGoldAmount: Int){

        val db = vt.writableDatabase
        val values = ContentValues()

        values.put("goldAmount", newGoldAmount)

        try {
            db.update("Golds", values, "goldType=?", arrayOf(goldType))
        }catch (e:Exception){
            e.printStackTrace()
        }

        db.close()

    }

    //altın verilerinin tamamının getirilmesi
    fun fetchAllGold(vt: Database) : ArrayList<Golds> {

        val goldsArrayList = ArrayList<Golds>()

        val db = vt.writableDatabase

        val cursor = db.rawQuery("SELECT * FROM Golds", null)

        //altın verilerinin sırası ile gezilerek listeye eklenmesi.
        while (cursor.moveToNext()){
            val gold = Golds(cursor.getInt(cursor.getColumnIndexOrThrow("goldId"))
                ,cursor.getString(cursor.getColumnIndexOrThrow("goldType"))
                ,cursor.getInt(cursor.getColumnIndexOrThrow("goldAmount")))

            goldsArrayList.add(gold)
        }
        cursor.close()
        db.close()

        return goldsArrayList

    }

}