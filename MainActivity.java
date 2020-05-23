package com.example.redzone;

import org.json.JSONObject;

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
    private final CircleRequest circleRequest = new CircleRequest();

    private DrawerLayout drawerLayout;
    private View drawerView;
    private MapView mMapView;
    private MapPoint tempMapPoint;
    private int count;
    private int mode = 0; // 0 = zoom level 0~8, 1 = zoom level 9~

    private static final int numLocParent = 17;
    private static final int numLocChild = 229;


    private JSONObject mapInfo = new JSONObject();
    private String[] disasterGroup = new String[3];
    private String[] disasterType = new String[15];
    private String[] warningLevel = new String[3];

    private LocationInfo[] locParent = new LocationInfo[numLocParent];
    private LocationInfo[] locChild = new LocationInfo[numLocChild];

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

        disasterGroup = null;
        disasterType = null;

        addCirclesChild();

        if (!checkLocationServicesStatus()) {

            showDialogForLocationServiceSetting();
        }else {
            checkRunTimePermission();
        }

        String start_date = new String("2020-01-01");
        String end_date = new String("2020-12-31");
        String disaster_group = new String("질병");
        List<String> disaster_type = new ArrayList<>(Arrays.asList("코로나"));
        List<String> disaster_level = new ArrayList<>(Arrays.asList("Info", "Warning"));

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
                TextView textView = (TextView)findViewById(R.id.textView);
                textView.setText(getNearLocName());
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

        Button text_list_button = (Button) findViewById(R.id.text_list_button);
        text_list_button.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                Intent message_intent = new Intent(getApplicationContext(), message_list.class);
                startActivityForResult(message_intent, REQUEST_CODE_FILTER);
            }
        });
        //  20.05.16 소스 추가 끝

        ImageButton cp_button = (ImageButton)findViewById(R.id.cp_button);
        cp_button.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                //MapView.setMapCenterPointAndZoomLevel(tempMapPoint, 5, false);
                loadAnswers(circleRequest);
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

    public void loadAnswers(CircleRequest circleRequest) {
        service.getCircleAPI(circleRequest).enqueue(new Callback<CircleResponse>() {
            @Override
            public void onResponse(Call<CircleResponse> call, Response<CircleResponse> response) {
                if(response.isSuccessful()) {
                    TextView textView = (TextView)findViewById(R.id.textView);
                    //textView.setText(response.body().getStartDate());
                    textView.setText(response.body().getCount().get(1).getWarningCount().toString());
                    Log.d("MainActivity", "posts loaded from API");
                }else {
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
        if (count == 0){
            tempMapPoint = mMapView.getMapCenterPoint();
            mMapView.setZoomLevel(5, false);
            count++;
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

    private String getNearLocName() {
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
            return locChild[minIndex].locationName;
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
            return locParent[minIndex].locationName;
        }
    }

    private void addCirclesParent() {
        MapCircle[] circles = new MapCircle[numLocParent];
        for (int i = 0; i < numLocParent; i++) {
            circles[i] = new MapCircle(
                    MapPoint.mapPointWithGeoCoord(locParent[i].latitude, locParent[i].longitude), // center
                    10000, // radius
                    Color.argb(128, 255, 0, 0), // strokeColor
                    Color.argb(128, 255, 0, 0) // fillColor
            );
            circles[i].setTag(i);
            mMapView.addCircle(circles[i]);
        }

    }
    private void addCirclesChild() {
        MapCircle[] circles = new MapCircle[numLocChild];
        for (int i = 0; i < numLocChild; i++) {
            circles[i] = new MapCircle(
                    MapPoint.mapPointWithGeoCoord(locChild[i].latitude, locChild[i].longitude), // center
                    1000, // radius
                    Color.argb(128, 255, 0, 0), // strokeColor
                    Color.argb(128, 255, 0, 0) // fillColor
            );
            circles[i].setTag(i);
            mMapView.addCircle(circles[i]);
        }

    }
}