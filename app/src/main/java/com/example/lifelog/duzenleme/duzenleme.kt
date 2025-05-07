package com.example.lifelog.duzenleme

import java.text.DecimalFormat

class duzenleme {
    companion object{
        fun formatNumber(sayi: Double): String {
            val formatter = DecimalFormat("0.##########")
            return formatter.format(sayi)
        }

        fun formatNumber2(sayi : Double): String{
            val formatter= DecimalFormat("0.##")
            return formatter.format(sayi)
        }

        fun formatNumber4(sayi : Double): String{
            val formatter= DecimalFormat("0.####")
            return formatter.format(sayi)
        }

    }
}