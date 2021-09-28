package com.pureblacksoft.gradinfo.fragment

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.pureblacksoft.gradinfo.R
import com.pureblacksoft.gradinfo.activity.MainActivity
import com.pureblacksoft.gradinfo.databinding.FragmentLoginBinding
import com.pureblacksoft.gradinfo.function.AppFun
import com.pureblacksoft.gradinfo.service.LoginService

class LoginFragment : Fragment(R.layout.fragment_login)
{
    private var _context: Context? = null
    private var _activity: MainActivity? = null
    private var _binding: FragmentLoginBinding? = null

    private val mContext get() = _context!!
    private val activity get() = _activity!!
    private val binding get() = _binding!!

    override fun onAttach(context: Context) {
        super.onAttach(context)

        _context = requireContext()
        _activity = requireActivity() as MainActivity
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //region Toolbar
        activity.binding.imgToolbarIconMA.setImageResource(R.drawable.ic_user)
        activity.binding.txtToolbarTitleMA.text = getString(R.string.User_Title)
        activity.binding.imgToolbarButtonMA.setImageResource(R.drawable.ic_pref)
        //endregion

        //region User Control
        fun navigateToUserFragment() {
            UserFragment.userGrad = LoginService.userGrad!!

            val action = LoginFragmentDirections.actionLoginFragmentToUserFragment()
            findNavController().navigate(action)
        }

        if (LoginService.userGrad != null) {
            navigateToUserFragment()
        }
        //endregion

        //region Event
        LoginService.onSuccess = {
            navigateToUserFragment()
        }

        LoginService.onFailure = {
            if (LoginService.wrongParam) {
                AppFun.showToast(mContext, R.string.Login_Wrong_Param, Toast.LENGTH_LONG)
            } else {
                AppFun.showToast(mContext, R.string.Login_Fail, Toast.LENGTH_LONG)
            }
        }
        //endregion

        //region Button
        activity.binding.imgToolbarButtonMA.setOnClickListener {
            val action = LoginFragmentDirections.actionGlobalPrefFragment()
            findNavController().navigate(action)
        }

        binding.txtLoginLF.setOnClickListener {
            val userNumber = binding.edtNumberLF.text.toString()
            val userPassword = binding.edtPasswordLF.text.toString()

            if (userNumber.length == 10 && userPassword.length >= 6) {
                startLoginService(userNumber, userPassword)
            } else {
                AppFun.showToast(mContext, R.string.Login_Wrong_Param, Toast.LENGTH_LONG)
            }
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

    private fun startLoginService(userNumber: String, userPassword: String) {
        val intent = Intent(mContext, LoginService::class.java)
        intent.putExtra("user_number", userNumber)
        intent.putExtra("user_password", userPassword)
        LoginService.enqueueWork(mContext, intent)
    }
}

//PureBlack Software / Murat BIYIK