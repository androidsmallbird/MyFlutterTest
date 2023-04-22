// RemoteCallBack.aidl
package com.example.mybindtest;
import com.example.mybindtest.Student;

// Declare any non-default types here with import statements

interface RemoteCallBack {
         oneway void onCallBack(in Student student);
}