package com.bestone.taghive

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.bestone.taghive.databinding.ActivityMainBinding
import com.bestone.taghive.retrofit.ApiService
import com.bestone.taghive.retrofit.RetrofitBuilder

class MainActivity : AppCompatActivity() {
    lateinit var viewModel: MainViewModel
    lateinit var factory: MainViewModelFactory
    lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        var apiService = RetrofitBuilder.getInstance().create(ApiService::class.java)
        factory = MainViewModelFactory(apiService)
        viewModel = ViewModelProvider(this, factory)[MainViewModel::class.java]
        binding.viewmodel = viewModel
        viewModel.getAllSymbols()
        initClickListener()
        initObserver()
        binding.swipeRefreshLayout.setOnRefreshListener {
            viewModel.getAllSymbols()
            if (binding.swipeRefreshLayout.isRefreshing) {
                binding.swipeRefreshLayout.isRefreshing = false
            }
        }
    }

    private fun initObserver() {
        viewModel.symbolsMutable.observe(this) {
            viewModel.adapter.setList(it)
        }
    }

    private fun initClickListener() {
        viewModel.adapter.setOnClickListener(object : SymbolsAdapter.OnItemClickListener {
            override fun onItemClick(symbol: String, color: Int) {
                startActivity(
                    Intent(
                        this@MainActivity,
                        ShowItemActivity::class.java
                    ).putExtra("symbol", symbol)
                    .putExtra("color", color)
                )
            }
        })
    }
}