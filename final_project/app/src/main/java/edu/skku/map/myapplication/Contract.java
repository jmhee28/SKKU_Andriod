package edu.skku.map.myapplication;

public interface Contract {
    interface View{
        void showLocation(String city);
        void showCurrent(String[] cur, String[] fas);
    }
    interface Presenter{  //결과 값 구하기 위한 메소드 선언
        void initCity(String address, double latitude, double longtitude);
    }
    interface Model{
        interface onFinished_Listener{
            void onFinished(String[] response, String[] fas);
        }

        void getCurrent(String address, double latitude, double longtitude,final onFinished_Listener onFinished_listener);
    }

}
