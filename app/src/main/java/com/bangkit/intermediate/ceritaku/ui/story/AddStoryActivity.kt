package com.bangkit.intermediate.ceritaku.ui.story

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import com.bangkit.intermediate.ceritaku.R
import com.bangkit.intermediate.ceritaku.databinding.ActivityAddStoryBinding
import com.bangkit.intermediate.ceritaku.ui.home.HomeActivity
import com.bangkit.intermediate.ceritaku.ui.viewModels.StoryViewModel
import com.bangkit.intermediate.ceritaku.utils.ApiResult
import com.bangkit.intermediate.ceritaku.utils.Prefs
import com.bangkit.intermediate.ceritaku.utils.getImageUri
import com.bangkit.intermediate.ceritaku.utils.reduceFileImage
import com.bangkit.intermediate.ceritaku.utils.uriToFile
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.io.FileNotFoundException

class AddStoryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddStoryBinding
    private var currentImageUri: Uri? = null
    private val storyViewModel: StoryViewModel by viewModel()

    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                Toast.makeText(this, "Kamera diberikan akses", Toast.LENGTH_LONG).show()
                openCamera()
            } else {
                Toast.makeText(this, "Kamera tidak diberikan akses", Toast.LENGTH_LONG).show()
            }
        }

    private val takePictureCallback = registerForActivityResult(ActivityResultContracts.TakePicture()) { isSuccess ->
        if (isSuccess) {
            displayImage()
        } else {
            Log.d("Camera", "Gagal mengambil gambar")
        }
    }

    private val galleryLauncher = registerForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        if (uri != null) {
            currentImageUri = uri
            displayImage()
        } else {
            Log.d("Photo Picker", "Tidak ada gambar yang dipilih")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddStoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.apply {
            galleryButton.setOnClickListener { openGallery() }
            cameraButton.setOnClickListener { checkCameraPermissionAndOpen() }
            uploadButton.setOnClickListener { uploadImage() }
            mToolbar.apply {
                setSupportActionBar(this)
                setNavigationIcon(R.drawable.ic_arrow_back)
                setNavigationOnClickListener { onBackPressed() }
            }
        }

        if (!allPermissionsGranted()) {
            requestCameraPermission()
        }

        observeViewModel()
    }

    private fun allPermissionsGranted(): Boolean {
        return ContextCompat.checkSelfPermission(
            this,
            REQUIRED_PERMISSION
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun requestCameraPermission() {
        requestPermissionLauncher.launch(REQUIRED_PERMISSION)
    }

    private fun checkCameraPermissionAndOpen() {
        if (allPermissionsGranted()) {
            openCamera()
        } else {
            requestCameraPermission()
        }
    }

    private fun openGallery() {
        galleryLauncher.launch("image/*")
    }

    private fun openCamera() {
        currentImageUri = getImageUri(this)
        currentImageUri?.let {
            takePictureCallback.launch(it)
        } ?: run {
            Toast.makeText(this, "Gagal mengambil gambar", Toast.LENGTH_SHORT).show()
        }
    }

    private fun displayImage() {
        currentImageUri?.let {
            Log.d("Image URI", "displayImage: $it")
            binding.previewImageView.setImageURI(it)
        }
    }

    private fun uploadImage() {
        currentImageUri?.let { uri ->
            try {
                val imageFile = uriToFile(uri, this)?.reduceFileImage()
                if (imageFile == null) {
                    Toast.makeText(this, "Gagal mengambil file gambar", Toast.LENGTH_SHORT).show()
                    return
                }
                val description = binding.edAddCaption.text.toString()
                if (description.isBlank()) {
                    Toast.makeText(this, "Deskripsi tidak boleh kosong", Toast.LENGTH_SHORT).show()
                    return
                }
                val token = Prefs.getToken
                storyViewModel.addNewStory(token, imageFile, description)
            } catch (e: FileNotFoundException) {
                Toast.makeText(this, "File tidak ditemukan", Toast.LENGTH_SHORT).show()
                e.printStackTrace()
            } catch (e: Exception) {
                Toast.makeText(this, "Terjadi kesalahan saat mengunggah gambar", Toast.LENGTH_SHORT).show()
                e.printStackTrace()
            }
        } ?: run {
            Toast.makeText(this, "Pilih gambar terlebih dahulu", Toast.LENGTH_SHORT).show()
        }
    }

    private fun observeViewModel() {
        storyViewModel.addStoryResult.observe(this, Observer { result ->
            when (result) {
                is ApiResult.Loading -> {
                }
                is ApiResult.Success -> {
                    Toast.makeText(this, "Sukses upload story", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this, HomeActivity::class.java))
                }
                is ApiResult.Error -> {
                    Toast.makeText(this, "Gagal upload story", Toast.LENGTH_SHORT).show()
                }
            }
        })
    }

    companion object {
        private const val REQUIRED_PERMISSION = Manifest.permission.CAMERA
    }
}
