package com.dilan.kamuda.houseownerapp.feature.main

import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.dilan.kamuda.houseownerapp.ActBase
import com.dilan.kamuda.houseownerapp.R
import com.dilan.kamuda.houseownerapp.common.util.KamuDaSecurePreference
import com.dilan.kamuda.houseownerapp.databinding.ActivityMainBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ActBase() {
    lateinit var binding: ActivityMainBinding

    companion object {
        var kamuDaSecurePreference = KamuDaSecurePreference()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        kamuDaSecurePreference.clearSharedPrefKeys(this)

        val navView: BottomNavigationView = binding.navView

        val navController = findNavController(R.id.nav_host_fragment_activity_main)

        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.homeFragment, R.id.orderFragment, R.id.menuFragment, R.id.mealFragment
            )
        )

        //setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

    }
}