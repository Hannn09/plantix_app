package com.example.plantix.ui.news

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.plantix.R
import com.example.plantix.databinding.ActivityDetailNewsBinding
import org.koin.android.ext.android.bind

class DetailNewsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailNewsBinding

    private var newsUrl: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailNewsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        newsUrl = intent?.getStringExtra("NEWS_URL")

        binding.webNews.apply {
            settings.javaScriptEnabled = true
            settings.domStorageEnabled = true
            newsUrl?.let { loadUrl(it) }
        }

        setupListener()
    }

    private fun setupListener() {
        binding.icBack.setOnClickListener { onBackPressedDispatcher.onBackPressed() }
        binding.icShare.setOnClickListener {
            newsUrl?.let {
                val shareIntent = Intent().apply {
                    action = Intent.ACTION_SEND
                    putExtra(Intent.EXTRA_TEXT, "Check out this news: $newsUrl")
                    type = "text/plain"
                }
                startActivity(Intent.createChooser(shareIntent, "Share news via"))
            }
        }
    }
}