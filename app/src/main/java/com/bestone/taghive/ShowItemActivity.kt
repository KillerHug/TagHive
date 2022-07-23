package com.bestone.taghive

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.bestone.taghive.databinding.ActivityShowItemBinding
import com.bestone.taghive.retrofit.ApiService
import com.bestone.taghive.retrofit.RetrofitBuilder
import kotlin.math.roundToInt

class ShowItemActivity : AppCompatActivity() {
    lateinit var binding: ActivityShowItemBinding
    lateinit var viewModel: ShowItmeViewModel
    lateinit var factory: MainViewModelFactory
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_show_item)
        var apiService = RetrofitBuilder.getInstance().create(ApiService::class.java)
        factory = MainViewModelFactory(apiService)
        viewModel = ViewModelProvider(this, factory)[ShowItmeViewModel::class.java]
        binding.viewmodel = viewModel
        if (intent != null) {
            viewModel.getSymbolItem(intent.getStringExtra("symbol"))
        }
        initObserver()
    }

    private fun initObserver() {
        viewModel.symbolsMutable.observe(this) {
            binding.tvSymbol.setTextColor(intent.getIntExtra("color", 0))
            binding.tvSymbol.text = it.symbol.subSequence(0, 1)
            binding.tvSymbolName.text = it.symbol
            binding.tvLastPrice.text = "₹ ${it.lastPrice}"
            binding.tvHighPrice.text = "₹ ${it.highPrice}"
            binding.tvLowPrice.text = "₹ ${it.lowPrice}"
            binding.tvBidPrice.text = "₹ ${it.bidPrice}"
            binding.tvAskPrice.text = "₹ ${it.askPrice}"
            binding.tvVol.text = "${it.volume}"
            binding.tvBaseAssets.text = "${it.baseAsset}"
            var bidPercentage =
                ((it.lastPrice.toDouble() - it.openPrice.toDouble()) / it.openPrice.toDouble()) * 100
            if (bidPercentage > 0) {
                binding.tvPercentage.text = "+${(bidPercentage * 100.0).roundToInt() / 100.0}%"
                binding.tvPercentage.setTextColor(Color.parseColor("#4CAF50"))
            } else {
                binding.tvPercentage.text = "${(bidPercentage * 100.0).roundToInt() / 100.0}%"
                binding.tvPercentage.setTextColor(Color.parseColor("#FF0000"))
            }
        }
    }
}