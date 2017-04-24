package com.assesment.pos7eleven;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.assesment.pos7eleven.ClasesAuxiliares.SessionHelper;

public class HomeActivity extends AppCompatActivity {

    private TextView nameText;
    private TextView storeText;
    private Button registerButton;
    private Button sellButton;
    private Button productButton;
    private Button trendButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        nameText = (TextView) findViewById(R.id.nameEmployeeText);
        storeText = (TextView) findViewById(R.id.storeEmployeeText);
        registerButton = (Button) findViewById(R.id.registerButton);
        sellButton = (Button) findViewById(R.id.sellButton);
        productButton = (Button) findViewById(R.id.productButton);
        trendButton = (Button) findViewById(R.id.trendsButton);

        nameText.setText(SessionHelper.first_name + " " + SessionHelper.last_name);

        if (SessionHelper.staff_user)
            trendButton.setVisibility(View.VISIBLE);

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), RegisterActivity.class);
                startActivity(intent);
            }
        });

        sellButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), SellActivity.class);
                startActivity(intent);
            }
        });

        productButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        trendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), TrendActivity.class);
                startActivity(intent);
            }
        });
    }
}
