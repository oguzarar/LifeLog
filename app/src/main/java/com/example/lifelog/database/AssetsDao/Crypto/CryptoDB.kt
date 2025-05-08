package com.example.lifelog.database.AssetsDao.Crypto

import java.io.Serializable

data class CryptoDB(var CryptoLongName: String,var CryptoShortName: String,var AmountOfCrypto: String,var AmountOfUSDT: String):
    Serializable{
}