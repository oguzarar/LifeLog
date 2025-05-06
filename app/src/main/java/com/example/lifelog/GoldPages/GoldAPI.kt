package com.example.lifelog.GoldPages

import retrofit2.Call
import retrofit2.http.GET

//api bilgileri
interface GoldAPI {

    //https://api.metalpriceapi.com/v1/
    //latest?api_key=12374d7baa69c81ada430534b458b8ad&base=TRY&currencies=XAU

    @GET("latest?api_key=12374d7baa69c81ada430534b458b8ad&base=TRY&currencies=XAU")
    fun getGoldPrice(): Call<GoldModel>


}