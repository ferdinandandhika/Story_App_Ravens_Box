package com.dicoding.picodiploma.loginwithanimation.database

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.dicoding.picodiploma.loginwithanimation.response.ListStoryItem

@Dao
interface StoryDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertStory(story: List<ListStoryItem>)

    @Query("SELECT * FROM storypage")
    fun getAllStory(): PagingSource<Int, ListStoryItem>

    @Query("DELETE FROM storypage")
    suspend fun deleteAll()
}