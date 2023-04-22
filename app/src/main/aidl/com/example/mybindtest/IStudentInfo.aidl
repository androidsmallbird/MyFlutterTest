// IStudentInfo.aidl
package com.example.mybindtest;
import com.example.mybindtest.Student;
import com.example.mybindtest.RemoteCallBack;

// Declare any non-default types here with import statements

interface IStudentInfo {
        Student getStudentInfo();
        oneway void register(in RemoteCallBack callBack);
}