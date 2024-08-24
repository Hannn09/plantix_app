package com.example.plantix.ui.splash

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.animation.AnimationUtils
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.core.data.Resource
import com.example.plantix.MainActivity
import com.example.plantix.R
import com.example.plantix.databinding.ActivitySplashScreenBinding
import com.example.plantix.ui.auth.login.LoginViewModel
import com.example.plantix.ui.welcome.WelcomeActivity
import org.koin.android.viewmodel.ext.android.viewModel
import kotlin.math.log

class SplashScreenActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySplashScreenBinding

    private val splashTime: Long = 3000

    private val loginViewModel: LoginViewModel by viewModel()

    private var token: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        loginViewModel.getSession()
        checkSession()

        val fadeInScale = AnimationUtils.loadAnimation(this, R.anim.fade_in_scale)
        binding.logo.startAnimation(fadeInScale)

        @Suppress("DEPRECATION")
        Handler().postDelayed({
            checkToken()
        }, splashTime)
    }

    private fun checkSession() {
        loginViewModel.token.observe(this) {
            when (it) {
                is Resource.Loading -> {}
                is Resource.Success -> {
                    token = it.data
                }
                is Resource.Error -> {
                    Log.d("SplashScreen", "error: ${it.message}")
                }
            }
        }
    }

    private fun checkToken() {
        Log.d("SplashScreen", "checkToken: $token")
        if (token.isEmpty()) {
            startActivity(Intent(this, WelcomeActivity::class.java))
            finish()
        } else {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
    }
}