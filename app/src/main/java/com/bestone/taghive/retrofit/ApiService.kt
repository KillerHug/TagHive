package com.bestone.taghive.retrofit

import com.bestone.taghive.response.SymbolsResponse
import com.bestone.taghive.response.SymbolsResponseItem
import org.json.JSONObject
import retrofit2.Response
import retrofit2.http.*


interface ApiService {
    @GET("tickers/24hr")
    suspend fun getSymbols(): Response<SymbolsResponse>

    @GET("ticker/24hr")
    suspend fun getSymbolsInfo(@Query("symbol") symbol: String): Response<SymbolsResponseItem>
}