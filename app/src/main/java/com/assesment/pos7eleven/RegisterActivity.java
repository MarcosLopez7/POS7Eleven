package com.assesment.pos7eleven;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.assesment.pos7eleven.ClasesAuxiliares.SessionHelper;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class RegisterActivity extends AppCompatActivity {

    private EditText orderIDText;
    private Button scanButton;
    private static final String URL_PATH = "/pos/inventory/create/";
    private static final String TAG = RegisterActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        orderIDText = (EditText) findViewById(R.id.orderIDEditText);
        scanButton = (Button) findViewById(R.id.scannRegisterButton);
        final Activity activity = this;

        scanButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!"".equals(orderIDText.getText().toString()))
                    if(Integer.parseInt(orderIDText.getText().toString()) > 0) {
                        IntentIntegrator integrator = new IntentIntegrator(activity);
                        integrator.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES);
                        integrator.setPrompt("Scanner Terminal");
                        integrator.setCameraId(0);
                        integrator.setBeepEnabled(true);
                        integrator.setBarcodeImageEnabled(false);
                        integrator.initiateScan();
                    } else
                        Toast.makeText(getApplicationContext(), "Please, insert a valid order ID",
                                Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(getApplicationContext(), "Please, insert a order ID",
                            Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void register(String barcode) {
        RequestBody body = new FormBody.Builder().add("barcode", barcode).
                add("order", orderIDText.getText().toString()).
                add("store", SessionHelper.store_id + "").
                build();

        final Request request = new Request.Builder().url(getString(R.string.ip) + URL_PATH).
                post(body).build();

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
                            if (jsondata.equals("\"created\"")) {
                                Toast.makeText(getApplicationContext(), "Product registred",
                                        Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(getApplicationContext(),
                                        "Error! Product not registred", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                } else {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(),
                                    "Error! Product not registred", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);

        if (result != null) {
            if (result.getContents() == null) {
                Toast.makeText(this, "Cancelled", Toast.LENGTH_SHORT).show();
            } else {
                register(result.getContents());
                //Toast.makeText(this, "Scanned: " + result.getContents(), Toast.LENGTH_SHORT).show();
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }

    }
}
