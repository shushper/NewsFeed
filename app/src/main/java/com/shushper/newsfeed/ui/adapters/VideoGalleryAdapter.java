package com.shushper.newsfeed.ui.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.shushper.newsfeed.R;
import com.shushper.newsfeed.api.model.Video;
import com.squareup.picasso.Picasso;

import java.util.List;


public class VideoGalleryAdapter extends RecyclerView.Adapter<VideoGalleryAdapter.ViewHolder> {

    private List<Video> mVideos;

    private Listener mListener;

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_photo_gallery, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final Video video = mVideos.get(position);

        Picasso.with(holder.itemView.getContext()).load(video.getThumbUrl()).into(holder.videoThumb);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) {
                    mListener.onVideoClick(video.getVideoUrl());
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mVideos.size();
    }

    public void setVideos(List<Video> videos) {
        mVideos = videos;
    }

    public interface Listener {
        void onVideoClick(String videoUrl);
    }

    public void setListener(Listener l) {
        mListener = l;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView videoThumb;

        public ViewHolder(View itemView) {
            super(itemView);

            videoThumb = (ImageView) itemView;
        }
    }

}
