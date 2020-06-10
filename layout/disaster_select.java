package com.geovengers.redzone;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.appcompat.app.AppCompatActivity;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class disaster_select extends AppCompatActivity {

    final int NUM_FIRST = 3;
    final int NUM_WEATHER = 11 + 1;
    final int NUM_DISEASE = 3 + 1;
    final int NUM_OTHER = 1 + 1;

    int i = 0;
    int checkcount_first = 0;
    int checkcount_second = 0;
    CompoundButton arg_first = null;

    String disaster_group = new String();

    public static List<String> disaster_list = new ArrayList<>();

    public LinearLayout layout_all;

    String[] name_first = new String[NUM_FIRST];

    String[] name_weather = new String[NUM_WEATHER];
    String[] name_disease = new String[NUM_DISEASE];
    String[] name_other = new String[NUM_OTHER];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_disaster_select);

        layout_all = new LinearLayout(this);

        /*disaster_list = null;
        disaster_group = null;*/

        ImageButton b_reset = (ImageButton) findViewById(R.id.reset);
        b_reset.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                //  클릭해서 적용 되었던 필터 설정을 전부 해제
                disaster_group = null;
                disaster_list.clear();
                if (arg_first != null && arg_first.isChecked()){
                    arg_first.setChecked(false);
                }
                arg_first = null;



                checkcount_first = 0;
                checkcount_second = 0;
            }
        });

        ImageButton b_apply = (ImageButton) findViewById(R.id.apply);
        b_apply.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                if (disaster_group == null){
                    Toast.makeText(disaster_select.this, "1차분류를 선택해주세요", Toast.LENGTH_LONG).show();
                }
                else if (disaster_list.isEmpty()){
                    Toast.makeText(disaster_select.this, "2차분류를 선택해주세요", Toast.LENGTH_LONG).show();
                }
                else {
                    // 필터 설정을 전부 파라미터로 넘기고 set_filter로 이동(뒤로가기)
                    Intent return_disease_intent = new Intent();
                    MsgRequest mybundle = new MsgRequest();                                             //  임시로 MsgRequest 객체를 만들어서
                    mybundle.setDisaster_type(disaster_list);                                           //  mybundle에 Disaster_type만 disaster_list로 저장해서
                    mybundle.setDisaster_group(disaster_group);
                    return_disease_intent.putExtra("DISASTER_LIST", mybundle);                  //  mybundle을 통째로 번들에 담아 set_fliter로 쏘고 finish
                    return_disease_intent.putExtra("COUNT_DISASTER", checkcount_second);        //  checkcount_second 역시 번들에 담아 set_filter로 쏜다(몇 개의 재난을 선택했는지)
                    setResult(RESULT_OK, return_disease_intent);
                    finish();
                }
            }
        });

        ImageButton b_exit = (ImageButton) findViewById(R.id.exit);
        b_exit.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {

                //  필터 전부 해제 후 뒤로가기

                onBackPressed();
            }
        });

        //  name_first 정의
        name_first[0] = "기상특보";
        name_first[1] = "질병";
        name_first[2] = "Other";

        //  name_weather (name_first[0]) 정의
        name_weather[0] = "전체";
        name_weather[1] = "강풍";
        name_weather[2] = "호우";
        name_weather[3] = "한파";
        name_weather[4] = "건조";
        name_weather[5] = "폭풍해일";
        name_weather[6] = "풍랑";
        name_weather[7] = "태풍";
        name_weather[8] = "대설";
        name_weather[9] = "황사";
        name_weather[10] = "폭염";
        name_weather[11] = "홍수";

        //  name_disease (name_first[1]) 정의
        name_disease[0] = "전체";
        name_disease[1] = "구제역";
        name_disease[2] = "AI";
        name_disease[3] = "코로나";

        //  name_other (name_first[2]) 정의
        name_other[0] = "전체";
        name_other[1] = "Other";

        // 질병 목록에 따라 동적으로 뷰를 생성해서 띄우는 코드
        LinearLayout layout_first = (LinearLayout)findViewById(R.id.layout_disaster_first);
        FrameLayout frame_second = (FrameLayout) findViewById(R.id.frame_disaster_second);


        final LinearLayout layout_weather = new LinearLayout(this);
        layout_weather.setOrientation(LinearLayout.VERTICAL);
        frame_second.addView(layout_weather);
        layout_weather.setVisibility(View.INVISIBLE);
        final ToggleButton[] button_weather = new ToggleButton[NUM_WEATHER];
        //  전체
        button_weather[0] = new ToggleButton(this);
        button_weather[0].setText(name_weather[0]);
        button_weather[0].setTextOn(name_weather[0]);
        button_weather[0].setTextOff(name_weather[0]);
        button_weather[0].setChecked(false);
        button_weather[0].setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){                                                 //  버튼이 눌리면 (ON 상태)
                    for (i = 1; i < NUM_WEATHER; i++) {
                        if (!(button_weather[i].isChecked()))
                            button_weather[i].setChecked(true);                     //  전부 ON
                    }
                }
                else {
                    for (i = 1; i < NUM_WEATHER; i++) {                         //  버튼이 꺼지면 (OFF 상태)
                        if (button_weather[i].isChecked())
                            button_weather[i].setChecked(false);                    //  전부 OFF
                    }
                }
            }
        });
        layout_weather.addView(button_weather[0]);

        for (i = 1; i < NUM_WEATHER; i++) {
            button_weather[i] = new ToggleButton(this);
            button_weather[i].setText(name_weather[i]);
            button_weather[i].setTextOn(name_weather[i]);
            button_weather[i].setTextOff(name_weather[i]);
            button_weather[i].setChecked(false);
            button_weather[i].setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){
                public void onCheckedChanged(CompoundButton arg0, boolean ischecked){
                    if (!ischecked) {
                        disaster_list.remove(arg0.getText().toString());
                        checkcount_second--;
                    }
                    else {
                        disaster_list.add(arg0.getText().toString());
                        checkcount_second++;
                    }
                }
            });
            layout_weather.addView(button_weather[i]);
        }

        final LinearLayout layout_disease = new LinearLayout(this);

        layout_disease.setOrientation(LinearLayout.VERTICAL);
        frame_second.addView(layout_disease);
        layout_disease.setVisibility(View.INVISIBLE);
        final ToggleButton[] button_disease = new ToggleButton[NUM_DISEASE];
        //  전체
        button_disease[0] = new ToggleButton(this);
        button_disease[0].setText(name_disease[0]);
        button_disease[0].setTextOn(name_disease[0]);
        button_disease[0].setTextOff(name_disease[0]);
        button_disease[0].setChecked(false);
        button_disease[0].setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){                                                 //  버튼이 눌리면 (ON 상태)
                    for (i = 1; i < NUM_DISEASE; i++) {
                        if (!(button_disease[i].isChecked()))
                            button_disease[i].setChecked(true);                     //  전부 ON
                    }
                }
                else {
                    for (i = 1; i < NUM_DISEASE; i++) {                         //  버튼이 꺼지면 (OFF 상태)
                        if (button_disease[i].isChecked())
                            button_disease[i].setChecked(false);                    //  전부 OFF
                    }
                }
            }
        });
        layout_disease.addView(button_disease[0]);

        for (i = 1; i < NUM_DISEASE; i++) {
            button_disease[i] = new ToggleButton(this);
            button_disease[i].setText(name_disease[i]);
            button_disease[i].setTextOn(name_disease[i]);
            button_disease[i].setTextOff(name_disease[i]);
            button_disease[i].setChecked(false);
            button_disease[i].setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){
                public void onCheckedChanged(CompoundButton arg0, boolean ischecked){
                    if (!ischecked) {
                        disaster_list.remove(arg0.getText().toString());
                        checkcount_second--;
                    }
                    else {
                        disaster_list.add(arg0.getText().toString());
                        checkcount_second++;
                    }
                }
            });
            layout_disease.addView(button_disease[i]);
        }

        final LinearLayout layout_other = new LinearLayout(this);
        layout_other.setOrientation(LinearLayout.VERTICAL);
        frame_second.addView(layout_other);
        layout_other.setVisibility(View.INVISIBLE);
        final ToggleButton[] button_other = new ToggleButton[NUM_OTHER];

        //  전체
        button_other[0] = new ToggleButton(this);
        button_other[0].setText(name_other[0]);
        button_other[0].setTextOn(name_other[0]);
        button_other[0].setTextOff(name_other[0]);
        button_other[0].setChecked(false);
        button_other[0].setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){                                                 //  버튼이 눌리면 (ON 상태)
                    for (i = 1; i < NUM_OTHER; i++) {
                        if (!(button_other[i].isChecked()))
                            button_other[i].setChecked(true);                     //  전부 ON
                    }
                }
                else {
                    for (i = 1; i < NUM_OTHER; i++) {                         //  버튼이 꺼지면 (OFF 상태)
                        if (button_other[i].isChecked())
                            button_other[i].setChecked(false);                    //  전부 OFF
                    }
                }
            }
        });
        layout_other.addView(button_other[0]);

        for (i = 1; i < NUM_OTHER; i++) {
            button_other[i] = new ToggleButton(this);
            button_other[i].setText(name_other[i]);
            button_other[i].setTextOn(name_other[i]);
            button_other[i].setTextOff(name_other[i]);
            button_other[i].setChecked(false);
            button_other[i].setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){
                public void onCheckedChanged(CompoundButton arg0, boolean ischecked){
                    if (!ischecked) {
                        disaster_list.remove(arg0.getText().toString());
                        checkcount_second--;
                    }
                    else {
                        disaster_list.add(arg0.getText().toString());
                        checkcount_second++;
                    }
                }
            });
            layout_other.addView(button_other[i]);
        }

        final ToggleButton[] button_first = new ToggleButton[NUM_FIRST];          //  1차 카테고리 버튼
        for (i = 0; i < NUM_FIRST; i++) {
            button_first[i] = new ToggleButton(this);
            button_first[i].setTextOn(name_first[i]);
            button_first[i].setTextOff(name_first[i]);
            button_first[i].setChecked(false);
            layout_first.addView(button_first[i]);
        }

        button_first[0].setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton arg0, boolean ischecked) {
                if (ischecked){
                    if (checkcount_first != 0) {
                        arg_first.setChecked(false);
                    }
                    checkcount_first++;
                    arg_first = arg0;
                    disaster_group = name_first[0];
                    layout_weather.setVisibility(View.VISIBLE);
                    layout_disease.setVisibility(View.INVISIBLE);
                    layout_other.setVisibility(View.INVISIBLE);
                }

                else {
                    checkcount_first--;
                    for(i=0;i<NUM_WEATHER;i++) {
                        if (button_weather[i].isChecked())
                            button_weather[i].setChecked(false);
                    }
                    disaster_group = null;
                    layout_weather.setVisibility(View.INVISIBLE);
                }

            }
        });

        button_first[1].setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton arg0, boolean ischecked) {
                if (ischecked) {
                    if (checkcount_first != 0) {
                        arg_first.setChecked(false);
                    }
                    checkcount_first++;
                    arg_first = arg0;
                    disaster_group = name_first[1];
                    layout_weather.setVisibility(View.INVISIBLE);
                    layout_disease.setVisibility(View.VISIBLE);
                    layout_other.setVisibility(View.INVISIBLE);
                }
                else {
                    checkcount_first--;
                    for(i=0;i<NUM_DISEASE;i++) {
                        if (button_disease[i].isChecked())
                            button_disease[i].setChecked(false);
                    }
                    disaster_group = null;
                    layout_disease.setVisibility(View.INVISIBLE);
                }
            }
        });

        button_first[2].setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton arg0, boolean ischecked) {
                if (ischecked){
                    if (checkcount_first != 0) {
                        arg_first.setChecked(false);
                    }
                    checkcount_first++;
                    arg_first = arg0;
                    disaster_group = name_first[2];
                    layout_weather.setVisibility(View.INVISIBLE);
                    layout_disease.setVisibility(View.INVISIBLE);
                    layout_other.setVisibility(View.VISIBLE);
                }
                else {
                    checkcount_first--;
                    for(i=0;i<NUM_OTHER;i++) {
                        if (button_other[i].isChecked())
                            button_other[i].setChecked(false);
                    }
                    disaster_group = null;
                    layout_other.setVisibility(View.INVISIBLE);
                }
            }
        });
        // 동적 생성 종료
    }
}
