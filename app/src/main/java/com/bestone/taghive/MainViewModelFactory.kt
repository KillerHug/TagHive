package com.bestone.taghive

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.bestone.taghive.retrofit.ApiService
class MainViewModelFactory(var apiService: ApiService) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T = when (modelClass) {
        MainViewModel::class.java -> MainViewModel(apiService)
        ShowItemViewModel::class.java -> ShowItemViewModel(apiService)
        else -> throw IllegalArgumentException("Unknown ViewModel class")
    } as T
}