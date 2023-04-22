package com.example.mybindtest

import android.app.Service
import android.content.Intent
import android.os.*
import android.util.Log

/***
 * @author lcy
 * @date 2023-04-19
 *
 */
class MyMessageService : Service() {
    private val handler = object : Handler(Looper.getMainLooper()) {
        override fun handleMessage(msg: Message) {
            val bundle = msg.data
            val id = bundle.getInt("id")
            Log.d("MessageService", "handleMessage_id=$id ")
            val replyMessenger = msg.replyTo
            replyMessenger?.let {
                val message = Message.obtain()
                message.data = Bundle().apply {
                    putInt("id", 333)
                    putString("name", "张三")
                }
                it.send(message)
            }
        }
    }
    private val binder = Messenger(handler).binder

    override fun onBind(intent: Intent?): IBinder? {
        return binder
    }
}