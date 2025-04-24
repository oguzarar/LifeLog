package com.example.lifelog.database

import java.io.Serial
import java.io.Serializable
                                        //Activityler arası nesne aktarımı için serializable özelliği
data class Golds(var goldId:Int, var goldType:String, var goldAmount:Int) : Serializable {
}