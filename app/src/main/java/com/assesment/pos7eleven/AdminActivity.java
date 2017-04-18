package com.assesment.pos7eleven;

import android.app.DownloadManager;
import android.app.VoiceInteractor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.assesment.pos7eleven.ClasesAuxiliares.SessionHelper;
import com.assesment.pos7eleven.ClasesAuxiliares.UserCustomAdapter;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class AdminActivity extends AppCompatActivity {

    private TextView nameTV;
    private TextView lastnameTV;
    private ListView userListView;
    private TextView noresTV;
    private ArrayList<ArrayList<String>> items;
    private static final String TAG = AdminActivity.class.getSimpleName();
    private static final String URL_PATH = "/pos/admin/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        nameTV = (TextView) findViewById(R.id.nameTV);
        lastnameTV = (TextView) findViewById(R.id.lastnameTV);
        userListView = (ListView) findViewById(R.id.listViewAdmin);
        noresTV = (TextView) findViewById(R.id.noresTV);

        nameTV.setText(SessionHelper.first_name);
        lastnameTV.setText(SessionHelper.last_name);

        init();
    }

    private void setList(String data) throws JSONException {
        JSONArray users = new JSONArray(data);
        items = new ArrayList<ArrayList<String>>();

        if (users.length() == 0) {
            noresTV.setVisibility(View.VISIBLE);
        }

        for(int i = 0; i < users.length(); ++i) {
            ArrayList<String> row = new ArrayList<String>();
            row.add(users.getJSONObject(i).getJSONObject("user").getString("first_name"));
            row.add(users.getJSONObject(i).getJSONObject("user").getString("last_name"));

            if (users.getJSONObject(i).getJSONObject("user").getBoolean("is_staff"))
                row.add("Gerente");
            else
                row.add("Empleado");

            row.add(users.getJSONObject(i).getJSONObject("store").getString("location"));
            items.add(row);
        }

        UserCustomAdapter adapter = new UserCustomAdapter(this, items);

        userListView.setAdapter(adapter);
    }

    private void init(){

        Request request = new Request.Builder().url(getString(R.string.ip) + URL_PATH).build();
        SessionHelper.client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String jsondata = response.body().string();
                if (response.isSuccessful()) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                setList(jsondata);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    });
                }
            }
        });
    }
}
