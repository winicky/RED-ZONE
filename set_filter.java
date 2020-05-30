package com.geovengers.redzone;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
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

    public static final int REQUEST_CODE_FILTER = 1001;
    public static final int REQUEST_CODE_DISEASE = 1002;
    public static final int REQUEST_CODE_REGION = 1003;


    Bundle total_bundle = new Bundle();
    String code_region = new String();
    String name_region = new String();
    String disease = new String();
    int info=0, warning=0;
    String startdate = new String();
    String enddate = new String();


    int num_first = 17;
    int i= 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_filter);

        String[] name_first = new String[num_first];            //  1차 지역의 이름을 저장
        String[] code_first = new String[num_first];            //  1차 지역의 법정동 코드를 저장
        for (i = 0; i < num_first; i++) {
            if (i < 7) {
                name_first[i] = MainActivity.locParent[i].locationName;
                code_first[i] = MainActivity.locParent[i].locationCode;
            }
            else if (i == 7) {                                           //  i=7 : 세종
                name_first[i] = "세종특별자치시";                       //  선택용 UI를 하나 만들어주고
                code_first[i] = "3611000000";                       //  값은 강제로 설정
            }
            else {
                name_first[i] = MainActivity.locParent[i-1].locationName;
                code_first[i] = MainActivity.locParent[i-1].locationCode;
            }
        }


        ImageButton b_reset = (ImageButton) findViewById(R.id.reset);
        b_reset.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                //  클릭해서 적용 되었던 필터 설정을 전부 해제
                b_info.setChecked(false);
                status_info = false;
                b_warning.setChecked(false);
                status_warning = false;
            }
        });

        ImageButton b_apply = (ImageButton) findViewById(R.id.apply);
        b_apply.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                // 필터 설정을 전부 파라미터로 넘기고 MainActivity로 이동(finish)
                Intent return_filter_intent = new Intent(getApplicationContext(), MainActivity.class);
                total_bundle.putString("CODE_REGION", code_region);
                total_bundle.putString("DISEASE", disease);
                total_bundle.putInt("INFO", info);
                total_bundle.putInt("WARNING", warning);
                total_bundle.putString("STARTDATE", startdate);
                total_bundle.putString("ENDDATE", enddate);
                return_filter_intent.putExtra("TOTAL_BUNDLE", total_bundle);
                setResult(REQUEST_CODE_FILTER, return_filter_intent);
                finish();
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
                if (!b_info.isChecked()) {                      //  info 버튼이 체크 되어있는 상태이므로
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
                if (!b_warning.isChecked()) {                   //  warning 버튼이 체크 되어있는 상태이므로
                    status_warning = false;                     //  status_warning을 false로 변경
                } else {                                        //  warning 버튼이 체크 해제 되어있는 상태이므로
                    status_warning = true;                      //  status_warning을 true로 변경
                }
            }
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Check which request we're responding to
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_FILTER) {
            if (resultCode == RESULT_OK) {
                total_bundle = data.getBundleExtra("BUNDLE");
                code_region = total_bundle.getString("PARAMETER_CODE");
                name_region = total_bundle.getString("PARAMETER_NAME");


            }
        }
    }
    /*
    protected void onResume() {
        TextView check_region = (TextView) findViewById(R.id.check_region);
        check_region.setText("region : " + name_region);
        super.onResume();
    }*/
}
