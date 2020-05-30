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

public class pie_chart extends AppCompatActivity {

    private static final int MESSAGE_REQUEST_CODE = 3002;
    private MsgResponse msgResponse = new MsgResponse();
    private MsgResponse[] tmpMsgResponse = new MsgResponse[3];
    private List<Message> message = new ArrayList<Message>();
    private JSONObject json = new JSONObject();
    private String location_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pie_chart);
        PieChart pieChart = findViewById(R.id.pieChart);
        ArrayList NoOfEmp = new ArrayList();
        List<String> temp = new ArrayList<String>();

        Intent intent = getIntent();

        if(intent.getExtras().getInt("mode") == 0){
            location_name = intent.getExtras().getString("location_name");

            tmpMsgResponse[0] = (MsgResponse) intent.getSerializableExtra("msgResponse0");
            tmpMsgResponse[1] = (MsgResponse) intent.getSerializableExtra("msgResponse1");
            tmpMsgResponse[2] = (MsgResponse) intent.getSerializableExtra("msgResponse2");
            /*
            Log.d("3번", "집가자" + location_name);

            Log.d("3번", "집가자" + tmpMsgResponse[0].getDisasterGroup());
            Log.d("3번", "집가자" + tmpMsgResponse[1].getDisasterGroup());
            Log.d("3번", "집가자" + tmpMsgResponse[2].getDisasterGroup());
               */
            for(int i=0; i<3; i++){
                if(tmpMsgResponse[i].getMessage().size() != 0){
                    message.addAll(tmpMsgResponse[i].getMessage());
                }
            }

            //message.addAll(tmpMsgResponse[0].getMessage());
            msgResponse.setMessage(message);
        }
        else {
            location_name = intent.getExtras().getString("location_name");
            msgResponse = (MsgResponse) intent.getSerializableExtra("msgResponse");
            Log.d("3번", "여기");
            Log.d("MainActivity", "success" + msgResponse.getMessage().size());
        }

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
        pieChart.setData(data);
        dataSet.setColors(ColorTemplate.COLORFUL_COLORS);
        pieChart.animateXY(5000, 5000);


        Button text_list_button = (Button) findViewById(R.id.text_list_button);
        text_list_button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent message_intent = new Intent(getApplicationContext(), message_list.class);
                message_intent.putExtra("msgResponse", msgResponse);
                startActivityForResult(message_intent, MESSAGE_REQUEST_CODE);
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

}
