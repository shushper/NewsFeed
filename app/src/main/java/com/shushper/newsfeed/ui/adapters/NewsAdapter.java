package com.shushper.newsfeed.ui.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.shushper.newsfeed.R;
import com.shushper.newsfeed.model.News;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.ViewHolder> {
    private static final String TAG = "NewsAdapter";

    private List<News> mNews;

    public NewsAdapter() {
        mNews = createNewsList();
    }

    private List<News> createNewsList() {
        List<News> newsList = new ArrayList<>();

        News news = new News();
        news.setId(0);
        news.setImageUrl("http://www.e1.ru/news/images/new1/432/591/images/DSC_0148_300x199.JPG");
        news.setDate(1446527100);
        news.setTitle("Роспотребнадзор признал, что паркоматы, которые не берут наличку, вне закона");
        newsList.add(news);

        return newsList;

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_news, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        News news = mNews.get(position);


        holder.title.setText(news.getTitle());
        holder.date.setText(String.valueOf(news.getDate()));

        Picasso.with(holder.itemView.getContext())
               .load(news.getImageUrl())
               .into(holder.image);

    }

    @Override
    public int getItemCount() {
        return mNews.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView image;
        private TextView  title;
        private TextView  date;


        public ViewHolder(View itemView) {
            super(itemView);

            image = (ImageView) itemView.findViewById(R.id.news_image);
            title = (TextView) itemView.findViewById(R.id.news_title);
            date = (TextView) itemView.findViewById(R.id.news_date);
        }
    }
}
