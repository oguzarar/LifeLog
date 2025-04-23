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
}