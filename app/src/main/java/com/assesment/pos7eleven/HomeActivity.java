package com.assesment.pos7eleven;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        nameText = (TextView) findViewById(R.id.nameEmployeeText);
        storeText = (TextView) findViewById(R.id.storeEmployeeText);
        registerButton = (Button) findViewById(R.id.registerButton);
        sellButton = (Button) findViewById(R.id.sellButton);
        productButton = (Button) findViewById(R.id.productButton);

        nameText.setText(SessionHelper.first_name + " " + SessionHelper.last_name);

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        sellButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        productButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }
}
