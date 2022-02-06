package com.example.movietv;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.squareup.picasso.Picasso;

import java.io.Serializable;
import java.util.List;

public class CourseGVAdapter extends ArrayAdapter<Results> {
    public CourseGVAdapter(@NonNull Context context, List<Results> courseModelArrayList) {
        super(context, 0, courseModelArrayList);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listitemView = convertView;
        if (listitemView == null) {
            // Layout Inflater inflates each item to be displayed in GridView.
            listitemView = LayoutInflater.from(getContext()).inflate(R.layout.json_xml, parent, false);
        }

        Results results = getItem(position);
        ImageView courseIV = listitemView.findViewById(R.id.thum_img);//im
        Picasso.with(getContext())
                .load("https://image.tmdb.org/t/p/w780/" + results.poster_path)
                .into(courseIV);
        listitemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //   Toast.makeText(getContext(), "image " + results.posterPath, Toast.LENGTH_SHORT).show();
                // Create the text message with a string.
                Intent sendIntent = new Intent(getContext(), MovieDetailsActivity.class);
                sendIntent.putExtra("data", (Serializable) results);
                getContext().startActivity(sendIntent);

            }
        });
        return listitemView;
    }
}
