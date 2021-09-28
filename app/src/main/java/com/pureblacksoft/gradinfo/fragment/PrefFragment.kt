package com.pureblacksoft.gradinfo.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.pureblacksoft.gradinfo.R
import com.pureblacksoft.gradinfo.activity.MainActivity
import com.pureblacksoft.gradinfo.databinding.FragmentPrefBinding
import com.pureblacksoft.gradinfo.function.PrefFun
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class PrefFragment : Fragment(R.layout.fragment_pref)
{
    private var _context: Context? = null
    private var _activity: MainActivity? = null
    private var _binding: FragmentPrefBinding? = null

    private val mContext get() = _context!!
    private val activity get() = _activity!!
    private val binding get() = _binding!!

    override fun onAttach(context: Context) {
        super.onAttach(context)

        _context = requireContext()
        _activity = requireActivity() as MainActivity
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentPrefBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //region Toolbar
        activity.binding.imgToolbarIconMA.setImageResource(R.drawable.ic_pref)
        activity.binding.txtToolbarTitleMA.text = getString(R.string.Pref_Title)
        activity.binding.imgToolbarButtonMA.setImageResource(R.drawable.ic_close)
        //endregion

        //region Theme Radio Group
        when (PrefFun.currentThemeId) {
            PrefFun.ID_THEME_DEFAULT -> binding.radGrpThemePF.check(binding.radBtnDefaultThemePF.id)
            PrefFun.ID_THEME_LIGHT -> binding.radGrpThemePF.check(binding.radBtnLightThemePF.id)
            PrefFun.ID_THEME_DARK -> binding.radGrpThemePF.check(binding.radBtnDarkThemePF.id)
        }

        binding.radGrpThemePF.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                binding.radBtnDefaultThemePF.id -> changeTheme(PrefFun.ID_THEME_DEFAULT)
                binding.radBtnLightThemePF.id -> changeTheme(PrefFun.ID_THEME_LIGHT)
                binding.radBtnDarkThemePF.id -> changeTheme(PrefFun.ID_THEME_DARK)
            }
        }
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

    private fun changeTheme(themeId: Int) {
        if (PrefFun.currentThemeId != themeId) {
            GlobalScope.launch {
                activity.storeFun.storePref(themeId)
            }
        }
    }
}

//PureBlack Software / Murat BIYIK