package com.dicoding.picodiploma.loginwithanimation.data

import android.content.Context
import com.dicoding.picodiploma.loginwithanimation.api.ApiConfig
import com.dicoding.picodiploma.loginwithanimation.datapaging.StoryRepository
import com.dicoding.picodiploma.loginwithanimation.pref.UserPreference
import com.project.storyappproject.data.datapaging.database.StoryDatabase

object Injection {
    fun provideRepository(context: Context): StoryRepository {
        val database = StoryDatabase.getDatabase(context)
        val userPreference = UserPreference(context)
        val apiService = ApiConfig().getApiService()
        return StoryRepository(context, userPreference, apiService, database)
    }
}
