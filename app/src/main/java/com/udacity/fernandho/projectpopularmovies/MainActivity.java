package com.udacity.fernandho.projectpopularmovies;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    private String TAG = MainActivity.class.getSimpleName();
    private ProgressDialog pDialog;
    private ListView lv;

    // URL to get contacts JSON
    private static String urlPopular = "https://api.themoviedb.org/3/movie/popular?api_key=XXX&language=en-US&page=1";
    private static String urlTopRated = "https://api.themoviedb.org/3/movie/top_rated?api_key=XXX&language=en-US&page=1";
    ArrayList<HashMap<String, String>> MoviesList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        MoviesList = new ArrayList<>();
        lv = (ListView) findViewById(R.id.list);
        new GetMovies().execute();
    }

    /**
     * Using asynchronous task class to retrieve HTTP
     */

    private class GetMovies extends AsyncTask<Void, Void, Void> {

        //Shows a dialog before complete initialize the apk
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(MainActivity.this);
            pDialog.setMessage("Loading...");
            pDialog.setCancelable(false);
            pDialog.show();
        }

    }
}
