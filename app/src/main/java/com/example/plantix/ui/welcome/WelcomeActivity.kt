package com.example.plantix.ui.welcome

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityOptionsCompat
import androidx.viewpager2.widget.ViewPager2
import com.example.plantix.R
import com.example.plantix.databinding.ActivityWelcomeBinding
import com.example.plantix.ui.auth.login.LoginActivity
import com.example.plantix.ui.auth.register.RegisterActivity

class WelcomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityWelcomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWelcomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupView()
    }

    private fun setupView() {
        val indicator = binding.dotsIndicator
        val adapter = WelcomePageAdapter(this)
        val viewPager = binding.welcomeViewPager
        viewPager.adapter = adapter
        viewPager.isUserInputEnabled = false
        indicator.dotsClickable = false
        indicator.attachTo(viewPager)

        viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                changeTextPage(position)
            }
        })

        binding.btnNext.setOnClickListener {
            if (viewPager.currentItem < adapter.itemCount - 1) {
                viewPager.currentItem += 1
            } else {
                val options = ActivityOptionsCompat.makeCustomAnimation(
                    this,
                    R.anim.slide_in_right,
                    R.anim.slide_out_left
                )
                startActivity(Intent(this, RegisterActivity::class.java), options.toBundle())
                finish()
            }
        }

        binding.btnSkip.setOnClickListener {
            when (viewPager.currentItem) {
                0 -> { viewPager.currentItem += 2 }
                1 -> { viewPager.currentItem -= 1 }
                else -> {
                    val options = ActivityOptionsCompat.makeCustomAnimation(
                        this,
                        R.anim.slide_in_right,
                        R.anim.slide_out_left
                    )
                    startActivity(Intent(this, LoginActivity::class.java), options.toBundle())
                    finish()
                }
            }
        }

    }

    private fun changeTextPage(position: Int) {
        when(position) {
            0 ->  {
                binding.imgLogo.setImageResource(R.drawable.welcome_first)
                binding.txtWelcome.visibility = View.GONE
                binding.txtTitle.text = getString(R.string.dummy_title)
                binding.txtDescription.text = getString(R.string.dummy_desc)
            }
            1 -> {
                binding.imgLogo.setImageResource(R.drawable.welcome_second)
                binding.txtWelcome.visibility = View.GONE
                binding.btnSkip.text = resources.getText(R.string.back)
                binding.txtTitle.text = getString(R.string.second_title)
                binding.txtDescription.text = getString(R.string.second_desc)
            }
            2 -> {
                binding.imgLogo.setImageResource(R.drawable.welcome_third)
                binding.btnSkip.text = resources.getText(R.string.masuk)
                binding.btnNext.text = resources.getText(R.string.register)
                binding.txtTitle.text = getString(R.string.third_title)
                binding.txtDescription.text = getString(R.string.third_desc)
            }
        }
    }
}