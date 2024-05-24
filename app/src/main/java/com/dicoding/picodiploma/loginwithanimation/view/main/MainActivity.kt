package com.dicoding.picodiploma.loginwithanimation.view.main

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.WindowInsets
import android.view.WindowManager
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dicoding.picodiploma.loginwithanimation.MapsActivity
import com.dicoding.picodiploma.loginwithanimation.R
import com.dicoding.picodiploma.loginwithanimation.adapter.StoryAdapter
import com.dicoding.picodiploma.loginwithanimation.api.ApiConfig
import com.dicoding.picodiploma.loginwithanimation.data.Injection
import com.dicoding.picodiploma.loginwithanimation.online.model.StoryResponse
import com.dicoding.picodiploma.loginwithanimation.datapaging.StoryRepository
import com.dicoding.picodiploma.loginwithanimation.pref.UserPreference
import com.dicoding.picodiploma.loginwithanimation.databinding.ActivityMainBinding
import com.dicoding.picodiploma.loginwithanimation.view.post.PostActivity
import com.dicoding.picodiploma.loginwithanimation.view.post.ViewModelFactory
import com.dicoding.picodiploma.loginwithanimation.view.welcome.WelcomeActivity
import com.project.storyappproject.data.datapaging.database.StoryDatabase
import com.scwang.smart.refresh.footer.BallPulseFooter
import com.scwang.smart.refresh.header.MaterialHeader
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.awaitResponse

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val viewModel: MainViewModel by viewModels { ViewModelFactory(Injection.provideRepository(this)) }
    private lateinit var storyAdapter: StoryAdapter
//
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupView()
        setupRecyclerView()
        setupObservers()
    }

    private fun setupView() {
        supportActionBar?.hide()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        }

        binding.refreshLayout.setRefreshHeader(MaterialHeader(this))
        binding.refreshLayout.setRefreshFooter(BallPulseFooter(this))

        binding.logoutButton.setOnClickListener { showLogoutDialog() }
        binding.mapsButton.setOnClickListener { startActivity(Intent(this, MapsActivity::class.java)) }
        binding.addPost.setOnClickListener { startActivity(Intent(this, PostActivity::class.java)) }

        binding.refreshLayout.setOnRefreshListener { refreshLayout ->
            storyAdapter.refresh()
            refreshLayout.finishRefresh()
        }

        binding.refreshLayout.setOnLoadMoreListener { refreshLayout ->
            refreshLayout.finishLoadMore()
        }
    }

    private fun setupRecyclerView() {
        storyAdapter = StoryAdapter()
        binding.listProfileStory.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = storyAdapter
        }
    }

    private fun setupObservers() {
        viewModel.getListStory.observe(this) { pagingData ->
            storyAdapter.submitData(lifecycle, pagingData)
        }
    }

    private fun showLogoutDialog() {
        AlertDialog.Builder(this)
            .setTitle("Logout")
            .setMessage("Are you sure you want to logout?")
            .setPositiveButton("Yes") { _, _ -> logout() }
            .setNegativeButton("No", null)
            .show()
    }

    private fun logout() {
        val userPref = UserPreference(this)
        userPref.clearUser()
        startActivity(Intent(this, WelcomeActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        })
        finish()
    }
}
