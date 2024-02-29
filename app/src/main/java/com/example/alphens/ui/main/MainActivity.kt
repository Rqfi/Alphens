package com.example.alphens.ui.main

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.example.alphens.R
import com.example.alphens.databinding.ActivityMainBinding
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host) as NavHostFragment
        navController = navHostFragment.navController
    }

    override fun onBackPressed() {
        val currentDestinationId = navController.currentDestination?.id
        when (currentDestinationId) {
            R.id.signupFragment -> {
                navController.navigate(R.id.action_signupFragment_to_loginFragment)
            }
            R.id.forgotPasswordFragment -> {
                navController.navigate(R.id.action_forgotPasswordFragment_to_loginFragment)
            }
            R.id.detailFragment -> {
                navController.navigate(R.id.action_detailFragment_to_listFragment)
            }
            R.id.listFragment -> {
                moveTaskToBack(true)
            }
            else -> {
                super.onBackPressed()
            }
        }
    }
}