package com.example.mybindtest;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

/***
 * @author lcy
 * @date 2023-04-20
 *
 */
class Student implements Parcelable {
   String name;
   int age;
   int score;

   protected Student(Parcel in) {
      name = in.readString();
      age = in.readInt();
      score = in.readInt();
   }

   public static final Creator<Student> CREATOR = new Creator<Student>() {
      @Override
      public Student createFromParcel(Parcel in) {
         return new Student(in);
      }

      @Override
      public Student[] newArray(int size) {
         return new Student[size];
      }
   };

   @Override
   public int describeContents() {
      return 0;
   }

   @Override
   public void writeToParcel(@NonNull Parcel dest, int flags) {
      dest.writeString(name);
      dest.writeInt(age);
      dest.writeInt(score);
   }
}
