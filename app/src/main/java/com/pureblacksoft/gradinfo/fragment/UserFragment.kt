package com.pureblacksoft.gradinfo.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.pureblacksoft.gradinfo.R
import com.pureblacksoft.gradinfo.activity.MainActivity
import com.pureblacksoft.gradinfo.data.Grad
import com.pureblacksoft.gradinfo.databinding.FragmentUserBinding

class UserFragment : Fragment(R.layout.fragment_user)
{
    companion object {
        lateinit var userGrad: Grad
    }

    private var _context: Context? = null
    private var _activity: MainActivity? = null
    private var _binding: FragmentUserBinding? = null

    private val mContext get() = _context!!
    private val activity get() = _activity!!
    private val binding get() = _binding!!

    private val backCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            val action = UserFragmentDirections.actionGlobalHomeFragment()
            findNavController().navigate(action)
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

        _context = requireContext()
        _activity = requireActivity() as MainActivity
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentUserBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //region Toolbar
        activity.binding.imgToolbarIconMA.setImageResource(R.drawable.ic_user)
        activity.binding.txtToolbarTitleMA.text = getString(R.string.User_Title)
        activity.binding.imgToolbarButtonMA.setImageResource(R.drawable.ic_pref)
        //endregion

        //region Get Data
        binding.incGradCardUF.txtNumberGC.text = userGrad.number.toString()
        binding.incGradCardUF.txtNameGC.text = userGrad.name
        binding.incGradCardUF.txtDegreeGC.text = userGrad.degree
        binding.incGradCardUF.txtYearGC.text = userGrad.year
        Glide.with(this).load(userGrad.image).into(binding.incGradCardUF.imgGradGC)

        binding.incGradCardPrivateUF.txtProfessionGCP.text = userGrad.profession
        binding.incGradCardPrivateUF.txtProvinceGCP.text = userGrad.province
        binding.incGradCardPrivateUF.txtEmailGCP.text = userGrad.email
        binding.incGradCardPrivateUF.txtPhoneGCP.text = userGrad.phone
        binding.incGradCardPrivateUF.txtNoteGCP.text = userGrad.note
        //endregion

        //region Button
        activity.binding.imgToolbarButtonMA.setOnClickListener {
            val action = UserFragmentDirections.actionGlobalPrefFragment()
            findNavController().navigate(action)
        }

        activity.onBackPressedDispatcher.addCallback(activity, backCallback)
        //endregion
    }

    override fun onDestroyView() {
        super.onDestroyView()

        backCallback.remove()

        _binding = null
    }

    override fun onDetach() {
        super.onDetach()

        _context = null
        _activity = null
    }
}

//PureBlack Software / Murat BIYIK