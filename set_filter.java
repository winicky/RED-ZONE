package com.geovengers.redzone;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class set_filter extends AppCompatActivity {

    private View     decorView;
    private int    uiOption;

    ToggleButton b_info;
    ToggleButton b_warning;

    String result_disease[] = new String[30];
    String result_region[] = new String[30];

    boolean status_info = false;
    boolean status_warning = false;

    public static final int REQUEST_CODE_DISEASE = 1002;
    public static final int REQUEST_CODE_REGION = 1003;

    int is_calendar_on = 0;

    Bundle total_bundle = new Bundle();             //  region_select -> set_filter로 받는 번들
    String first_name = new String();
    String code_region = new String();
    String name_region = new String();

    String start_date;
    String end_date;
    Date first_date;
    Date second_date;

    MsgRequest bundle = new MsgRequest();           //  set_filter -> mainActivity로 쏘는 번들

    int num_first = 17;
    int i = 0;
    int start_year;
    int start_month;
    int start_dayofmonth;
    int end_year;
    int end_month;
    int end_dayofmonth;

    LinearLayout temp;
    List<String> disaster_type = new ArrayList<>();
    List<String> disaster_level = new ArrayList<>();
    String disaster_group = new String();
    String current_disaster_name = new String();
    String temp_name = new String();
    ImageView help_filter;
    CalendarView start_calendar;
    CalendarView end_calendar;
    FrameLayout frame_calendar;
    TextView textview_start_date;
    TextView textview_end_date;
    TextView current_region;
    TextView current_disaster_first;
    TextView current_disaster_second;
    LinearLayout current_filter_layout;

    ImageView help_set_filter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_filter);
        final Typeface koregular = Typeface.createFromAsset(getAssets(), "fonts/koregular.ttf");

        start_date = "";
        end_date = "";

        first_date = new Date();
        second_date = new Date();

        start_calendar = new CalendarView(this);
        end_calendar = new CalendarView(this);
        frame_calendar = (FrameLayout) findViewById(R.id.frame_calendar);
        textview_start_date = (TextView) findViewById(R.id.textview_start_date);
        textview_end_date = (TextView) findViewById(R.id.textview_end_date);

        current_filter_layout = new LinearLayout(this);
        code_region = null;

        ImageButton b_reset = (ImageButton) findViewById(R.id.reset);
        b_reset.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                //  클릭해서 적용 되었던 필터 설정을 전부 해제
                b_info.setChecked(false);
                status_info = false;
                b_warning.setChecked(false);
                status_warning = false;
                code_region = null;
                disaster_type.clear();
                start_date = "";
                end_date = "";
                Toast.makeText(set_filter.this, "초기화 되었습니다.", Toast.LENGTH_LONG).show();
                current_disaster_first.setText("현재 재난 1차 설정 값 : ");
                current_disaster_second.setText("현재 재난 2차 설정 값 : ");
                current_region.setText("현재 지역 설정 값 : ");
                textview_start_date.setText(start_date);
                textview_end_date.setText(end_date);
            }
        });

        ImageButton b_apply = (ImageButton) findViewById(R.id.apply);
        b_apply.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {

                // 필터 설정을 전부 파라미터로 넘기고 MainActivity로 이동(finish)
                if (code_region == null){
                    Toast.makeText(set_filter.this, "지역을 선택해 주세요.", Toast.LENGTH_LONG).show();
                }
                else if (disaster_type.isEmpty()){
                    Toast.makeText(set_filter.this, "재난을 선택해 주세요.", Toast.LENGTH_LONG).show();
                }
                else if (start_date == ""){
                    Toast.makeText(set_filter.this, "시작 날짜를 선택해 주세요.", Toast.LENGTH_LONG).show();
                }
                else if (end_date == ""){
                    Toast.makeText(set_filter.this, "끝 날짜를 선택해 주세요.", Toast.LENGTH_LONG).show();
                }
                /*else if (start_calendar.getDate() - end_calendar.getDate() > 0){
                    Toast.makeText(set_filter.this, "날짜 입력을 확인해주세요.", Toast.LENGTH_LONG).show();
                }*/
                else {
                    Intent return_filter_intent = new Intent(getApplicationContext(), MainActivity.class);

                    if (b_info.isChecked()) {
                        disaster_level.add("info");
                    }
                    if (b_warning.isChecked()) {
                        disaster_level.add("warning");
                    }
                    if (!b_info.isChecked() && !b_warning.isChecked()) {
                        disaster_level.add("info");
                        disaster_level.add("warning");
                    }
                    bundle.setDisaster_level(disaster_level);
                    bundle.setDisaster_group(disaster_group);
                    bundle.setDisaster_type(disaster_type);
                    bundle.setLocation_code(code_region);
                    bundle.setStart_date(start_date);
                    bundle.setEnd_date(end_date);
                    bundle.setLocation_code(code_region);

                    return_filter_intent.putExtra("TOTAL_BUNDLE", bundle);
                    setResult(RESULT_OK, return_filter_intent);
                    finish();
                }
            }
        });

        help_set_filter = findViewById(R.id.help_set_filter);
        help_set_filter.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                help_set_filter.setVisibility(View.INVISIBLE);
            }
        });
        help_set_filter.setVisibility(View.INVISIBLE);

        ImageButton b_help = (ImageButton) findViewById(R.id.help);
        b_help.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                help_set_filter.setVisibility(View.VISIBLE);
            }
        });

        Button b_disaster_select = (Button) findViewById(R.id.disaster_select);
        b_disaster_select.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                Intent go_disaster_intent = new Intent(set_filter.this, disaster_select.class);
                startActivityForResult(go_disaster_intent, REQUEST_CODE_DISEASE);
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

        textview_start_date.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                is_calendar_on = 1;
                current_filter_layout.setVisibility(View.INVISIBLE);
                end_calendar.setVisibility(View.INVISIBLE);
                start_calendar.setVisibility(View.VISIBLE);
            }
        });

        textview_end_date.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                is_calendar_on = 2;
                current_filter_layout.setVisibility(View.INVISIBLE);
                start_calendar.setVisibility(View.INVISIBLE);
                end_calendar.setVisibility(View.VISIBLE);
            }
        });



        current_filter_layout.setOrientation(LinearLayout.VERTICAL);

        current_region = new TextView(this);
        current_region.setTypeface(koregular);
        current_region.setText("현재 지역 설정 값 : ");
        current_filter_layout.addView(current_region);

        current_disaster_first = new TextView(this);
        current_disaster_first.setTypeface(koregular);
        current_disaster_first.setText("현재 재난 1차 설정 값 : ");
        current_filter_layout.addView(current_disaster_first);

        current_disaster_second = new TextView(this);
        current_disaster_second.setTypeface(koregular);
        current_disaster_second.setText("현재 재난 2차 설정 값 : ");
        current_filter_layout.addView(current_disaster_second);

        frame_calendar.addView(current_filter_layout);


        frame_calendar.addView(start_calendar);
        start_calendar.setVisibility(View.INVISIBLE);
        start_calendar.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                start_date = year + "-" + (month+1) + "-" + dayOfMonth;
                start_year = year;
                start_month = month+1;
                start_dayofmonth = dayOfMonth;
                first_date.setTime(start_calendar.getDate());
                textview_start_date.setText(start_date);
                start_calendar.setVisibility(View.INVISIBLE);
                current_filter_layout.setVisibility(View.VISIBLE);
            }
        });

        frame_calendar.addView(end_calendar);
        end_calendar.setVisibility(View.INVISIBLE);
        end_calendar.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                end_date = year + "-" + (month+1) + "-" + dayOfMonth;
                end_year = year;
                end_month = month+1;
                end_dayofmonth = dayOfMonth;
                textview_end_date.setText(end_date);
                end_calendar.setVisibility(View.INVISIBLE);
                current_filter_layout.setVisibility(View.VISIBLE);
            }
        });





    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Check which request we're responding to
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case (REQUEST_CODE_REGION) :
                if (resultCode == RESULT_OK) {
                    total_bundle = data.getBundleExtra("REGION_BUNDLE");
                    first_name = total_bundle.getString("FIRST_NAME");
                    code_region = total_bundle.getString("PARAMETER_CODE");
                    name_region = total_bundle.getString("PARAMETER_NAME");
                    current_region.setText("현재 지역 설정 값 : " + first_name + " " + name_region);
                }
                break;
            case (REQUEST_CODE_DISEASE) :
                if (resultCode == RESULT_OK) {
                    MsgRequest disasterRequest = (MsgRequest) data.getSerializableExtra("DISASTER_LIST");
                    disaster_type = disasterRequest.getDisaster_type();
                    disaster_group = disasterRequest.getDisaster_group();
                    current_disaster_name = "";
                    temp_name = "";
                    int roofcount = 0;
                    for (roofcount=0;roofcount<disaster_type.size();roofcount++){
                        if (roofcount==0){
                            current_disaster_name = " " + disaster_type.get(roofcount);
                        }
                        else {
                            temp_name = " " + disaster_type.get(roofcount);
                            current_disaster_name = current_disaster_name + temp_name;
                        }
                    }

                    current_disaster_first.setText("현재 재난 1차 설정 값 : " + disaster_group);
                    current_disaster_second.setText("현재 재난 2차 설정 값 :" + current_disaster_name);

                    //  'data' 인텐트에서 getSerializableExtra로 MsgRequest 객체를 번들로 받아 그 안에 저장한 Disaster_type을 저장
                }
        }
    }

    @Override
    public void onBackPressed() {
        if (is_calendar_on == 1){
            is_calendar_on = 0;
            start_calendar.setVisibility(View.INVISIBLE);
        }
        else if (is_calendar_on == 2){
            is_calendar_on = 0;
            end_calendar.setVisibility(View.INVISIBLE);
        }
        else {
            super.onBackPressed();
        }
    }
    /*
    protected void onResume() {
        TextView check_region = (TextView) findViewById(R.id.check_region);
        check_region.setText("region : " + name_region);
        super.onResume();
    }*/
}
