package com.dicoding.picodiploma.loginwithanimation.view.post

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.dicoding.picodiploma.loginwithanimation.api.ApiConfig
import com.dicoding.picodiploma.loginwithanimation.R
import com.dicoding.picodiploma.loginwithanimation.online.model.PostStoryResponse
import com.dicoding.picodiploma.loginwithanimation.datapaging.StoryRepository
import com.dicoding.picodiploma.loginwithanimation.pref.UserPreference
import com.dicoding.picodiploma.loginwithanimation.databinding.ActivityPostBinding
import com.dicoding.picodiploma.loginwithanimation.view.main.MainActivity
import com.google.gson.Gson
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PostActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPostBinding
    private var currentImageUri: Uri? = null

    private val viewModel by viewModels<PostViewModel> {
        ViewModelFactory.getInstance(this)
    }

    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
            if (isGranted) {
                Toast.makeText(this, "Permission request granted", Toast.LENGTH_LONG).show()
            }
        }

    private fun allPermissionsGranted() =
        ContextCompat.checkSelfPermission(
            this,
            REQUIRED_PERMISSION
        ) == PackageManager.PERMISSION_GRANTED



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPostBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (!allPermissionsGranted()) {
            requestPermissionLauncher.launch(REQUIRED_PERMISSION)
        }

        binding.galleryButton.setOnClickListener { startGallery() }
        binding.cameraButton.setOnClickListener { startCamera() }
//        binding.uploadButton.setOnClickListener { uploadImage() }

        binding.back1.setOnClickListener { finish() }

    }


//    private fun uploadImage() {
//        currentImageUri?.let { uri ->
//            val imageFile = uriToFile(uri, this).reduceFileImage()
//            Log.d("Image File", "showImage: ${imageFile.path}")
//            val description = binding.descriptionEditText.text.toString()
//
//            showLoading(true)
//
//            val repository = StoryRepository.getInstance(
//                UserPreference(context = this),
//                ApiConfig().getApiService()
//            )
//
//            val token = UserPreference(this).getUser().token
//
//            repository.uploadImage(imageFile, description, token).enqueue(object : Callback<PostStoryResponse> {
//                override fun onResponse(call: Call<PostStoryResponse>, response: Response<PostStoryResponse>) {
//                    if (response.isSuccessful) {
//                        val successResponse = response.body()
//                        showToast(successResponse?.message ?: "Image uploaded successfully")
//                        goToMain()
//                    } else {
//                        val errorBody = response.errorBody()?.string()
//                        val errorResponse = Gson().fromJson(errorBody, PostStoryResponse::class.java)
//                        showToast(errorResponse.message ?: "Failed to upload image")
//                    }
//                    showLoading(false)
//                }
//
//                override fun onFailure(call: Call<PostStoryResponse>, t: Throwable) {
//                    showToast("Failed to upload image: ${t.message}")
//                    showLoading(false)
//                }
//            })
//
//
//            viewModel.isLoading.observe(this) { isLoading ->
//                binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
//            }
//
//            viewModel.isError.observe(this) { errorMessage ->
//                if (errorMessage.isNotEmpty()) {
//                    Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show()
//                }
//            }
//        } ?: showToast(getString(R.string.empty_image_warning))
//    }

    private fun goToMain() {
        lifecycleScope.launch {
            delay(2000)
            startActivity(Intent(this@PostActivity, MainActivity::class.java))
            finish()
        }
    }

    private fun startGallery() {
        launcherGallery.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
    }

    private val launcherGallery = registerForActivityResult(
        ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        if (uri != null) {
            currentImageUri = uri
            showImage()
        } else {
            Log.d("Photo Picker", "No media selected")
        }
    }

    private fun startCamera() {
        currentImageUri = getImageUri(this)
        launcherIntentCamera.launch(currentImageUri!!)
    }
    private val launcherIntentCamera = registerForActivityResult(
        ActivityResultContracts.TakePicture()
    ) { isSuccess ->
        if (isSuccess) {
            showImage()
        }
    }

    private fun showImage() {
        currentImageUri?.let {
            Log.d("Image URI", "showImage: $it")
            binding.previewImageView.setImageURI(it)
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressIndicator.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    companion object {
        private const val REQUIRED_PERMISSION = Manifest.permission.CAMERA
    }
}
