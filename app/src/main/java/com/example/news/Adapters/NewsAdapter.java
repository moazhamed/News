package com.example.news.Adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.news.API.Model.NewsResponse.ArticlesItem;
import com.example.news.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.ViewHolder> {
    List<ArticlesItem> newsList;
    onItemClickListener onImageClickListener;
    onItemClickListener onShareButtonClickListener;

    public void setOnShareButtonClickListener(onItemClickListener onShareButtonClickListener) {
        this.onShareButtonClickListener = onShareButtonClickListener;
    }



    public void setOnImageClickListener(onItemClickListener onImageClickListener) {
        this.onImageClickListener = onImageClickListener;
    }


    public NewsAdapter(List<ArticlesItem> newsList) {
        this.newsList = newsList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.card_item_news, viewGroup, false);
        return new ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, final int i) {
        final ArticlesItem newsItem = newsList.get(viewHolder.getAdapterPosition());
        viewHolder.sourceName.setText(newsItem.getSource().getName());

        Date date = new Date();

        String dtStart = newsItem.getPublishedAt();
        SimpleDateFormat format =
                new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        try {
            date = format.parse(dtStart);
            System.out.println(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }


        viewHolder.newsDate.setText(date.toString());


        viewHolder.newsTitle.setText(newsItem.getTitle());
        Glide.with(viewHolder.itemView).load(newsItem.getUrlToImage())
                .into(viewHolder.newsImage);
        if (onImageClickListener != null) {
            viewHolder.newsImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onImageClickListener.onItemClick(i, newsItem);
                }
            });

        }
        if(onShareButtonClickListener !=null){

            viewHolder.shareButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onShareButtonClickListener.onItemClick(i , newsItem);
                }
            });

        }



    }

    public void ChangeData(List<ArticlesItem> list) {

        this.newsList = list;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        if (newsList == null) return 0;
        return newsList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView sourceName, newsTitle, newsDate;
        ImageView newsImage;
        Button shareButton;

        public ViewHolder(View view) {
            super(view);
            sourceName = view.findViewById(R.id.source_name);
            newsTitle = view.findViewById(R.id.news_title);
            newsDate = view.findViewById(R.id.news_date);
            newsImage = view.findViewById(R.id.news_image);
            shareButton = view.findViewById(R.id.share_button);
        }

    }

    public interface onItemClickListener {
        public void onItemClick(int pos, ArticlesItem item);
    }


}
