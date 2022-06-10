package edu.skku.map.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class Signin extends AppCompatActivity  {

    EditText name,id,pw,pw2,email,birthyear,birthdate,birthday;
    Button pwcheck, submit,back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);
        Intent intent = getIntent();

        initView();
    }
    private void initView() {
        back = findViewById(R.id.back);
        name = findViewById(R.id.signName);
        id=findViewById(R.id.signID);
        pw=findViewById(R.id.signPW);
        pw2=findViewById(R.id.signPW2);
        email=findViewById(R.id.signmail);
        birthyear=findViewById(R.id.signBirth);
        birthdate=findViewById(R.id.signBirth2);
        birthday=findViewById(R.id.signBirth3);
        submit = findViewById(R.id.signupbutton);
        pwcheck = findViewById(R.id.pwcheckbutton);
        pwcheck.setOnClickListener(v -> {
            (Signin.this).runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (pw.getText().toString().equals(pw2.getText().toString())) {
                        pwcheck.setText("일치");
                    } else {
                        Toast.makeText(Signin.this, "비밀번호가 다릅니다.", Toast.LENGTH_LONG).show();
                    }
                }
            });
        });

        submit.setOnClickListener(v -> {
            (Signin.this).runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    String ID = id.getText().toString();
                    String pwd = pw.getText().toString();
                     Log.i("id, pwd",id.getText().toString()+ pw.getText().toString());
                    OkHttpClient client = new OkHttpClient();
                    IDPWDModel data = new IDPWDModel();
                    data.setID(ID);
                    data.setPwd(pwd);
                    Gson gson = new Gson();
                    String json = gson.toJson(data, IDPWDModel.class);

                    HttpUrl.Builder urlBuilder = HttpUrl.parse("Your AWS RDS URL /dev/adduser").newBuilder();
                    String url = urlBuilder.build().toString();

                    Request req = new Request.Builder().url(url).post(RequestBody.create(json, MediaType.parse("application/json"))).build();

                    client.newCall(req).enqueue(new Callback() {
                        @Override
                        public void onFailure(@NonNull Call call, @NonNull IOException e) {
                            e.printStackTrace();
                        }

                        @Override
                        public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                            final String res = response.body().string();
                            Gson gson = new GsonBuilder().create();
                            try {
                                JSONObject jObject = new JSONObject(res);
                                String ret_b = jObject.getString("success");
                                boolean lo = Boolean.parseBoolean(ret_b);
                                if (lo==false) {
                                    Log.i("ret_b: ", ret_b);
                                    postToastMessage("로그인에 실패하였습니다.");

                                } else if (lo==true) {
                                    Log.i("ret_b: ", ret_b);
                                    postToastMessage("로그인에 성공하였습니다.");

                                    Intent intent2 = new Intent(Signin.this, MainActivity.class);
                                    startActivity(intent2);
                                }
                            }catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                    });
                }
            });
        });
    }
    public void postToastMessage(final String message) {
        Handler handler = new Handler(Looper.getMainLooper());
        handler.post(new Runnable() {
        @Override
        public void run() {
            Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
        }
        });
    }

}