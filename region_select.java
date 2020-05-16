package com.geovengers.redzone;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

import java.util.HashMap;

public class region_select extends AppCompatActivity {

    //  지역 1차구분&2차구분을 사전에 입력
    int i=0;
    String[] first_region = new String[]{"서울특별시", "부산광역시", "대구광역시", "인천광역시", "광주광역시", "대전광역시", "울산광역시", "세종특별자치시", "경기도", "강원도", "충청남도", "충청북도", "경상남도", "경상북도", "전라남도", "전라북도", "제주특별자치도"};
    String[] sec_seoul = new String[]{"강남구", "강동구", "강북구", "강서구", "관악구", "광진구", "구로구", "금천구", "노원구", "도봉구", "동대문구", "동작구", "마포구", "서대문구", "서초구", "성동구", "성북구", "송파구", "양천구", "영등포구", "용산구", "은평구", "종로구", "중구", "중랑구"};
    //String[] code_seoul = newasdgak
    String[] sec_pusan = new String[]{"강서구", "금정구", "기장군", "남구", "동구", "동래구", "부산진구", "북구", "사상구", "사하구", "서구", "수영구", "연제구", "중구", "해운대구"};
    String[] sec_daegu = new String[]{"남구", "달서구", "달성군", "동구", "북구", "서구", "수성구", "중구"};
    HashMap<String, String> sec_gwangju = new HashMap<String, String>();

    public void setting(){
        sec_gwangju.put("2920000000", "광산구");
        sec_gwangju.put("2915500000", "남구");
        sec_gwangju.put("2911000000", "동구");
        sec_gwangju.put("2917000000", "북구");
        sec_gwangju.put("2914000000", "서구");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_region_select);

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

                Intent intent = new Intent(region_select.this, set_filter.class);
                startActivity(intent);
            }
        });

        ImageButton b_exit = (ImageButton) findViewById(R.id.exit);
        b_exit.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {

                //  필터 전부 해제 후 뒤로가기

                onBackPressed();
            }
        });

        LinearLayout first_layout = (LinearLayout) findViewById(R.id.first_region_layout);
        final LinearLayout second_layout = (LinearLayout) findViewById(R.id.second_region_layout);

        final Button button_seoul[] = new Button[sec_seoul.length];
        for(i=0;i<sec_seoul.length;i++){
            button_seoul[i] = new Button(this);

            button_seoul[i].setText(sec_seoul[i]);
            button_seoul[i].setOnClickListener(new Button.OnClickListener(){
                public void onClick(View v){
                    //result
                }
            });
        }
        Button first_button[] = new Button[first_region.length];
        for(i=0;i<first_region.length;i++){
            first_button[i] = new Button(this);
            first_button[i].setText(first_region[i]);
            first_layout.addView(first_button[i]);
            first_button[i].setOnClickListener(new Button.OnClickListener(){
                public void onClick(View v){
                    //second_layout.setVisibility();
                    switch (i){
                        case 0 :
                            for (int j=0;j<sec_seoul.length;j++){
                                second_layout.addView(button_seoul[j]);
                            }
                    }
                }
            });
        }


    }


}

