package com.dicoding.picodiploma.loginwithanimation.view.detail

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.dicoding.picodiploma.loginwithanimation.response.ListStoryItem
import com.dicoding.picodiploma.loginwithanimation.databinding.ActivityDetailBinding

class DetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.back1.setOnClickListener { finish() }

        setupData()
    }

    private fun setupData() {
        val story = intent.getParcelableExtra<ListStoryItem>("Story")
        story?.let {
            Glide.with(applicationContext)
                .load(it.photoUrl)
                .into(binding.profileImageView)
            binding.nameTextView.text = it.name
            binding.descTextView.text = it.description
        }
    }
}