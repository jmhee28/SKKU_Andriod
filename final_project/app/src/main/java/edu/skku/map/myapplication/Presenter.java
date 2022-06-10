package edu.skku.map.myapplication;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

public class Presenter implements Contract.Presenter {
    Contract.View view;
    Model mainModel;
    private Context mContext;
    private Activity mact;
    public Presenter(Contract.View view,Context mContext, Activity act) {
        this.mContext = mContext;
        this.view = view;
        this.mact = act;
        mainModel = new Model(this, this.mContext, this.mact);    //Model 객체 생성
    }
    public void initCity(String address, double latitude, double longtitude)
    {

        mainModel.getCurrent(address, latitude, longtitude,new Contract.Model.onFinished_Listener() {
            @Override
            public void onFinished(String[] response, String[] fas) {
                view.showCurrent(response, fas);
            }

        });

    }

}
