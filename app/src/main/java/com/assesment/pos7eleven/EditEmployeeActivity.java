package com.assesment.pos7eleven;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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

import com.assesment.pos7eleven.ClasesAuxiliares.SessionHelper;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Request;
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

    }

    private void setSpinner(String data) throws JSONException {
        JSONArray stores = new JSONArray(data);
        items = new ArrayList<String>();
        store = stores.getJSONObject(0).getString("location");

        for(int i = 0; i < stores.length(); ++i)
            items.add(stores.getJSONObject(i).getString("location"));

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, items);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        storeSpinner.setAdapter(adapter);

        ArrayAdapter<CharSequence> adapterP = ArrayAdapter.createFromResource(this,
                R.array.position, android.R.layout.simple_spinner_item);
        adapterP.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        positionSpinner.setAdapter(adapterP);
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
