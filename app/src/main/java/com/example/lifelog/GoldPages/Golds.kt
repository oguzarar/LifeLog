package com.example.lifelog.GoldPages

import java.io.Serializable
                                        //Activityler arası nesne aktarımı için serializable özelliği
data class Golds(var goldId:Int, var goldType:String, var goldAmount:Int) : Serializable {
}