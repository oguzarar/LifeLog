package com.example.lifelog.database

import android.content.ContentValues

class databasedao {
    fun taskAdd(vt: database,task: String){
        val db=vt.writableDatabase
        val values= ContentValues()
        values.put("task",task)

        db.insertOrThrow("ToDoList",null,values)
        db.close()
    }
}