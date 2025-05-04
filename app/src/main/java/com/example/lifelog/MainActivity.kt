package com.example.lifelog

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.example.lifelog.database.Database
import com.example.lifelog.databinding.ActivityMainBinding

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