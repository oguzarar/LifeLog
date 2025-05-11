package com.example.lifelog.database.TaskDaos.derstakip

import android.content.ContentValues
import com.example.lifelog.database.Database
import com.example.lifelog.database.TaskDaos.TaskDaos
import com.example.lifelog.database.TaskDaos.derstakip.DersTakip
import java.util.ArrayList

class DersTakipDao: TaskDaos<DersTakip> {
    override fun TaskAdd(vt: Database, item: DersTakip
    ) {
        val db=vt.writableDatabase
        val content= ContentValues()
        content.put("DersAdi",item.dersAdi)
        content.put("SinavTarih",item.dersTarih)
        content.put("SinavSaat",item.dersSaat)

        db.insertOrThrow("DersTakip",null,content)
        db.close()
    }

    override fun GetAllTask(vt: Database): ArrayList<DersTakip> {
        var gelenders= ArrayList<DersTakip>()
        val db = vt.writableDatabase
        val cursor = db.rawQuery("SELECT * FROM DersTakip", null)
        while (cursor.moveToNext()) {
            val ders = DersTakip(
                cursor.getInt(cursor.getColumnIndexOrThrow("Ders_id")),
                cursor.getString(cursor.getColumnIndexOrThrow("DersAdi")),
                cursor.getString(cursor.getColumnIndexOrThrow("SinavTarih")),
                cursor.getString(cursor.getColumnIndexOrThrow("SinavSaat")))
            gelenders.add(ders)
        }
        db.close()
        return gelenders
    }

    override fun DeleteTask(vt: Database, Task_id: Int) {
        val db=vt.writableDatabase
        db.delete("DersTakip","Ders_id=?",arrayOf(Task_id.toString()))
        db.close()
    }
}