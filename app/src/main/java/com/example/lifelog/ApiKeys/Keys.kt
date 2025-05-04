package com.example.lifelog.ApiKeys

class Keys {
    private val kriptoApiKeys="qtay5LNwD11NCh4wk0H9CQ==jOshOFz3lSrAYmH2"

    private val dovizApiKeys="fca_live_OKG5N98GmLSwt8AeENdvYrAhKVwSx9pfSgXmk9uv"
    fun getKriptoKey(): String{
        return kriptoApiKeys
    }

    fun getDovizKey(): String{
        return dovizApiKeys
    }

}