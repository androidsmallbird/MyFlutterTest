package com.example.mybindtest

import android.app.Application
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.*
import androidx.appcompat.app.AppCompatActivity
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import kotlin.concurrent.thread
import kotlin.math.log

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        findViewById<TextView>(R.id.btn).setOnClickListener {
            bindService(
                Intent(this, MyServerService::class.java),
                connection,
                Context.BIND_AUTO_CREATE
            )
        }
        findViewById<TextView>(R.id.btn_2).setOnClickListener {
            bindService(
                Intent(this, MyMessageService::class.java),
                messageConnection,
                Context.BIND_AUTO_CREATE
            )
        }
        findViewById<TextView>(R.id.btn_3).setOnClickListener {
            val intent = Intent().apply {
                component = ComponentName(
                    "com.example.mybindtestb",
                    "com.example.mybindtestb.MyServerService"
                )
            }
            bindService(
                intent,
                aidlConnection,
                Context.BIND_AUTO_CREATE
            )
        }
        val handler = object : Handler(mainLooper) {
            override fun handleMessage(msg: Message) {
                super.handleMessage(msg)
                Log.d(
                    "handleMessage",
                    "${Thread.currentThread().id} what=${msg.what} .time = ${System.currentTimeMillis() - (msg.obj as Long)}"
                )
            }
        }
        for (i in 1..100) {
            val msg = Message.obtain()
            msg.what = i
            msg.obj = System.currentTimeMillis()
            handler.sendMessageDelayed(msg, 1000)
        }
        val thread = HandlerThread("tag")
        val handlex = object :Handler(thread.looper){
            override fun handleMessage(msg: Message) {
                super.handleMessage(msg)
                Log.d(
                    "handleMessage",
                    "${Thread.currentThread().id}" +
                            "what=${msg.what} .time = ${System.currentTimeMillis() - (msg.obj as Long)}"
                )
            }
        }
        thread.run {
            val msg = Message.obtain()
            msg.what = 300
            msg.obj = System.currentTimeMillis()
            handlex.sendMessageDelayed(msg, 1000)
        }


    }


    private var iStudentInfo: IStudentInfo? = null
    private val remoteCallback = object : RemoteCallBack.Stub() {
        override fun onCallBack(student: Student?) {
            student?.let {
                Log.d("remoteCallback", "${it.name} 的分数=${it.score}")
                Toast.makeText(this@MainActivity, "${it.name} 的分数=${it.score}", Toast.LENGTH_LONG)
                    .show()
            }
        }
    }

    private val aidlConnection = object : ServiceConnection {
        @RequiresApi(Build.VERSION_CODES.P)
        override fun onServiceConnected(name: ComponentName?, service: IBinder) {
            Log.d("remoteCallback_connect", "xx")
            iStudentInfo = IStudentInfo.Stub.asInterface(service)
            iStudentInfo?.register(remoteCallback)
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            Log.d("remoteCallback_onServiceDisconnected", "xx")
        }

    }
    private val messageConnection = object : ServiceConnection {
        @RequiresApi(Build.VERSION_CODES.P)
        override fun onServiceConnected(name: ComponentName?, service: IBinder) {
            val messenger = Messenger(service)
            val message = Message.obtain()
            message.data = Bundle().apply {
                putInt("id", 20)
            }
            message.replyTo = Messenger(object : Handler(Looper.getMainLooper()) {
                override fun handleMessage(msg: Message) {
                    Log.d("MessageService-receiver", "name = ${msg.data.getString("name")}")
                }
            })
            try {
                messenger.send(message)
            } catch (e: Exception) {
                Log.d("MessageService_error", e.toString())
            }

        }

        override fun onServiceDisconnected(name: ComponentName?) {

        }

    }
    private val connection = object : ServiceConnection {
        @RequiresApi(Build.VERSION_CODES.P)
        override fun onServiceConnected(name: ComponentName?, service: IBinder) {
            val iMyServer = IMyServer1.Stub.asInterface(service)
            val word = "Im go"
            try {
                iMyServer.say(word)
                val age = iMyServer.tell(word, 2)
                Log.d(
                    "MyServerBinder_req",
                    age.toString() + " == ipc:" + Application.getProcessName()
                )
            } catch (e: Exception) {
                Log.d("onServiceConnected_error", e.toString())
            }

        }

        override fun onServiceDisconnected(name: ComponentName?) {

        }

    }
}