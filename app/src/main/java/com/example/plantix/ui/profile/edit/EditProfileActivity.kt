package com.example.plantix.ui.profile.edit

import android.app.AlertDialog
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.Window
import android.view.WindowManager
import android.widget.ProgressBar
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.example.core.data.Resource
import com.example.core.data.source.remote.response.DetailUser
import com.example.core.utils.convertUrlToFile
import com.example.core.utils.reduceImageFile
import com.example.core.utils.uriToFile
import com.example.plantix.R
import com.example.plantix.databinding.ActivityEditProfileBinding
import com.example.plantix.ui.profile.ProfileViewModel
import com.google.android.material.bottomsheet.BottomSheetDialog
import kotlinx.coroutines.launch
import org.koin.android.viewmodel.ext.android.viewModel

class EditProfileActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEditProfileBinding
    private val profileViewModel: ProfileViewModel by viewModel()

    private var userId: Int? = null
    private var currentUriImage: Uri? = null
    private var successDialog: BottomSheetDialog? = null
    private var progressDialog: Dialog? = null

    private val galleryLauncher = registerForActivityResult(ActivityResultContracts.GetContent()) {
        currentUriImage = it
        try {
            binding.imgProfile.setImageURI(currentUriImage)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private var lastUpdatedData = DetailUser()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        observeData()
        setupView()
        setupListener()
        updateResult()
    }

    private fun setupListener() {
        binding.apply {
            icEditProfile.setOnClickListener { galleryLauncher.launch("image/*") }
            btnSave.setOnClickListener { updateHandler() }
            icArrowBack.setOnClickListener { onBackPressedDispatcher.onBackPressed() }
            usernameEditText.setOnClickListener { showDialogWarning("Data username tidak bisa diubah semenjak selesai pendaftaran") }
            emailEditText.setOnClickListener { showDialogWarning("Data email tidak bisa diubah semenjak selesai pendaftaran") }
        }
    }

    private fun showDialogWarning(data: String) {
        val dialog = Dialog(this)
        dialog.apply {
            requestWindowFeature(Window.FEATURE_NO_TITLE)
            setCancelable(false)
            setContentView(R.layout.warning_dialog)
            window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            val width = (resources.displayMetrics.widthPixels * 0.95).toInt()
            val height = WindowManager.LayoutParams.WRAP_CONTENT
            window?.setLayout(width, height)

            val dataTextView = dialog.findViewById<TextView>(R.id.data)
            dataTextView.text = data
            show()
            Handler(Looper.getMainLooper()).postDelayed({
                dismiss()
            }, 3000)

        }
    }

    private fun observeData() {
        userId = intent?.getIntExtra("USER_ID", 0)
        userId?.let { profileViewModel.getDetailUser(it) }
        detailUserResult()
    }

    private fun detailUserResult() {
        profileViewModel.detailUser.observe(this) {
            when (it) {
                is Resource.Loading -> {}
                is Resource.Success -> {
                    val data = it.data.data?.first()
                    val name = data?.username?.split(" ")
                        ?.joinToString(separator = " ") { it.capitalize() }
                    val email = data?.email
                    val fullName = data?.fullName
                    val profileImage = data?.profilePictureUrl

                    binding.apply {
                        val imageUrl =
                            "https://storage.googleapis.com/bucket-adoptify/plantixImagesUser/$profileImage"
                        nameEditText.setText(fullName)
                        emailEditText.setText(email)
                        usernameEditText.setText(name)

                        Glide.with(this@EditProfileActivity)
                            .load(imageUrl)
                            .placeholder(R.drawable.skeleton_background)
                            .into(imgProfile)

                        lastUpdatedData = DetailUser(
                            fullName = fullName,
                            username = name,
                            email = email,
                            profilePictureUrl = profileImage
                        )
                    }
                }

                is Resource.Error -> {
                    Log.e("EditProfile", "error: ${it.message}")
                }
            }
        }
    }

    private fun updateHandler() {
        binding.apply {
            val name = nameEditText.text.toString()
            lifecycleScope.launch {
                val imageFile = if (currentUriImage != null) {
                    uriToFile(currentUriImage!!, this@EditProfileActivity).reduceImageFile().path
                } else {
                    lastUpdatedData.profilePictureUrl?.let {
                        val imageUrl =
                            "https://storage.googleapis.com/bucket-adoptify/plantixImagesUser/$it"
                        convertUrlToFile(this@EditProfileActivity, imageUrl)?.path ?: it
                    }
                }

                val finalImage = imageFile ?: ""
                val data = DetailUser(
                    fullName = name,
                    profilePictureUrl = finalImage
                )
                userId?.let { profileViewModel.updateProfileUser(it, data) }
            }
        }
    }

    private fun updateResult() {
        profileViewModel.updateProfile.observe(this) {
            when(it) {
                is Resource.Loading -> {showLoading(true)}
                is Resource.Success -> {
                    showLoading(false)
                    handleNotification("Berhasil!", "Berhasil")
                }
                is Resource.Error -> {
                    showLoading(false)
                    handleNotification("Gagal!", "Gagal")
                    Log.d("EditProfileActivity", "error: ${it.message}")
                }
            }
        }
    }

    private fun setupView() {
        binding.apply {
            usernameEditText.apply {
                isFocusable = false
                isFocusableInTouchMode = false
                isClickable = true
                isLongClickable = false
                inputType = InputType.TYPE_NULL
            }
            emailEditText.apply {
                isFocusable = false
                isFocusableInTouchMode = false
                isClickable = true
                isLongClickable = false
                inputType = InputType.TYPE_NULL
            }
            nameEditText.addTextChangedListener(textWatcher)
        }
    }

    private val textWatcher = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        override fun afterTextChanged(s: Editable?) {
            validateForm()
        }
    }

    private fun validateForm() {
        binding.apply {
            val name = nameEditText.text.toString()

            val isNameFilled = name.isNotEmpty()

            val isNameChanged = name != lastUpdatedData.fullName && name.isNotEmpty()
            val isImageChanged = currentUriImage != null

            val isFormValid = isNameFilled && (isNameChanged || isImageChanged)
            btnSave.isEnabled = isFormValid
            btnSave.backgroundTintList = ContextCompat.getColorStateList(
                this@EditProfileActivity,
                if (isFormValid) R.color.primary_color else R.color.btn_disabled
            )
        }
    }

    private fun handleNotification(title: String, status: String) {
        showSuccessBottomSheet(title, status)
        Handler(Looper.getMainLooper()).postDelayed({
            setResult(RESULT_OK)
            finish()
        }, 3000)
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) progressBarDialog() else dismissProgressDialog()
    }


    private fun showSuccessBottomSheet(title: String, status: String) {
        successDialog = BottomSheetDialog(this).apply {
            val view = layoutInflater.inflate(R.layout.bottom_modal_update, null)
            val txtUpload = view.findViewById<TextView>(R.id.descSuccess)
            val titleDialog = view.findViewById<TextView>(R.id.success)
            txtUpload.text = status
            titleDialog.text = title
            setCancelable(false)
            setContentView(view)
            show()
        }
    }

    private fun progressBarDialog() {
        if (progressDialog == null) {
            progressDialog = Dialog(this).apply {
                val view = LayoutInflater.from(this@EditProfileActivity)
                    .inflate(R.layout.loading_dialog, null)
                setContentView(view)
                setCancelable(false)
                window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                val progressBar = view.findViewById<ProgressBar>(R.id.progressBar)
                progressBar.indeterminateTintList = ContextCompat.getColorStateList(
                    this@EditProfileActivity,
                    R.color.primary_color
                )
            }
        }
        progressDialog?.show()
    }

    private fun dismissProgressDialog() {
        progressDialog?.let {
            if (it.isShowing) {
                it.dismiss()
            }
        }
    }

    override fun onDestroy() {
        dismissProgressDialog()
        successDialog?.dismiss()
        super.onDestroy()
    }

    override fun onPause() {
        dismissProgressDialog()
        successDialog?.dismiss()
        super.onPause()
    }
}