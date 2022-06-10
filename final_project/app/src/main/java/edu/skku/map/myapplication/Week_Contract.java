package edu.skku.map.myapplication;

import java.util.ArrayList;

public interface Week_Contract {
    interface Week_View{
        void addItem(String[] response, String[] fas, int i);
    }
    interface Week_Presenter{  //결과 값 구하기 위한 메소드 선언
        void initWeek(String city, String lat, String longti);
        String[][] get_P_items();
    }
    interface Week_Model{
        interface onFinished_Listener{
            void onFinished(String[] response, String[] fas, int i);
        }
        void getWeather(String City,String lat, String longti,final onFinished_Listener onFinished_listener);

    }
}
