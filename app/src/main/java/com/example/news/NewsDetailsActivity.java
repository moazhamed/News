package com.example.news;

import android.os.Bundle;
import android.widget.TextView;

import com.example.news.Base.BaseActivity;

public class NewsDetailsActivity extends BaseActivity {

    protected TextView sourceName;
    protected TextView newsTitle;
    protected TextView newsDate;
    protected TextView contentHolder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.activity_news_details);
        initView();
        String source = getIntent().getStringExtra("source");
        String title = getIntent().getStringExtra("title");
        String date = getIntent().getStringExtra("date");
        String content = getIntent().getStringExtra("content");
       // showProgressBar();
        sourceName.setText(source);
        newsTitle.setText(title);
        newsDate.setText(date);
        contentHolder.setText(content);
        //hideProgressBar();


    }

    private void initView() {
        sourceName =  findViewById(R.id.source_name);
        newsTitle =  findViewById(R.id.news_title);
        newsDate = findViewById(R.id.news_date);
        contentHolder = findViewById(R.id.content_holder);
    }
}
