package com.example.lifelog.database

import android.content.ContentValues

class AddPagesDao {
    fun PluginAdd(vt: Database,Plugin_name: String){
        val db=vt.writableDatabase
        val values= ContentValues()
        values.put("Plugin_name",Plugin_name)

        db.insertOrThrow("Plugins",null,values)
        db.close()
    }
    fun getAllPlugin(vt: Database): ArrayList<Plugins>{
        val PluginList= ArrayList<Plugins>()
        val db=vt.writableDatabase
        val cursor=db.rawQuery("SELECT * FROM Plugins",null)
        while(cursor.moveToNext()){
            val plugin= Plugins(cursor.getString(cursor.getColumnIndexOrThrow("Plugin_name")))
            PluginList.add(plugin)
        }
        db.close()
        return PluginList
    }
}