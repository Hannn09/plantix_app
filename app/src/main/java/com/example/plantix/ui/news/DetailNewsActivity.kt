package com.example.plantix.ui.news

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.plantix.R
import com.example.plantix.databinding.ActivityDetailNewsBinding
import org.koin.android.ext.android.bind

class DetailNewsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailNewsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailNewsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupListener()
    }

    private fun setupListener() {
        binding.icBack.setOnClickListener { onBackPressedDispatcher.onBackPressed() }
    }
}