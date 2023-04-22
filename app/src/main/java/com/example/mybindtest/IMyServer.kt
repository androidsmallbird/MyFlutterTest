package com.example.mybindtest

import android.app.Application
import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.Build
import android.os.IBinder
import android.os.Parcel
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import kotlin.concurrent.thread

/***
 * @author lcy
 * @date 2023-04-19
 *
 */
interface IMyServer {
    fun say(word: String)
}



class MyServerService : Service() {
    private val stub = object  :IMyServer1.Stub(){

        override fun say(word: String?) {
            Log.d("MyServerBinder_say", word +"  ==ipc:"+ getProcessName() )

        }

        override fun tell(word: String?, age: Int): Int {
            return age +20
        }

    }
    override fun onBind(intent: Intent?): IBinder {
        return stub
    }

}

fun getProcessName():String{
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
        Application.getProcessName()
    } else {
       ""
    }
}