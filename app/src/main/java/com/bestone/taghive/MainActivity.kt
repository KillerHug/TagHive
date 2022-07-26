package com.bestone.taghive

import android.content.Context
import android.net.ConnectivityManager
import android.os.Bundle
import android.view.View
import android.widget.Toast
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
        binding.animationView.visibility = View.VISIBLE
//        call api for get all symbol
        if (Const.checkConnection(this)) {
            viewModel.getAllSymbols()
        } else {
            Toast.makeText(this, "Internet Connection not Found", Toast.LENGTH_SHORT).show()
        }
        initClickListener()
        initObserver()
        binding.swipeRefreshLayout.setOnRefreshListener {
            binding.animationView.visibility = View.VISIBLE
            if (Const.checkConnection(this)) {
                viewModel.getAllSymbols()
            } else {
                Toast.makeText(this, "Internet Connection not Found", Toast.LENGTH_SHORT).show()
            }
            if (binding.swipeRefreshLayout.isRefreshing) {
                binding.swipeRefreshLayout.isRefreshing = false
            }
        }
    }

    private fun initObserver() {
        viewModel.symbolsMutable.observe(this) {
            binding.animationView.visibility = View.GONE
//            set data on adapter
            viewModel.adapter.setList(it)
        }
    }

    private fun initClickListener() {
        viewModel.adapter.setOnClickListener(object : SymbolsAdapter.OnItemClickListener {
            override fun onItemClick(symbol: String, color: Int) {
//                item click listener

                /*startActivity(
                    Intent(
                        this@MainActivity,
                        ShowItemActivity::class.java
                    ).putExtra("symbol", symbol)
                    .putExtra("color", color)
                )*/
                var bundle = Bundle()
                bundle.putString("symbol", symbol)
                bundle.putInt("color", color)
                val fragment = ShowSymbolBottomSheet()
                fragment.arguments = bundle
                fragment.show(supportFragmentManager, fragment.tag)
            }
        })
    }
}