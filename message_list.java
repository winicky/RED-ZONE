package com.geovengers.redzone;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.MenuItem;
import androidx.appcompat.widget.SearchView;

import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;


import androidx.appcompat.app.AppCompatActivity;

    public class message_list extends AppCompatActivity {

        private MsgResponse msgResponse = new MsgResponse();

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_message_list);

            SearchView search_view = (SearchView)findViewById(R.id.search_view);
            LinearLayout linearLayout = (LinearLayout)findViewById(R.id.linearLayout);

            Intent intent = getIntent();
            msgResponse = (MsgResponse)intent.getSerializableExtra("msgResponse");

            for(int i=0; i<msgResponse.getMessage().size(); i++){
                final String temp = "["+msgResponse.getMessage().get(i).getDisasterGroup()+"]["
                        +msgResponse.getMessage().get(i).getDisasterLevel()+"]["
                        +msgResponse.getMessage().get(i).getLocationName()+"]"
                        +"\n["
                        +msgResponse.getMessage().get(i).getMsgCreateDt()+"]\n"
                        +msgResponse.getMessage().get(i).getMsg();
                Button btn = new Button(this);
                btn.setId(i);
                btn.setText(temp);
                btn.setEllipsize(TextUtils.TruncateAt.END);
                btn.setMaxLines(3);
                btn.setGravity(Gravity.LEFT);
                linearLayout.addView(btn);

                btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(message_list.this,temp, Toast.LENGTH_SHORT).show();
                    }
                });
            }

            search_view.setOnQueryTextListener(new SearchView.OnQueryTextListener(){
                @Override
                public boolean onQueryTextSubmit(String query) {

                    return true;
                }

                @Override
                public boolean onQueryTextChange(String newText) {

                    return false;
                }
            });
        }
}
