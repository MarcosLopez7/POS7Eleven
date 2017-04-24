package com.assesment.pos7eleven;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.assesment.pos7eleven.ClasesAuxiliares.SessionHelper;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Request;
import okhttp3.Response;

public class TrendActivity extends AppCompatActivity {

    private BarChart hourChart;
    ArrayList<BarEntry> barEntries;
    private static final String URL_PATH = "/pos/stats/sales/";
    private static final String TAG = TrendActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trend);

        hourChart = (BarChart) findViewById(R.id.hourChart);
        barEntries = new ArrayList<BarEntry>();

        init();
    }

    private void setChart(String data) throws JSONException {
        JSONObject json = new JSONObject(data);
        JSONArray array = json.getJSONArray("products");
        ArrayList<String> dates = new ArrayList<>();

        for (int i = 0; i < array.length(); i++) {
            barEntries.add(new BarEntry(array.getJSONObject(i).getInt("quantity"), i));
            dates.add(array.getJSONObject(i).getString("name"));
        }

        BarDataSet dataSet = new BarDataSet(barEntries, "Trends by hours");
        BarData barData = new BarData(dataSet);

        hourChart.setData(barData);
        hourChart.setTouchEnabled(true);
        hourChart.setDragEnabled(true);
        hourChart.setScaleEnabled(true);

    }

    private void init() {

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
                                Log.d(TAG, jsondata);
                                setChart(jsondata);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                }
            }
        });

    }
}
