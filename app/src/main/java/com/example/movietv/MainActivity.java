package com.example.movietv;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.GridView;
import android.widget.Toast;

import com.example.movietv.databinding.ActivityMainBinding;

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
public class MainActivity extends DrawerBaseActivity {

    GridView coursesGV;
    // flag for Internet connection status
    Boolean isInternetPresent = false;
    // private ResultProfileBinding binding;
 ActivityMainBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding =  ActivityMainBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        coursesGV = findViewById(R.id.idGVcourses);
        new AsyncFetch().execute();
    }

    private class AsyncFetch extends AsyncTask<String, String, String> {
        ProgressDialog pdLoading = new ProgressDialog(MainActivity.this);
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
                url = new URL("http://api.themoviedb.org/3/movie/popular?api_key=90787843a200cfbfd55b14b39270f6a1");

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
            Log.d("",result);
            JSONObject json = null;
            try {
                json = new JSONObject(result);
            } catch (JSONException e) {
                Log.d("Error","I'm in exception");

                e.printStackTrace();
            }
            pdLoading.dismiss();
            List<Results> data=new ArrayList<>();

            pdLoading.dismiss();
            try {

                JSONArray jArray =json.getJSONArray("results");

                // Extract data from json and store into ArrayList as class objects
                for(int i=0;i<jArray.length();i++){
                    JSONObject json_data = jArray.getJSONObject(i);
                    //          Log.d("",json_data.toString());
                    Results result1 = new Results();
                    result1.original_language= json_data.getString("original_language");
                    result1.original_title= json_data.getString("original_title");
                    result1.poster_path = json_data.getString("poster_path");
                    result1.release_date = json_data.getString("release_date");
                    result1.title = json_data.getString("title");
                    result1.vote_average = json_data.getString("vote_average");
                    result1.overiew = json_data.getString("overview");
                    result1.id = json_data.getString("id");
                    data.add(result1);

                }
                //      Log.d("Data final",data.toString());
                // Setup and Handover data to recyclerview

                CourseGVAdapter adapter = new CourseGVAdapter(MainActivity.this, data);
                coursesGV.setAdapter(adapter);



            } catch (JSONException e) {
                Toast.makeText(MainActivity.this, e.toString(), Toast.LENGTH_LONG).show();
            }

        }

    }
}