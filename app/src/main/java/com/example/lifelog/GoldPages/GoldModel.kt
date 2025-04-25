package com.example.lifelog.GoldPages

import com.google.gson.annotations.SerializedName

//apiden gelecek verileri yakalamak için data class.
data class GoldModel(
    val success: Boolean,
    val base: String,
    val timestamp: Long,
    val rates: Rates
){
    //yalnızca altın fiyatından yararlanacağım için sadece altın fiyatını alacağım.
    data class Rates(
        @SerializedName("TRYXAU")
        val tryXau: Double
    )

}


