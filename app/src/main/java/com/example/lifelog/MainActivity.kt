package com.example.lifelog

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.Worker
import com.example.lifelog.database.Database
import com.example.lifelog.databinding.ActivityMainBinding
import java.util.Calendar
import java.util.TimeZone
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navhostFragment = supportFragmentManager.findFragmentById(R.id.navHost) as NavHostFragment //Id ile navhost bulundu.
        NavigationUI.setupWithNavController(binding.bottomNav,navhostFragment.navController)//navhost ile bottom nav bağlandı.







    }

}