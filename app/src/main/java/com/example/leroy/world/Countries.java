package com.example.leroy.world;

import android.app.SearchManager;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class Countries extends AppCompatActivity {
    OkHttpClient mOkHttpClient;
    Request mRequest;
    private String url = "https://restcountries.eu/rest/v2/all";
    List<String> nation = new ArrayList<String>();
    ListView nations;
    ArrayAdapter<String> arrayAdapter;

    public class DownloadTask extends AsyncTask<String, Void, String>{

        @Override
        protected String doInBackground(String... strings) {
            mOkHttpClient = new OkHttpClient();
            mRequest = new Request.Builder().url(strings[0]).build();

            try {
                Response response = mOkHttpClient.newCall(mRequest).execute();
                return response.body().string();

            } catch (IOException e) {
                e.printStackTrace();
            }
            return "finished";
        }
    }

    private class Downloads extends AsyncTask<String, Void, String> {

        OkHttpClient client = new OkHttpClient();

        @Override
        protected String doInBackground(String... strings) {

            Request.Builder builder = new Request.Builder();
            builder.url(strings[0]);
            Request request = builder.build();
            try {
                Response response = client.newCall(request).execute();
                return response.body().string();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String string) {
            super.onPostExecute(string);
            try {
                String countries = string;
                JSONArray jsonArray = new JSONArray(countries);

                for(int j=0; j<jsonArray.length(); j++){
                    JSONObject names = jsonArray.getJSONObject(j);
                    nation.add(names.getString("name"));
                }

                nations = (ListView) findViewById(R.id.unitedNations);
                arrayAdapter = new ArrayAdapter<String>(Countries.this, android.R.layout.simple_list_item_1, nation);
                nations.setAdapter(arrayAdapter);

            } catch (Exception e) {
                //didn't work
            }
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_countries);
    }

    @Override
    protected void onStart() {
        Downloads task = new Downloads();
        task.execute(url);
        super.onStart();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_search, menu);
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        MenuItem menuItem = menu.findItem(R.id.menuSearch);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(menuItem);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                arrayAdapter.getFilter().filter(newText);
                return false;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }
}
