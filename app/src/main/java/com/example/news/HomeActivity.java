package com.example.news;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.news.API.APIManager;
import com.example.news.API.Model.NewsResponse.ArticlesItem;
import com.example.news.API.Model.NewsSourcesResponce.SourcesItem;
import com.example.news.Adapters.NewsAdapter;
import com.example.news.Base.BaseActivity;
import com.example.news.Repos.newsReposiotry;

import java.util.List;

public class HomeActivity extends BaseActivity {

    protected TabLayout tabeLayout;
    protected RecyclerView recyclerView;
    newsReposiotry reposiotry;
    String Lang = "en";
    NewsAdapter adapter;
    RecyclerView.LayoutManager layoutManager;
    ViewPager viewPager;
    SwipeRefreshLayout swipeRefreshLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.activity_home);
        //Toolbar toolbar = findViewById(R.id.toolbar);
       // setSupportActionBar(toolbar);
        initView();

        reposiotry = new newsReposiotry(Lang);
        adapter = new NewsAdapter(null);
        layoutManager = new LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(layoutManager);
        showProgressBar();

        reposiotry.getNewsSources(onSourcesPreparedListener);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                swipeRefreshLayout.setRefreshing(false);
            }
        });


    }

 // public void  GetNews(){}

    newsReposiotry.OnNewsPreparedListener onNewsPreparedListener =
            new newsReposiotry.OnNewsPreparedListener() {
                @Override
                public void OnNewsPrepared(final List<ArticlesItem> articlesList) {

                    adapter.ChangeData(articlesList);
                    adapter.setOnImageClickListener(new NewsAdapter.onItemClickListener() {
                        @Override
                        public void onItemClick(int pos, ArticlesItem item) {
                            Intent i = new Intent(HomeActivity.this, NewsDetailsActivity.class);
                            i.putExtra("source", articlesList.get(pos).getSource().getName());
                            i.putExtra("title", articlesList.get(pos).getTitle());
                            i.putExtra("date", articlesList.get(pos).getPublishedAt());
                            i.putExtra("content", articlesList.get(pos).getContent());
                            startActivity(i);

                        }
                    });
                    adapter.setOnShareButtonClickListener(new NewsAdapter.onItemClickListener() {
                        @Override
                        public void onItemClick(int pos, ArticlesItem item) {

                            Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                            sharingIntent.setType("text/plain");
                            String shareBody = item.getTitle() +item.getUrl();
                            String shareSub = item.getTitle();
                            Log.e("body",shareBody);
                            sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, shareSub);
                            sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
                            startActivity(Intent.createChooser(sharingIntent, "Share using"));
                        }
                    });
                }
            };


    newsReposiotry.OnSourcesPreparedListener onSourcesPreparedListener =
            new newsReposiotry.OnSourcesPreparedListener() {
                @Override
                public void OnSourcesPrepared(final List<SourcesItem> sourcesList) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            hideProgressBar();
                            addSourcesToTabLayout(sourcesList);
                        }
                    });

                }
            };

    private void addSourcesToTabLayout(List<SourcesItem> sourcesList) {
        if (sourcesList == null) return;
        tabeLayout.removeAllTabs();
        for (int i = 0; i < sourcesList.size(); i++) {
            SourcesItem sourcesItem = sourcesList.get(i);
            TabLayout.Tab tab = tabeLayout.newTab();
            tab.setText(sourcesItem.getName());
            tab.setTag(sourcesItem);
            tabeLayout.addTab(tab);
            tabeLayout.getTabAt(0).select();
            tabeLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
                @Override
                public void onTabSelected(TabLayout.Tab tab) {
                    SourcesItem item = (SourcesItem) tab.getTag();
                    reposiotry.getNewsResponseById(Lang, item.getId(), onNewsPreparedListener);


                }

                @Override
                public void onTabUnselected(TabLayout.Tab tab) {

                }

                @Override
                public void onTabReselected(TabLayout.Tab tab) {
                    SourcesItem item = (SourcesItem) tab.getTag();
                    reposiotry.getNewsResponseById(Lang, item.getId(), onNewsPreparedListener);

                }
            });
        }
        tabeLayout.setupWithViewPager(viewPager);
    }

    private void initView() {
        tabeLayout = findViewById(R.id.tabe_layout);
        recyclerView = findViewById(R.id.recycler_view);
        viewPager = findViewById(R.id.pager);
        swipeRefreshLayout = findViewById(R.id.swipeId);
    }
}
