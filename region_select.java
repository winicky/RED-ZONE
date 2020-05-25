package com.geovengers.redzone;

import android.content.Intent;
import android.graphics.Color;
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

import java.util.Arrays;
import java.util.HashMap;
import java.util.StringTokenizer;

/*class test_structure{
    Button test_button;
    String test_code;
    String test_region;
}*/

public class region_select extends AppCompatActivity {

    public static final int REQUEST_CODE_REGION = 1003;

    int i = 0, j = 0;
    String parameter_name = new String();
    String parameter_code = new String();
    int checkcount = 0;
    int ischeckedid = 0;
    int incount = 0;

    //  1차지역 순서는 서울-부산-대구-인천-광주-대전-울산-세종-경기-강원-충북-충남-전북-전남-경북-경남-제주 순

    //  NUM+지역(대문자) : 해당 1차지역의 2차지역 개수 + 1(전체 선택용)
    private static final int NUM_SEOUL = 25 + 1;
    private static final int NUM_PUSAN = 16 + 1;
    private static final int NUM_DAEGU = 8 + 1;
    private static final int NUM_INCHEON = 10 + 1;
    private static final int NUM_GWANGJU = 5 + 1;
    private static final int NUM_DAEJEON = 5 + 1;
    private static final int NUM_ULSAN = 5 + 1;
    private static final int NUM_SEJONG = 1 + 1;
    private static final int NUM_GYEONGGI = 31 + 1;
    private static final int NUM_GANGWON = 18 + 1;
    private static final int NUM_CHUNGBUK = 11 + 1;
    private static final int NUM_CHUNGNAM = 15 + 1;
    private static final int NUM_JEONBUK = 14 + 1;
    private static final int NUM_JEONNAM = 22 + 1;
    private static final int NUM_GYEONGBUK = 23 + 1;
    private static final int NUM_GYEONGNAM = 18 + 1;
    private static final int NUM_JEJU = 2 + 1;


    private static final int ACC_NUM_SEOUL = NUM_SEOUL - 1;
    private static final int ACC_NUM_PUSAN = ACC_NUM_SEOUL + NUM_PUSAN - 1;
    private static final int ACC_NUM_DAEGU = ACC_NUM_PUSAN + NUM_DAEGU - 1;
    private static final int ACC_NUM_INCHEON = ACC_NUM_DAEGU + NUM_INCHEON - 1;
    private static final int ACC_NUM_GWANGJU = ACC_NUM_INCHEON + NUM_GWANGJU - 1;
    private static final int ACC_NUM_DAEJEON = ACC_NUM_GWANGJU + NUM_DAEJEON - 1;
    private static final int ACC_NUM_ULSAN = ACC_NUM_DAEJEON + NUM_ULSAN - 1;
    private static final int ACC_NUM_SEJONG = ACC_NUM_ULSAN + NUM_SEJONG - 1;
    private static final int ACC_NUM_GYEONGGI = ACC_NUM_SEJONG + NUM_GYEONGGI - 1;
    private static final int ACC_NUM_GANGWON = ACC_NUM_GYEONGGI + NUM_GANGWON - 1;
    private static final int ACC_NUM_CHUNGBUK = ACC_NUM_GANGWON + NUM_CHUNGBUK - 1;
    private static final int ACC_NUM_CHUNGNAM = ACC_NUM_CHUNGBUK + NUM_CHUNGNAM - 1;
    private static final int ACC_NUM_JEONBUK = ACC_NUM_CHUNGNAM + NUM_JEONBUK - 1;
    private static final int ACC_NUM_JEONNAM = ACC_NUM_JEONBUK + NUM_JEONNAM - 1;
    private static final int ACC_NUM_GYEONGBUK = ACC_NUM_JEONNAM + NUM_GYEONGBUK - 1;
    private static final int ACC_NUM_GYEONGNAM = ACC_NUM_GYEONGBUK + NUM_GYEONGNAM - 1;
    private static final int ACC_NUM_JEJU = ACC_NUM_GYEONGNAM + NUM_JEJU - 1;


    int num_first = MainActivity.numLocParent + 1;              //  1차지역(시/도)의 개수 (세종때문에 +1)
    int num_second = MainActivity.numLocChild;                  //  2차지역(구/군)의 개수 (mainactivity.java에 있는 상수)

    //  이하 (시/도) 구분 지역을 1차 지역, (구/군) 구분 지역을 2차 지역으로 표기

    String[] name_first = new String[num_first];            //  1차 지역의 이름을 저장
    String[] code_first = new String[num_first];            //  1차 지역의 법정동 코드를 저장
    String[] name_second = new String[num_second];            //  2차 지역의 전체 이름을 저장
    String[] code_second = new String[num_second];            //  2차 지역의 전체 법정동 코드를 저장

    //  name_1차지역 : 1차 지역에 포함되는 2차 지역들의 이름을 저장
    //  code_1차지역 : 1차 지역에 포함되는 2차 지역들의 법정동 코드를 저장
    //  아래 배열은 name_second, code_second를 지역별로 분리해서 저장하는 의미

    String[] name_seoul = new String[NUM_SEOUL];
    String[] code_seoul = new String[NUM_SEOUL];
    String[] name_pusan = new String[NUM_PUSAN];
    String[] code_pusan = new String[NUM_PUSAN];
    String[] name_daegu = new String[NUM_DAEGU];
    String[] code_daegu = new String[NUM_DAEGU];
    String[] name_incheon = new String[NUM_INCHEON];
    String[] code_incheon = new String[NUM_INCHEON];
    String[] name_gwangju = new String[NUM_GWANGJU];
    String[] code_gwangju = new String[NUM_GWANGJU];
    String[] name_daejeon = new String[NUM_DAEJEON];
    String[] code_daejeon = new String[NUM_DAEJEON];
    String[] name_ulsan = new String[NUM_ULSAN];
    String[] code_ulsan = new String[NUM_ULSAN];
    String[] name_sejong = new String[NUM_SEJONG];
    String[] code_sejong = new String[NUM_SEJONG];
    String[] name_gyeonggi = new String[NUM_GYEONGGI];
    String[] code_gyeonggi = new String[NUM_GYEONGGI];
    String[] name_gangwon = new String[NUM_GANGWON];
    String[] code_gangwon = new String[NUM_GANGWON];
    String[] name_chungbuk = new String[NUM_CHUNGBUK];
    String[] code_chungbuk = new String[NUM_CHUNGBUK];
    String[] name_chungnam = new String[NUM_CHUNGNAM];
    String[] code_chungnam = new String[NUM_CHUNGNAM];
    String[] name_jeonbuk = new String[NUM_JEONBUK];
    String[] code_jeonbuk = new String[NUM_JEONBUK];
    String[] name_jeonnam = new String[NUM_JEONNAM];
    String[] code_jeonnam = new String[NUM_JEONNAM];
    String[] name_gyeongbuk = new String[NUM_GYEONGBUK];
    String[] code_gyeongbuk = new String[NUM_GYEONGBUK];
    String[] name_gyeongnam = new String[NUM_GYEONGNAM];
    String[] code_gyeongnam = new String[NUM_GYEONGNAM];
    String[] name_jeju = new String[NUM_JEJU];
    String[] code_jeju = new String[NUM_JEJU];

    Button[] first_button = new Button[num_first];
    BtnOnClickListener second_listener = new BtnOnClickListener();
    CompoundButton arg1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_region_select);

        ImageButton b_reset = (ImageButton) findViewById(R.id.reset);
        b_reset.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                parameter_code = null;
                parameter_name = null;
                arg1.setChecked(false);
                arg1 = null;
                //  클릭해서 적용 되었던 필터 설정을 전부 해제
            }
        });

        ImageButton b_apply = (ImageButton) findViewById(R.id.apply);
        b_apply.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                // 필터 설정을 전부 파라미터로 넘기고 set_filter로 이동(finish)
                Intent return_region_intent = new Intent(getApplicationContext(), set_filter.class);
                Bundle region_bundle = new Bundle();
                region_bundle.putString("PARAMETER_CODE", parameter_code);
                region_bundle.putString("PARAMETER_NAME", parameter_name);
                return_region_intent.putExtra("REGION_BUNDLE", region_bundle);
                setResult(REQUEST_CODE_REGION, return_region_intent);
                finish();
            }
        });

        ImageButton b_exit = (ImageButton) findViewById(R.id.exit);
        b_exit.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                //  뒤로가기
                onBackPressed();
            }
        });

        LinearLayout first_layout = (LinearLayout) findViewById(R.id.first_region_layout);
        FrameLayout second_frame = (FrameLayout) findViewById(R.id.second_frame);


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

        for (i = 0; i < num_second; i++) {
            name_second[i] = MainActivity.locChild[i].locationName;
            code_second[i] = MainActivity.locChild[i].locationCode;
        }


        //  2차지역 배열(이름, 코드)에 데이터를 입력하는 과정
        //  법정동 코드는 그냥 전체에서 순서를 맞춰서 입력해주면 되는데
        //  이름은 공백으로 파싱한 뒷 문자열을 넣어줘야 함(ex: 서울특별시 '동대문구')

        StringTokenizer str;

        name_seoul[0] = "전체";
        code_seoul[0] = code_first[0];
        for (i = 1; i < NUM_SEOUL; i++) {                                //  서울
            str = new StringTokenizer(name_second[i], " ");
            str.nextToken();
            name_seoul[i] = str.nextToken();
            code_seoul[i] = code_second[i];
        }

        name_pusan[0] = "전체";
        code_pusan[0] = code_first[1];
        for (i = 1, j = ACC_NUM_SEOUL; j < ACC_NUM_PUSAN; i++, j++) {    //  부산
            str = new StringTokenizer(name_second[j], " ");
            str.nextToken();
            name_pusan[i] = str.nextToken();
            code_pusan[i] = code_second[j];
        }

        name_daegu[0] = "전체";
        code_daegu[0] = code_first[2];
        for (i = 1, j = ACC_NUM_PUSAN; j < ACC_NUM_DAEGU; i++, j++) {    //  대구
            str = new StringTokenizer(name_second[j], " ");
            str.nextToken();
            name_daegu[i] = str.nextToken();
            code_daegu[i] = code_second[j];
        }

        name_incheon[0] = "전체";
        code_incheon[0] = code_first[3];
        for (i = 1, j = ACC_NUM_DAEGU; j < ACC_NUM_INCHEON; i++, j++) {    //  인천
            str = new StringTokenizer(name_second[j], " ");
            str.nextToken();
            name_incheon[i] = str.nextToken();
            code_incheon[i] = code_second[j];
        }

        name_gwangju[0] = "전체";
        code_gwangju[0] = code_first[4];
        for (i = 1, j = ACC_NUM_INCHEON; j < ACC_NUM_GWANGJU; i++, j++) {    //  광주
            str = new StringTokenizer(name_second[j], " ");
            str.nextToken();
            name_gwangju[i] = str.nextToken();
            code_gwangju[i] = code_second[j];
        }

        name_daejeon[0] = "전체";
        code_daejeon[0] = code_first[5];
        for (i = 1, j = ACC_NUM_GWANGJU; j < ACC_NUM_DAEJEON; i++, j++) {    //  대전
            str = new StringTokenizer(name_second[j], " ");
            str.nextToken();
            name_daejeon[i] = str.nextToken();
            code_daejeon[i] = code_second[j];
        }

        name_ulsan[0] = "전체";
        code_ulsan[0] = code_first[6];
        for (i = 1, j = ACC_NUM_DAEJEON; j < ACC_NUM_ULSAN; i++, j++) {    //  울산
            str = new StringTokenizer(name_second[j], " ");
            str.nextToken();
            name_ulsan[i] = str.nextToken();
            code_ulsan[i] = code_second[j];
        }

        name_sejong[0] = "전체";
        code_sejong[0] = code_first[7];
        for (i = 1, j = ACC_NUM_ULSAN; j < ACC_NUM_SEJONG; i++, j++) {    //  세종
            /*str = new StringTokenizer(name_second[j], " ");
            str.nextToken();
            name_sejong[i] = str.nextToken();*/
            name_sejong[i] = name_second[j];            //  세종은 세종특별자치시로 입력되어 있어서
            code_sejong[i] = code_second[j];            //  파싱 없이 강제로 때려 박음

        }

        name_gyeonggi[0] = "전체";
        code_gyeonggi[0] = code_first[8];
        for (i = 1, j = ACC_NUM_SEJONG; j < ACC_NUM_GYEONGGI; i++, j++) {    //  경기
            str = new StringTokenizer(name_second[j], " ");
            str.nextToken();
            name_gyeonggi[i] = str.nextToken();
            code_gyeonggi[i] = code_second[j];
        }

        name_gangwon[0] = "전체";
        code_gangwon[0] = code_first[9];
        for (i = 1, j = ACC_NUM_GYEONGGI; j < ACC_NUM_GANGWON; i++, j++) {    //  강원
            str = new StringTokenizer(name_second[j], " ");
            str.nextToken();
            name_gangwon[i] = str.nextToken();
            code_gangwon[i] = code_second[j];
        }

        name_chungbuk[0] = "전체";
        code_chungbuk[0] = code_first[10];
        for (i = 1, j = ACC_NUM_GANGWON; j < ACC_NUM_CHUNGBUK; i++, j++) {    //  충북
            str = new StringTokenizer(name_second[j], " ");
            str.nextToken();
            name_chungbuk[i] = str.nextToken();
            code_chungbuk[i] = code_second[j];
        }

        name_chungnam[0] = "전체";
        code_chungnam[0] = code_first[11];
        for (i = 1, j = ACC_NUM_CHUNGBUK; j < ACC_NUM_CHUNGNAM; i++, j++) {    //  충남
            str = new StringTokenizer(name_second[j], " ");
            str.nextToken();
            name_chungnam[i] = str.nextToken();
            code_chungnam[i] = code_second[j];
        }

        name_jeonbuk[0] = "전체";
        code_jeonbuk[0] = code_first[12];
        for (i = 1, j = ACC_NUM_CHUNGNAM; j < ACC_NUM_JEONBUK; i++, j++) {    //  전북
            str = new StringTokenizer(name_second[j], " ");
            str.nextToken();
            name_jeonbuk[i] = str.nextToken();
            code_jeonbuk[i] = code_second[j];
        }

        name_jeonnam[0] = "전체";
        code_jeonnam[0] = code_first[13];
        for (i = 1, j = ACC_NUM_JEONBUK; j < ACC_NUM_JEONNAM; i++, j++) {    //  전남
            str = new StringTokenizer(name_second[j], " ");
            str.nextToken();
            name_jeonnam[i] = str.nextToken();
            code_jeonnam[i] = code_second[j];
        }

        name_gyeongbuk[0] = "전체";
        code_gyeongbuk[0] = code_first[14];
        for (i = 1, j = ACC_NUM_JEONNAM; j < ACC_NUM_GYEONGBUK; i++, j++) {    //  경북
            str = new StringTokenizer(name_second[j], " ");
            str.nextToken();
            name_gyeongbuk[i] = str.nextToken();
            code_gyeongbuk[i] = code_second[j];
        }

        name_gyeongnam[0] = "전체";
        code_gyeongnam[0] = code_first[15];
        for (i = 1, j = ACC_NUM_GYEONGBUK; j < ACC_NUM_GYEONGNAM; i++, j++) {    //  경남
            str = new StringTokenizer(name_second[j], " ");
            str.nextToken();
            name_gyeongnam[i] = str.nextToken();
            code_gyeongnam[i] = code_second[j];
        }

        name_jeju[0] = "전체";
        code_jeju[0] = code_first[16];
        for(i = 1, j=ACC_NUM_GYEONGNAM;j<ACC_NUM_JEJU;i++, j++) {    //  제주
            str = new StringTokenizer(name_second[j], " ");
            str.nextToken();
            name_jeju[i] = str.nextToken();
            code_jeju[i] = code_second[j];
        }
        //  2차지역 배열에 이름, 법정동코드 입력 종료


        //  지역별로 개별 레이아웃을 만들어 2차지역 프레임레이아웃에 삽입
        final LinearLayout layout_seoul = new LinearLayout(this);       //  서울
        layout_seoul.setOrientation(LinearLayout.VERTICAL);
        second_frame.addView(layout_seoul);
        layout_seoul.setVisibility(View.INVISIBLE);

        final ToggleButton[] button_seoul = new ToggleButton[NUM_SEOUL];
        for (i = 0; i < NUM_SEOUL; i++) {
            button_seoul[i] = new ToggleButton(this);
            button_seoul[i].setTextOn(name_seoul[i]);
            button_seoul[i].setTextOff(name_seoul[i]);
            button_seoul[i].setText(code_seoul[i]);
            button_seoul[i].setChecked(false);
            button_seoul[i].setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){
                public void onCheckedChanged(CompoundButton arg0, boolean ischecked){
                    if (!ischecked) {
                        checkcount--;
                    }
                    else {
                        if (checkcount == 0) {
                            parameter_code = button_seoul[i].getText().toString();
                            checkcount++;
                            arg1 = arg0;
                        } else {
                            checkcount++;
                            arg1.setChecked(false);
                            arg1 = arg0;
                        }
                    }
                }
            });
            layout_seoul.addView(button_seoul[i]);
        }

        final LinearLayout layout_pusan = new LinearLayout(this);       //  부산
        layout_pusan.setOrientation(LinearLayout.VERTICAL);
        second_frame.addView(layout_pusan);
        layout_pusan.setVisibility(View.INVISIBLE);
        final ToggleButton[] button_pusan = new ToggleButton[NUM_PUSAN];
        for (i = 0; i < NUM_PUSAN; i++) {
            button_pusan[i] = new ToggleButton(this);
            button_pusan[i].setTextOn(name_pusan[i]);
            button_pusan[i].setTextOff(name_pusan[i]);
            button_pusan[i].setText(code_pusan[i]);
            button_pusan[i].setChecked(false);
            button_pusan[i].setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                public void onCheckedChanged(CompoundButton arg0, boolean ischecked) {
                    if (!ischecked) {
                        checkcount--;
                    } else {
                        if (checkcount == 0) {
                            parameter_code = button_pusan[i].getText().toString();
                            checkcount++;
                            arg1 = arg0;
                        } else {
                            checkcount++;
                            arg1.setChecked(false);
                            arg1 = arg0;
                        }
                    }
                }
            });
            layout_pusan.addView(button_pusan[i]);
        }

        final LinearLayout layout_daegu = new LinearLayout(this);       //  대구
        layout_daegu.setOrientation(LinearLayout.VERTICAL);
        second_frame.addView(layout_daegu);
        layout_daegu.setVisibility(View.INVISIBLE);
        final ToggleButton[] button_daegu = new ToggleButton[NUM_DAEGU];
        for (i = 0; i < NUM_DAEGU; i++) {
            button_daegu[i] = new ToggleButton(this);
            button_daegu[i].setTextOn(name_daegu[i]);
            button_daegu[i].setTextOff(name_daegu[i]);
            button_daegu[i].setText(code_daegu[i]);
            button_daegu[i].setChecked(false);
            button_daegu[i].setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                public void onCheckedChanged(CompoundButton arg0, boolean ischecked) {
                    if (!ischecked) {
                        checkcount--;
                    } else {
                        if (checkcount == 0) {
                            parameter_code = button_daegu[i].getText().toString();
                            checkcount++;
                            arg1 = arg0;
                        } else {
                            checkcount++;
                            arg1.setChecked(false);
                            arg1 = arg0;
                        }
                    }
                }
            });
            layout_daegu.addView(button_daegu[i]);
        }

        final LinearLayout layout_incheon = new LinearLayout(this);     //  인천
        layout_incheon.setOrientation(LinearLayout.VERTICAL);
        second_frame.addView(layout_incheon);
        layout_incheon.setVisibility(View.INVISIBLE);
        final ToggleButton[] button_incheon = new ToggleButton[NUM_INCHEON];
        for (i = 0; i < NUM_INCHEON; i++) {
            button_incheon[i] = new ToggleButton(this);
            button_incheon[i].setTextOn(name_incheon[i]);
            button_incheon[i].setTextOff(name_incheon[i]);
            button_incheon[i].setText(code_incheon[i]);
            button_incheon[i].setChecked(false);
            button_incheon[i].setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                public void onCheckedChanged(CompoundButton arg0, boolean ischecked) {
                    if (!ischecked) {
                        checkcount--;
                    } else {
                        if (checkcount == 0) {
                            parameter_code = button_incheon[i].getText().toString();
                            checkcount++;
                            arg1 = arg0;
                        } else {
                            checkcount++;
                            arg1.setChecked(false);
                            arg1 = arg0;
                        }
                    }
                }
            });
            layout_incheon.addView(button_incheon[i]);
        }

        final LinearLayout layout_gwangju = new LinearLayout(this);     //  광주
        layout_gwangju.setOrientation(LinearLayout.VERTICAL);
        second_frame.addView(layout_gwangju);
        layout_gwangju.setVisibility(View.INVISIBLE);
        final ToggleButton[] button_gwangju = new ToggleButton[NUM_GWANGJU];
        for (i = 0; i < NUM_GWANGJU; i++) {
            button_gwangju[i] = new ToggleButton(this);
            button_gwangju[i].setTextOn(name_gwangju[i]);
            button_gwangju[i].setTextOff(name_gwangju[i]);
            button_gwangju[i].setText(code_gwangju[i]);
            button_gwangju[i].setChecked(false);
            button_gwangju[i].setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                public void onCheckedChanged(CompoundButton arg0, boolean ischecked) {
                    if (!ischecked) {
                        checkcount--;
                    } else {
                        if (checkcount == 0) {
                            parameter_code = button_gwangju[i].getText().toString();
                            checkcount++;
                            arg1 = arg0;
                        } else {
                            checkcount++;
                            arg1.setChecked(false);
                            arg1 = arg0;
                        }
                    }
                }
            });
            layout_gwangju.addView(button_gwangju[i]);
        }

        final LinearLayout layout_daejeon = new LinearLayout(this);     //  대전
        layout_daejeon.setOrientation(LinearLayout.VERTICAL);
        second_frame.addView(layout_daejeon);
        layout_daejeon.setVisibility(View.INVISIBLE);
        final ToggleButton[] button_daejeon = new ToggleButton[NUM_DAEJEON];
        for (i = 0; i < NUM_DAEJEON; i++) {
            button_daejeon[i] = new ToggleButton(this);
            button_daejeon[i].setTextOn(name_daejeon[i]);
            button_daejeon[i].setTextOff(name_daejeon[i]);
            button_daejeon[i].setText(code_daejeon[i]);
            button_daejeon[i].setChecked(false);
            button_daejeon[i].setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                public void onCheckedChanged(CompoundButton arg0, boolean ischecked) {
                    if (!ischecked) {
                        checkcount--;
                    } else {
                        if (checkcount == 0) {
                            parameter_code = button_daejeon[i].getText().toString();
                            checkcount++;
                            arg1 = arg0;
                        } else {
                            checkcount++;
                            arg1.setChecked(false);
                            arg1 = arg0;
                        }
                    }
                }
            });
            layout_daejeon.addView(button_daejeon[i]);
        }

        final LinearLayout layout_ulsan = new LinearLayout(this);       //  울산
        layout_ulsan.setOrientation(LinearLayout.VERTICAL);
        second_frame.addView(layout_ulsan);
        layout_ulsan.setVisibility(View.INVISIBLE);
        final ToggleButton[] button_ulsan = new ToggleButton[NUM_ULSAN];
        for (i = 0; i < NUM_ULSAN; i++) {
            button_ulsan[i] = new ToggleButton(this);
            button_ulsan[i].setTextOn(name_ulsan[i]);
            button_ulsan[i].setTextOff(name_ulsan[i]);
            button_ulsan[i].setText(code_ulsan[i]);
            button_ulsan[i].setChecked(false);
            button_ulsan[i].setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                public void onCheckedChanged(CompoundButton arg0, boolean ischecked) {
                    if (!ischecked) {
                        checkcount--;
                    } else {
                        if (checkcount == 0) {
                            parameter_code = button_ulsan[i].getText().toString();
                            checkcount++;
                            arg1 = arg0;
                        } else {
                            checkcount++;
                            arg1.setChecked(false);
                            arg1 = arg0;
                        }
                    }
                }
            });
            layout_ulsan.addView(button_ulsan[i]);
        }

        final LinearLayout layout_sejong = new LinearLayout(this);      //  세종
        layout_sejong.setOrientation(LinearLayout.VERTICAL);
        second_frame.addView(layout_sejong);
        layout_sejong.setVisibility(View.INVISIBLE);
        final ToggleButton[] button_sejong = new ToggleButton[NUM_SEJONG];
        for (i = 0; i < NUM_SEJONG; i++) {
            button_sejong[i] = new ToggleButton(this);
            button_sejong[i].setTextOn(name_sejong[i]);
            button_sejong[i].setTextOff(name_sejong[i]);
            button_sejong[i].setText(code_sejong[i]);
            button_sejong[i].setChecked(false);
            button_sejong[i].setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                public void onCheckedChanged(CompoundButton arg0, boolean ischecked) {
                    if (!ischecked) {
                        checkcount--;
                    } else {
                        if (checkcount == 0) {
                            parameter_code = button_sejong[i].getText().toString();
                            checkcount++;
                            arg1 = arg0;
                        } else {
                            checkcount++;
                            arg1.setChecked(false);
                            arg1 = arg0;
                        }
                    }
                }
            });
            layout_sejong.addView(button_sejong[i]);
        }

        final LinearLayout layout_gyeonggi = new LinearLayout(this);    //  경기
        layout_gyeonggi.setOrientation(LinearLayout.VERTICAL);
        second_frame.addView(layout_gyeonggi);
        layout_gyeonggi.setVisibility(View.INVISIBLE);
        final ToggleButton[] button_gyeonggi = new ToggleButton[NUM_GYEONGGI];
        for (i = 0; i < NUM_GYEONGGI; i++) {
            button_gyeonggi[i] = new ToggleButton(this);
            button_gyeonggi[i].setTextOn(name_gyeonggi[i]);
            button_gyeonggi[i].setTextOff(name_gyeonggi[i]);
            button_gyeonggi[i].setText(code_gyeonggi[i]);
            button_gyeonggi[i].setChecked(false);
            button_gyeonggi[i].setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                public void onCheckedChanged(CompoundButton arg0, boolean ischecked) {
                    if (!ischecked) {
                        checkcount--;
                    } else {
                        if (checkcount == 0) {
                            parameter_code = button_gyeonggi[i].getText().toString();
                            checkcount++;
                            arg1 = arg0;
                        } else {
                            checkcount++;
                            arg1.setChecked(false);
                            arg1 = arg0;
                        }
                    }
                }
            });
            layout_gyeonggi.addView(button_gyeonggi[i]);
        }

        final LinearLayout layout_gangwon = new LinearLayout(this);     //  강원
        layout_gangwon.setOrientation(LinearLayout.VERTICAL);
        second_frame.addView(layout_gangwon);
        layout_gangwon.setVisibility(View.INVISIBLE);
        final ToggleButton[] button_gangwon = new ToggleButton[NUM_GANGWON];
        for (i = 0; i < NUM_GANGWON; i++) {
            button_gangwon[i] = new ToggleButton(this);
            button_gangwon[i].setTextOn(name_gangwon[i]);
            button_gangwon[i].setTextOff(name_gangwon[i]);
            button_gangwon[i].setText(code_gangwon[i]);
            button_gangwon[i].setChecked(false);
            button_gangwon[i].setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                public void onCheckedChanged(CompoundButton arg0, boolean ischecked) {
                    if (!ischecked) {
                        checkcount--;
                    } else {
                        if (checkcount == 0) {
                            parameter_code = button_gangwon[i].getText().toString();
                            checkcount++;
                            arg1 = arg0;
                        } else {
                            checkcount++;
                            arg1.setChecked(false);
                            arg1 = arg0;
                        }
                    }
                }
            });
            layout_gangwon.addView(button_gangwon[i]);
        }

        final LinearLayout layout_chungbuk = new LinearLayout(this);    //  충북
        layout_chungbuk.setOrientation(LinearLayout.VERTICAL);
        second_frame.addView(layout_chungbuk);
        layout_chungbuk.setVisibility(View.INVISIBLE);
        final ToggleButton[] button_chungbuk = new ToggleButton[NUM_CHUNGBUK];
        for (i = 0; i < NUM_CHUNGBUK; i++) {
            button_chungbuk[i] = new ToggleButton(this);
            button_chungbuk[i].setTextOn(name_chungbuk[i]);
            button_chungbuk[i].setTextOff(name_chungbuk[i]);
            button_chungbuk[i].setText(code_chungbuk[i]);
            button_chungbuk[i].setChecked(false);
            button_chungbuk[i].setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                public void onCheckedChanged(CompoundButton arg0, boolean ischecked) {
                    if (!ischecked) {
                        checkcount--;
                    } else {
                        if (checkcount == 0) {
                            parameter_code = button_chungbuk[i].getText().toString();
                            checkcount++;
                            arg1 = arg0;
                        } else {
                            checkcount++;
                            arg1.setChecked(false);
                            arg1 = arg0;
                        }
                    }
                }
            });
            layout_chungbuk.addView(button_chungbuk[i]);
        }

        final LinearLayout layout_chungnam = new LinearLayout(this);    //  충남
        layout_chungnam.setOrientation(LinearLayout.VERTICAL);
        second_frame.addView(layout_chungnam);
        layout_chungnam.setVisibility(View.INVISIBLE);
        final ToggleButton[] button_chungnam = new ToggleButton[NUM_CHUNGNAM];
        for (i = 0; i < NUM_CHUNGNAM; i++) {
            button_chungnam[i] = new ToggleButton(this);
            button_chungnam[i].setTextOn(name_chungnam[i]);
            button_chungnam[i].setTextOff(name_chungnam[i]);
            button_chungnam[i].setText(code_chungnam[i]);
            button_chungnam[i].setChecked(false);
            button_chungnam[i].setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                public void onCheckedChanged(CompoundButton arg0, boolean ischecked) {
                    if (!ischecked) {
                        checkcount--;
                    } else {
                        if (checkcount == 0) {
                            parameter_code = button_chungnam[i].getText().toString();
                            checkcount++;
                            arg1 = arg0;
                        } else {
                            checkcount++;
                            arg1.setChecked(false);
                            arg1 = arg0;
                        }
                    }
                }
            });
            layout_chungnam.addView(button_chungnam[i]);
        }

        final LinearLayout layout_jeonbuk = new LinearLayout(this);     //  전북
        layout_jeonbuk.setOrientation(LinearLayout.VERTICAL);
        second_frame.addView(layout_jeonbuk);
        layout_jeonbuk.setVisibility(View.INVISIBLE);
        final ToggleButton[] button_jeonbuk = new ToggleButton[NUM_JEONBUK];
        for (i = 0; i < NUM_JEONBUK; i++) {
            button_jeonbuk[i] = new ToggleButton(this);
            button_jeonbuk[i].setTextOn(name_jeonbuk[i]);
            button_jeonbuk[i].setTextOff(name_jeonbuk[i]);
            button_jeonbuk[i].setText(code_jeonbuk[i]);
            button_jeonbuk[i].setChecked(false);
            button_jeonbuk[i].setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                public void onCheckedChanged(CompoundButton arg0, boolean ischecked) {
                    if (!ischecked) {
                        checkcount--;
                    } else {
                        if (checkcount == 0) {
                            parameter_code = button_jeonbuk[i].getText().toString();
                            checkcount++;
                            arg1 = arg0;
                        } else {
                            checkcount++;
                            arg1.setChecked(false);
                            arg1 = arg0;
                        }
                    }
                }
            });
            layout_jeonbuk.addView(button_jeonbuk[i]);
        }

        final LinearLayout layout_jeonnam = new LinearLayout(this);     //  전남
        layout_jeonnam.setOrientation(LinearLayout.VERTICAL);
        second_frame.addView(layout_jeonnam);
        layout_jeonnam.setVisibility(View.INVISIBLE);
        final ToggleButton[] button_jeonnam = new ToggleButton[NUM_JEONNAM];
        for (i = 0; i < NUM_JEONNAM; i++) {
            button_jeonnam[i] = new ToggleButton(this);
            button_jeonnam[i].setTextOn(name_jeonnam[i]);
            button_jeonnam[i].setTextOff(name_jeonnam[i]);
            button_jeonnam[i].setText(code_jeonnam[i]);
            button_jeonnam[i].setChecked(false);
            button_jeonnam[i].setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                public void onCheckedChanged(CompoundButton arg0, boolean ischecked) {
                    if (!ischecked) {
                        checkcount--;
                    } else {
                        if (checkcount == 0) {
                            parameter_code = button_jeonnam[i].getText().toString();
                            checkcount++;
                            arg1 = arg0;
                        } else {
                            checkcount++;
                            arg1.setChecked(false);
                            arg1 = arg0;
                        }
                    }
                }
            });
            layout_jeonnam.addView(button_jeonnam[i]);
        }

        final LinearLayout layout_gyeongbuk = new LinearLayout(this);   //  경북
        layout_gyeongbuk.setOrientation(LinearLayout.VERTICAL);
        second_frame.addView(layout_gyeongbuk);
        layout_gyeongbuk.setVisibility(View.INVISIBLE);
        final ToggleButton[] button_gyeongbuk = new ToggleButton[NUM_GYEONGBUK];
        for (i = 0; i < NUM_GYEONGBUK; i++) {
            button_gyeongbuk[i] = new ToggleButton(this);
            button_gyeongbuk[i].setTextOn(name_gyeongbuk[i]);
            button_gyeongbuk[i].setTextOff(name_gyeongbuk[i]);
            button_gyeongbuk[i].setText(code_gyeongbuk[i]);
            button_gyeongbuk[i].setChecked(false);
            button_gyeongbuk[i].setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                public void onCheckedChanged(CompoundButton arg0, boolean ischecked) {
                    if (!ischecked) {
                        checkcount--;
                    } else {
                        if (checkcount == 0) {
                            parameter_code = button_gyeongbuk[i].getText().toString();
                            checkcount++;
                            arg1 = arg0;
                        } else {
                            checkcount++;
                            arg1.setChecked(false);
                            arg1 = arg0;
                        }
                    }
                }
            });
            layout_gyeongbuk.addView(button_gyeongbuk[i]);
        }

        final LinearLayout layout_gyeongnam = new LinearLayout(this);   //  경남
        layout_gyeongnam.setOrientation(LinearLayout.VERTICAL);
        second_frame.addView(layout_gyeongnam);
        layout_gyeongnam.setVisibility(View.INVISIBLE);
        final ToggleButton[] button_gyeongnam = new ToggleButton[NUM_GYEONGNAM];
        for (i = 0; i < NUM_GYEONGNAM; i++) {
            button_gyeongnam[i] = new ToggleButton(this);
            button_gyeongnam[i].setTextOn(name_gyeongnam[i]);
            button_gyeongnam[i].setTextOff(name_gyeongnam[i]);
            button_gyeongnam[i].setText(code_gyeongnam[i]);
            button_gyeongnam[i].setChecked(false);
            button_gyeongnam[i].setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                public void onCheckedChanged(CompoundButton arg0, boolean ischecked) {
                    if (!ischecked) {
                        checkcount--;
                    } else {
                        if (checkcount == 0) {
                            parameter_code = button_gyeongnam[i].getText().toString();
                            checkcount++;
                            arg1 = arg0;
                        } else {
                            checkcount++;
                            arg1.setChecked(false);
                            arg1 = arg0;
                        }
                    }
                }
            });
            layout_gyeongnam.addView(button_gyeongnam[i]);
        }



        final LinearLayout layout_jeju = new LinearLayout(this);        //  제주
        layout_jeju.setOrientation(LinearLayout.VERTICAL);
        second_frame.addView(layout_jeju);
        layout_jeju.setVisibility(View.INVISIBLE);
        final ToggleButton[] button_jeju = new ToggleButton[NUM_JEJU];
        for (i = 0; i < NUM_JEJU; i++) {
            button_jeju[i] = new ToggleButton(this);
            button_jeju[i].setTextOn(name_jeju[i]);
            button_jeju[i].setTextOff(name_jeju[i]);
            button_jeju[i].setText(code_jeju[i]);
            button_jeju[i].setChecked(false);
            button_jeju[i].setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                public void onCheckedChanged(CompoundButton arg0, boolean ischecked) {
                    if (!ischecked) {
                        checkcount--;
                    } else {
                        if (checkcount == 0) {
                            parameter_code = button_jeju[i].getText().toString();
                            checkcount++;
                            arg1 = arg0;
                        } else {
                            checkcount++;
                            arg1.setChecked(false);
                            arg1 = arg0;
                        }
                    }
                }
            });
            layout_jeju.addView(button_jeju[i]);
        }
        //  2차지역 프레임레이아웃에 삽입 종료


        //  1차지역 버튼들을 화면 좌측(1차지역 레이아웃)에 삽입
        for (i = 0; i < num_first; i++) {
            first_button[i] = new Button(this);
            first_button[i].setText(name_first[i]);
            first_layout.addView(first_button[i]);
        }

        // 1차지역 버튼 리스너 목록 : 해당 지역을 제외한 나머지의 Visibility를 GONE으로, 해당 지역만 VISIBLE로 설정
        first_button[0].setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                layout_seoul.setVisibility(View.VISIBLE);
                layout_pusan.setVisibility(View.GONE);
                layout_daegu.setVisibility(View.GONE);
                layout_incheon.setVisibility(View.GONE);
                layout_gwangju.setVisibility(View.GONE);
                layout_daejeon.setVisibility(View.GONE);
                layout_ulsan.setVisibility(View.GONE);
                layout_sejong.setVisibility(View.GONE);
                layout_gyeonggi.setVisibility(View.GONE);
                layout_gangwon.setVisibility(View.GONE);
                layout_chungbuk.setVisibility(View.GONE);
                layout_chungnam.setVisibility(View.GONE);
                layout_gyeongbuk.setVisibility(View.GONE);
                layout_gyeongnam.setVisibility(View.GONE);
                layout_jeonbuk.setVisibility(View.GONE);
                layout_jeonnam.setVisibility(View.GONE);
                layout_jeju.setVisibility(View.GONE);
            }
        });

        first_button[1].setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                layout_seoul.setVisibility(View.GONE);
                layout_pusan.setVisibility(View.VISIBLE);
                layout_daegu.setVisibility(View.GONE);
                layout_incheon.setVisibility(View.GONE);
                layout_gwangju.setVisibility(View.GONE);
                layout_daejeon.setVisibility(View.GONE);
                layout_ulsan.setVisibility(View.GONE);
                layout_sejong.setVisibility(View.GONE);
                layout_gyeonggi.setVisibility(View.GONE);
                layout_gangwon.setVisibility(View.GONE);
                layout_chungbuk.setVisibility(View.GONE);
                layout_chungnam.setVisibility(View.GONE);
                layout_gyeongbuk.setVisibility(View.GONE);
                layout_gyeongnam.setVisibility(View.GONE);
                layout_jeonbuk.setVisibility(View.GONE);
                layout_jeonnam.setVisibility(View.GONE);
                layout_jeju.setVisibility(View.GONE);
            }
        });

        first_button[2].setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                layout_seoul.setVisibility(View.GONE);
                layout_pusan.setVisibility(View.GONE);
                layout_daegu.setVisibility(View.VISIBLE);
                layout_incheon.setVisibility(View.GONE);
                layout_gwangju.setVisibility(View.GONE);
                layout_daejeon.setVisibility(View.GONE);
                layout_ulsan.setVisibility(View.GONE);
                layout_sejong.setVisibility(View.GONE);
                layout_gyeonggi.setVisibility(View.GONE);
                layout_gangwon.setVisibility(View.GONE);
                layout_chungbuk.setVisibility(View.GONE);
                layout_chungnam.setVisibility(View.GONE);
                layout_gyeongbuk.setVisibility(View.GONE);
                layout_gyeongnam.setVisibility(View.GONE);
                layout_jeonbuk.setVisibility(View.GONE);
                layout_jeonnam.setVisibility(View.GONE);
                layout_jeju.setVisibility(View.GONE);
            }
        });

        first_button[3].setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                layout_seoul.setVisibility(View.GONE);
                layout_pusan.setVisibility(View.GONE);
                layout_daegu.setVisibility(View.GONE);
                layout_incheon.setVisibility(View.VISIBLE);
                layout_gwangju.setVisibility(View.GONE);
                layout_daejeon.setVisibility(View.GONE);
                layout_ulsan.setVisibility(View.GONE);
                layout_sejong.setVisibility(View.GONE);
                layout_gyeonggi.setVisibility(View.GONE);
                layout_gangwon.setVisibility(View.GONE);
                layout_chungbuk.setVisibility(View.GONE);
                layout_chungnam.setVisibility(View.GONE);
                layout_gyeongbuk.setVisibility(View.GONE);
                layout_gyeongnam.setVisibility(View.GONE);
                layout_jeonbuk.setVisibility(View.GONE);
                layout_jeonnam.setVisibility(View.GONE);
                layout_jeju.setVisibility(View.GONE);
            }
        });

        first_button[4].setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                layout_seoul.setVisibility(View.GONE);
                layout_pusan.setVisibility(View.GONE);
                layout_daegu.setVisibility(View.GONE);
                layout_incheon.setVisibility(View.GONE);
                layout_gwangju.setVisibility(View.VISIBLE);
                layout_daejeon.setVisibility(View.GONE);
                layout_ulsan.setVisibility(View.GONE);
                layout_sejong.setVisibility(View.GONE);
                layout_gyeonggi.setVisibility(View.GONE);
                layout_gangwon.setVisibility(View.GONE);
                layout_chungbuk.setVisibility(View.GONE);
                layout_chungnam.setVisibility(View.GONE);
                layout_gyeongbuk.setVisibility(View.GONE);
                layout_gyeongnam.setVisibility(View.GONE);
                layout_jeonbuk.setVisibility(View.GONE);
                layout_jeonnam.setVisibility(View.GONE);
                layout_jeju.setVisibility(View.GONE);
            }
        });

        first_button[5].setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                layout_seoul.setVisibility(View.GONE);
                layout_pusan.setVisibility(View.GONE);
                layout_daegu.setVisibility(View.GONE);
                layout_incheon.setVisibility(View.GONE);
                layout_gwangju.setVisibility(View.GONE);
                layout_daejeon.setVisibility(View.VISIBLE);
                layout_ulsan.setVisibility(View.GONE);
                layout_sejong.setVisibility(View.GONE);
                layout_gyeonggi.setVisibility(View.GONE);
                layout_gangwon.setVisibility(View.GONE);
                layout_chungbuk.setVisibility(View.GONE);
                layout_chungnam.setVisibility(View.GONE);
                layout_gyeongbuk.setVisibility(View.GONE);
                layout_gyeongnam.setVisibility(View.GONE);
                layout_jeonbuk.setVisibility(View.GONE);
                layout_jeonnam.setVisibility(View.GONE);
                layout_jeju.setVisibility(View.GONE);
            }
        });

        first_button[6].setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                layout_seoul.setVisibility(View.GONE);
                layout_pusan.setVisibility(View.GONE);
                layout_daegu.setVisibility(View.GONE);
                layout_incheon.setVisibility(View.GONE);
                layout_gwangju.setVisibility(View.GONE);
                layout_daejeon.setVisibility(View.GONE);
                layout_ulsan.setVisibility(View.VISIBLE);
                layout_sejong.setVisibility(View.GONE);
                layout_gyeonggi.setVisibility(View.GONE);
                layout_gangwon.setVisibility(View.GONE);
                layout_chungbuk.setVisibility(View.GONE);
                layout_chungnam.setVisibility(View.GONE);
                layout_gyeongbuk.setVisibility(View.GONE);
                layout_gyeongnam.setVisibility(View.GONE);
                layout_jeonbuk.setVisibility(View.GONE);
                layout_jeonnam.setVisibility(View.GONE);
                layout_jeju.setVisibility(View.GONE);
            }
        });

        first_button[7].setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                layout_seoul.setVisibility(View.GONE);
                layout_pusan.setVisibility(View.GONE);
                layout_daegu.setVisibility(View.GONE);
                layout_incheon.setVisibility(View.GONE);
                layout_gwangju.setVisibility(View.GONE);
                layout_daejeon.setVisibility(View.GONE);
                layout_ulsan.setVisibility(View.GONE);
                layout_sejong.setVisibility(View.VISIBLE);
                layout_gyeonggi.setVisibility(View.GONE);
                layout_gangwon.setVisibility(View.GONE);
                layout_chungbuk.setVisibility(View.GONE);
                layout_chungnam.setVisibility(View.GONE);
                layout_gyeongbuk.setVisibility(View.GONE);
                layout_gyeongnam.setVisibility(View.GONE);
                layout_jeonbuk.setVisibility(View.GONE);
                layout_jeonnam.setVisibility(View.GONE);
                layout_jeju.setVisibility(View.GONE);
            }
        });

        first_button[8].setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                layout_seoul.setVisibility(View.GONE);
                layout_pusan.setVisibility(View.GONE);
                layout_daegu.setVisibility(View.GONE);
                layout_incheon.setVisibility(View.GONE);
                layout_gwangju.setVisibility(View.GONE);
                layout_daejeon.setVisibility(View.GONE);
                layout_ulsan.setVisibility(View.GONE);
                layout_sejong.setVisibility(View.GONE);
                layout_gyeonggi.setVisibility(View.VISIBLE);
                layout_gangwon.setVisibility(View.GONE);
                layout_chungbuk.setVisibility(View.GONE);
                layout_chungnam.setVisibility(View.GONE);
                layout_gyeongbuk.setVisibility(View.GONE);
                layout_gyeongnam.setVisibility(View.GONE);
                layout_jeonbuk.setVisibility(View.GONE);
                layout_jeonnam.setVisibility(View.GONE);
                layout_jeju.setVisibility(View.GONE);
            }
        });

        first_button[9].setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                layout_seoul.setVisibility(View.GONE);
                layout_pusan.setVisibility(View.GONE);
                layout_daegu.setVisibility(View.GONE);
                layout_incheon.setVisibility(View.GONE);
                layout_gwangju.setVisibility(View.GONE);
                layout_daejeon.setVisibility(View.GONE);
                layout_ulsan.setVisibility(View.GONE);
                layout_sejong.setVisibility(View.GONE);
                layout_gyeonggi.setVisibility(View.GONE);
                layout_gangwon.setVisibility(View.VISIBLE);
                layout_chungbuk.setVisibility(View.GONE);
                layout_chungnam.setVisibility(View.GONE);
                layout_gyeongbuk.setVisibility(View.GONE);
                layout_gyeongnam.setVisibility(View.GONE);
                layout_jeonbuk.setVisibility(View.GONE);
                layout_jeonnam.setVisibility(View.GONE);
                layout_jeju.setVisibility(View.GONE);
            }
        });

        first_button[10].setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                layout_seoul.setVisibility(View.GONE);
                layout_pusan.setVisibility(View.GONE);
                layout_daegu.setVisibility(View.GONE);
                layout_incheon.setVisibility(View.GONE);
                layout_gwangju.setVisibility(View.GONE);
                layout_daejeon.setVisibility(View.GONE);
                layout_ulsan.setVisibility(View.GONE);
                layout_sejong.setVisibility(View.GONE);
                layout_gyeonggi.setVisibility(View.GONE);
                layout_gangwon.setVisibility(View.GONE);
                layout_chungbuk.setVisibility(View.VISIBLE);
                layout_chungnam.setVisibility(View.GONE);
                layout_gyeongbuk.setVisibility(View.GONE);
                layout_gyeongnam.setVisibility(View.GONE);
                layout_jeonbuk.setVisibility(View.GONE);
                layout_jeonnam.setVisibility(View.GONE);
                layout_jeju.setVisibility(View.GONE);
            }
        });

        first_button[11].setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                layout_seoul.setVisibility(View.GONE);
                layout_pusan.setVisibility(View.GONE);
                layout_daegu.setVisibility(View.GONE);
                layout_incheon.setVisibility(View.GONE);
                layout_gwangju.setVisibility(View.GONE);
                layout_daejeon.setVisibility(View.GONE);
                layout_ulsan.setVisibility(View.GONE);
                layout_sejong.setVisibility(View.GONE);
                layout_gyeonggi.setVisibility(View.GONE);
                layout_gangwon.setVisibility(View.GONE);
                layout_chungbuk.setVisibility(View.GONE);
                layout_chungnam.setVisibility(View.VISIBLE);
                layout_gyeongbuk.setVisibility(View.GONE);
                layout_gyeongnam.setVisibility(View.GONE);
                layout_jeonbuk.setVisibility(View.GONE);
                layout_jeonnam.setVisibility(View.GONE);
                layout_jeju.setVisibility(View.GONE);
            }
        });

        first_button[12].setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                layout_seoul.setVisibility(View.GONE);
                layout_pusan.setVisibility(View.GONE);
                layout_daegu.setVisibility(View.GONE);
                layout_incheon.setVisibility(View.GONE);
                layout_gwangju.setVisibility(View.GONE);
                layout_daejeon.setVisibility(View.GONE);
                layout_ulsan.setVisibility(View.GONE);
                layout_sejong.setVisibility(View.GONE);
                layout_gyeonggi.setVisibility(View.GONE);
                layout_gangwon.setVisibility(View.GONE);
                layout_chungbuk.setVisibility(View.GONE);
                layout_chungnam.setVisibility(View.GONE);
                layout_gyeongbuk.setVisibility(View.GONE);
                layout_gyeongnam.setVisibility(View.GONE);
                layout_jeonbuk.setVisibility(View.VISIBLE);
                layout_jeonnam.setVisibility(View.GONE);
                layout_jeju.setVisibility(View.GONE);
            }
        });

        first_button[13].setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                layout_seoul.setVisibility(View.GONE);
                layout_pusan.setVisibility(View.GONE);
                layout_daegu.setVisibility(View.GONE);
                layout_incheon.setVisibility(View.GONE);
                layout_gwangju.setVisibility(View.GONE);
                layout_daejeon.setVisibility(View.GONE);
                layout_ulsan.setVisibility(View.GONE);
                layout_sejong.setVisibility(View.GONE);
                layout_gyeonggi.setVisibility(View.GONE);
                layout_gangwon.setVisibility(View.GONE);
                layout_chungbuk.setVisibility(View.GONE);
                layout_chungnam.setVisibility(View.GONE);
                layout_gyeongbuk.setVisibility(View.GONE);
                layout_gyeongnam.setVisibility(View.GONE);
                layout_jeonbuk.setVisibility(View.GONE);
                layout_jeonnam.setVisibility(View.VISIBLE);
                layout_jeju.setVisibility(View.GONE);
            }
        });

        first_button[14].setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                layout_seoul.setVisibility(View.GONE);
                layout_pusan.setVisibility(View.GONE);
                layout_daegu.setVisibility(View.GONE);
                layout_incheon.setVisibility(View.GONE);
                layout_gwangju.setVisibility(View.GONE);
                layout_daejeon.setVisibility(View.GONE);
                layout_ulsan.setVisibility(View.GONE);
                layout_sejong.setVisibility(View.GONE);
                layout_gyeonggi.setVisibility(View.GONE);
                layout_gangwon.setVisibility(View.GONE);
                layout_chungbuk.setVisibility(View.GONE);
                layout_chungnam.setVisibility(View.GONE);
                layout_gyeongbuk.setVisibility(View.VISIBLE);
                layout_gyeongnam.setVisibility(View.GONE);
                layout_jeonbuk.setVisibility(View.GONE);
                layout_jeonnam.setVisibility(View.GONE);
                layout_jeju.setVisibility(View.GONE);
            }
        });

        first_button[15].setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                layout_seoul.setVisibility(View.GONE);
                layout_pusan.setVisibility(View.GONE);
                layout_daegu.setVisibility(View.GONE);
                layout_incheon.setVisibility(View.GONE);
                layout_gwangju.setVisibility(View.GONE);
                layout_daejeon.setVisibility(View.GONE);
                layout_ulsan.setVisibility(View.GONE);
                layout_sejong.setVisibility(View.GONE);
                layout_gyeonggi.setVisibility(View.GONE);
                layout_gangwon.setVisibility(View.GONE);
                layout_chungbuk.setVisibility(View.GONE);
                layout_chungnam.setVisibility(View.GONE);
                layout_gyeongbuk.setVisibility(View.GONE);
                layout_gyeongnam.setVisibility(View.VISIBLE);
                layout_jeonbuk.setVisibility(View.GONE);
                layout_jeonnam.setVisibility(View.GONE);
                layout_jeju.setVisibility(View.GONE);
            }
        });

        first_button[16].setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                layout_seoul.setVisibility(View.GONE);
                layout_pusan.setVisibility(View.GONE);
                layout_daegu.setVisibility(View.GONE);
                layout_incheon.setVisibility(View.GONE);
                layout_gwangju.setVisibility(View.GONE);
                layout_daejeon.setVisibility(View.GONE);
                layout_ulsan.setVisibility(View.GONE);
                layout_sejong.setVisibility(View.GONE);
                layout_gyeonggi.setVisibility(View.GONE);
                layout_gangwon.setVisibility(View.GONE);
                layout_chungbuk.setVisibility(View.GONE);
                layout_chungnam.setVisibility(View.GONE);
                layout_gyeongbuk.setVisibility(View.GONE);
                layout_gyeongnam.setVisibility(View.GONE);
                layout_jeonbuk.setVisibility(View.GONE);
                layout_jeonnam.setVisibility(View.GONE);
                layout_jeju.setVisibility(View.VISIBLE);
            }
        });

        //  1차 버튼 리스너 종료
    }

    class BtnOnClickListener implements Button.OnClickListener{
        @Override
        public void onClick(View v){
            //v.isCheck
            if (checkcount == 0) {
                //parameter_code = v.;
            }
            else{
                //  선택 취소하고 토스트로 1개만 선택하라는 메세지 출력
            }
        }
    }
}


/*
해야 할 일

1. 1차 버튼 선택에 1차 법정동 코드 삽입
2. 2차 버튼 선택 리스너 달기
   2-1. 필요하다면 버튼 단위를 구조체 단위로 바꿔서 버튼, 법정동코드, 이름을 동시에 저장할 수 있는 구조체를 선언

*/

