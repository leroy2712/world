package com.example.leroy.world;

import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class CountriesActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {
    private String url = "https://restcountries.eu/rest/v2/all";
    List<String> nation = new ArrayList<String>();
    ListView nations;
    ArrayAdapter<String> arrayAdapter;
    GoogleApiClient mGoogleApiClient;

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

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
                final String countries = string;
                JSONArray jsonArray = new JSONArray(countries);

                for(int j=0; j<jsonArray.length(); j++){
                    JSONObject names = jsonArray.getJSONObject(j);
                    nation.add(names.getString("name"));
                }

                nations = (ListView) findViewById(R.id.unitedNations);
                arrayAdapter = new ArrayAdapter<String>(CountriesActivity.this, android.R.layout.simple_list_item_1, nation);
                nations.setAdapter(arrayAdapter);

                nations.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        String countryName = nation.get(i);

                        Intent intent = new Intent(CountriesActivity.this, CountryActivity.class);
                        intent.putExtra("country", countryName);
                        intent.putExtra("countries", countries);
                        startActivity(intent);
                    }
                });

            } catch (Exception e) {
                //didn't work
            }
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_countries);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        Downloads task = new Downloads();
        task.execute(url);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    public void onRestart() {
        super.onRestart();
        //When BACK BUTTON is pressed, the activity on the stack is restarted
        //Do what you want on the refresh procedure here
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

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_extras, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case R.id.menu_sign_out:
                confirmation();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void signOut() {
        Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(Status status) {
                        Intent intent = new Intent(CountriesActivity.this, ExploreActivity.class);
                        startActivity(intent);
                    }
                });
    }

    public void confirmation (){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("Are you sure you want to logout?");

        alertDialogBuilder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //what to do if user clicks yes
                signOut();
            }
        });

        alertDialogBuilder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //what to do if user clicks no
                finish();
            }
        });

        alertDialogBuilder.create().show();
    }
}
