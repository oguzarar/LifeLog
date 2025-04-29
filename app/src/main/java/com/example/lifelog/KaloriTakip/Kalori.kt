package com.example.lifelog.KaloriTakip

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import org.jetbrains.annotations.NotNull

@Entity(tableName = "urunler")
data class Kalori(@PrimaryKey(autoGenerate = true)
                  var id: Int,
                  @ColumnInfo(name="isim")@NotNull var isim: String,
                  @ColumnInfo(name="kategori")@NotNull
                  var kategori : String,
                  @ColumnInfo(name="kalori")@NotNull
                  var kalori: String,
                  @ColumnInfo(name="protein")@NotNull
                  var protein: String) {
}