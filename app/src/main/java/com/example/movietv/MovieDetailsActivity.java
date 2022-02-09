package com.example.movietv;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.YouTubePlayerCallback;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MovieDetailsActivity extends AppCompatActivity {
    TextView moviename, voteAverage, releaseDate, orginalLanguage, overView;
    ImageView moviethumb,trailerfirst;
    RecyclerView thumbnail;
    RecyclerView.LayoutManager RecyclerViewLayoutManager;
    LinearLayoutManager HorizontalLayout;
    Results results;
    YouTubePlayerView youTubePlayerView;
    String YOUTUBE_THUMBNAIL_URL = "https://img.youtube.com/vi/";
    String YOUTUBE_IMAGE_EXT = "/0.jpg";

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);

        Intent intent = getIntent();
        results = (Results) intent.getSerializableExtra("data");

        moviethumb = findViewById(R.id.movie_thumb);
        Picasso.with(this)
                .load("https://image.tmdb.org/t/p/w780/" + results.poster_path)
                .into(moviethumb);

        moviename = findViewById(R.id.movie_name);
        moviename.setText(results.original_title);

        voteAverage = findViewById(R.id.vote_count);
        voteAverage.setText(results.vote_average);

        releaseDate = findViewById(R.id.release_date);
        releaseDate.setText(results.release_date);

        thumbnail = findViewById(R.id.recyclerview);
        RecyclerViewLayoutManager
                = new LinearLayoutManager(
                getApplicationContext());

        thumbnail.setLayoutManager(
                RecyclerViewLayoutManager);

        orginalLanguage = findViewById(R.id.original_lang);
        orginalLanguage.setText(results.original_language);

        overView = findViewById(R.id.description);
        overView.setText(results.overiew);
        youTubePlayerView = findViewById(R.id.video_img);

        getLifecycle().addObserver(youTubePlayerView);
        LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiver,
                new IntentFilter("custom-message"));
        new AsyncFetch().execute();

    }

    public BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // Get extra data included in the Intent
            String ItemName = intent.getStringExtra("result");
            if(intent.getStringExtra("result") != null){
                if(ItemName != null){
                    youTubePlayerView.getYouTubePlayerWhenReady(new YouTubePlayerCallback() {
                        @Override
                        public void onYouTubePlayer(@NonNull YouTubePlayer youTubePlayer) {
                            youTubePlayer.loadVideo(ItemName,0);
                        }
                    });
                }
            }
        }
    };

    private class AsyncFetch extends AsyncTask<String, String, String> {
        ProgressDialog pdLoading = new ProgressDialog(MovieDetailsActivity.this);
        HttpURLConnection conn;
        URL url = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            //this method will be running on UI thread
            pdLoading.setMessage("\tLoading...");
            pdLoading.setCancelable(false);
            pdLoading.show();

        }

        @Override
        protected String doInBackground(String... params) {
            try {

                // Enter URL address where your json file resides
                // Even you can make call to php file which returns json data
                url = new URL("https://api.themoviedb.org/3/movie/" + results.id + "/videos?api_key=90787843a200cfbfd55b14b39270f6a1");
            } catch (MalformedURLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                return e.toString();
            }
            try {

                // Setup HttpURLConnection class to send and receive data from php and mysql
                conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");
                conn.connect();
                // setDoOutput to true as we receive data from json file
                //conn.setDoOutput(true);

            } catch (IOException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
                return e1.toString();
            }

            try {

                int response_code = conn.getResponseCode();

                // Check if successful connection made
                if (response_code == HttpURLConnection.HTTP_OK) {

                    // Read data sent from server
                    InputStream input = conn.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(input));
                    StringBuilder result = new StringBuilder();
                    String line;

                    while ((line = reader.readLine()) != null) {
                        result.append(line);
                    }

                    // Pass data to onPostExecute method
                    return (result.toString());

                } else {

                    return ("unsuccessful");
                }

            } catch (IOException e) {
                e.printStackTrace();
                return e.toString();
            } finally {
                conn.disconnect();
            }


        }

        @Override
        protected void onPostExecute(String result) {

            //this method will be running on UI thread
            Log.d("", result);
            JSONObject json = null;

            try {
                json = new JSONObject(result);//results
               JSONObject finalJson = json;
               JSONArray jArray = finalJson.getJSONArray("results");//
                List<VideoResponse> vid = new ArrayList<>();
                youTubePlayerView.addYouTubePlayerListener(new AbstractYouTubePlayerListener() {
                    @Override
                    public void onReady(@NonNull YouTubePlayer youTubePlayer) {
                        String videoId = null;
                        try {
                            videoId = jArray.getJSONObject(0).getString("key");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        youTubePlayer.loadVideo(videoId, 0);
                    }
                });
                for(int i=0;i<jArray.length();i++) {
                    JSONObject json_data = jArray.getJSONObject(i);
                    VideoResponse RES = new VideoResponse();
                    RES.key = json_data.getString("key");
                    vid.add(RES);
                }

                HorizontalLayout = new LinearLayoutManager(MovieDetailsActivity.this, LinearLayoutManager.HORIZONTAL, false);
                thumbnail.setLayoutManager(HorizontalLayout);

                VideoAdapter adapter = new VideoAdapter( vid,MovieDetailsActivity.this);
                thumbnail.setAdapter(adapter);

            } catch (JSONException e) {
                Log.d("Error", "I'm in exception");

                e.printStackTrace();
            }
            pdLoading.dismiss();
            List<Results> data = new ArrayList<>();

            pdLoading.dismiss();


        }
    }
}