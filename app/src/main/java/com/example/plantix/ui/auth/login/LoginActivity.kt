package com.example.plantix.ui.auth.login

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityOptionsCompat
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.core.data.Resource
import com.example.plantix.MainActivity
import com.example.plantix.R
import com.example.plantix.databinding.ActivityLoginBinding
import com.example.plantix.ui.auth.register.RegisterActivity
import org.koin.android.viewmodel.ext.android.viewModel

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding

    private val loginViewModel: LoginViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupView()
        loginResult()
        setupListener()
        validateForm()
    }

    private fun setupView() {
        binding.apply {
            usernameEditText.addTextChangedListener(textWatcher)
            passwordEditText.addTextChangedListener(textWatcher)
        }
    }

    private fun validateForm() {
        binding.apply {
            val username = usernameEditText.text.toString().trim()
            val password = passwordEditText.text.toString().trim()

            val isFormValid = username.isNotEmpty() && password.isNotEmpty()
            btnLogin.isEnabled = isFormValid
            btnLogin.backgroundTintList = ContextCompat.getColorStateList(this@LoginActivity, if (isFormValid) R.color.primary_color else R.color.btn_disabled)
        }
    }

    private val textWatcher = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        override fun afterTextChanged(s: android.text.Editable?) { validateForm() }
    }

    private fun setupListener() {
        val options = ActivityOptionsCompat.makeCustomAnimation(
            this,
            R.anim.slide_in_right,
            R.anim.slide_out_left
        )
        binding.btnLogin.setOnClickListener { loginHandler() }
        binding.txtRegister.setOnClickListener {
            startActivity(
                Intent(
                    this@LoginActivity,
                    RegisterActivity::class.java
                ), options.toBundle()
            )
        }
    }

    private fun loginHandler() {
        binding.apply {
            val username = usernameEditText.text.toString().trim()
            val password = passwordEditText.text.toString().trim()

            loginViewModel.login(username, password)
        }
    }

    private fun loginResult() {
        loginViewModel.result.observe(this) {
            when (it) {
                is Resource.Loading -> {showLoading(true)}
                is Resource.Success -> {
                    showLoading(false)
                    val token = it.data.accessToken
                    val username = it.data.username
                    if (token != null) {
                        getSession(token, username)
                    }
                }

                is Resource.Error -> {
                    showLoading(false)
                    dialogNotification("Oops!", "${it.message}", R.drawable.error)
                    Log.d("LoginActivity", "error: ${it.message}")
                }
            }
        }
    }

    private fun getSession(token: String, username: String) {
        loginViewModel.saveSession(token, username)
        val intent = Intent(this, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
        val options = ActivityOptionsCompat.makeCustomAnimation(
            this,
            R.anim.slide_in_right,
            R.anim.slide_out_left
        )
        startActivity(intent, options.toBundle())
        finish()
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun dialogNotification(title: String, desc: String, image: Int) {
        val dialog = Dialog(this)
        dialog.apply {
            requestWindowFeature(Window.FEATURE_NO_TITLE)
            setCancelable(false)
            setContentView(R.layout.dialog_notification)
            window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            val width = (resources.displayMetrics.widthPixels * 0.95).toInt()
            val height = WindowManager.LayoutParams.WRAP_CONTENT
            window?.setLayout(width, height)

            val imageView = dialog.findViewById<ImageView>(R.id.img_alert)
            val titleText = dialog.findViewById<TextView>(R.id.title)
            val descText = dialog.findViewById<TextView>(R.id.desc)
            val btnClose = dialog.findViewById<Button>(R.id.btnClose)

            imageView.setImageDrawable(ContextCompat.getDrawable(this@LoginActivity, image))
            titleText.text = title
            descText.text = desc
            btnClose.setOnClickListener {
                dismiss()
            }
            show()
        }
    }

}
