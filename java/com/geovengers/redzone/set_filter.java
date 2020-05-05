package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ToggleButton;

import androidx.appcompat.app.AppCompatActivity;

public class set_filter extends AppCompatActivity {

    ToggleButton b_info;
    ToggleButton b_warning;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_filter);

        Button b_disease_select = (Button) findViewById(R.id.disease_select);
        b_disease_select.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(set_filter.this, disease_select.class);
                startActivity(intent);
            }
        });

        Button b_region_select = (Button) findViewById(R.id.region_select);
        b_region_select.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(set_filter.this, region_select.class);
                startActivity(intent);
            }
        });

        b_info = (ToggleButton)findViewById(R.id.info);
        b_info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (b_info.isChecked()) {
                    // 체크 상태(필터 적용) 표시
                } else {
                    // 체크 해제 상태(필터 해제) 표시
                }
            }
        });

        b_warning = (ToggleButton)findViewById(R.id.warning);
        b_warning.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (b_warning.isChecked()) {
                    // 체크 상태(필터 적용) 표시
                } else {
                    // 체크 해제 상태(필터 해제) 표시
                }
            }
        });
    }
}
