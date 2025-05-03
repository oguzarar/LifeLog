package com.example.lifelog.KaloriTakip

import androidx.room.Dao
import androidx.room.Query
import com.example.lifelog.KaloriTakip.Kalori

@Dao
interface Kaloridao {
    @Query("SELECT * FROM urunler")//Altaki fonksiyonun ne yapacaığını yazdık
    suspend fun tumKisiler(): List<Kalori>
}