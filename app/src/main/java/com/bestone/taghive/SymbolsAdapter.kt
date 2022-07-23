package com.bestone.taghive

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bestone.taghive.databinding.ItemSymbolsLayoutBinding
import com.bestone.taghive.response.SymbolsResponse
import com.bestone.taghive.response.SymbolsResponseItem
import java.util.*
import kotlin.math.roundToInt

class SymbolsAdapter : RecyclerView.Adapter<SymbolsAdapter.ViewHolder>() {
    var itemList: SymbolsResponse = SymbolsResponse()
    lateinit var onItemClickListener: OnItemClickListener
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_symbols_layout, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindData(itemList[position])
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    fun setList(itemList: SymbolsResponse) {
        this.itemList = itemList
        notifyDataSetChanged()
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var itemBinding: ItemSymbolsLayoutBinding = DataBindingUtil.bind(itemView.rootView)!!
        fun bindData(model: SymbolsResponseItem) {
            val rnd = Random()
            val color = Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256))
            itemBinding.tvSymbol.setTextColor(color)
            itemBinding.tvSymbol.text = model.symbol.subSequence(0, 1)
            itemBinding.tvSymbolName.text = model.symbol
            itemBinding.tvLastPrice.text = "₹ ${model.lastPrice}"
            itemBinding.tvHighPrice.text = "₹ ${model.highPrice}"
            itemBinding.tvLowPrice.text = "₹ ${model.lowPrice}"
            itemBinding.tvQuoteAsset.text = "₹ ${model.quoteAsset}"
            var bidPercentage =
                ((model.lastPrice.toDouble() - model.openPrice.toDouble()) / model.openPrice.toDouble()) * 100
            if (bidPercentage > 0) {
                itemBinding.tvPercentage.text = "+${(bidPercentage * 100.0).roundToInt() / 100.0}%"
                itemBinding.tvPercentage.setTextColor(Color.parseColor("#4CAF50"))
            } else {
                itemBinding.tvPercentage.text = "${(bidPercentage * 100.0).roundToInt() / 100.0}%"
                itemBinding.tvPercentage.setTextColor(Color.parseColor("#FF0000"))
            }
            var bidDiffer = if (model.lastPrice.toDouble() > model.openPrice.toDouble()) {
                model.lastPrice.toDouble() - model.openPrice.toDouble()
            } else {
                model.openPrice.toDouble() - model.lastPrice.toDouble()
            }
            if (bidDiffer > 0) {
                itemBinding.tvDiffer.text = "+ ₹${(bidDiffer * 100.0).roundToInt() / 100.0}"
            } else {
                itemBinding.tvDiffer.text = "- ₹${(bidDiffer * 100.0).roundToInt() / 100.0}"
            }
            itemBinding.root.setOnClickListener {
                onItemClickListener.onItemClick(model.symbol, color)
            }
        }
    }

    interface OnItemClickListener {
        fun onItemClick(symbol: String, color: Int)
    }

    fun setOnClickListener(onItemClickListener: OnItemClickListener) {
        this.onItemClickListener = onItemClickListener
    }
}