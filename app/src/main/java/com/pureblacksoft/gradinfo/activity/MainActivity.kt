package com.pureblacksoft.gradinfo.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.asLiveData
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.pureblacksoft.gradinfo.R
import com.pureblacksoft.gradinfo.databinding.ActivityMainBinding
import com.pureblacksoft.gradinfo.dialog.InfoDialog
import com.pureblacksoft.gradinfo.function.PrefFun
import com.pureblacksoft.gradinfo.function.StoreFun
import com.pureblacksoft.gradinfo.service.DataService

class MainActivity : AppCompatActivity()
{
    companion object {
        private var dataLoaded = false

        var onSuccessfulService: (() -> Unit)? = null
    }

    lateinit var binding: ActivityMainBinding
    lateinit var storeFun: StoreFun
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setTheme(R.style.Theme_Gradinfo)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        storeFun = StoreFun(this)
        observeData()

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.containerMA) as NavHostFragment
        navController = navHostFragment.findNavController()

        //region Bottom Navigation
        binding.bottomNavMA.setupWithNavController(navController)
        binding.bottomNavMA.itemIconTintList = null
        //endregion

        //region Load Data
        if (!dataLoaded) {
            startDataService()
            dataLoaded = true
        }
        //endregion

        //region Event
        DataService.onSuccess = {
            binding.spinKitMA.visibility = View.GONE

            onSuccessfulService?.invoke()
        }

        DataService.onFailure = {
            binding.spinKitMA.visibility = View.GONE

            //region Conn Fail Dialog
            val builder = InfoDialog.alertBuilder(this)
            builder.setCancelable(false)
            builder.setTitle(R.string.Main_Conn_Fail_Title)
            builder.setMessage(R.string.Main_Conn_Fail_Content)
            builder.setPositiveButton(R.string.Main_Conn_Fail_Button) { _, _ ->
                startDataService()
            }
            builder.show()
            //endregion
        }
        //endregion
    }

    private fun observeData() {
        storeFun.themeIdFlow.asLiveData().observe(this, {
            PrefFun.currentThemeId = it
            PrefFun.setAppTheme()
        })
    }

    fun startDataService() {
        binding.spinKitMA.visibility = View.VISIBLE

        val intent = Intent(this, DataService::class.java)
        DataService.enqueueWork(this, intent)
    }
}

//PureBlack Software / Murat BIYIK