package com.assesment.pos7eleven;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Path;
import android.support.test.espresso.core.deps.guava.net.MediaType;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    private Button loginButton;
    private EditText usernameText;
    private EditText passwordText;
    private static final String URL_PATH = "/pos/login/";
    private static final String TAG = MainActivity.class.getSimpleName();
    private static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    private OkHttpClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        loginButton = (Button) findViewById(R.id.loginButton);
        usernameText = (EditText) findViewById(R.id.usernameField);
        passwordText = (EditText) findViewById(R.id.passwordField);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });
    }

    private void login() {
        client = new OkHttpClient();
        RequestBody body = new FormBody.Builder().add("username", usernameText.getText().toString()).
                add("password", passwordText.getText().toString()).build();

        final Request request = new Request.Builder().url(getString(R.string.ip) + URL_PATH).post(body).build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String jsondata = response.body().string();
                if (response.isSuccessful()) {
                    if (jsondata != "invalid") {
                        try {
                            //Logged(jsondata);
                        }
                        catch (Exception e){
                            e.printStackTrace();
                        }
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(getApplicationContext(), "Estas logeado",
                                        Toast.LENGTH_LONG).show();
                                Log.d(TAG, jsondata);
                                /*Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                                startActivity(intent);*/
                            }
                        });
                    }

                } else {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Log.d(TAG, jsondata);
                            //Toast.makeText(getApplicationContext(), jsondata,
                            //        Toast.LENGTH_LONG).show();
                        }
                    });
                }
            }
        });
    }

    private void Logged(String jsond) throws JSONException {
        JSONObject user = new JSONObject(jsond);
        /*
        SessionHelper.logged_in = true;
        SessionHelper.id_user = user.getInt("pk");
        SessionHelper.admin_user = user.getBoolean("administrador");
        Log.d(TAG, "session id: " + SessionHelper.id_user);
        */
    }

}
