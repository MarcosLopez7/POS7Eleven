package com.assesment.pos7eleven;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.NotificationCompat;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.assesment.pos7eleven.ClasesAuxiliares.ProductCustomAdapter;
import com.assesment.pos7eleven.ClasesAuxiliares.SessionHelper;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.MediaType;

public class SellActivity extends AppCompatActivity {

    private Button scanButton;
    private ListView productListView;
    private static final String URL_PATH_STORE = "/pos/stores/";
    private static final String URL_PATH_INVENTORY = "/inventory/";
    private static final String URL_PATH = "/pos/sales/create/";
    private static final String TAG = SellActivity.class.getSimpleName();
    private TextView totalPriceText;
    private Spinner methodSpinner;
    private  Button paymentButton;
    private ArrayList<ArrayList<String>> items;
    ProductCustomAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sell);

        scanButton = (Button) findViewById(R.id.scanSellProduct);
        productListView = (ListView) findViewById(R.id.listViewProductSell);
        totalPriceText = (TextView) findViewById(R.id.totalPriceText);
        methodSpinner = (Spinner) findViewById(R.id.methodPaySpinner);
        paymentButton = (Button) findViewById(R.id.paymentButton);
        totalPriceText.setText("0.00");

        ArrayAdapter<CharSequence> adapterMehtod = ArrayAdapter.createFromResource(this,
                R.array.payment_method, android.R.layout.simple_spinner_item);
        adapterMehtod.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        methodSpinner.setAdapter(adapterMehtod);

        final Activity activity = this;

        setList();

        scanButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IntentIntegrator integrator = new IntentIntegrator(activity);
                integrator.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES);
                integrator.setPrompt("Scanner Terminal");
                integrator.setCameraId(0);
                integrator.setBeepEnabled(true);
                integrator.setBarcodeImageEnabled(false);
                integrator.initiateScan();
            }
        });

        methodSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //store = parent.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                //store = parent.getSelectedItem().toString();
            }
        });

        paymentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!items.isEmpty())
                    pay();
                else
                    Toast.makeText(getApplicationContext(),
                            "There are any products in the list to sell",
                            Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void pay(){

        String json = "{\"store\": " + SessionHelper.store_id + "," +
                "\"gender\": \"null\"," +
                "\"age\": 0," +
                "\"products\": [";

        for (int i = 0; i < items.size(); i++) {
            String element = "{ \"barcode\": \"" + items.get(i).get(4) + "\"," +
                    "\"quantity\": " + items.get(i).get(2) + "}";
            if (i + 1 != items.size())
                element += ",";

            json += element;
        }

        json += "]}";

        Log.d(TAG, json);

        MediaType contentType = MediaType.parse("application/json; charset=utf-8");
        RequestBody body = RequestBody.create(contentType, json);
        Request request = new Request.Builder().url(getString(R.string.ip) + URL_PATH)
                .post(body).build();

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
                            Toast.makeText(getApplicationContext(), "successful sell!", Toast.LENGTH_SHORT)
                                    .show();
                            Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                            startActivity(intent);
                        }
                    });
                } else {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(), jsondata, Toast.LENGTH_SHORT)
                                    .show();
                            Log.d(TAG, jsondata);
                        }
                    });
                }
            }
        });
    }

    private void setList() {
        items = new ArrayList<ArrayList<String>>();

        adapter = new ProductCustomAdapter(getApplicationContext(), items);

        productListView.setAdapter(adapter);

        productListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //Log.d(TAG, items.get(position).get(0));
                int quantity = Integer.parseInt(items.get(position).get(2));
                if (Integer.parseInt(items.get(position).get(3)) > quantity + 1) {
                    items.get(position).set(2, (quantity + 1) + "");
                    totalPriceText.setText( (Double.parseDouble(totalPriceText.getText().toString()) +
                            Double.parseDouble(items.get(position).get(1))) + "");
                    adapter.notifyDataSetChanged();
                } else
                    Toast.makeText(getApplicationContext(),
                            "This is the maximum quantity of this product in this store",
                            Toast.LENGTH_SHORT).show();
            }
        });

        productListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                int quantity = Integer.parseInt(items.get(position).get(2));
                if (quantity - 1 > 0) {
                    items.get(position).set(2, (quantity - 1) + "");
                    totalPriceText.setText( (Double.parseDouble(totalPriceText.getText().toString()) -
                            Double.parseDouble(items.get(position).get(1))) + "");
                    adapter.notifyDataSetChanged();
                } else {
                    totalPriceText.setText( (Double.parseDouble(totalPriceText.getText().toString()) -
                            Double.parseDouble(items.get(position).get(1))) + "");
                    items.remove(position);
                    adapter.notifyDataSetChanged();
                }



                return false;
            }
        });
    }

    private void productToList(String data) throws JSONException {
        JSONObject json = new JSONObject(data);

        ArrayList<String> row = new ArrayList<String>();

        row.add(json.getJSONObject("product").getString("name"));
        row.add(json.getJSONObject("product").getString("store_price"));
        row.add("1");
        row.add(json.getInt("quantity") + "");
        row.add(json.getJSONObject("product").getString("barcode"));

        totalPriceText.setText( (Double.parseDouble(totalPriceText.getText().toString()) +
                Double.parseDouble(json.getJSONObject("product").getString("store_price"))) + "");

        items.add(row);
        adapter.notifyDataSetChanged();
    }

    private boolean checkProductInList(String data) throws JSONException {
        boolean productInList = false;
        JSONObject json = new JSONObject(data);
        String product = json.getJSONObject("product").getString("name");

        for(int i = 0; i < items.size(); i++) {
            if (product.equals(items.get(i).get(0))) {
                productInList = true;
                break;
            }
        }

        return productInList;
    }

    private void addProduct(String barcode) {

        Request request = new Request.Builder().url(getString(R.string.ip) +
                URL_PATH_STORE + SessionHelper.store_id + URL_PATH_INVENTORY + barcode + "/")
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
                                if(!checkProductInList(jsondata))
                                    productToList(jsondata);
                                else
                                    Toast.makeText(getApplicationContext(),
                                            "Product is already in the list",
                                            Toast.LENGTH_SHORT).show();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                } else {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(),
                                    "The product is not registered in the inventory",
                                    Toast.LENGTH_SHORT);
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
                addProduct(result.getContents());
                //Toast.makeText(this, "Scanned: " + result.getContents(), Toast.LENGTH_SHORT).show();
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }

    }
}
