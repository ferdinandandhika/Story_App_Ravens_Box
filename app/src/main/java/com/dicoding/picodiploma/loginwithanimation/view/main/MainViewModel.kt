package com.dicoding.picodiploma.loginwithanimation.view.main


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.dicoding.picodiploma.loginwithanimation.response.ListStoryItem
import com.dicoding.picodiploma.loginwithanimation.data.StoryRepository


class MainViewModel(private val repository: StoryRepository) : ViewModel() {


    private val _listStory = MutableLiveData<List<ListStoryItem>>()
    val listStory: LiveData<List<ListStoryItem>> get() = _listStory

    private val _Message = MutableLiveData<String>()
    val  message: LiveData<String> get() = _Message

    val getListStory: LiveData<PagingData<ListStoryItem>> =
        repository.getListStories().cachedIn(viewModelScope)



    companion object{
        private const val TAG = "MainViewModel"
    }

}