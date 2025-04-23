package com.example.lifelog.database

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class Database(context: Context): SQLiteOpenHelper(context,"LifeLog",null,1) {

    override fun onCreate(db: SQLiteDatabase?) {//Veritabanındaki tablolar oluşturuldu.
        db?.execSQL("CREATE TABLE Notes(note_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "note TEXT,note_date TEXT,note_time TEXT, note_title TEXT)")

        db?.execSQL("CREATE TABLE ToDoList(task_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "task TEXT)")
    }

    override fun onUpgrade(db: SQLiteDatabase?, p1: Int, p2: Int) {
        db?.execSQL("DROP TABLE IF EXISTS Notes")

        db?.execSQL("DROP TABLE IF EXISTS ToDoList")
        onCreate(db)


    }
}