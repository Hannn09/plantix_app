package com.example.plantix.ui.scan.detection

import android.app.Dialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.core.data.Resource
import com.example.core.data.source.remote.response.RequestDataDetection
import com.example.core.utils.reduceImageFile
import com.example.core.utils.uriToFile
import com.example.plantix.R
import com.example.plantix.databinding.ActivityScanBinding
import com.example.plantix.ui.auth.login.LoginViewModel
import com.example.plantix.ui.scan.DetectionViewModel
import com.google.android.material.textfield.TextInputEditText
import org.koin.android.viewmodel.ext.android.viewModel
import java.io.File
import java.text.SimpleDateFormat
import java.util.Locale

class ScanActivity : AppCompatActivity() {

    private lateinit var binding: ActivityScanBinding

    private val detectionViewModel: DetectionViewModel by viewModel()

    private val loginViewModel: LoginViewModel by viewModel()

    private var imageCapture: ImageCapture? = null
    private val REQUEST_CODE_PERMISSIONS = 20
    private val REQUIRED_PERMISSIONS = arrayOf("android.permission.CAMERA")
    private var userId: Int? = null
    private var plantName: String? = null
    private var currentUriImage: Uri? = null
    private var source: String? = null
    private var name: String? = null
    private var progressDialog: Dialog? = null
    private var plantId: Int? = null

    private val galleryLauncher = registerForActivityResult(ActivityResultContracts.GetContent()) {
        currentUriImage = it
        try {
            binding.previewImage.setImageURI(currentUriImage)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private val outputDirectory: File by lazy {
        val mediaDir = externalMediaDirs.firstOrNull()?.let {
            File(it, resources.getString(R.string.app_name)).apply { mkdirs() }
        }
        if (mediaDir != null && mediaDir.exists())
            mediaDir else filesDir
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityScanBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (allPermissionAllowed()) {
            startCamera()
        } else {
            ActivityCompat.requestPermissions(this, REQUIRED_PERMISSIONS, REQUEST_CODE_PERMISSIONS)
        }
        observeData()
        setupListener()
        detectionResult()
        createPlantResult()
    }

    private fun allPermissionAllowed() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(baseContext, it) == PackageManager.PERMISSION_GRANTED
    }

    private fun observeData() {
        loginViewModel.getUserId()
        loginViewModel.userId.observe(this) {
            when (it) {
                is Resource.Loading -> {}
                is Resource.Success -> {
                    userId = it.data
                    Log.d("ScanActivity", "userId: $userId")
                }

                is Resource.Error -> {
                    Log.d("ScanActivity", "error: ${it.message}")
                }
            }
        }
        source = intent?.getStringExtra("SOURCE")
        name = intent?.getStringExtra("PLANT_NAME")
        plantId = intent?.getIntExtra("PLANT_ID", 0)
    }

    private fun setupListener() {
        binding.apply {
            bottomButton.icGallery.setOnClickListener { galleryLauncher.launch("image/*") }
            bottomButton.icCatchImage.setOnClickListener { capturePhoto() }
            bottomButton.btnSubmit.setOnClickListener {
                if (source == "fragment") {
                    showDialogPlantName()
                } else {
                    plantId?.let { id -> submitDetection(id) }
                }
            }

            previewImage.addOnLayoutChangeListener { _, _, _, _, _, _, _, _, _ -> validateImage() }
        }
    }

    private fun submitPlant() {
        userId?.let { plantName?.let { plant -> detectionViewModel.createPlant(it, plant) } }
    }

    private fun createPlantResult() {
        detectionViewModel.createPlant.observe(this) {
            when (it) {
                is Resource.Loading -> {
                    showLoading(true)
                    binding.bottomButton.btnSubmit.isEnabled = false
                }

                is Resource.Success -> {
                    showLoading(false)
                    binding.bottomButton.btnSubmit.isEnabled = true
                    val id = it.data.data?.id
                    if (id != null) {
                        submitDetection(id)
                    }
                    Log.d("ScanActivity", "success: $it")
                }

                is Resource.Error -> {
                    showLoading(false)
                    binding.bottomButton.btnSubmit.isEnabled = true
                    Log.d("ScanActivity", "error: ${it.message}")
                }
            }
        }
    }

    private fun submitDetection(id: Int) {
        binding.apply {
            val plantImage =
                currentUriImage?.let { uriToFile(it, this@ScanActivity).reduceImageFile() }?.path
            val requestDetection = RequestDataDetection(
                plantId = id,
                image = plantImage,
                userId = userId
            )
            detectionViewModel.detectionDisease(requestDetection)
        }
    }

    private fun detectionResult() {
        detectionViewModel.detectionResult.observe(this) {
            when (it) {
                is Resource.Loading -> {
                    showLoading(true)
                    binding.bottomButton.btnSubmit.isEnabled = false
                }

                is Resource.Success -> {
                    showLoading(false)
                    binding.bottomButton.btnSubmit.isEnabled = true
                    setResult(RESULT_OK)
                    dialogNotification(
                        "Scan Berhasil",
                        "Yeiyy!",
                        "Tanaman berhasil di deteksi!Anda bisa melihat detail informasinya",
                        R.drawable.scan_success
                    ) {
                        finish()
                    }
                    Log.d("ScanActivity", "success: $it")
                }

                is Resource.Error -> {
                    showLoading(false)
                    binding.bottomButton.btnSubmit.isEnabled = true
                    dialogNotification(
                        "Scan Gagal",
                        "Yaahh!",
                        "Tanaman gagal di deteksi!Silahkan coba lagi nanti",
                        R.drawable.scan_failed
                    )
                    Log.d("ScanActivity", "error: ${it.message}")
                }
            }
        }
    }

    private fun showDialogPlantName() {
        val dialog = Dialog(this)
        dialog.apply {
            requestWindowFeature(Window.FEATURE_NO_TITLE)
            setCancelable(false)
            setContentView(R.layout.dialog_name_plant)
            window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            val width = (resources.displayMetrics.widthPixels * 0.95).toInt()
            val height = WindowManager.LayoutParams.WRAP_CONTENT
            window?.setLayout(width, height)
            val txtNamePlant = dialog.findViewById<TextInputEditText>(R.id.plantNameEditText)
            val btnSubmit = dialog.findViewById<Button>(R.id.btnSubmit)
            val btnClose = dialog.findViewById<ImageView>(R.id.ic_close)
            btnSubmit.setOnClickListener {
                plantName = txtNamePlant.text.toString()
                submitPlant()
                dismiss()
            }
            btnClose.setOnClickListener { dismiss() }
            show()
        }
    }

    private fun startCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(this)
        cameraProviderFuture.addListener({
            val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()

            val preview = Preview.Builder()
                .build()
                .also {
                    it.setSurfaceProvider(binding.cameraPreview.surfaceProvider)
                }
            imageCapture = ImageCapture.Builder().build()

            val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

            try {
                cameraProvider.unbindAll()

                cameraProvider.bindToLifecycle(this, cameraSelector, preview, imageCapture)
            } catch (e: Exception) {
                Log.d("ScanActivity", "error: ${e.message}")
            }
        }, ContextCompat.getMainExecutor(this))
    }

    private fun capturePhoto() {
        val imageCapture = imageCapture ?: return

        val photoFile = File(
            outputDirectory,
            SimpleDateFormat(
                "yyyy-MM-dd-HH-mm-ss-SSS",
                Locale.US
            ).format(System.currentTimeMillis()) + ".jpg"
        )

        val outputOption = ImageCapture.OutputFileOptions.Builder(photoFile).build()

        imageCapture.takePicture(
            outputOption,
            ContextCompat.getMainExecutor(this), object : ImageCapture.OnImageSavedCallback {
                override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
                    currentUriImage = Uri.fromFile(photoFile)
                    binding.previewImage.setImageURI(currentUriImage)
                }

                override fun onError(exception: ImageCaptureException) {
                    Log.e("ScanActivity", "Photo capture failed: ${exception.message}", exception)
                }
            }
        )
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) progressBarDialog() else dismissProgressDialog()
    }

    private fun validateImage() {
        binding.apply {
            val isImageEmpty = previewImage.drawable != null
            bottomButton.btnSubmit.isEnabled = isImageEmpty
            bottomButton.btnSubmit.backgroundTintList = ContextCompat.getColorStateList(
                this@ScanActivity,
                if (isImageEmpty) R.color.primary_color else R.color.btn_disabled
            )
        }
    }


    private fun progressBarDialog() {
        if (progressDialog == null) {
            progressDialog = Dialog(this).apply {
                val view =
                    LayoutInflater.from(this@ScanActivity).inflate(R.layout.loading_dialog, null)
                setContentView(view)
                setCancelable(false)
                window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
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

    private fun dialogNotification(
        title: String,
        descDialog: String,
        desc: String,
        image: Int,
        onDismiss: () -> Unit = {}
    ) {
        val dialog = Dialog(this)
        dialog.apply {
            requestWindowFeature(Window.FEATURE_NO_TITLE)
            setCancelable(false)
            setContentView(R.layout.notification_scan_dialog)

            window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            val width = (resources.displayMetrics.widthPixels * 0.95).toInt()
            val height = WindowManager.LayoutParams.WRAP_CONTENT
            window?.setLayout(width, height)

            val imageView = dialog.findViewById<ImageView>(R.id.img_alert)
            val descNotification = dialog.findViewById<TextView>(R.id.title)
            val titleDialog = dialog.findViewById<TextView>(R.id.title_dialog)
            val descText = dialog.findViewById<TextView>(R.id.desc)
            val btnClose = dialog.findViewById<Button>(R.id.btnClose)

            imageView.setImageDrawable(ContextCompat.getDrawable(this@ScanActivity, image))
            titleDialog.text = title
            descNotification.text = descDialog
            descText.text = desc
            btnClose.setOnClickListener {
                onDismiss()
                dismiss()
            }
            show()
        }
    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (allPermissionAllowed()) {
                startCamera()
            } else {
                Toast.makeText(this, "Permission not granted by the user", Toast.LENGTH_SHORT)
                    .show()
                finish()
            }
        }
    }

    override fun onDestroy() {
        dismissProgressDialog()
        super.onDestroy()
    }

    override fun onPause() {
        dismissProgressDialog()
        super.onPause()
    }
}