package com.example.news.Repos;

import com.example.news.API.APIManager;
import com.example.news.API.Model.NewsResponse.ArticlesItem;
import com.example.news.API.Model.NewsResponse.NewsResponse;
import com.example.news.API.Model.NewsSourcesResponce.SourcesItem;
import com.example.news.API.Model.NewsSourcesResponce.SourcesResponse;
import com.example.news.DataBase.MyDataBase;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class newsReposiotry {
    List<SourcesItem> sourcesItemList;
    String Lang;
    private static String apiKey = "01901d477bfe402784fd170f812e981c";

    public newsReposiotry(String lang) {
        Lang = lang;
        sourcesItemList = new ArrayList<>();
    }

    public void getNewsSources(final OnSourcesPreparedListener onSourcesPreparedListener) {
        APIManager.getApis()
                .getNewsSources(apiKey, Lang)
                .enqueue(new Callback<SourcesResponse>() {
                    @Override
                    public void onResponse(Call<SourcesResponse> call,
                                           Response<SourcesResponse> response) {
                        if (response.isSuccessful() &&
                                ("ok".equals(response.body().getStatus()))) {
                            sourcesItemList = response.body().getSources();
                            if (onSourcesPreparedListener != null) {
                                onSourcesPreparedListener.OnSourcesPrepared(sourcesItemList);
                                insertSourcesIntoDataBaseThread th = new insertSourcesIntoDataBaseThread(response.body().getSources());
                                th.start();

                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<SourcesResponse> call, Throwable t) {
                        //handel database call
                        getSourcesFromDataBaseThread th = new getSourcesFromDataBaseThread(onSourcesPreparedListener);
                        th.start();
                    }
                });
    }

    public void getNewsResponseById(String Lang, String sourceID, final OnNewsPreparedListener onNewsPreparedListener) {
        APIManager.getApis()
                .getNewssourceID(apiKey, Lang, sourceID)
                .enqueue(new Callback<NewsResponse>() {
                    @Override
                    public void onResponse(Call<NewsResponse> call,
                                           Response<NewsResponse> response) {
                        if (response.isSuccessful()
                                && "ok".equals(response.body().getStatus())) {
                            if (onNewsPreparedListener != null) {
                                onNewsPreparedListener.OnNewsPrepared(response.body().getArticles());
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<NewsResponse> call, Throwable t) {
                        //handle database call
                    }
                });
    }

    public interface OnSourcesPreparedListener {
        void OnSourcesPrepared(List<SourcesItem> sourcesList);
    }

    public interface OnNewsPreparedListener {
        void OnNewsPrepared(List<ArticlesItem> articlesList);
    }

    class insertSourcesIntoDataBaseThread extends Thread {

        List<SourcesItem> items;

        public insertSourcesIntoDataBaseThread(List<SourcesItem> items) {

            this.items = items;
        }

        public void run() {
            MyDataBase.getInstance()
                    .sourcesDao()
                    .AddSources(items);
        }


    }

    class getSourcesFromDataBaseThread extends Thread {

        OnSourcesPreparedListener onSourcesPreparedListener;
         public  getSourcesFromDataBaseThread (OnSourcesPreparedListener onSourcesPreparedListener){
             this.onSourcesPreparedListener = onSourcesPreparedListener;

         }

        public void run() {
            List<SourcesItem> items = MyDataBase.getInstance()
                    .sourcesDao()
                    .getAllSources();
            onSourcesPreparedListener.OnSourcesPrepared(items);
        }


    }


}
