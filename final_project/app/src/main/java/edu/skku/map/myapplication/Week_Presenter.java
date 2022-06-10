package edu.skku.map.myapplication;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import java.util.ArrayList;

import edu.skku.map.myapplication.Week_Contract;
import edu.skku.map.myapplication.Week_Model;

public class Week_Presenter implements Week_Contract.Week_Presenter {
    Week_Contract.Week_View view;
    Week_Model mainModel;
    private Context mContext;
    private Activity mact;
    public String Curdate;
    //public ArrayList<weather_information> items;
    public String[][] res_lst;
    public String[][] fas_lst;
    int idx = 0;
    public Week_Presenter(Week_Contract.Week_View view,Context mContext, Activity act,String Curdate) {
        this.mContext = mContext;
        this.view = view;
        this.mact = act;
        this.Curdate =Curdate;
        //this.items = items;
        mainModel = new  Week_Model(this, this.mContext, this.mact, this.Curdate);    //Model 객체 생성
    }
    @Override
    public void initWeek(String city, String lat, String longti)
    {
        mainModel.getWeather(city,lat, longti,new Week_Contract.Week_Model.onFinished_Listener(){
            @Override
            public void onFinished(String[] response, String[] fas, int i) {
                view.addItem(response,fas, i);
            }

        });

    }
    @Override
    public String[][] get_P_items()
    {
        //Log.i("ites", this.items.get(0).date);
        return res_lst;
    }


}