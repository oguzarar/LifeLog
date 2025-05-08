package com.example.lifelog.database.AssetsDao

import com.example.lifelog.database.Database

interface DatabaseDao<T> {
    fun addAsset(vt: Database,item:T)
    fun getAllAssets(vt: Database): ArrayList<T>
    fun getTotalAmount(vt: Database): Double
    fun sellAsset(vt: Database,item: T)
    fun updateAsset(vt: Database,item: T)
    fun updatePrice(vt: Database,item:T)

}