package com.example.lifelog.database

import java.io.Serializable

class Notes(var note_id:Int?,
            var note: String,
            var note_date: String,
            var note_time: String,
            var note_title: String) : Serializable {    //Nesneyi başka activitylere de taşıyabilmek için serializable özelliği ekledik.
}