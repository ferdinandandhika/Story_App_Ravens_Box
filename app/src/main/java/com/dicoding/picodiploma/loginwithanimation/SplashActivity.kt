package com.dicoding.picodiploma.loginwithanimation

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import com.dicoding.picodiploma.loginwithanimation.pref.UserModel
import com.dicoding.picodiploma.loginwithanimation.pref.UserPreference
import com.dicoding.picodiploma.loginwithanimation.view.main.MainActivity
import com.dicoding.picodiploma.loginwithanimation.view.welcome.WelcomeActivity

class SplashActivity : AppCompatActivity() {
    private lateinit var userPreference: UserPreference
    private lateinit var userModel: UserModel
    private val splash_delay = 2000L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        userPreference = UserPreference(this)
        userModel =userPreference.getUser()
        intentHandler()
    }

    private fun intentHandler() {
        if (userModel.name != null && userModel.userId != null && userModel.token != null) {
            val intent = Intent(this, MainActivity::class.java)
            Handler(Looper.getMainLooper()).postDelayed({
                startActivity(intent)
                finish()
            }, splash_delay)
        } else {
            val intent = Intent(this, WelcomeActivity::class.java)
            Handler(Looper.getMainLooper()).postDelayed({
                startActivity(intent)
                finish()
            }, splash_delay)
        }
    }
}