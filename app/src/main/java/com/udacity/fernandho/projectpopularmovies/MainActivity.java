package com.udacity.fernandho.projectpopularmovies;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
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
    private String urlToSearch = urlTopRated;
    ArrayList<HashMap<String, String>> MoviesList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        MoviesList = new ArrayList<>();
        lv = (ListView) findViewById(R.id.list);
        new GetMovies().execute();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
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

        @Override
        protected Void doInBackground(Void... parameters) {
            HttpConnection sh = new HttpConnection();

            // Making a request to url and getting response
            String jsonStr = sh.makeServiceCall(urlToSearch);

            Log.e(TAG, "Response from url: " + jsonStr);

            if (jsonStr != null) {
                try {
                    JSONObject jsonObj = new JSONObject(jsonStr);

                    // Getting JSON Array node
                    JSONArray movies = jsonObj.getJSONArray("results");

                    // looping through All movies
                    for (int i = 0; i < movies.length(); i++) {
                        JSONObject c = movies.getJSONObject(i);

                        String id = c.getString("id");
                        String title = c.getString("title");
                        String popularity = c.getString("popularity");
                        String poster_path = c.getString("poster_path");

                        // Example of node: Phone node is JSON Object
                        //JSONObject phone = c.getJSONObject("phone");
                        //String mobile = phone.getString("mobile");
                        //String home = phone.getString("home");
                        //String office = phone.getString("office");

                        // tmp hash map for single movie
                        HashMap<String, String> movie = new HashMap<>();

                        // adding each child node to HashMap key => value
                        movie.put("id", id);
                        movie.put("title", title);
                        movie.put("popularity", popularity);
                        movie.put("poster_path", "http://image.tmdb.org/t/p/w185/"+poster_path);


                        // adding movies to movies list
                        MoviesList.add(movie);
                    }
                } catch (final JSONException e) {
                    Log.e(TAG, "Json parsing error: " + e.getMessage());
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(),
                                    "Json parsing error: " + e.getMessage(),
                                    Toast.LENGTH_LONG)
                                    .show();
                        }
                    });

                }
            } else {
                Log.e(TAG, "Couldn't get json from server.");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(),
                                "Couldn't get json from server. Check LogCat for possible errors!",
                                Toast.LENGTH_LONG)
                                .show();
                    }
                });

            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            // Dismiss the progress dialog
            if (pDialog.isShowing())
                pDialog.dismiss();
            /**
             * Updating parsed JSON data into ListView
             * */
            ListAdapter adapter = new SimpleAdapter(
                    MainActivity.this, MoviesList,
                    R.layout.list_item, new String[]{"title", "popularity",
                    "id","poster_path"}, new int[]{R.id.title,
                    R.id.popularity, R.id.id, R.id.poster_path});

            lv.setAdapter(adapter);
        }


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.menuSortPop:
                urlToSearch = urlPopular;
                //Toast.makeText(MainActivity.this, urlToSearch, Toast.LENGTH_SHORT).show();
                MoviesList.clear();
                new GetMovies().execute();
                return true;
            case R.id.menuSortTop:
                urlToSearch = urlTopRated;
                //Toast.makeText(MainActivity.this, urlToSearch, Toast.LENGTH_SHORT).show();
                MoviesList.clear();
                new GetMovies().execute();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
