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
    fun GetAllTask(vt: Database) : ArrayList<ToDoList>{
        val TaskList= ArrayList<ToDoList>()
        val db=vt.writableDatabase
        val cursor=db.rawQuery("SELECT * FROM ToDoList",null)
        while(cursor.moveToNext()){
            val Task= ToDoList(cursor.getInt(cursor.getColumnIndex("task_id")),
                cursor.getString(cursor.getColumnIndex("task")))
            TaskList.add(Task)
        }
        db.close()
        return TaskList
    }
    fun DeleteTask(vt: Database,kisi_id:Int){
        val db=vt.writableDatabase
        db.delete("ToDoList","task_id=?",arrayOf(kisi_id.toString()))
        db.close()
    }

}