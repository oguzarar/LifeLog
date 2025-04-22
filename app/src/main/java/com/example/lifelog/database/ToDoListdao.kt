package com.example.lifelog.database

import android.content.ContentValues

class ToDoListdao {
    fun TaskAdd(vt: Database,Task: String){ //Veri ekleme fonskiyonu
        val db=vt.writableDatabase
        val values= ContentValues()
        values.put("task",Task)

        db.insertOrThrow("ToDoList",null,values)
        db.close()
    }
    
}