package com.pureblacksoft.gradinfo.dialog

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import com.pureblacksoft.gradinfo.databinding.DialogCriteriaBinding

class CriteriaDialog(context: Context) : Dialog(context)
{
    companion object {
        var currentCriteriaId = 0

        var onChange: (() -> Unit)? = null
        var onNoChange: (() -> Unit)? = null
    }

    private lateinit var binding: DialogCriteriaBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DialogCriteriaBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //region Button
        binding.txtNameCD.setOnClickListener {
            if (currentCriteriaId != 0) {
                currentCriteriaId = 0

                onChange?.invoke()
            } else {
                onNoChange?.invoke()
            }
        }

        binding.txtNumberCD.setOnClickListener {
            if (currentCriteriaId != 1) {
                currentCriteriaId = 1

                onChange?.invoke()
            } else {
                onNoChange?.invoke()
            }
        }
        //endregion
    }
}

//PureBlack Software / Murat BIYIK