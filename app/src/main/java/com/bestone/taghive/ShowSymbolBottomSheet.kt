package com.bestone.taghive

import android.app.Dialog
import android.content.DialogInterface
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.bestone.taghive.databinding.ShowSymbolBottomSheetBinding
import com.bestone.taghive.retrofit.ApiService
import com.bestone.taghive.retrofit.RetrofitBuilder
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlin.math.roundToInt


class ShowSymbolBottomSheet : BottomSheetDialogFragment() {
    lateinit var binding: ShowSymbolBottomSheetBinding
    lateinit var viewModel: ShowItemViewModel
    lateinit var factory: MainViewModelFactory
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val bottomSheetDialog =
            if (activity != null) object : BottomSheetDialog(requireActivity(), theme) {
                override fun onBackPressed() {
                    if (childFragmentManager.backStackEntryCount > 0) {
                        childFragmentManager.popBackStack()
                    } else {
                        super.onBackPressed()
                    }
                }
            } else (super.onCreateDialog(savedInstanceState) as BottomSheetDialog)
        bottomSheetDialog.setOnShowListener { dialog1: DialogInterface ->
            val dialog: BottomSheetDialog = dialog1 as BottomSheetDialog
            dialog.setCanceledOnTouchOutside(true)
        }
        return bottomSheetDialog
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.show_symbol_bottom_sheet, container, false)
        var apiService = RetrofitBuilder.getInstance().create(ApiService::class.java)
        factory = MainViewModelFactory(apiService)
        viewModel = ViewModelProvider(this, factory)[ShowItemViewModel::class.java]
        init()
        initObserver()
        return binding.root
    }

    private fun init() {
        if (arguments != null) {
            viewModel.getSymbolItem(requireArguments().getString("symbol"))
        }
    }

    private fun initObserver() {
        viewModel.symbolsMutable.observe(this) {
            binding.tvSymbol.setTextColor(requireArguments().getInt("color", 0))
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