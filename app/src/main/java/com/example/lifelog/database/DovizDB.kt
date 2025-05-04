package com.example.lifelog.database

import java.io.Serializable

data class DovizDB(var DovizLongName: String,var DovizShortName: String,var DovizMiktari: String,var DovizMiktariTRY: String):
    Serializable {
}