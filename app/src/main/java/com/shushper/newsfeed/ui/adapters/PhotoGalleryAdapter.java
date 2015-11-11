package com.shushper.newsfeed.ui.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.shushper.newsfeed.R;
import com.shushper.newsfeed.api.model.Photo;
import com.squareup.picasso.Picasso;

import java.util.List;


public class PhotoGalleryAdapter extends RecyclerView.Adapter<PhotoGalleryAdapter.ViewHolder> {

    private List<Photo> mPhotos;

    private Listener mListener;

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_photo_gallery, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final Photo photo = mPhotos.get(position);

        Picasso.with(holder.itemView.getContext()).load(photo.getImageUrl()).into(holder.photo);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) {
                    mListener.onPhotoClick(photo.getId());
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mPhotos.size();
    }

    public void setPhotos(List<Photo> photos) {
        mPhotos = photos;
    }

    public interface Listener {
        void onPhotoClick(int photoId);
    }

    public void setListener(Listener l) {
        mListener = l;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView photo;


        public ViewHolder(View itemView) {
            super(itemView);

            photo = (ImageView) itemView;
        }
    }
}
