package com.example.lifelog.database

import android.content.ContentValues
import android.provider.ContactsContract.Data
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class Notesdao {

    fun notEkle(vt: Database, note:String, note_title:String){  //Not kayıt

        val formatterDate = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())
        val formatterTime = SimpleDateFormat("HH:mm", Locale.getDefault())
        val currentDate = formatterDate.format(Date())  //Anlık tarih ve saat bilgilerini alma
        val currentTime = formatterTime.format(Date())

        val db = vt.writableDatabase    //veritabanımızın yazma işlemini aktif etme
        val values = ContentValues()    //verileri eklemek için key-value eşleşmesi sağlarız

        values.put("note", note)
        values.put("note_title", note_title)    //verilerin eklenmesi
        values.put("note_date", currentDate)
        values.put("note_time", currentTime)

        //olası hataya karşı try-catch bloğu
        try {
            db.insertOrThrow("Notes", null, values)    //yazma işlemi
        }catch (e: Exception){
            e.printStackTrace()
        }

        db.close()  //veritabanını kapatma.,

    }

    fun notGetir(vt: Database) : ArrayList<Notes>{  //Not Getirme, görüntüleme

        val notesArrayList = ArrayList<Notes>()

        val db = vt.writableDatabase
        val cursor = db.rawQuery("SELECT * FROM Notes", null)

        while (cursor.moveToNext()){

            val note = Notes(cursor.getInt(cursor.getColumnIndexOrThrow("note_id"))
                ,cursor.getString(cursor.getColumnIndexOrThrow("note"))
                ,cursor.getString(cursor.getColumnIndexOrThrow("note_date"))
                ,cursor.getString(cursor.getColumnIndexOrThrow("note_time"))
                ,cursor.getString(cursor.getColumnIndexOrThrow("note_title")))

            notesArrayList.add(note)
        }
        cursor.close()
        db.close()

        return notesArrayList
    }

    fun notSil(vt: Database, note_id:Int){      //Not silme

        val db = vt.writableDatabase

        db.delete("Notes", "note_id=?", arrayOf(note_id.toString()))
        db.close()

    }

}