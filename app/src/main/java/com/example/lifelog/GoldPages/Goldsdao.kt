package com.example.lifelog.GoldPages

import android.content.ContentValues
import android.util.Log
import com.example.lifelog.database.Database
import kotlinx.coroutines.currentCoroutineContext

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

    //Altın miktarının güncellenmesi
    fun updateGoldAmount(vt: Database, goldId: Int, newAmount: Int){

        val db = vt.writableDatabase

        val values = ContentValues()

        values.put("goldAmount", newAmount)

        db.update("Golds", values, "goldId=?", arrayOf(goldId.toString()))

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

    fun getGoldByType(vt: Database, goldType:String) : Golds? {

        val db = vt.writableDatabase

        val cursor = db.rawQuery("SELECT * FROM  golds WHERE goldType=?", arrayOf(goldType))

        var gold: Golds? = null
        while (cursor.moveToNext()){
            val goldId = cursor.getInt(cursor.getColumnIndexOrThrow("goldId"))
            val goldAmount = cursor.getInt(cursor.getColumnIndexOrThrow("goldAmount"))

            gold = Golds(goldId, goldType, goldAmount)
        }

        cursor.close()
        db.close()

        return gold
    }

    fun sellGoldByType(vt: Database, goldType: String, goldAmount: Int) : Golds? {

        val db = vt.writableDatabase
        val cursor = db.rawQuery("SELECT * FROM Golds WHERE goldType=?", arrayOf(goldType))

        var gold: Golds? = null
        if(cursor.moveToFirst()){
            val goldId = cursor.getInt(cursor.getColumnIndexOrThrow("goldId"))
            val currentAmount = cursor.getInt(cursor.getColumnIndexOrThrow("goldAmount"))
            if(currentAmount >= goldAmount){
                val newAmount = currentAmount - goldAmount
                if(newAmount == 0){
                    db.delete("Golds", "goldId=?", arrayOf(goldId.toString()))
                }
                else{
                    updateGoldAmount(vt, goldId, newAmount)
                }
                gold = Golds(goldId, goldType, goldAmount)
            }
            else{
                Log.e("Goldsdao", "Yeterli ALtın Yok")
            }
        }
        else{
            Log.e("Goldsdao", "Altın TÜrü Bulunamadı")
        }

        cursor.close()
        db.close()

        return gold
    }

    fun addGoldByType(vt: Database, goldType: String, goldAmount: Int) {

        val db = vt.writableDatabase
        val cursor = db.rawQuery("SELECT * FROM Golds WHERE goldType=?", arrayOf(goldType))

        if (cursor.moveToFirst()) {
            val goldId = cursor.getInt(cursor.getColumnIndexOrThrow("goldId"))
            val currentAmount = cursor.getInt(cursor.getColumnIndexOrThrow("goldAmount"))
            val newAmount = currentAmount + goldAmount
            updateGoldAmount(vt, goldId, newAmount)
        } else {
            val values = ContentValues()
            values.put("goldType", goldType)
            values.put("goldAmount", goldAmount)
            db.insertOrThrow("Golds", null, values)
        }

        cursor.close()
        db.close()
    }

    fun deleteGold(vt: Database, goldId: Int){
        val db = vt.writableDatabase

        db.delete("Golds","goldId", arrayOf(goldId.toString()))
        db.close()
    }

}