package com.pureblacksoft.gradinfo.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import com.pureblacksoft.gradinfo.databinding.CardButtonBinding

class ButtonAdapter(private val context: Context, private val stringList: MutableList<String>) :
    BaseAdapter() {
    override fun getItemId(i: Int): Long = 0

    override fun getItem(i: Int): Any? = null

    override fun getCount(): Int = stringList.size

    @SuppressLint("ViewHolder")
    override fun getView(position: Int, view: View?, parent: ViewGroup?): View {
        val layoutInflater = LayoutInflater.from(context)
        val binding = CardButtonBinding.inflate(layoutInflater, parent, false)
        binding.txtTitleBC.text = stringList[position]

        return binding.root
    }
}

//PureBlack Software / Murat BIYIK