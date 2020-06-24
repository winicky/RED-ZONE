package com.geovengers.redzone;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import androidx.appcompat.widget.SearchView;

import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;


import androidx.appcompat.app.AppCompatActivity;


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class message_list extends AppCompatActivity {

    private MsgResponse msgResponse = new MsgResponse();
    private List<String> messagesForSort = new ArrayList<String>();
    private MsgRequest[] initMsgRequest = new MsgRequest[3];
    private MsgRequest msgRequest = new MsgRequest();
    private MsgResponse[] tmpMsgResponse = new MsgResponse[3];
    private List<Message> message = new ArrayList<Message>();
    private AlertDialog dialog;
    private Toast toast;

    private Service service = ApiUtils.getService();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_list);

        Intent intent = getIntent();


        if(intent.getExtras().getInt("mode") == 0){
            initMsgRequest[0] = (MsgRequest) intent.getSerializableExtra("msgRequest0");
            initMsgRequest[1] = (MsgRequest) intent.getSerializableExtra("msgRequest1");
            initMsgRequest[2] = (MsgRequest) intent.getSerializableExtra("msgRequest2");
            initMsgAPI(initMsgRequest);
        }
        else{
            msgRequest = (MsgRequest) intent.getSerializableExtra("msgRequest");
            loadMsgAPI(msgRequest);
        }

    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
    public void loadMsgAPI(MsgRequest request) {
        setProgressDialog();
        service.getMsgAPI(request).enqueue(new Callback<MsgResponse>() {
            @Override
            public void onResponse(Call<MsgResponse> call, Response<MsgResponse> response) {
                final Typeface koregular = Typeface.createFromAsset(getAssets(), "fonts/koregular.ttf");
                if(response.isSuccessful()) {
                    msgResponse = response.body();
                    deleteProgressDialog();

                    SearchView search_view = (SearchView)findViewById(R.id.search_view);
                    final LinearLayout linearLayout = (LinearLayout)findViewById(R.id.linearLayout);


                    for(int i=0; i<msgResponse.getMessage().size(); i++){
                        messagesForSort.add(i,"["+msgResponse.getMessage().get(msgResponse.getMessage().size() - i - 1).getMsgCreateDt()+"]\n["
                                +msgResponse.getMessage().get(msgResponse.getMessage().size() - i - 1).getDisasterGroup()+"]["
                                +msgResponse.getMessage().get(msgResponse.getMessage().size() - i - 1).getDisasterLevel()+"]["
                                +msgResponse.getMessage().get(msgResponse.getMessage().size() - i - 1).getLocationName()+"]\n"
                                +msgResponse.getMessage().get(msgResponse.getMessage().size() - i - 1).getMsg());
                    }
                    Collections.sort(messagesForSort);
                    Collections.reverse(messagesForSort);

                    for(int i=0; i<msgResponse.getMessage().size(); i++){
                        final String temp = messagesForSort.get(i);
                        Button btn = new Button(getBaseContext());
                        btn.setId(i);
                        btn.setText(temp);
                        btn.setEllipsize(TextUtils.TruncateAt.END);
                        btn.setMaxLines(3);
                        btn.setTypeface(koregular);
                        btn.setGravity(Gravity.LEFT);
                        linearLayout.addView(btn);

                        btn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Toast.makeText(message_list.this,temp, (Toast.LENGTH_LONG)*2).show();
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
                            linearLayout.removeAllViews();

                            for(int i=0; i<msgResponse.getMessage().size(); i++){
                                final String temp = messagesForSort.get(i);
                                if(temp.contains(newText)) {
                                    Button btn = new Button(getBaseContext());
                                    btn.setId(i);
                                    btn.setText(temp);
                                    btn.setEllipsize(TextUtils.TruncateAt.END);
                                    btn.setMaxLines(3);
                                    btn.setTypeface(koregular);;
                                    btn.setGravity(Gravity.LEFT);
                                    linearLayout.addView(btn);

                                    btn.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            //toast.cancel();
                                            //toast = new Toast;
                                            //toast = Toast.makeText(message_list.this, temp, (Toast.LENGTH_LONG)*3);
                                            //toast.show();
                                            showToast(message_list.this, temp);
                                        }
                                    });
                                }
                            }

                            return false;
                        }
                    });

                }else {
                    int statusCode  = response.code();
                    // handle request errors depending on status code
                }
            }

            @Override
            public void onFailure(Call<MsgResponse> call, Throwable t) {
                //showErrorMessage();
                Log.d("MainActivity", "error loading from API");

            }
        });
    }

    private void initMsgAPI(MsgRequest[] msgRequest) {
        setProgressDialog();
        final int[] count = {0, 0};
        for (int i = 0; i < 3; i++) {
            final int j = i;
            Log.d("여기는??", "여기도");
            service.getMsgAPI(msgRequest[i]).enqueue(new Callback<MsgResponse>() {
                @Override
                public synchronized void onResponse(Call<MsgResponse> call, Response<MsgResponse> response) {
                    final Typeface koregular = Typeface.createFromAsset(getAssets(), "fonts/koregular.ttf");
                    if (response.isSuccessful()) {

                        Log.d("여기는???", "여기도ll");
                        //tmpMsgResponse[j] = response.body();
                        //Log.d("여기여기", "제발" + tmpMsgResponse[j].getDisasterGroup());


                        if (j == 0) {
                            tmpMsgResponse[j] = response.body();
                            msgResponse = response.body();
                            count[0]++;
                        } else if (j == 1) {
                            tmpMsgResponse[j] = response.body();
                            count[0]++;
                        } else if (j == 2) {
                            tmpMsgResponse[j] = response.body();
                            count[0]++;
                        }

                        if (count[0] == 3) {

                            for(int i=0; i<3; i++){
                                if(tmpMsgResponse[i].getMessage().size() != 0){
                                    message.addAll(tmpMsgResponse[i].getMessage());
                                }
                            }
                            msgResponse.setMessage(message);
                            //return msgResponse;

                            SearchView search_view = (SearchView)findViewById(R.id.search_view);
                            final LinearLayout linearLayout = (LinearLayout)findViewById(R.id.linearLayout);


                            for(int i=0; i<msgResponse.getMessage().size(); i++){
                                messagesForSort.add(i,"["+msgResponse.getMessage().get(msgResponse.getMessage().size() - i - 1).getMsgCreateDt()+"]\n["
                                        +msgResponse.getMessage().get(msgResponse.getMessage().size() - i - 1).getDisasterGroup()+"]["
                                        +msgResponse.getMessage().get(msgResponse.getMessage().size() - i - 1).getDisasterLevel()+"]["
                                        +msgResponse.getMessage().get(msgResponse.getMessage().size() - i - 1).getLocationName()+"]\n"
                                        +msgResponse.getMessage().get(msgResponse.getMessage().size() - i - 1).getMsg());
                            }
                            Collections.sort(messagesForSort);
                            Collections.reverse(messagesForSort);


                            for(int i=0; i<msgResponse.getMessage().size(); i++){
                                final String temp = messagesForSort.get(i);
                                Button btn = new Button(getBaseContext());
                                btn.setId(i);
                                btn.setText(temp);
                                btn.setEllipsize(TextUtils.TruncateAt.END);
                                btn.setMaxLines(3);
                                btn.setTypeface(koregular);;
                                btn.setGravity(Gravity.LEFT);
                                linearLayout.addView(btn);

                                btn.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        Toast.makeText(message_list.this,temp, (Toast.LENGTH_LONG)*2).show();
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
                                    linearLayout.removeAllViews();

                                    for(int i=0; i<msgResponse.getMessage().size(); i++){
                                        final String temp = messagesForSort.get(i);
                                        if(temp.contains(newText)) {
                                            Button btn = new Button(getBaseContext());
                                            btn.setId(i);
                                            btn.setText(temp);
                                            btn.setEllipsize(TextUtils.TruncateAt.END);
                                            btn.setMaxLines(3);
                                            btn.setTypeface(koregular);;
                                            btn.setGravity(Gravity.LEFT);
                                            linearLayout.addView(btn);

                                            btn.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    //toast.cancel();
                                                    //toast = new Toast;
                                                    //toast = Toast.makeText(message_list.this, temp, (Toast.LENGTH_LONG)*3);
                                                    //toast.show();
                                                    showToast(message_list.this, temp);
                                                }
                                            });
                                        }
                                    }

                                    return false;
                                }
                            });
                            deleteProgressDialog();
                        }

                    } else {
                        int statusCode = response.code();
                        // handle request errors depending on status code
                    }
                }


                @Override
                public void onFailure(Call<MsgResponse> call, Throwable t) {
                    //showErrorMessage();
                    Log.d("MainActivity", "error loading from API");

                }
            });

        }
    }
    public void setProgressDialog() {

        int llPadding = 30;
        LinearLayout ll = new LinearLayout(this);
        ll.setOrientation(LinearLayout.HORIZONTAL);
        ll.setPadding(llPadding, llPadding, llPadding, llPadding);
        ll.setGravity(Gravity.CENTER);
        LinearLayout.LayoutParams llParam = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        llParam.gravity = Gravity.CENTER;
        ll.setLayoutParams(llParam);

        ProgressBar progressBar = new ProgressBar(this);
        progressBar.setIndeterminate(true);
        progressBar.setPadding(0, 0, llPadding, 0);
        progressBar.setLayoutParams(llParam);

        llParam = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        llParam.gravity = Gravity.CENTER;
        TextView tvText = new TextView(this);
        tvText.setText("Loading ...");
        tvText.setTextColor(Color.parseColor("#000000"));
        tvText.setTextSize(20);
        tvText.setLayoutParams(llParam);

        ll.addView(progressBar);
        ll.addView(tvText);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setView(ll);

        dialog = builder.create();
        dialog.show();
        Window window = dialog.getWindow();
        if (window != null) {
            WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
            layoutParams.copyFrom(dialog.getWindow().getAttributes());
            layoutParams.width = LinearLayout.LayoutParams.WRAP_CONTENT;
            layoutParams.height = LinearLayout.LayoutParams.WRAP_CONTENT;
            dialog.getWindow().setAttributes(layoutParams);
        }
    }

    public void deleteProgressDialog() {
        dialog.dismiss();
    }

    public void showToast(Context context, String message) {
        if (toast == null) {
            toast = Toast.makeText(context, message, (Toast.LENGTH_LONG)*3);
        }
        else {
            toast.setText(message);
        }
        toast.show();
    }

}
