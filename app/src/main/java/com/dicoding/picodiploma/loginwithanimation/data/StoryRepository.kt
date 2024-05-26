package com.dicoding.picodiploma.loginwithanimation.data

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.liveData
import com.dicoding.picodiploma.loginwithanimation.api.ApiService
import com.dicoding.picodiploma.loginwithanimation.response.ListStoryItem
import com.dicoding.picodiploma.loginwithanimation.response.PostStoryResponse
import com.dicoding.picodiploma.loginwithanimation.response.StoryResponse
import com.dicoding.picodiploma.loginwithanimation.pref.UserModel
import com.dicoding.picodiploma.loginwithanimation.pref.UserPreference
import com.dicoding.picodiploma.loginwithanimation.database.StoryDatabase
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Call
import java.io.File

class StoryRepository(
    private val context: Context,
    private val userPreference: UserPreference,
    private val apiService: ApiService,
    private val storyDatabase: StoryDatabase
){

    fun getSession(): UserModel {
        return userPreference.getUser()
    }

    fun uploadImage(file: File, description: String, token: String?): Call<PostStoryResponse> {
        val descriptionBody = description.toRequestBody("text/plain".toMediaType())
        val requestImageFile = file.asRequestBody("image/jpeg".toMediaType())
        val multipartBody = MultipartBody.Part.createFormData("photo", file.name, requestImageFile)
        return apiService.uploadImage("Bearer $token", multipartBody, descriptionBody)
    }


    fun getListStories(): LiveData<PagingData<ListStoryItem>> {
        @OptIn(ExperimentalPagingApi::class)
        return Pager(
            config = PagingConfig(
                pageSize = 5
            ),
            remoteMediator = StoryRemoteMediator(context, storyDatabase, apiService),
            pagingSourceFactory = {
                storyDatabase.storyDao().getAllStory()
            }
        ).liveData
    }

    companion object {
        @Volatile
        private var instance: StoryRepository? = null

        fun getInstance(context: Context,  userPreference: UserPreference, apiService: ApiService, storyDatabase: StoryDatabase) =
            instance ?: synchronized(this) {
                instance ?: StoryRepository(context, userPreference, apiService, storyDatabase).also { instance = it }
            }
    }
}
