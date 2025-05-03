package com.example.room

import android.content.Context
import androidx.room.Database
import androidx.room.Entity
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.lifelog.KaloriTakip.Kaloridao
import com.example.lifelog.KaloriTakip.Kalori


@Database(entities = [Kalori::class], version = 1)//Projedeki hangis sınıfların veritabanı üzerindeki tabloları temsil ediyor o sınıfları yazdık
abstract class veritabani: RoomDatabase() {
    abstract fun getkisilerDao(): Kaloridao

    companion object{
        var INSTANCE:veritabani?=null
        fun veritabaniErisim(context: Context):veritabani?{// veritabanı bağlantısını almak için kullanılan bir fonksiyon.
            if(INSTANCE==null){//Eğer nullsa daha önce veritabanı bağlantısı oluşturulmamış.
                synchronized (veritabani::class){//Daha performanslı çalışmasını sağlar
                    INSTANCE= Room.databaseBuilder(context.applicationContext,//Veritabanı örneği oluşturmak için kullanılır.
                        veritabani::class.java,//Veritabanı sınıfını belirtir.
                        "yemek.sqlite").
                    createFromAsset("yemek.sqlite")//hangi veritabanından kopyalanacaığını belirti
                        .build()//işlemerli tamamlar
                }
            }
            return INSTANCE
        }
    }

}