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
        db?.execSQL("CREATE TABLE  Plugins(Plugin_name TEXT PRIMARY KEY)")

        db?.execSQL("CREATE TABLE Golds(goldId INTEGER PRIMARY KEY AUTOINCREMENT," +
                " goldType TEXT, goldAmount INTEGER)")

        db?.execSQL("CREATE TABLE CryptoDB(CryptoLongName TEXT PRIMARY KEY,CryptoShortName TEXT," +
                "AmountOfCrypto TEXT,AmountOfUSDT TEXT)")

        db?.execSQL("CREATE TABLE DersTakip(DersAdi TEXT ,SinavTarih TEXT,SinavSaat TEXT)")
    }

    override fun onUpgrade(db: SQLiteDatabase?, p1: Int, p2: Int) {
        db?.execSQL("DROP TABLE IF EXISTS Notes")

        db?.execSQL("DROP TABLE IF EXISTS ToDoList")

        db?.execSQL("DROP TABLE IF EXISTS Plugins")

        db?.execSQL("DROP TABLE IF EXISTS Golds")

        db?.execSQL("DROP TABLE IF EXISTS CryptoDB")

        db?.execSQL("DROP TABLE IF EXISTS DersTakip")
        onCreate(db)


    }
}