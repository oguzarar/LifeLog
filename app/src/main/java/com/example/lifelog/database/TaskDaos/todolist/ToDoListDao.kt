package com.example.lifelog.database.TaskDaos.todolist

import android.content.ContentValues
import com.example.lifelog.database.Database
import com.example.lifelog.database.TaskDaos.TaskDaos

class ToDoListDao: TaskDaos<ToDoList> {
    override fun TaskAdd(
        vt: Database,
        item: ToDoList
    ) {
        val db=vt.writableDatabase
        val values= ContentValues()
        values.put("task",item.task)

        db.insertOrThrow("ToDoList",null,values)
        db.close()
    }


    override fun GetAllTask(vt: Database): ArrayList<ToDoList> {
        val TaskList= ArrayList<ToDoList>()
        val db=vt.writableDatabase
        val cursor=db.rawQuery("SELECT * FROM ToDoList",null)
        while(cursor.moveToNext()){
            val Task= ToDoList(cursor.getInt(cursor.getColumnIndexOrThrow("task_id")),
                cursor.getString(cursor.getColumnIndexOrThrow("task")))
            TaskList.add(Task)
        }
        db.close()
        return TaskList
    }

    override fun DeleteTask(vt: Database, Task_id: Int) {
        val db=vt.writableDatabase
        db.delete("ToDoList","task_id=?",arrayOf(Task_id.toString()))
        db.close()
    }

}