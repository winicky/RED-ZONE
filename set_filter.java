package com.example.redzone;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ToggleButton;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Date;

public class set_filter extends AppCompatActivity {

    ToggleButton b_info;
    ToggleButton b_warning;

    String result_disease[] = new String[30];
    String result_region[] = new String[30];

    boolean status_info = false;
    boolean status_warning = false;

    Date start_date = new Date();
    Date end_date = new Date();


    public static final int REQUEST_CODE_DISEASE = 1002;
    public static final int REQUEST_CODE_REGION = 1003;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_filter);

        ImageButton b_reset = (ImageButton) findViewById(R.id.reset);
        b_reset.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                //  클릭해서 적용 되었던 필터 설정을 전부 해제
            }
        });

        ImageButton b_apply = (ImageButton) findViewById(R.id.apply);
        b_apply.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {

                // 필터 설정을 전부 파라미터로 넘기고 set_filter로 이동(뒤로가기)


                onBackPressed();
            }
        });

        ImageButton b_exit = (ImageButton) findViewById(R.id.exit);
        b_exit.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {

                //  필터 전부 해제 후 뒤로가기

                onBackPressed();
            }
        });

        Button b_disease_select = (Button) findViewById(R.id.disease_select);
        b_disease_select.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                Intent go_disease_intent = new Intent(set_filter.this, disease_select.class);
                startActivityForResult(go_disease_intent, REQUEST_CODE_DISEASE);
            }
        });

        Button b_region_select = (Button) findViewById(R.id.region_select);
        b_region_select.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                Intent go_region_intent = new Intent(set_filter.this, region_select.class);
                startActivityForResult(go_region_intent, REQUEST_CODE_REGION);
            }
        });

        b_info = (ToggleButton)findViewById(R.id.info);
        b_info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (b_info.isChecked()) {                       //  info 버튼이 체크 되어있는 상태이므로
                    status_info = false;                        //  status_info를 false로 변경
                } else {                                        //  info 버튼이 체크 해제 되어있는 상태이므로
                    status_info = true;                         //  status_info를 true로 변경
                }
            }
        });

        b_warning = (ToggleButton)findViewById(R.id.warning);
        b_warning.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (b_warning.isChecked()) {                    //  warning 버튼이 체크 되어있는 상태이므로
                    status_warning = false;                     //  status_warning을 false로 변경
                } else {                                        //  warning 버튼이 체크 해제 되어있는 상태이므로
                    status_warning = true;                      //  status_warning을 true로 변경
                }
            }
        });


    }
}
