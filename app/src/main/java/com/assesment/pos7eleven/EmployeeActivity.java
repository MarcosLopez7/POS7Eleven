package com.assesment.pos7eleven;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.assesment.pos7eleven.ClasesAuxiliares.SessionHelper;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Request;
import okhttp3.Response;

import static com.assesment.pos7eleven.R.id.isActiveText;

public class EmployeeActivity extends AppCompatActivity {

    private TextView employeeIDText;
    private TextView nameText;
    private TextView usernameText;
    private TextView positionText;
    private TextView storeText;
    private TextView activeText;
    private Button deleteButton;
    private Button editButtont;
    private int pk;
    private static final String URL_PATH = "/pos/employee/";
    private static final String URL_PATH_DELETE = "/pos/deleteemployee/";
    private static final String TAG = EmployeeActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee);

        employeeIDText = (TextView) findViewById(R.id.employeeID);
        nameText = (TextView) findViewById(R.id.nameText);
        usernameText = (TextView) findViewById(R.id.usernameText);
        positionText = (TextView) findViewById(R.id.positionText);
        storeText = (TextView) findViewById(R.id.storeText);
        activeText = (TextView) findViewById(isActiveText);
        deleteButton = (Button) findViewById(R.id.deleteEmployeeButton);
        editButtont = (Button) findViewById(R.id.editEmployeeButton);

        Intent intent = getIntent();
        pk = intent.getIntExtra(getString(R.string.pk), 1) + 1;

        init();

        editButtont.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteUser();
            }
        });
    }

    private void deleteUser() {
        Request request = new Request.Builder().
                url(getString(R.string.ip) + URL_PATH_DELETE + pk + '/')
                .delete()
                .build();

        SessionHelper.client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                //final String jsondata = response.body().string();
                if (response.isSuccessful()){
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Intent intent = new Intent(getApplicationContext(), AdminActivity.class);
                            startActivity(intent);
                        }
                    });
                }
            }
        });
    }

    private void setText (String data) throws JSONException {
        JSONObject employee = new JSONObject(data);

        employeeIDText.setText("Empleado " + employee.getInt("pk"));
        nameText.setText("Nombre: " + employee.getJSONObject("user").getString("first_name") + " " +
                employee.getJSONObject("user").getString("last_name"));
        usernameText.setText("Nombre de usuario: " +
                employee.getJSONObject("user").getString("username"));
        storeText.setText("Tienda: " + employee.getJSONObject("store").getString("location"));


        if(employee.getJSONObject("user").getBoolean("is_active"))
            activeText.setText("Activo");
        else
            activeText.setText("Inactivo");

        if (employee.getJSONObject("user").getBoolean("is_staff"))
            positionText.setText("Posición: Gerente");
        else
            positionText.setText("Posición: Empleado");
    }

    private void init() {
        Request request = new Request.Builder().url(getString(R.string.ip) + URL_PATH + pk + '/')
                .build();
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
                                setText(jsondata);
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
