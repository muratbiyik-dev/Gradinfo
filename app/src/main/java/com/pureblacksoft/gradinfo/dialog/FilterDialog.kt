package com.pureblacksoft.gradinfo.dialog

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import com.pureblacksoft.gradinfo.adapter.ButtonAdapter
import com.pureblacksoft.gradinfo.databinding.DialogFilterBinding
import com.pureblacksoft.gradinfo.service.DataService

class FilterDialog(context: Context) : Dialog(context), AdapterView.OnItemSelectedListener
{
    companion object {
        var filterActive = false

        var currentDegreeId = 0
        var currentYearId = 0

        var onCancel: (() -> Unit)? = null
        var onApply: (() -> Unit)? = null
    }

    private lateinit var binding: DialogFilterBinding
    private var selectedDegreeId = 0
    private var selectedYearId = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DialogFilterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //region Spinner
        binding.spnDegreeFD.onItemSelectedListener = this
        val degreeAdapter = ButtonAdapter(context, DataService.degreeList)
        binding.spnDegreeFD.adapter = degreeAdapter

        binding.spnYearFD.onItemSelectedListener = this
        val yearAdapter = ButtonAdapter(context, DataService.yearList)
        binding.spnYearFD.adapter = yearAdapter
        //endregion

        //region Button
        binding.txtDegreeFD.setOnClickListener {
            binding.spnDegreeFD.performClick()
        }

        binding.txtYearFD.setOnClickListener {
            binding.spnYearFD.performClick()
        }

        binding.txtCancelFD.setOnClickListener {
            onCancel?.invoke()
        }

        binding.txtApplyFD.setOnClickListener {
            filterActive = true
            currentDegreeId = selectedDegreeId
            currentYearId = selectedYearId

            onApply?.invoke()
        }
        //endregion
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        selectedDegreeId = binding.spnDegreeFD.selectedItemPosition
        binding.txtDegreeFD.text = DataService.degreeList[binding.spnDegreeFD.selectedItemPosition]

        selectedYearId = binding.spnYearFD.selectedItemPosition
        binding.txtYearFD.text = DataService.yearList[binding.spnYearFD.selectedItemPosition]
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {}

    override fun show() {
        super.show()

        binding.spnDegreeFD.setSelection(currentDegreeId)
        binding.spnYearFD.setSelection(currentYearId)
    }
}

//PureBlack Software / Murat BIYIK