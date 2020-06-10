package com.geovengers.redzone;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


import androidx.appcompat.app.AppCompatActivity;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class pie_chart extends AppCompatActivity {

    private static final int MESSAGE_REQUEST_CODE = 3002;
    private MsgRequest[] initMsgRequest = new MsgRequest[3];
    private MsgRequest msgRequest = new MsgRequest();
    private MsgResponse msgResponse = new MsgResponse();
    //private MsgResponse[] tmpMsgResponse = new MsgResponse[3];
    private MsgResponse[] tmpMsgResponse = new MsgResponse[3];
    private MsgResponse realMsgResponse = new MsgResponse();
    private List<Message> message = new ArrayList<Message>();
    private JSONObject json = new JSONObject();
    private String location_name;

    private Service service = ApiUtils.getService();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pie_chart);


        Intent intent = getIntent();

        if(intent.getExtras().getInt("mode") == 0){
            location_name = intent.getExtras().getString("location_name");
            initMsgRequest[0] = (MsgRequest) intent.getSerializableExtra("msgRequest0");
            initMsgRequest[1] = (MsgRequest) intent.getSerializableExtra("msgRequest1");
            initMsgRequest[2] = (MsgRequest) intent.getSerializableExtra("msgRequest2");

            initMsgAPI(initMsgRequest);
        }
        else {
            location_name = intent.getExtras().getString("location_name");
            msgRequest = (MsgRequest) intent.getSerializableExtra("msgRequest");
            Log.d("3번", "여기");
//            Log.d("MainActivity", "success" + msgResponse.getMessage().size());
            loadMsgAPI(msgRequest);
        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    public void loadMsgAPI(MsgRequest request) {
        service.getMsgAPI(request).enqueue(new Callback<MsgResponse>() {
            @Override
            public void onResponse(Call<MsgResponse> call, Response<MsgResponse> response) {
                if(response.isSuccessful()) {
                    msgResponse = response.body();
                    Log.d("2번", "여기");
                    //mAdapter.updateAnswers(response.body().getItems());
                    Log.d("MainActivity", "posts loaded from API" + msgResponse.getEndDate());
                    ArrayList NoOfEmp = new ArrayList();
                    List<String> temp = new ArrayList<String>();

                    TextView textView = (TextView)findViewById(R.id.location_name);
                    textView.setText(location_name);
                    PieChart pieChart = findViewById(R.id.pieChart);


                    for(int i = 0; i < msgResponse.getMessage().size(); i++){
                        int s = 0;
                        if(json.isNull(msgResponse.getMessage().get(i).getDisasterType())){
                            try {
                                json.put(msgResponse.getMessage().get(i).getDisasterType(), 1);
                                temp.add(msgResponse.getMessage().get(i).getDisasterType());
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        else {
                            try {
                                s = json.getInt(msgResponse.getMessage().get(i).getDisasterType());
                                s++;
                                json.put(msgResponse.getMessage().get(i).getDisasterType(), s);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    for(int i = 0; i < temp.size(); i++) {
                        try {
                            int s = json.getInt(temp.get(i));
                            NoOfEmp.add(new Entry(s, i));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    PieDataSet dataSet = new PieDataSet(NoOfEmp, "Number Of Type");

                    for (int i = 0; i < temp.size(); i++) {
                        Log.d("여기4", "후아" + temp.get(i));
                    }
                    ArrayList type = new ArrayList();

                    for(int i = 0; i < temp.size(); i++) {
                        type.add(temp.get(i));
                    }
                    PieData data = new PieData(type, dataSet);          // MPAndroidChart v3.X 오류 발생
                    data.setValueTextSize(10f);
                    dataSet.setColors(ColorTemplate.COLORFUL_COLORS);
                    pieChart.animateXY(5000, 5000);

                    pieChart.setData(data);



                    Button text_list_button = (Button) findViewById(R.id.text_list_button);
                    text_list_button.setOnClickListener(new View.OnClickListener() {
                        public void onClick(View v) {
                            Intent message_intent = new Intent(getApplicationContext(), message_list.class);
                            message_intent.putExtra("msgRequest", msgRequest);
                            message_intent.putExtra("location_name", location_name);
                            message_intent.putExtra("mode", 1);
                            startActivityForResult(message_intent, MESSAGE_REQUEST_CODE);
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
        final int[] count = {0, 0};
        for (int i = 0; i < 3; i++) {
            final int j = i;
            Log.d("여기는??", "여기도");
            service.getMsgAPI(msgRequest[i]).enqueue(new Callback<MsgResponse>() {
                @Override
                public synchronized void onResponse(Call<MsgResponse> call, Response<MsgResponse> response) {
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
                            ArrayList NoOfEmp = new ArrayList();
                            List<String> temp = new ArrayList<String>();

                            PieChart pieChart = findViewById(R.id.pieChart);
                            TextView textView = (TextView)findViewById(R.id.location_name);
                            textView.setText(location_name);

                            for(int i = 0; i < msgResponse.getMessage().size(); i++){
                                int s = 0;
                                if(json.isNull(msgResponse.getMessage().get(i).getDisasterType())){
                                    try {
                                        json.put(msgResponse.getMessage().get(i).getDisasterType(), 1);
                                        temp.add(msgResponse.getMessage().get(i).getDisasterType());
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                                else {
                                    try {
                                        s = json.getInt(msgResponse.getMessage().get(i).getDisasterType());
                                        s++;
                                        json.put(msgResponse.getMessage().get(i).getDisasterType(), s);
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                            Log.d("여기4", "후아2222" + temp.size());

                            for(int i = 0; i < temp.size(); i++) {
                                try {
                                    int s = json.getInt(temp.get(i));
                                    NoOfEmp.add(new Entry(s, i));
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                            PieDataSet dataSet = new PieDataSet(NoOfEmp, "Number Of Type");
                            for (int i = 0; i < temp.size(); i++) {
                                Log.d("여기4", "후아" + temp.size());
                            }
                            ArrayList type = new ArrayList();

                            for(int i = 0; i < temp.size(); i++) {
                                type.add(temp.get(i));
                            }
                            PieData data = new PieData(type, dataSet);          // MPAndroidChart v3.X 오류 발생
                            data.setValueTextSize(10f);
                            dataSet.setColors(ColorTemplate.COLORFUL_COLORS);
                            pieChart.animateXY(5000, 5000);
                            pieChart.setData(data);



                            Button text_list_button = (Button) findViewById(R.id.text_list_button);
                            text_list_button.setOnClickListener(new View.OnClickListener() {
                                public void onClick(View v) {
                                    Intent message_intent = new Intent(getApplicationContext(), message_list.class);
                                    message_intent.putExtra("msgRequest0", initMsgRequest[0]);
                                    message_intent.putExtra("msgRequest1", initMsgRequest[1]);
                                    message_intent.putExtra("msgRequest2", initMsgRequest[2]);
                                    message_intent.putExtra("location_name", location_name);
                                    message_intent.putExtra("mode", 0);
                                    startActivityForResult(message_intent, MESSAGE_REQUEST_CODE);
                                }
                            });
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
}
