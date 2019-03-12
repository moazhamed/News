package com.example.news;

import android.app.Application;

import com.example.news.DataBase.MyDataBase;

public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        MyDataBase.init(this);

    }
}
