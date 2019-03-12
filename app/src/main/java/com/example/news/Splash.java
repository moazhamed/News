package com.example.news;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.news.Base.BaseActivity;

public class Splash extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

      new Handler().postDelayed(new Runnable() {
          @Override
          public void run() {
              Intent i = new Intent(activity , HomeActivity.class);
              startActivity(i);
              finish();
          }
      },3000);

    }
}
