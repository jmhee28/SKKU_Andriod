package edu.skku.map.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.Button;
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

public class LoginActivity extends AppCompatActivity {
    private TextView editID;
    private TextView editPWD;
    private Button Login;
    private Button Sign;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initView();
    }
    private void initView() {
        editID = findViewById(R.id.editID );
        editPWD = findViewById(R.id.editPWD);
        Login = findViewById(R.id.loginbutton);
        Sign = findViewById(R.id.signin);

        Login.setOnClickListener(view -> {
            //presenter.sendIDPWD(editID.toString(), editPWD.toString());

                (LoginActivity.this).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        String ID =  editID.getText().toString();
                        String pwd = editPWD .getText().toString();
                        //Log.i("id, pwd",id.getText().toString()+ pw.getText().toString());
                        OkHttpClient client = new OkHttpClient();
                        IDPWDModel data = new IDPWDModel();
                        data.setID(ID);
                        data.setPwd(pwd);
                        Gson gson = new Gson();
                        String json = gson.toJson(data, IDPWDModel.class);

                        HttpUrl.Builder urlBuilder = HttpUrl.parse("Your AWS RDS URL /dev/login").newBuilder();
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

                                        Intent intent2 = new Intent(LoginActivity.this, MainActivity.class);
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

        Sign.setOnClickListener(view -> {
            Intent intent = new Intent(LoginActivity.this, Signin.class);
            startActivity(intent);
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