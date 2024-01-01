package com.example.roomdatabase.utility

import android.content.Context
import android.widget.Toast
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class Constants {

}

 fun showToast(context: Context, message:String){
     CoroutineScope(Dispatchers.Main).launch {
         Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
     }

}

var isOnBackPress = false
var isOtpVarify = false

const val COUNTDOWN_INTERVAL = 1000L
const val COUNTDOWN_DURATION = 55000L

