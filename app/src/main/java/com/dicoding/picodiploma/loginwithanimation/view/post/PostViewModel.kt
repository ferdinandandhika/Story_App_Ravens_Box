package com.dicoding.picodiploma.loginwithanimation.view.post

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.dicoding.picodiploma.loginwithanimation.datapaging.StoryRepository
import com.dicoding.picodiploma.loginwithanimation.adapter.ResultState
import kotlinx.coroutines.Dispatchers
import java.io.File

class PostViewModel(private val repository: StoryRepository) : ViewModel() {

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading

    private val _isError = MutableLiveData<String>()
    val isError: LiveData<String> get() = _isError


    fun uploadImage(file: File, description: String, token: String) = liveData(Dispatchers.IO) {
        emit(ResultState.Loading)
        try {
            val response = repository.uploadImage(file, description, token)
            emit(ResultState.Success(response))
        } catch (e: Exception) {
            emit(ResultState.Error(e.message ?: "An error occurred"))
        }
    }
}
