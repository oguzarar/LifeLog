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
    fun getAllTask(vt: database): ArrayList<ToDoList>{
        val TasksArrayList= ArrayList<ToDoList>()
        val db=vt.writableDatabase
        val cursor=db.rawQuery("SELECT * FROM ToDoList",null)
        while(cursor.moveToNext()){
            val task= ToDoList(cursor.getInt(cursor.getColumnIndex("task_id")+1),
                                cursor.getString(cursor.getColumnIndex("task")+1))
            TasksArrayList.add(task)
        }
        return TasksArrayList
        db.close()
    }

}