package com.example.news.API;

import com.example.news.API.Model.NewsResponse.NewsResponse;
import com.example.news.API.Model.NewsSourcesResponce.SourcesResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface Services {

    @GET("sources")
    Call<SourcesResponse> getNewsSources( @Query("apiKey") String apiKey , @Query("language") String lang );

    @GET("everything")
    Call<NewsResponse> getNewssourceID(@Query("apiKey") String apiKey ,
                                       @Query("language") String lang  ,
                                       @Query("sources") String sourceId );
}
