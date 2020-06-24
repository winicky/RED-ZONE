package com.geovengers.redzone;

import android.app.Activity;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class Notice extends Activity {
    private List<String> noticeContent = new ArrayList<String>();
    private List<String> noticeTitle = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        final Typeface maplelight = Typeface.createFromAsset(getAssets(), "fonts/maplelight.ttf");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notice);

        noticeTitle.add("Version 1.0 릴리즈 안내");
        noticeContent.add("Version 1.0 최초 배포 되었습니다.");

        noticeTitle.add("서버스 중단 안내");
        noticeContent.add("aws server 구입 비 부족으로 인한 서버스 중단 안내");

        LinearLayout linearLayout = findViewById(R.id.linearLayoutNotice);

        for(int i=noticeTitle.size()-1; i>-1; i--){
            final String tempTitle = noticeTitle.get(i);
            final String tempContent = noticeContent.get(i);
            Button btn = new Button(getBaseContext());
            btn.setId(i);
            btn.setGravity(Gravity.CENTER);
            btn.setText(tempTitle);
            btn.setTextSize(30);
            btn.setHeight(180);
            btn.setBackgroundResource(R.drawable.notice_curved_rect);
            btn.setEllipsize(TextUtils.TruncateAt.END);
            //btn.setGravity(Gravity.LEFT);
            btn.setTypeface(maplelight);
            linearLayout.addView(btn);

            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(Notice.this, tempContent, (Toast.LENGTH_LONG)*2).show();
                }
            });
        }



    }
}
