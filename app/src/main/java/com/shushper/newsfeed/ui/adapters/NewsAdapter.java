package com.shushper.newsfeed.ui.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.shushper.newsfeed.R;
import com.shushper.newsfeed.api.model.News;
import com.shushper.newsfeed.helpers.TimeFormatter;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.ViewHolder> {
    private static final String TAG = "NewsAdapter";

    private List<News>    mNews;
    private TimeFormatter mTimeFormatter;

    public NewsAdapter() {
        mNews = new ArrayList<>();
        mTimeFormatter = new TimeFormatter(TimeFormatter.PATTERN_NEWS_FEED);
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
        holder.date.setText(mTimeFormatter.format(news.getCreatedAt()));

        Picasso.with(holder.itemView.getContext())
               .load(news.getImageUrl())
               .into(holder.image);

    }

    @Override
    public int getItemCount() {
        return mNews.size();
    }

    public void setNews(List<News> news) {
        mNews = news;
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
