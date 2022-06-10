package edu.skku.map.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class WeekDisplay extends AppCompatActivity implements  Week_Contract.Week_View{
    private Week_Contract.Week_Presenter presenter;

    private TextView first;
    private TextView second;
    private TextView third;
    private TextView fourth;
    private TextView fifth;
    private Button button;

    public String curCity;
    public String curdate;
    public String curday;
    public String longti;
    public String lat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_week_display);
        initView();
        Intent intent = getIntent();
        curdate =intent.getStringExtra("current_date");
        curCity =intent.getStringExtra("City");
        longti =intent.getStringExtra("long");
        lat =intent.getStringExtra("lat");


        presenter = new Week_Presenter(this, getApplicationContext(), WeekDisplay.this, curdate);

              WeekDisplay.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                presenter.initWeek(curCity,lat, longti );

                    }
                });

            }



    private void initView()
    {
        //listview = findViewById(R.id.listview);
        first = findViewById(R.id.first);
        second = findViewById(R.id.second);
        third = findViewById(R.id.third);
        fourth = findViewById(R.id.fourth);
        fifth = findViewById(R.id.fifth);
        button = findViewById(R.id.button);
        button.setOnClickListener(view -> {
            Intent sendIntent = new Intent();
            sendIntent.setAction(Intent.ACTION_SEND);
            sendIntent.putExtra(Intent.EXTRA_TEXT, first.getText().toString()+"\n"+ second.getText().toString()+"\n"+ third.getText().toString()+"\n"+ fourth.getText().toString()+"\n"+ fifth.getText().toString());
            sendIntent.setType("text/plain");

            Intent shareIntent = Intent.createChooser(sendIntent, null);
            startActivity(shareIntent);
        });
    }
    @Override
    public void addItem(String[] response, String[] fas, int i) {
        WeekDisplay.this.runOnUiThread(new Runnable() {
            String fassionview;
            @Override
            public void run() {
                if(fas.length == 2)
                    fassionview = fas[0] +" " + fas[1];
                else if(fas.length == 3)
                    fassionview = fas[0] +", " + fas[1]+", "+ fas[2];
                if (i == 0)
                    first.setText(response[0] + "   최고:"+ response[1] +"   최저:"+ response[2] +"\n추천 의상: "+ fassionview);
                else if (i == 1)
                    second.setText(response[0] + "   최고:"+ response[1] +"   최저:"+ response[2] +"\n추천 의상: "+ fassionview);
                else if (i == 2)
                    third.setText(response[0] + "   최고:"+ response[1] +"   최저:"+ response[2] +"\n추천 의상: "+fassionview);
                else if (i == 3)
                    fourth.setText(response[0] + "   최고:"+ response[1] +"   최저:"+ response[2] +"\n추천 의상: "+ fassionview);
                else if (i == 4)
                    fifth.setText(response[0] + "   최고:"+ response[1] +"   최저:"+ response[2] +"\n추천 의상: "+ fassionview);

            }
        });
    }
}