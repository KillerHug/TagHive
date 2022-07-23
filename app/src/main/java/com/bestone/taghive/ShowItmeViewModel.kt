package com.bestone.taghive

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.bestone.taghive.retrofit.ApiService
import com.bestone.taghive.response.SymbolsResponseItem
import kotlinx.coroutines.*

class ShowItmeViewModel(var apiService: ApiService) : ViewModel() {
    val viewModelJob = Job()
    var symbolsMutable = MutableLiveData<SymbolsResponseItem>()
    private val coroutineScope = CoroutineScope(Dispatchers.IO + viewModelJob)

    suspend fun getSymbols(symbol: String?) {
        var job = CoroutineScope(Dispatchers.Main).async {
            apiService.getSymbolsInfo(symbol!!)
        }
        if (job.await().isSuccessful) {
            symbolsMutable.postValue(job.await().body()!!)
        }
    }

    fun getSymbolItem(symbol: String?) {
        coroutineScope.launch {
            getSymbols(symbol)
        }
    }
}