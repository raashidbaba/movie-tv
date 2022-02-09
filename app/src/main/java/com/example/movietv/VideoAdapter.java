package com.example.movietv;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class VideoAdapter extends RecyclerView.Adapter<VideoAdapter.ThumbnailViewHolder> {
    public List<VideoResponse> videoList;
    private final Context context;
    public VideoAdapter(List<VideoResponse> videoList,Context context) {
        this.videoList = videoList;
        this.context = context;
    }

    @NonNull
    @Override
    public ThumbnailViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
       View root = LayoutInflater.from(parent.getContext()).inflate(R.layout.item,parent,false);
       return new ThumbnailViewHolder(root);
    }

    @Override
    public void onBindViewHolder(@NonNull ThumbnailViewHolder holder, int position) {
        String YOUTUBE_THUMBNAIL_URL = "https://img.youtube.com/vi/";
        String YOUTUBE_IMAGE_EXT = "/0.jpg";
        final String YOUTUBE_APP_URI = "vnd.youtube:";
        holder.categoryItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent returnIntent = new Intent("custom-message");//INTENT
                returnIntent.putExtra("result",videoList.get(holder.getAbsoluteAdapterPosition()).getKey());//DATA
                LocalBroadcastManager.getInstance(context).sendBroadcast(returnIntent);
            }
        });
        Picasso.with(context).load(YOUTUBE_THUMBNAIL_URL + this.videoList.get(position).getKey() + YOUTUBE_IMAGE_EXT)
                .into(holder.thumbnail);

    }

    @Override
    public int getItemCount() {
       if(this.videoList != null){
           return this.videoList.size();
       }
       return 0;
    }

    public class ThumbnailViewHolder extends RecyclerView.ViewHolder{
        ImageView thumbnail;
        private final View categoryItem;
        public ThumbnailViewHolder(@NonNull View itemView) {
            super(itemView);
            thumbnail = itemView.findViewById(R.id.thumbnail);
            this.categoryItem = itemView;
        }
    }
}
