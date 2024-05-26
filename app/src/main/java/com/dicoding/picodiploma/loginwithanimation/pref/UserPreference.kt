package com.dicoding.picodiploma.loginwithanimation.pref

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "session")

class UserPreference(context: Context ) {

    private val preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)

    fun setLogin(value: UserModel) {
        val editor = preferences.edit()
        editor.putString(NAME, value.name)
        editor.putString(USER_ID, value.userId)
        editor.putString(TOKEN, value.token)
        editor.apply()
    }

    fun getUser(): UserModel {
        val name = preferences.getString(NAME, null)
        val userId = preferences.getString(USER_ID, null)
        val token = preferences.getString(TOKEN, null)
        return UserModel(userId, name, token)
    }

    fun clearUser() {
        val editor = preferences.edit().clear()
        editor.clear()
        editor.apply()
    }

    companion object {
        private const val PREF_NAME = "userPref"
        private const val NAME = "name"
        private const val USER_ID = "userId"
        private const val TOKEN = "token"

    }
}