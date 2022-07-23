package com.bestone.taghive

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.bestone.taghive.retrofit.ApiService
import com.bestone.taghive.SymbolsAdapter
import com.bestone.taghive.response.SymbolsResponse
import kotlinx.coroutines.*

class MainViewModel(var apiService: ApiService) : ViewModel() {
    var etMobileMutable = MutableLiveData<String>()
    var isSuccess = MutableLiveData<Boolean>()
    val viewModelJob = Job()
    var adapter = SymbolsAdapter()
    var symbolsMutable=MutableLiveData<SymbolsResponse>()
    private val coroutineScope = CoroutineScope(Dispatchers.IO + viewModelJob)

    suspend fun getSymbols() {
        var job = CoroutineScope(Dispatchers.Main).async {
            apiService.getSymbols()
        }
//        wait for api response
        if (job.await().isSuccessful) {
            symbolsMutable.postValue(job.await().body()!!)
        }
    }

    fun getAllSymbols() {
        coroutineScope.launch {
            getSymbols()
        }
    }
}