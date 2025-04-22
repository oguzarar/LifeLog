package com.example.lifelog.database

import android.content.ContentValues

class Databasedao {

    fun taskAdd(vt: Database, task: String){
        val db = vt.writableDatabase
        val values = ContentValues()
        values.put("task",task)

        db.insertOrThrow("ToDoList",null,values)
        db.close()
    }

}