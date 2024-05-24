package com.dicoding.picodiploma.loginwithanimation.view.signup

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dicoding.picodiploma.loginwithanimation.api.ApiConfig
import com.dicoding.picodiploma.loginwithanimation.online.model.RegisterResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SignUpViewModel : ViewModel() {

    private val _isSuccess = MutableLiveData<Boolean>()
    val isSuccess: LiveData<Boolean> = _isSuccess

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading

    private val _isError = MutableLiveData<String>()
    val isError: LiveData<String> get() = _isError

    private val _registerResult = MutableLiveData<RegisterResponse>()
    val registerResult: LiveData<RegisterResponse> = _registerResult

    fun register(name: String, email: String, password: String) {
        _isLoading.value = true
        _isError.value = ""

        ApiConfig().getApiService()
            .postRegister(name, email, password)
            .enqueue(object : Callback<RegisterResponse> {
                override fun onResponse(
                    call: Call<RegisterResponse>,
                    response: Response<RegisterResponse>
                ) {
                    if (response.isSuccessful) {
                        _registerResult.value = response.body()
                        _isLoading.value = false
                        _isSuccess.value = true
                        Log.d("register", "${response.message()}")
                    } else {
                        _isError.value = "Registration failed: ${response.message()}"
                        _isSuccess.value = false
                        _isLoading.value = false
                        Log.d("register", "Registration failed: ${response.message()}")
                    }
                }

                override fun onFailure(call: Call<RegisterResponse>, t: Throwable) {
                    _isLoading.value = false
                    _isError.value = "Error: ${t.message}"
                    _isSuccess.value = false
                    Log.d("register", "${t.message}")
                }
            })
    }
}
