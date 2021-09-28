package com.pureblacksoft.gradinfo.dialog

import android.content.Context
import android.view.ContextThemeWrapper
import androidx.appcompat.app.AlertDialog
import com.pureblacksoft.gradinfo.R

class InfoDialog
{
    companion object {
        fun alertBuilder(context: Context): AlertDialog.Builder {
            return AlertDialog.Builder(ContextThemeWrapper(context, R.style.PureDialog_Alert))
        }
    }
}

//PureBlack Software / Murat BIYIK