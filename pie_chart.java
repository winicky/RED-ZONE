package com.example.redzone;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;


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
    private JSONObject json = new JSONObject();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pie_chart);
        PieChart pieChart = findViewById(R.id.pieChart);
        ArrayList NoOfEmp = new ArrayList();
        List<String> temp = new ArrayList<String>();

        Intent intent = getIntent();

        msgResponse = (MsgResponse)intent.getSerializableExtra("msgResponse");
        Log.d("3번", "여기");
        Log.d("MainActivity", "success" + msgResponse.getMessage().size());

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
}
