package com.example.lifelog.database

import java.io.Serializable

data class CryptoDB(var CryptoLongName: String,var CryptoShortName: String,var AmountOfUSDT: String,var AmountOfCrypto: String):
    Serializable{
}