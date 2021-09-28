package com.pureblacksoft.gradinfo.function

import android.content.Context
import android.view.Gravity
import android.widget.Toast

class AppFun
{
    companion object {
        fun showToast(context: Context, string: Int, length: Int) {
            val toast = Toast.makeText(context, string, length)
            toast.setGravity(Gravity.BOTTOM, 0, 160)
            toast.show()
        }
    }
}

//PureBlack Software / Murat BIYIK