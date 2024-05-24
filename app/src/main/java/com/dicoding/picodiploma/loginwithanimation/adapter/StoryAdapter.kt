package com.dicoding.picodiploma.loginwithanimation.adapter


import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.app.ActivityOptionsCompat
import androidx.core.util.Pair
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dicoding.picodiploma.loginwithanimation.R
import com.dicoding.picodiploma.loginwithanimation.online.model.ListStoryItem
import com.dicoding.picodiploma.loginwithanimation.view.detail.DetailActivity
import androidx.paging.PagingDataAdapter

class StoryAdapter : PagingDataAdapter<ListStoryItem, StoryAdapter.ViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_row, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val story = getItem(position)
        story?.let {
            holder.bind(it)
        }
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val profileImageView: ImageView = view.findViewById(R.id.profileImageView)
        private val nameTextView: TextView = view.findViewById(R.id.nameTextView)
        private val descTextView: TextView = view.findViewById(R.id.descTextView)

        fun bind(story: ListStoryItem) {
            nameTextView.text = story.name
            descTextView.text = story.description
            Glide.with(itemView.context)
                .load(story.photoUrl)
                .circleCrop()
                .into(profileImageView)

            itemView.setOnClickListener {
                val context = itemView.context
                val intent = Intent(context, DetailActivity::class.java)
                intent.putExtra("Story", story)

                val options = ActivityOptionsCompat.makeSceneTransitionAnimation(
                    context as Activity,
                    Pair(profileImageView, "profileImage"),
                    Pair(nameTextView, "name"),
                    Pair(descTextView, "description")
                )

                context.startActivity(intent, options.toBundle())
            }
        }
    }

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ListStoryItem>() {
            override fun areItemsTheSame(oldItem: ListStoryItem, newItem: ListStoryItem): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: ListStoryItem, newItem: ListStoryItem): Boolean {
                return oldItem == newItem
            }
        }
    }
}
