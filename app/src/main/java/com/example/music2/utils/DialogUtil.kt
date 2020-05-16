package com.example.music2.utils

import android.content.Context
import com.afollestad.materialdialogs.MaterialDialog
import com.example.music2.R

fun waringDialog(content : String,context: Context){
    MaterialDialog(context).show{
        title(R.string.warning)
        message(text = content)
        autoDismissEnabled
    }
}

fun successDialog(content : String,context: Context){
    MaterialDialog(context).show{
        title(R.string.success)
        message(text = content)
        autoDismissEnabled
    }
}