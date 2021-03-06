package com.example.leroy.world;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class CountryActivity extends AppCompatActivity {
    private TextView countryNameTextView;
    ArrayList<String> info = new ArrayList<String>();
    String imageURL = "http://davisiama.co.ke/flags/";
    private String url = "https://restcountries.eu/rest/v2/name/";
    private ListView nation;
    private ImageView flag;
    ArrayAdapter<String> arrAdapt;
    private TextView nationalName;

    private class DownloadTask extends AsyncTask<String, Void, String>{

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

            final String countries = string;
            try {
                JSONArray jsonArray = new JSONArray(countries);
                JSONObject jsonObject = jsonArray.getJSONObject(0);

                String topLevelDomain= jsonObject.getString("topLevelDomain");
                String alpha2Code= jsonObject.getString("alpha2Code");
                String callingCodes= jsonObject.getString("callingCodes");
                String capital= jsonObject.getString("capital");
                String region= jsonObject.getString("region");
                String subregion= jsonObject.getString("subregion");
                String population= jsonObject.getString("population");
                String demonym= jsonObject.getString("demonym");
                String area= jsonObject.getString("area");
                String gini= jsonObject.getString("gini");
                String nativeName= jsonObject.getString("nativeName");
                String numericCode= jsonObject.getString("numericCode");

                info.add("Web Domain: " + topLevelDomain);
                info.add("Calling Code: " + callingCodes);
                info.add("Capital City: " + capital);
                info.add("Continent: " + region);
                info.add("Sub-Region: " + subregion);
                info.add("Population: " + population);
                info.add("Demonym: " + demonym);
                info.add("Area: " + area);
                info.add("GINI: " + gini);
                info.add("Native Name: " + nativeName);
                info.add("Numeric Code: " + numericCode);

                nation = (ListView) findViewById(R.id.country_facts);
                flag = (ImageView) findViewById(R.id.flag_image);

                Picasso.with(CountryActivity.this)
                        .load(imageURL + alpha2Code.toLowerCase() + ".png")
                        .into(flag);

                arrAdapt = new ArrayAdapter<String>(CountryActivity.this, android.R.layout.simple_list_item_1, info);
                nation.setAdapter(arrAdapt);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public void imageClick (View view){
        Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.rotate);
        flag.startAnimation(animation);

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent i = getIntent();
                String currentNation = i.getStringExtra("country");

                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://en.wikipedia.org/wiki/" + currentNation));
                startActivity(intent);
            }
        }, 1200);


    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_country);

        Intent intent = getIntent();
        String country = intent.getStringExtra("country");

        nationalName = (TextView) findViewById(R.id.country_name);
        nationalName.setText(country);

        DownloadTask task = new DownloadTask();
        task.execute(url + country);
    }
}
