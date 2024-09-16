package com.example.plantix.ui.auth.register

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.util.Patterns
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
import com.example.plantix.R
import com.example.plantix.databinding.ActivityRegisterBinding
import com.example.plantix.ui.auth.login.LoginActivity
import org.koin.android.viewmodel.ext.android.viewModel

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding

    private val registerViewModel: RegisterViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupView()
        registerResult()
        setupListener()
        validateForm()
    }

    private fun setupView() {
        binding.apply {
            usernameEditText.addTextChangedListener(textWatcher)
            emailEditText.addTextChangedListener(textWatcher)
            passwordEditText.addTextChangedListener(textWatcher)
            confirmPasswordEditText.addTextChangedListener(textWatcher)
        }
    }

    private fun setupListener() {
        binding.btnRegister.setOnClickListener { registerHandler() }
        binding.txtLogin.setOnClickListener { startActivity(Intent(this@RegisterActivity, LoginActivity::class.java)) }
    }

    private fun registerHandler() {
        binding.apply {
            val username = usernameEditText.text.toString().trim()
            val email = emailEditText.text.toString().trim()
            val password = passwordEditText.text.toString().trim()

            registerViewModel.register(username, email, password)
        }
    }

    private fun registerResult() {
        registerViewModel.result.observe(this) {
            when (it) {
                is Resource.Loading -> { showLoading(true) }

                is Resource.Success -> {
                    showLoading(false)
                    val options = ActivityOptionsCompat.makeCustomAnimation(
                        this,
                        R.anim.slide_in_right,
                        R.anim.slide_out_left
                    )
                    dialogNotification("Yeiyyy!", "Akun anda berhasil dibuat. Silahkan login untuk melanjutkan", R.drawable.success) {
                        startActivity(Intent(this@RegisterActivity, LoginActivity::class.java), options.toBundle())
                    }
                }

                is Resource.Error -> {
                    showLoading(false)
                    dialogNotification("Oops!", "${it.message}", R.drawable.error)
                    Log.d("RegisterActivity", "error: ${it.message}")
                }
            }
        }
    }

    private fun validateForm() {
        binding.apply {
            val username = usernameEditText.text.toString().trim()
            val email = emailEditText.text.toString().trim()
            val password = passwordEditText.text.toString().trim()
            val confirmPassword = confirmPasswordEditText.text.toString().trim()

            val isPasswordValid = password == confirmPassword
            val isEmailValid = email.isNotEmpty() && Patterns.EMAIL_ADDRESS.matcher(email).matches()

            if (!isEmailValid) {
                emailEditText.error = "Enter a valid email address"
            }

            val isFormValid = username.isNotEmpty() && email.isNotEmpty() && isPasswordValid
            btnRegister.isEnabled = isFormValid
            btnRegister.backgroundTintList = ContextCompat.getColorStateList(this@RegisterActivity, if (isFormValid) R.color.primary_color else R.color.btn_disabled)
        }
    }

    private val textWatcher = object : TextWatcher {
        override fun afterTextChanged(s: Editable?) { validateForm() }
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun dialogNotification(title: String, desc: String, image: Int, onDismiss: () -> Unit = {}) {
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

            imageView.setImageDrawable(ContextCompat.getDrawable(this@RegisterActivity, image))
            titleText.text = title
            descText.text = desc
            btnClose.setOnClickListener {
                dismiss()
                onDismiss()
            }
            show()
        }
    }
}