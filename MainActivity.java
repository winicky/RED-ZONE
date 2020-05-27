package com.geovengers.redzone;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.graphics.Color;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.geovengers.redzone.ApiUtils;
import com.geovengers.redzone.CircleRequest;
import com.geovengers.redzone.CircleResponse;
import com.geovengers.redzone.MsgRequest;
import com.geovengers.redzone.R;
import com.geovengers.redzone.Service;
import com.geovengers.redzone.message_list;
import com.geovengers.redzone.pie_chart;
import com.geovengers.redzone.set_filter;

import net.daum.mf.map.api.MapCircle;
import net.daum.mf.map.api.MapPoint;
import net.daum.mf.map.api.MapReverseGeoCoder;
import net.daum.mf.map.api.MapView;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.view.View.*;

class LocationInfo {
    String locationName;
    double latitude;
    double longitude;
    String locationCode;
    int infoCount;
    int warningCount;
}

public class MainActivity extends AppCompatActivity implements MapView.MapViewEventListener, MapView.CurrentLocationEventListener, MapReverseGeoCoder.ReverseGeoCodingResultListener {

    private static final String LOG_TAG = "MainActivity";

    private Service service = ApiUtils.getService();

    private CircleRequest[] initRequest = new CircleRequest[3];
    private CircleRequest circleRequest = new CircleRequest();
    private MsgRequest msgRequest = new MsgRequest();
    private MsgResponse msgResponse = new MsgResponse();

    private DrawerLayout drawerLayout;
    private View drawerView;
    private MapView mMapView;
    private MapPoint tempMapPoint;
    private int isStart;
    private int mode = 0; // 0(시군구) = zoom level 0~8, 1(도) = zoom level 9~

    public static final int numLocParent = 16;
    public static final int numLocChild = 229;

    private Integer sumParentInfoCount = 0;
    private Integer sumParentWarningCount = 0;
    private Integer sumChildInfoCount = 0;
    private Integer sumChildWarningCount = 0;

    public static LocationInfo[] locParent = new LocationInfo[numLocParent];
    public static LocationInfo[] locChild = new LocationInfo[numLocChild];

    AssetManager assetManager;

    private static final int GPS_ENABLE_REQUEST_CODE = 2001;
    private static final int PERMISSIONS_REQUEST_CODE = 100;
    public static final int REQUEST_CODE_FILTER = 1001;

    String[] REQUIRED_PERMISSIONS  = {Manifest.permission.ACCESS_FINE_LOCATION};


    @Override
    protected void onCreate(Bundle savedInstanceState) {    //세로모드 가로모드 전환시에 전역변수 유지하고싶을때
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        drawerLayout = (DrawerLayout)findViewById(R.id.drawer_layout);
        drawerView = (View)findViewById(R.id.drawer);
        mMapView = (MapView) findViewById(R.id.map_view);
        assetManager = getResources().getAssets();

        InputStream inputStream = null;

        for(int i=0; i<numLocParent; i++)
            locParent[i] = new LocationInfo();

        for(int i=0; i<numLocChild; i++)
            locChild[i] = new LocationInfo();

        try {
            // csv 데이타 파일
            //File csv = new File("d:\\data\\Regression_ver20130401.csv");
            inputStream = assetManager.open("location01.csv", AssetManager.ACCESS_BUFFER);
            BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
            String line = "";
            int row = 0;
            while ((line = br.readLine()) != null) {
                // -1 옵션은 마지막 "," 이후 빈 공백도 읽기 위한 옵션
                String[] token = line.split(",", -1);
                locParent[row].locationName = token[0];
                locParent[row].latitude = Double.parseDouble(token[1]);
                locParent[row].longitude = Double.parseDouble(token[2]);
                locParent[row].locationCode = token[3];

                row++;

            }

            br.close();

        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        catch (IOException e) {
            e.printStackTrace();
        }

        try {
            // csv 데이타 파일
            //File csv = new File("d:\\data\\Regression_ver20130401.csv");
            inputStream = assetManager.open("location02.csv", AssetManager.ACCESS_BUFFER);
            BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
            String line = "";
            int row = 0;
            while ((line = br.readLine()) != null) {
                // -1 옵션은 마지막 "," 이후 빈 공백도 읽기 위한 옵션
                String[] token = line.split(",", -1);
                locChild[row].locationName = token[0];
                locChild[row].latitude = Double.parseDouble(token[1]);
                locChild[row].longitude = Double.parseDouble(token[2]);
                locChild[row].locationCode = token[3];

                row++;

            }

            br.close();

        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        catch (IOException e) {
            e.printStackTrace();
        }


        //mMapView.setDaumMapApiKey(MapApiConst.DAUM_MAPS_ANDROID_APP_API_KEY);
        mMapView.setCurrentLocationEventListener(this);
        mMapView.setMapViewEventListener(this);

        if (!checkLocationServicesStatus()) {

            showDialogForLocationServiceSetting();
        }else {
            checkRunTimePermission();
        }

        String start_date;
        String end_date;
        String disaster_group;
        List<String> disaster_type;
        List<String> disaster_level;

        // 앱 시작 후 레드 존 초기화 (initCircleAPI)
        start_date = new String("2020-03-01");
        end_date = new String("2020-05-24");
        disaster_type = new ArrayList<String>();
        disaster_level = new ArrayList<String>();
        for(int i=0; i<3; i++) {
            initRequest[i] = new CircleRequest();
            initRequest[i].setStart_date(start_date);
            initRequest[i].setEnd_date(end_date);
            initRequest[i].setDisaster_type(disaster_type);
            initRequest[i].setDisaster_level(disaster_level);
        }
        initRequest[0].setDisaster_group("기상특보");
        initRequest[1].setDisaster_group("질병");
        initRequest[2].setDisaster_group("other");

        clearCount();
        initCircleAPI(initRequest[0]);
        initCircleAPI(initRequest[1]);
        initCircleAPI(initRequest[2]);

        addCirclesChild();


        // 필터 쿼리 (loadCircleAPI)
        start_date = new String("2017-01-01");
        end_date = new String("2020-05-10");
        disaster_group = new String("질병");
        disaster_type = new ArrayList<>();
        disaster_level = new ArrayList<>();

        circleRequest.setStart_date(start_date);
        circleRequest.setEnd_date(end_date);
        circleRequest.setDisaster_group(disaster_group);
        circleRequest.setDisaster_type(disaster_type);
        circleRequest.setDisaster_level(disaster_level);


        Button open_button = (Button)findViewById(R.id.open_button);
        open_button.setOnClickListener(new OnClickListener(){
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(drawerView);
            }
        });

        Button close_button = (Button)findViewById(R.id.close_button);
        close_button.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.closeDrawers();
            }
        });

        Button detail_button = (Button)findViewById(R.id.detail_button);
        detail_button.setOnClickListener(new OnClickListener(){
            @Override
            public void onClick(View v) {
                msgRequest.setLocation_code("1100000000");
                msgRequest.setStart_date(circleRequest.getStart_date());
                msgRequest.setEnd_date(circleRequest.getEnd_date());
                msgRequest.setDisaster_group(circleRequest.getDisaster_group());
                msgRequest.setDisaster_type(circleRequest.getDisaster_type());
                msgRequest.setDisaster_level(circleRequest.getDisaster_level());

                loadMsgAPI(msgRequest);
                Log.d("1번", "여기");

            }
        });

        //  20.05.16 소스 추가
        Button b_filter_button = (Button) findViewById(R.id.filter_button);
        b_filter_button.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                Intent filter_intent = new Intent(getApplicationContext(), set_filter.class);
                startActivityForResult(filter_intent, REQUEST_CODE_FILTER);
            }
        });

        ImageButton cp_button = (ImageButton)findViewById(R.id.cp_button);
        cp_button.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                //mMapView.setMapCenterPointAndZoomLevel(tempMapPoint, 5, false);
                loadCircleAPI(circleRequest);
            }
        });

        drawerLayout.setDrawerListener(listner);
        drawerView.setOnTouchListener(new OnTouchListener(){
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return false;
            }
        });

    }

    DrawerLayout.DrawerListener listner = new DrawerLayout.DrawerListener() {
        @Override
        public void onDrawerSlide(@NonNull View drawerView, float slideOffset) {

        }

        @Override
        public void onDrawerOpened(@NonNull View drawerView) {

        }

        @Override
        public void onDrawerClosed(@NonNull View drawerView) {

        }

        @Override
        public void onDrawerStateChanged(int newState) {

        }
    };

    public void loadCircleAPI(CircleRequest circleRequest) {
        service.getCircleAPI(circleRequest).enqueue(new Callback<CircleResponse>() {
            @Override
            public void onResponse(Call<CircleResponse> call, Response<CircleResponse> response) {
                if (response.isSuccessful()) {
                    //TextView textView = (TextView)findViewById(R.id.textView);
                    //textView.setText(response.body().getCount().get(1).getWarningCount().toString());
                    CircleResponse body = response.body();

                    clearLocationInfo();
                    clearCount();

                    for (int i = 0; i < body.getCount().size(); i++) {
                        if (body.getCount().get(i).getLocationGroup() == 0) {
                            for (int j = 0; j < numLocParent; j++) {
                                if (body.getCount().get(i).getLocationCode().equals(locParent[j].locationCode)) {
                                    locParent[j].infoCount = body.getCount().get(i).getInfoCount();
                                    locParent[j].warningCount = body.getCount().get(i).getWarningCount();
                                    sumParentInfoCount += locParent[j].infoCount;
                                    sumParentWarningCount += locParent[j].warningCount;
                                }
                            }

                        } else {
                            for(int j=0; j<numLocChild; j++) {
                                if(body.getCount().get(i).getLocationCode().equals(locChild[j].locationCode)) {
                                    locChild[j].infoCount = body.getCount().get(i).getInfoCount();
                                    locChild[j].warningCount = body.getCount().get(i).getWarningCount();
                                    sumChildInfoCount += locChild[j].infoCount;
                                    sumChildWarningCount += locChild[j].warningCount;
                                    switch(locChild[j].locationCode.substring(0, 2)) {
                                        case "11":
                                            locParent[0].infoCount += locChild[j].infoCount;
                                            locParent[0].warningCount += locChild[j].warningCount;
                                            break;
                                        case "26":
                                            locParent[1].infoCount += locChild[j].infoCount;
                                            locParent[1].warningCount += locChild[j].warningCount;
                                            break;
                                        case "27":
                                            locParent[2].infoCount += locChild[j].infoCount;
                                            locParent[2].warningCount += locChild[j].warningCount;
                                            break;
                                        case "28":
                                            locParent[3].infoCount += locChild[j].infoCount;
                                            locParent[3].warningCount += locChild[j].warningCount;
                                            break;
                                        case "29":
                                            locParent[4].infoCount += locChild[j].infoCount;
                                            locParent[4].warningCount += locChild[j].warningCount;
                                            break;
                                        case "30":
                                            locParent[5].infoCount += locChild[j].infoCount;
                                            locParent[5].warningCount += locChild[j].warningCount;
                                            break;
                                        case "31":
                                            locParent[6].infoCount += locChild[j].infoCount;
                                            locParent[6].warningCount += locChild[j].warningCount;
                                            break;
                                        case "41":
                                            locParent[7].infoCount += locChild[j].infoCount;
                                            locParent[7].warningCount += locChild[j].warningCount;
                                            break;
                                        case "42":
                                            locParent[8].infoCount += locChild[j].infoCount;
                                            locParent[8].warningCount += locChild[j].warningCount;
                                            break;
                                        case "43":
                                            locParent[9].infoCount += locChild[j].infoCount;
                                            locParent[9].warningCount += locChild[j].warningCount;
                                            break;
                                        case "44":
                                            locParent[10].infoCount += locChild[j].infoCount;
                                            locParent[10].warningCount += locChild[j].warningCount;
                                            break;
                                        case "45":
                                            locParent[11].infoCount += locChild[j].infoCount;
                                            locParent[11].warningCount += locChild[j].warningCount;
                                            break;
                                        case "46":
                                            locParent[12].infoCount += locChild[j].infoCount;
                                            locParent[12].warningCount += locChild[j].warningCount;
                                            break;
                                        case "47":
                                            locParent[13].infoCount += locChild[j].infoCount;
                                            locParent[13].warningCount += locChild[j].warningCount;
                                            break;
                                        case "48":
                                            locParent[14].infoCount += locChild[j].infoCount;
                                            locParent[14].warningCount += locChild[j].warningCount;
                                            break;
                                        case "50":
                                            locParent[15].infoCount += locChild[j].infoCount;
                                            locParent[15].warningCount += locChild[j].warningCount;
                                            break;
                                        default:
                                            ;
                                    }
                                }
                            }
                        }
                    }
                    sumParentInfoCount += sumChildInfoCount;
                    sumParentWarningCount += sumChildWarningCount;
                    Log.d("loadCircleAPI", "ParentInfoCount = " + sumParentInfoCount.toString() + " ParentWarningCount = " + sumParentWarningCount.toString());
                    Log.d("loadCircleAPI", "ChildInfoCount = " + sumChildInfoCount.toString() + " ChildWarningCount = " + sumChildWarningCount.toString());
                }
                else {
                    int statusCode  = response.code();
                    // handle request errors depending on status code
                }
            }

            @Override
            public void onFailure(Call<CircleResponse> call, Throwable t) {
                //showErrorMessage();
                Log.d("MainActivity", "error loading from API");

            }
        });
    }



    public void initCircleAPI(CircleRequest circleRequest) {
        service.getCircleAPI(circleRequest).enqueue(new Callback<CircleResponse>() {
            @Override
            public void onResponse(Call<CircleResponse> call, Response<CircleResponse> response) {
                if (response.isSuccessful()) {
                    //TextView textView = (TextView)findViewById(R.id.textView);
                    //textView.setText(response.body().getCount().get(1).getWarningCount().toString());
                    CircleResponse body = response.body();

                    for (int i = 0; i < body.getCount().size(); i++) {
                        if (body.getCount().get(i).getLocationGroup() == 0) {
                            for (int j = 0; j < numLocParent; j++) {
                                if (body.getCount().get(i).getLocationCode().equals(locParent[j].locationCode)) {
                                    locParent[j].infoCount += body.getCount().get(i).getInfoCount();
                                    locParent[j].warningCount += body.getCount().get(i).getWarningCount();
                                    sumParentInfoCount += locParent[j].infoCount;
                                    sumParentWarningCount += locParent[j].warningCount;
                                }
                            }

                        } else {
                            for(int j=0; j<numLocChild; j++) {
                                if(body.getCount().get(i).getLocationCode().equals(locChild[j].locationCode)) {
                                    locChild[j].infoCount += body.getCount().get(i).getInfoCount();
                                    locChild[j].warningCount += body.getCount().get(i).getWarningCount();
                                    sumChildInfoCount += locChild[j].infoCount;
                                    sumChildWarningCount += locChild[j].warningCount;
                                    switch(locChild[j].locationCode.substring(0, 2)) {
                                        case "11":
                                            locParent[0].infoCount += locChild[j].infoCount;
                                            locParent[0].warningCount += locChild[j].warningCount;
                                            break;
                                        case "26":
                                            locParent[1].infoCount += locChild[j].infoCount;
                                            locParent[1].warningCount += locChild[j].warningCount;
                                            break;
                                        case "27":
                                            locParent[2].infoCount += locChild[j].infoCount;
                                            locParent[2].warningCount += locChild[j].warningCount;
                                            break;
                                        case "28":
                                            locParent[3].infoCount += locChild[j].infoCount;
                                            locParent[3].warningCount += locChild[j].warningCount;
                                            break;
                                        case "29":
                                            locParent[4].infoCount += locChild[j].infoCount;
                                            locParent[4].warningCount += locChild[j].warningCount;
                                            break;
                                        case "30":
                                            locParent[5].infoCount += locChild[j].infoCount;
                                            locParent[5].warningCount += locChild[j].warningCount;
                                            break;
                                        case "31":
                                            locParent[6].infoCount += locChild[j].infoCount;
                                            locParent[6].warningCount += locChild[j].warningCount;
                                            break;
                                        case "41":
                                            locParent[7].infoCount += locChild[j].infoCount;
                                            locParent[7].warningCount += locChild[j].warningCount;
                                            break;
                                        case "42":
                                            locParent[8].infoCount += locChild[j].infoCount;
                                            locParent[8].warningCount += locChild[j].warningCount;
                                            break;
                                        case "43":
                                            locParent[9].infoCount += locChild[j].infoCount;
                                            locParent[9].warningCount += locChild[j].warningCount;
                                            break;
                                        case "44":
                                            locParent[10].infoCount += locChild[j].infoCount;
                                            locParent[10].warningCount += locChild[j].warningCount;
                                            break;
                                        case "45":
                                            locParent[11].infoCount += locChild[j].infoCount;
                                            locParent[11].warningCount += locChild[j].warningCount;
                                            break;
                                        case "46":
                                            locParent[12].infoCount += locChild[j].infoCount;
                                            locParent[12].warningCount += locChild[j].warningCount;
                                            break;
                                        case "47":
                                            locParent[13].infoCount += locChild[j].infoCount;
                                            locParent[13].warningCount += locChild[j].warningCount;
                                            break;
                                        case "48":
                                            locParent[14].infoCount += locChild[j].infoCount;
                                            locParent[14].warningCount += locChild[j].warningCount;
                                            break;
                                        case "50":
                                            locParent[15].infoCount += locChild[j].infoCount;
                                            locParent[15].warningCount += locChild[j].warningCount;
                                            break;
                                        default:
                                            ;
                                    }
                                    //sumParentInfoCount += locChild[j].infoCount;
                                    //sumParentWarningCount += locChild[j].warningCount;

                                }
                            }
                        }
                    }
                    sumParentInfoCount += sumChildInfoCount;
                    sumParentWarningCount += sumChildWarningCount;
                    Log.d("initCircleAPI", "parentInfoCount = " + sumParentInfoCount.toString() + " parentWarningCount = " + sumParentWarningCount.toString());
                    Log.d("initCircleAPI", "childInfoCount = " + sumChildInfoCount.toString() + " childWarningCount = " + sumChildWarningCount.toString());
                } else {
                    int statusCode  = response.code();
                    // handle request errors depending on status code
                }
            }

            @Override
            public void onFailure(Call<CircleResponse> call, Throwable t) {
                //showErrorMessage();
                Log.d("MainActivity", "error loading from API");

            }
        });
    }
    public void loadMsgAPI(MsgRequest msgRequest) {
        service.getMsgAPI(msgRequest).enqueue(new Callback<MsgResponse>() {
            @Override
            public void onResponse(Call<MsgResponse> call, Response<MsgResponse> response) {
                if(response.isSuccessful()) {
                    msgResponse = response.body();
                    Log.d("2번", "여기");
                    //mAdapter.updateAnswers(response.body().getItems());
                    Log.d("MainActivity", "posts loaded from API" + msgResponse.getEndDate());

                    Intent detail_intent = new Intent(getApplicationContext(), pie_chart.class);
                    detail_intent.putExtra("msgResponse", msgResponse);
                    startActivityForResult(detail_intent, REQUEST_CODE_FILTER);
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mMapView.setCurrentLocationTrackingMode(MapView.CurrentLocationTrackingMode.TrackingModeOff);
        mMapView.setShowCurrentLocationMarker(false);
    }

    @Override
    public void onCurrentLocationUpdate(MapView mapView, MapPoint currentLocation, float accuracyInMeters) {
        //MapPoint.GeoCoordinate mapPointGeo = currentLocation.getMapPointGeoCoord();
        //Log.i(LOG_TAG, String.format("MapView onCurrentLocationUpdate (%f,%f) accuracy (%f)", mapPointGeo.latitude, mapPointGeo.longitude, accuracyInMeters));
    }

    @Override
    public void onMapViewInitialized(MapView mapView){

    }

    @Override
    public void onMapViewCenterPointMoved(MapView mapView, MapPoint mapCenterPoint){

    }

    @Override
    public void onMapViewZoomLevelChanged(MapView mapView, int zoomLevel){
        if(mMapView.getZoomLevel() >= 9){
            mMapView.removeAllCircles();
            addCirclesParent();
            mode = 1;
        }
        else {
            mMapView.removeAllCircles();
            addCirclesChild();
            mode = 0;
        }

    }

    @Override
    public void onMapViewSingleTapped(MapView mapView, MapPoint mapPoint){

    }

    @Override
    public void onMapViewDoubleTapped(MapView mapView, MapPoint mapPoint){

    }

    @Override
    public void onMapViewLongPressed(MapView mapView, MapPoint mapPoint){

    }

    @Override
    public void onMapViewDragStarted(MapView mapView, MapPoint mapPoint){

    }

    @Override
    public void onMapViewDragEnded(MapView mapView, MapPoint mapPoint){

    }

    @Override
    public void onMapViewMoveFinished(MapView mapView, MapPoint mapPoint){
        mMapView.setCurrentLocationTrackingMode(MapView.CurrentLocationTrackingMode.TrackingModeOff);
        if (isStart == 0){
            tempMapPoint = mMapView.getMapCenterPoint();
            mMapView.setZoomLevel(5, false);
            isStart++;
        }
    }

    @Override
    public void onCurrentLocationDeviceHeadingUpdate(MapView mapView, float v) {

    }

    @Override
    public void onCurrentLocationUpdateFailed(MapView mapView) {

    }

    @Override
    public void onCurrentLocationUpdateCancelled(MapView mapView) {

    }

    @Override
    public void onReverseGeoCoderFoundAddress(MapReverseGeoCoder mapReverseGeoCoder, String s) {
        mapReverseGeoCoder.toString();
        onFinishReverseGeoCoding(s);
    }

    @Override
    public void onReverseGeoCoderFailedToFindAddress(MapReverseGeoCoder mapReverseGeoCoder) {
        onFinishReverseGeoCoding("Fail");
    }

    private void onFinishReverseGeoCoding(String result) {
//        Toast.makeText(LocationDemoActivity.this, "Reverse Geo-coding : " + result, Toast.LENGTH_SHORT).show();
    }




    /*
     * ActivityCompat.requestPermissions를 사용한 퍼미션 요청의 결과를 리턴받는 메소드입니다.
     */
    @Override
    public void onRequestPermissionsResult(int permsRequestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grandResults) {

        if ( permsRequestCode == PERMISSIONS_REQUEST_CODE && grandResults.length == REQUIRED_PERMISSIONS.length) {

            // 요청 코드가 PERMISSIONS_REQUEST_CODE 이고, 요청한 퍼미션 개수만큼 수신되었다면

            boolean check_result = true;


            // 모든 퍼미션을 허용했는지 체크합니다.

            for (int result : grandResults) {
                if (result != PackageManager.PERMISSION_GRANTED) {
                    check_result = false;
                    break;
                }
            }


            if ( check_result ) {
                Log.d("@@@", "start");
                //위치 값을 가져올 수 있음
                mMapView.setCurrentLocationTrackingMode(MapView.CurrentLocationTrackingMode.TrackingModeOnWithoutHeading);
            }
            else {
                // 거부한 퍼미션이 있다면 앱을 사용할 수 없는 이유를 설명해주고 앱을 종료합니다.2 가지 경우가 있습니다.

                if (ActivityCompat.shouldShowRequestPermissionRationale(this, REQUIRED_PERMISSIONS[0])) {

                    Toast.makeText(MainActivity.this, "퍼미션이 거부되었습니다. 앱을 다시 실행하여 퍼미션을 허용해주세요.", Toast.LENGTH_LONG).show();
                    finish();


                }else {

                    Toast.makeText(MainActivity.this, "퍼미션이 거부되었습니다. 설정(앱 정보)에서 퍼미션을 허용해야 합니다. ", Toast.LENGTH_LONG).show();

                }
            }

        }
    }

    void checkRunTimePermission(){

        //런타임 퍼미션 처리
        // 1. 위치 퍼미션을 가지고 있는지 체크합니다.
        int hasFineLocationPermission = ContextCompat.checkSelfPermission(MainActivity.this,
                Manifest.permission.ACCESS_FINE_LOCATION);


        if (hasFineLocationPermission == PackageManager.PERMISSION_GRANTED ) {

            // 2. 이미 퍼미션을 가지고 있다면
            // ( 안드로이드 6.0 이하 버전은 런타임 퍼미션이 필요없기 때문에 이미 허용된 걸로 인식합니다.)


            // 3.  위치 값을 가져올 수 있음
            mMapView.setCurrentLocationTrackingMode(MapView.CurrentLocationTrackingMode.TrackingModeOnWithoutHeading);


        } else {  //2. 퍼미션 요청을 허용한 적이 없다면 퍼미션 요청이 필요합니다. 2가지 경우(3-1, 4-1)가 있습니다.
            // 3-1. 사용자가 퍼미션 거부를 한 적이 있는 경우에는
            if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this, REQUIRED_PERMISSIONS[0])) {

                // 3-2. 요청을 진행하기 전에 사용자가에게 퍼미션이 필요한 이유를 설명해줄 필요가 있습니다.
                Toast.makeText(MainActivity.this, "이 앱을 실행하려면 위치 접근 권한이 필요합니다.", Toast.LENGTH_LONG).show();
                // 3-3. 사용자게에 퍼미션 요청을 합니다. 요청 결과는 onRequestPermissionResult에서 수신됩니다.
                ActivityCompat.requestPermissions(MainActivity.this, REQUIRED_PERMISSIONS,
                        PERMISSIONS_REQUEST_CODE);


            } else {
                // 4-1. 사용자가 퍼미션 거부를 한 적이 없는 경우에는 퍼미션 요청을 바로 합니다.
                // 요청 결과는 onRequestPermissionResult에서 수신됩니다.
                ActivityCompat.requestPermissions(MainActivity.this, REQUIRED_PERMISSIONS,
                        PERMISSIONS_REQUEST_CODE);
            }

        }

    }

    //여기부터는 GPS 활성화를 위한 메소드들
    private void showDialogForLocationServiceSetting() {

        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("위치 서비스 비활성화");
        builder.setMessage("앱을 사용하기 위해서는 위치 서비스가 필요합니다.\n"
                + "위치 설정을 수정하실래요?");
        builder.setCancelable(true);
        builder.setPositiveButton("설정", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                Intent callGPSSettingIntent
                        = new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivityForResult(callGPSSettingIntent, GPS_ENABLE_REQUEST_CODE);
            }
        });
        builder.setNegativeButton("취소", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        });
        builder.create().show();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {

            case GPS_ENABLE_REQUEST_CODE:

                //사용자가 GPS 활성 시켰는지 검사
                if (checkLocationServicesStatus()) {
                    if (checkLocationServicesStatus()) {

                        Log.d("@@@", "onActivityResult : GPS 활성화 되있음");
                        checkRunTimePermission();
                        return;
                    }
                }

                break;
        }
    }

    public boolean checkLocationServicesStatus() {
        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
                || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }

    private double getDistance(double latitude, double longitude){
        double x, y;
        x = mMapView.getMapCenterPoint().getMapPointGeoCoord().latitude;
        y = mMapView.getMapCenterPoint().getMapPointGeoCoord().longitude;
        return Math.abs(latitude - x) + Math.abs(longitude - y);
    }

    private String getNearLocCode() {
        double minDistance, tempDistance;
        int minIndex = 0;
        if (mode == 0){
            minDistance = getDistance(locChild[0].latitude, locChild[0].longitude);
            for (int i=1; i<numLocChild; i++) {
                tempDistance = getDistance(locChild[i].latitude, locChild[i].longitude);
                if (tempDistance < minDistance) {
                    minDistance = tempDistance;
                    minIndex = i;
                }
            }
            return locChild[minIndex].locationCode;
        }
        else {
            minDistance = getDistance(locParent[0].latitude, locParent[0].longitude);
            for (int i=1; i<numLocParent; i++) {
                tempDistance = getDistance(locParent[i].latitude, locParent[i].longitude);
                if (tempDistance < minDistance) {
                    minDistance = tempDistance;
                    minIndex = i;
                }
            }
            return locParent[minIndex].locationCode;
        }
    }

    private void addCirclesParent() {
        MapCircle[] circles = new MapCircle[numLocParent];
        int radius;
        int total = sumParentInfoCount + sumParentWarningCount;
        Log.d("addCirclesParent", "ParentInfoCount = " + sumParentInfoCount.toString() + " ParentWarningCount = " + sumParentWarningCount.toString());
        Log.d("addCirclesParent", "ChildInfoCount = " + sumChildInfoCount.toString() + " ChildWarningCount = " + sumChildWarningCount.toString());
        for (int i = 0; i < numLocParent; i++) {
            try {
                radius = 100000 * (locParent[i].infoCount + locParent[i].warningCount) / total;
            } catch(ArithmeticException e) {
                radius = 0;
                Log.d("addCirclesParent", "[ArithmeticException] : " + locParent[i].locationName + " set radius 0");
            }
            circles[i] = new MapCircle(
                    MapPoint.mapPointWithGeoCoord(locParent[i].latitude, locParent[i].longitude), // center
                    radius,
                    //10000, // radius
                    Color.argb(128, 255, 0, 0), // strokeColor
                    Color.argb(128, 255, 0, 0) // fillColor
            );
            circles[i].setTag(i);
            Log.d("addCirclesParent", locParent[i].locationName + " radius: " + radius);
            Log.d("addCirclesParent", "infoCount = " + locParent[i].infoCount + " warningCount = " + locParent[i].warningCount);
            mMapView.addCircle(circles[i]);
        }

    }
    private void addCirclesChild() {
        MapCircle[] circles = new MapCircle[numLocChild];
        int radius;
        int total = sumChildInfoCount + sumChildWarningCount;
        Log.d("addCirclesChild", "ParentInfoCount = " + sumParentInfoCount.toString() + " ParentWarningCount = " + sumParentWarningCount.toString());
        Log.d("addCirclesChild", "ChildInfoCount = " + sumChildInfoCount.toString() + " ChildWarningCount = " + sumChildWarningCount.toString());
        for (int i = 0; i < numLocChild; i++) {
            try {
                radius = 100000 * (locChild[i].infoCount + locChild[i].warningCount) / total;
            } catch(ArithmeticException e) {
                radius = 0;
                Log.d("addCirclesChild", "[ArithmeticException] : " + locChild[i].locationName + " set radius 0");
            }

            circles[i] = new MapCircle(
                    MapPoint.mapPointWithGeoCoord(locChild[i].latitude, locChild[i].longitude), // center
                    radius,
                    //1000, // radius
                    Color.argb(128, 255, 0, 0), // strokeColor
                    Color.argb(128, 255, 0, 0) // fillColor
            );
            circles[i].setTag(i);
            Log.d("addCirclesChild", locChild[i].locationName + " radius: " + radius);
            mMapView.addCircle(circles[i]);
        }
    }

    private void clearLocationInfo() {
        for(int i=0; i<numLocParent; i++) {
            locParent[i].infoCount = 0;
            locParent[i].warningCount = 0;
        }
        for(int i=0; i<numLocChild; i++) {
            locChild[i].infoCount = 0;
            locChild[i].warningCount = 0;
        }
    }

    private void clearCount() {
        sumParentInfoCount = 0;
        sumParentWarningCount = 0;
        sumChildInfoCount = 0;
        sumChildWarningCount = 0;
    }
}