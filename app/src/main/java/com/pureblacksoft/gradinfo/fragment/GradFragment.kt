package com.pureblacksoft.gradinfo.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.pureblacksoft.gradinfo.R
import com.pureblacksoft.gradinfo.activity.MainActivity
import com.pureblacksoft.gradinfo.data.Grad
import com.pureblacksoft.gradinfo.databinding.FragmentGradBinding

class GradFragment : Fragment(R.layout.fragment_grad)
{
    companion object {
        lateinit var accessedGrad: Grad
    }

    private var _context: Context? = null
    private var _activity: MainActivity? = null
    private var _binding: FragmentGradBinding? = null

    private val mContext get() = _context!!
    private val activity get() = _activity!!
    private val binding get() = _binding!!

    override fun onAttach(context: Context) {
        super.onAttach(context)

        _context = requireContext()
        _activity = requireActivity() as MainActivity
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentGradBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //region Toolbar
        activity.binding.imgToolbarIconMA.setImageResource(R.drawable.ic_grad)
        activity.binding.txtToolbarTitleMA.text = getString(R.string.Grad_Title)
        activity.binding.imgToolbarButtonMA.setImageResource(R.drawable.ic_close)
        //endregion

        //region Get Data
        binding.incGradCardGF.txtNumberGC.text = accessedGrad.number.toString()
        binding.incGradCardGF.txtNameGC.text = accessedGrad.name
        binding.incGradCardGF.txtDegreeGC.text = accessedGrad.degree
        binding.incGradCardGF.txtYearGC.text = accessedGrad.year
        Glide.with(this).load(accessedGrad.image).into(binding.incGradCardGF.imgGradGC)

        binding.incGradCardPrivateGF.txtProfessionGCP.text = accessedGrad.profession
        binding.incGradCardPrivateGF.txtProvinceGCP.text = accessedGrad.province
        binding.incGradCardPrivateGF.txtEmailGCP.text = accessedGrad.email
        binding.incGradCardPrivateGF.txtPhoneGCP.text = accessedGrad.phone
        binding.incGradCardPrivateGF.txtNoteGCP.text = accessedGrad.note
        //endregion

        //region Button
        activity.binding.imgToolbarButtonMA.setOnClickListener {
            activity.onBackPressed()
        }
        //endregion
    }

    override fun onDestroyView() {
        super.onDestroyView()

        _binding = null
    }

    override fun onDetach() {
        super.onDetach()

        _context = null
        _activity = null
    }
}

//PureBlack Software / Murat BIYIK