package edu.skku.map.myapplication;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import java.io.IOException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class Week_Model implements Week_Contract.Week_Model {
    String[] day_list = {"월요일", "화요일", "수요일", "목요일", "금요일", "토요일", "일요일"};
    int[] three_one_month = {1, 3, 5, 7, 8, 10, 12};
    int[] thirty_month = {4, 6, 9, 11};
    Week_Contract.Week_Presenter presenter;
    private Context mContext;
    private Activity mact;
    public String Curdate;
    public Data dt;

    public Week_Model(Week_Contract.Week_Presenter presenter, Context mContext, Activity act, String Curdate) {
        this.presenter = presenter;
        this.mContext = mContext;
        this.mact = act;
        this.Curdate = Curdate;
    }

    public void getWeather(String City,String lat, String longti, final onFinished_Listener onFinished_listener) {
        //"2019-09-07 08:14"
        OkHttpClient client = new OkHttpClient();
        HttpUrl.Builder urlBuilder = HttpUrl.parse("https://weatherbit-v1-mashape.p.rapidapi.com/forecast/daily").newBuilder();
        //urlBuilder.addQueryParameter("q", City + ",Cus");
        urlBuilder.addQueryParameter("lat", lat);
        urlBuilder.addQueryParameter("lon", longti);
       // urlBuilder.addQueryParameter("units", "metric or imperial");
        String url = urlBuilder.build().toString();
        Request req = new Request.Builder()
                .url(url)
                .get()
                .addHeader("X-RapidAPI-Host", "weatherbit-v1-mashape.p.rapidapi.com")
                .addHeader("X-RapidAPI-Key", "Your API key")
                .build();


        client.newCall(req).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws
                    IOException {
                final String res = response.body().string();
                Gson gson = new GsonBuilder().create();
                Log.i("show res main", res);
                try {
                    JSONObject jObject = new JSONObject(res);
                    JSONArray jsonArr = (JSONArray) jObject.getJSONArray("data");

                   for (int i = 0; i < jsonArr.length(); i++) {

                       JSONObject jsonObj = (JSONObject) jsonArr.getJSONObject(i);
                       String weatherjson =  jsonObj.getString("weather");

                       Log.i("icon",weatherjson);
                        int max_t =  jsonObj.getInt("max_temp");
                        int min_t = jsonObj.getInt("min_temp");
                        int average_temp = (int) Math.round((min_t + max_t) / 2);
                        String[] cur = new String[3];
                        cur[0] = (String) jsonObj.get("valid_date");
                        cur[1] = String.format("%s℃", Integer.toString(max_t));
                        cur[2] = String.format("%s℃", Integer.toString(min_t));


                        HttpUrl.Builder builder = HttpUrl.parse("Your AWS S3 URL /dev/access").newBuilder();
                        builder.addQueryParameter("temperature", String.valueOf(average_temp)); // Your API key
                        String url = builder.build().toString();
                        Request req = new Request.Builder().url(url).build();

                       int final_i = i;
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

                                String[] fashion = nres.split(":");

                                onFinished_listener.onFinished(cur, fashion, final_i);
                            }
                        });

                  }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });
    }

    class Data{
        public String  moonrise_ts;
        public int wind_cdir;
        public int rh;
        public int pres;
        public int high_temp;
        public int sunset_ts;
        public int ozone;
        public int moon_phase;
        public int wind_gust_spd;
        public int snow_depth;
        public int clouds;
        public int ts;
        public int sunrise_ts;
        public int app_min_temp;
        public int wind_spd;
        public int pop;
        public String wind_cdir_full;
        public int slp;
        public int moon_phase_lunation;
        public String valid_date;
        public int app_max_temp;
        public int vis;
        public int dewpt;
        public int snow;
        public int uv;
        public ArrayList<weather_data> weather;
        public int wind_dir;
        public int max_dhi;
        public int clouds_hi;
        public int precip;
        public int low_temp;
        public int max_temp;
        public int moonset_ts;
        public String datetime;
        public int temp;
        public int min_temp;
        public int clouds_mid;
        public int clouds_low;


    }
    class weather_data{
        public String icon;
        public int code;
        public String description;
    }
}