package edu.skku.map.myapplication;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.media.audiofx.AcousticEchoCanceler;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.chromium.base.Log;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class Model implements Contract.Model{
    Contract.Presenter presenter;
    private Context mContext;
    private Activity mact;
    public Model(Contract.Presenter presenter,Context mContext, Activity act){
        this.presenter = presenter;
        this.mContext = mContext;
        this.mact = act;
    }


    public void getCurrent(String address, double latitude, double longtitude,final onFinished_Listener onFinished_listener)
    {
        Log.i("long", String.valueOf(longtitude));
        Log.i("long", String.valueOf(latitude));
        OkHttpClient client = new OkHttpClient();
        HttpUrl.Builder urlBuilder = HttpUrl.parse("https://weatherbit-v1-mashape.p.rapidapi.com/current").newBuilder();
        //urlBuilder.addQueryParameter("q", City + ",Cus");
        urlBuilder.addQueryParameter("lon", String.valueOf(longtitude));
        urlBuilder.addQueryParameter("lat", String.valueOf(latitude));
        urlBuilder.addQueryParameter("units", "metric");
        // urlBuilder.addQueryParameter("units", "metric or imperial");
        String url = urlBuilder.build().toString();
        Request req = new Request.Builder()
                .url(url)
                .get()
                .addHeader("X-RapidAPI-Host", "weatherbit-v1-mashape.p.rapidapi.com")
                .addHeader("X-RapidAPI-Key", "Your API Key")
                .build();
        client.newCall(req).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                final String res = response.body().string();
                Log.i("origin", res);

                try {
                    Gson gson = new GsonBuilder().create();
                    JSONObject jObject = new JSONObject(res);
                    JSONArray jsonArr = (JSONArray) jObject.getJSONArray("data");
                    JSONObject jsonObj = (JSONObject) jsonArr.getJSONObject(0);
                    int tem = jsonObj.getInt("temp");
                    String[] cur = new String[4];
                    cur[2] = String.format("%s℃", String.valueOf(tem));
                    cur[1] = String.format("%s℃", String.valueOf(tem));
                    cur[0] =String.valueOf(jsonObj.getString("ob_time"));
                    HttpUrl.Builder builder = HttpUrl.parse("Your AWS S3 URL /dev/access").newBuilder();
                    builder.addQueryParameter("temperature", String.valueOf(tem)); // Your API key

                    String url = builder.build().toString();
                    Request req = new Request.Builder().url(url).build();

                    client.newCall(req).enqueue(new Callback() {
                        @Override
                        public void onFailure(@NonNull Call call, @NonNull IOException e) {
                            e.printStackTrace();
                        }

                        @Override
                        public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                            final String res = response.body().string();
                            Gson gson = new GsonBuilder().create();
                            String nres = res.replace("\"", "");
                            Log.i("fas", res);
                            String[] fashion = nres.split(":");

                            onFinished_listener.onFinished(cur, fashion);
                        }
                    });
                }catch (JSONException e) {
                    e.printStackTrace();
                }





            }
        });
    }


}

