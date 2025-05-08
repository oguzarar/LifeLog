package com.example.lifelog.DersTakip

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.DialogInterface
import android.icu.util.Calendar
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.lifelog.R
import com.example.lifelog.database.Database
import com.example.lifelog.database.TaskDaos.derstakip.DersTakip
import com.example.lifelog.database.TaskDaos.derstakip.DersTakipDao
import com.example.lifelog.databinding.ActivityDersTakip2Binding

class DersTakip2Activity : AppCompatActivity() {
    private lateinit var binding: ActivityDersTakip2Binding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_ders_takip2)
        binding= ActivityDersTakip2Binding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.Tarihinput.setOnClickListener {
            val calendar= Calendar.getInstance()
            val yil=calendar.get(Calendar.YEAR)
            val ay=calendar.get(Calendar.MONTH)
            val gun=calendar.get(Calendar.DAY_OF_MONTH)

            val datepicker = DatePickerDialog(
                this@DersTakip2Activity, DatePickerDialog.OnDateSetListener { _, y, a, g ->
                    val formattedDate = String.format("%02d/%02d/%04d", g, a + 1, y)
                    binding.Tarihinput.setText(formattedDate)
                },
                yil, ay, gun
            )

            //Saateki gibi aynısı yapıldı güncel tarih bilgisi alındı.

            datepicker.setTitle("Saat seçiniz")
            datepicker.setButton(DialogInterface.BUTTON_POSITIVE,"Ayarla",datepicker)//Ayarlamak için buton ayarlandı
            datepicker.setButton(DialogInterface.BUTTON_NEGATIVE,"İPTAL",datepicker)//İptal etmek için buton ayarlandı
            datepicker.show()
        }
        binding.saatinput.setOnClickListener {
            val calendar= Calendar.getInstance()
            val saat1=calendar.get(Calendar.HOUR_OF_DAY)
            val dakika=calendar.get(Calendar.MINUTE)
            val timePicker = TimePickerDialog(
                this@DersTakip2Activity, TimePickerDialog.OnTimeSetListener { _, i, i2 ->
                    val formattedTime = String.format("%02d:%02d", i, i2)
                    binding.saatinput.setText(formattedTime)
                },
                saat1, dakika, true
            )
            //Güncel saat bilgisi alındı

            timePicker.setTitle("Saat seçiniz")
            timePicker.setButton(DialogInterface.BUTTON_POSITIVE,"Ayarla",timePicker)//Ayarlamak için buton ayarlandı
            timePicker.setButton(DialogInterface.BUTTON_NEGATIVE,"İPTAL",timePicker)//İptal etmek için buton ayarlandı
            timePicker.show()

        }
        val tarihkontrol="\\d{2}/\\d{2}/\\d{4}"
        val saatkontrol="\\d{2}:\\d{2}"
        binding.SNavekle.setOnClickListener {
            val vt= Database(this)
            val dersad=binding.dersinput.text.trim()
            val dersTarih=binding.Tarihinput.text.trim()
            val dersSaat=binding.saatinput.text.trim()
            if(dersad.isNullOrEmpty()||dersTarih.isNullOrEmpty()||dersSaat.isNullOrEmpty()){
                Toast.makeText(this,"Boş değer girilemez", Toast.LENGTH_SHORT).show()
            }else{
                if(dersTarih.matches(Regex(tarihkontrol))&&dersSaat.matches(Regex(saatkontrol))){
                    val ders= DersTakip(dersAdi =dersad.toString(), dersTarih =dersTarih.toString(), dersSaat = dersSaat.toString()  )
                    DersTakipDao().TaskAdd(vt,ders)
                    binding.dersinput.text.clear()
                    binding.saatinput.text.clear()
                    binding.Tarihinput.text.clear()
                    Toast.makeText(this,"Eklendi",Toast.LENGTH_SHORT).show()
                }else{
                    Toast.makeText(this,"Tarih veya saat yanlış yazıldı",Toast.LENGTH_SHORT).show()
                }

            }

        }
        binding.backbutton.setOnClickListener {
            finish()
        }
    }
}
