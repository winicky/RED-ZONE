package com.example.redzone;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

public class disease_select extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_disease_select);

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

                Intent intent = new Intent(disease_select.this, set_filter.class);
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

        // 테스트용 변수들
        int NUM_DISEASE = 10;
        int i=0;
        String temp[] = new String[NUM_DISEASE];
        for (i=0;i<NUM_DISEASE;i++){
            temp[i] = "abc + " + i+1;
        }

        // 질병 목록에 따라 동적으로 뷰를 생성해서 띄우는 코드
        LinearLayout first = (LinearLayout)findViewById(R.id.first);

        Button dis_button[] = new Button[NUM_DISEASE];
        for (i = 0;i<NUM_DISEASE;i++){
            dis_button[i] = new Button(this);
            dis_button[i].setText(temp[i]);
            dis_button[i].setWidth(66);
            dis_button[i].setTextSize(11);
            dis_button[i].setId(i);

            first.addView(dis_button[i]);

        }

        // 동적 생성 종료



        /* first layout에서 클릭 시 second에 setVisible(true)로 변경 */

    }
}
