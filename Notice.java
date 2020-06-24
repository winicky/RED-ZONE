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

        noticeTitle.add("[업데이트] Ver1.0 릴리즈 업데이트 안내");
        noticeContent.add("ver1.0 릴리즈\n지도 위 각 버튼 UI가 개선\npiechart activity내 UI가 개선\n각 activity 내 폰트 적용\nNavigation bar 내 기능 구현");

        noticeTitle.add("[업데이트] ver1.1 릴리즈 업데이트 안내");
        noticeContent.add("filter activity 내 UI 개선\n filter activity 내 도움말 기능 추가");

        LinearLayout linearLayout = findViewById(R.id.linearLayoutNotice);

        for(int i=noticeTitle.size()-1; i>-1; i--){
            final String tempTitle = noticeTitle.get(i);
            final String tempContent = noticeContent.get(i);
            Button btn = new Button(getBaseContext());
            btn.setId(i);
            //btn.setGravity(Gravity.CENTER);
            btn.setText(tempTitle);
            btn.setTextSize(20);
            btn.setHeight(100);
            btn.setPaddingRelative(20,0,0,0);
            btn.setBackgroundResource(R.drawable.notice_curved_rect);
            btn.setEllipsize(TextUtils.TruncateAt.END);
            btn.setGravity(Gravity.LEFT);
            btn.setGravity(Gravity.CENTER_VERTICAL);
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
