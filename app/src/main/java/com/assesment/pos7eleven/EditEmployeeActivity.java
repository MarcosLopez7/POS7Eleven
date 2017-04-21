package com.assesment.pos7eleven;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.assesment.pos7eleven.ClasesAuxiliares.SessionHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by marco on 21/04/2017.
 */

public class EditEmployeeActivity extends AppCompatActivity {

    private EditText nameText;
    private EditText lastnameText;
    private EditText passwordText;
    private EditText usernameText;
    private Spinner positionSpinner;
    private Spinner storeSpinner;
    private LinearLayout linearLayout;
    private String is_staff;
    private String store;
    private Button submitButton;
    private CheckBox activeCheck;
    private int pk;
    private ArrayList<String> items;
    private static final String URL_PATH = "/pos/editstoreemployee/";
    private static final String URL_PATH_UDPATE = "/pos/editemployee/";
    private static final String TAG = CreateEmployeeActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_employee);

        nameText = (EditText) findViewById(R.id.nameField);
        lastnameText = (EditText) findViewById(R.id.lastnameField);
        passwordText = (EditText) findViewById(R.id.passwordFieldE);
        usernameText = (EditText) findViewById(R.id.usernameFieldE);
        positionSpinner = (Spinner) findViewById(R.id.positionSP);
        storeSpinner = (Spinner) findViewById(R.id.storeSP);
        submitButton = (Button) findViewById(R.id.createEmployeeButton);
        linearLayout = (LinearLayout) findViewById(R.id.linearEmployeeActive);
        activeCheck = (CheckBox) findViewById(R.id.isActiveCheckBox);

        Intent intent = getIntent();
        pk = intent.getIntExtra(getString(R.string.pk), 1);

        init();

        positionSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if("Gerente".equals(parent.getSelectedItem().toString()))
                    is_staff = "true";
                else
                    is_staff = "false";
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                is_staff = "true";
            }
        });

        storeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                store = parent.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                store = parent.getSelectedItem().toString();
            }
        });

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submit();
            }
        });
    }

    private void submit() {
        String is_active;

        if (activeCheck.isChecked())
            is_active = "true";
        else
            is_active = "false";

        RequestBody body = new FormBody.Builder().add("username", usernameText.getText().toString()).
                add("password", passwordText.getText().toString()).
                add("first_name", nameText.getText().toString()).
                add("last_name", lastnameText.getText().toString()).
                add("is_staff", is_staff).
                add("is_active", is_active).
                add("store", store).
                build();

        final Request request = new Request.Builder().url(getString(R.string.ip) +
                URL_PATH_UDPATE + pk + "/").
                put(body).build();

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
                            Intent intent = new Intent(getApplicationContext(), AdminActivity.class);
                            startActivity(intent);
                        }
                    });

                } else {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Log.d(TAG, jsondata);
                            Toast.makeText(getApplicationContext(), jsondata,
                                    Toast.LENGTH_LONG).show();
                        }
                    });
                }
            }
        });
    }

    private void setSpinner(String data) throws JSONException {
        JSONArray stores = new JSONArray(data);
        items = new ArrayList<String>();
        JSONObject employee = stores.getJSONObject(0);
        store = employee.getJSONObject("store").getString("location");
        items.add(store);

        for(int i = 1; i < stores.length(); ++i)
            items.add(stores.getJSONObject(i).getString("location"));

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, items);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        storeSpinner.setAdapter(adapter);

        ArrayAdapter<CharSequence> adapterP;

        if (employee.getJSONObject("user").getBoolean("is_staff"))
            adapterP = ArrayAdapter.createFromResource(this,
                    R.array.position, android.R.layout.simple_spinner_item);
        else
            adapterP = ArrayAdapter.createFromResource(this,
                    R.array.back_position, android.R.layout.simple_spinner_item);

        adapterP.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        positionSpinner.setAdapter(adapterP);
    }

    private void setViews(String data) throws JSONException {

        JSONArray dataArr = new JSONArray(data);
        JSONObject user = dataArr.getJSONObject(0).getJSONObject("user");

        nameText.setText(user.getString("first_name"));
        lastnameText.setText(user.getString("last_name"));
        usernameText.setText(user.getString("username"));
        linearLayout.setVisibility(View.VISIBLE);
        activeCheck.setChecked(user.getBoolean("is_active"));
    }

    private void init() {

        Request request = new Request.Builder().url(getString(R.string.ip) + URL_PATH + pk + '/').build();
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
                                setSpinner(jsondata);
                                setViews(jsondata);
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
