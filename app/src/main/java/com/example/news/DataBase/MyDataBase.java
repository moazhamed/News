package com.example.news.DataBase;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import com.example.news.API.Model.NewsResponse.ArticlesItem;
import com.example.news.API.Model.NewsSourcesResponce.SourcesItem;
import com.example.news.DataBase.DOAs.SourcesDao;


@Database(entities = {SourcesItem.class}
        , version = 1, exportSchema = false)
public abstract class MyDataBase extends RoomDatabase {


    public abstract SourcesDao sourcesDao();
    private static MyDataBase myDataBase;

     public static void init(Context context){
         if (myDataBase == null) {
             myDataBase =
                     Room.databaseBuilder(context.getApplicationContext(),
                             MyDataBase.class, "News-DataBase")
                             // allow queries on the main thread.
                             // Don't do this on a real app! See PersistenceBasicSample for an example.
                        //     .allowMainThreadQueries()
                             .build();

         }
     }


    public static MyDataBase getInstance() {

        return myDataBase;


    }
}
