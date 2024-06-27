package com.bangkit.intermediate.ceritaku

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bangkit.intermediate.ceritaku.databinding.ActivityMainBinding
import com.bangkit.intermediate.ceritaku.ui.auth.OnboardingActivity
import com.bangkit.intermediate.ceritaku.ui.home.HomeActivity
import com.bangkit.intermediate.ceritaku.utils.Prefs

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        startActivity(Intent(this@MainActivity, OnboardingActivity::class.java))
        checkUser()
    }

    private fun checkUser() {
        val token = Prefs.getToken
        val intent = if (token.isNotEmpty()) {
            Intent(this, HomeActivity::class.java)
        } else {
            Intent(this, OnboardingActivity::class.java)
        }
        startActivity(intent)
        finish()
    }
}