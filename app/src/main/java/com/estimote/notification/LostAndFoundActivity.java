package com.estimote.notification;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.estimote.cloud_plugin.common.EstimoteCloudCredentials;
import com.estimote.internal_plugins_api.cloud.CloudCredentials;
import com.estimote.internal_plugins_api.cloud.proximity.ProximityAttachment;
import com.estimote.proximity_sdk.proximity.ProximityObserver;
import com.estimote.proximity_sdk.proximity.ProximityObserverBuilder;
import com.estimote.proximity_sdk.proximity.ProximityZone;

import kotlin.Unit;
import kotlin.jvm.functions.Function1;

//
// Running into any issues? Drop us an email to: contact@estimote.com
//

public class LostAndFoundActivity extends AppCompatActivity {

    public CloudCredentials cloudCredentials;
    private ProximityObserver proximityObserver;
    public static Double latitude = 0.0;
    public static Double longitude = 0.0;
    LocationManager locationManager;
    LocationListener locationListener;

    public void printLocation(Location location){
        Log.i("location",location.toString());
        TextView lastSeen = findViewById(R.id.lastSeenTextView);
        latitude = location.getLatitude();
        longitude = location.getLongitude();
        String latStr = latitude.toString().substring(0,7);
        String lngStr = longitude.toString().substring(0,7);
        lastSeen.setText(latStr + "," + lngStr);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == 1){
            if(ContextCompat.checkSelfPermission(LostAndFoundActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,0,0,locationListener);
                Location lastLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                if(lastLocation != null){
                    printLocation(lastLocation);
                }
                else{
                    lastLocation = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                    printLocation(lastLocation);
                }
            }
        }



    }

    public void onClickGo(View view) {
        startMonitoring();

    }

    public void onClickMaps(View view) {
        Uri mapUri = Uri.parse("geo:" + latitude + "," + longitude);
        Intent intent = new Intent(Intent.ACTION_VIEW, mapUri);
        intent.setPackage("com.google.android.apps.maps");
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lost_and_found);





        cloudCredentials = new EstimoteCloudCredentials("swetha-chandrasekar-sjsu-e-e0b", "850d498a60406d936540b6b2ea9d0328");
        startMonitoring();


    }


    public void startMonitoring() {
        this.proximityObserver = new ProximityObserverBuilder(getApplicationContext(), cloudCredentials)
                .withOnErrorAction(new Function1<Throwable, Unit>() {
                    @Override
                    public Unit invoke(Throwable throwable) {
                        Log.e("app", "proximity observer error: " + throwable);
                        return null;
                    }
                })
                .withBalancedPowerMode()
                .build();

        ProximityZone zone1 = this.proximityObserver.zoneBuilder()
                .forAttachmentKeyAndValue("beacon", "darkblue")
                .inCustomRange(2.0)
                .withOnEnterAction(new Function1<ProximityAttachment, Unit>() {
                    @Override
                    public Unit invoke(ProximityAttachment attachment) {
                        Log.i("app", "Welcome to the 1st floor");
                        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
                        locationListener = new LocationListener() {
                            @Override
                            public void onLocationChanged(Location location) {

                            }

                            @Override
                            public void onStatusChanged(String provider, int status, Bundle extras) {

                            }

                            @Override
                            public void onProviderEnabled(String provider) {

                            }

                            @Override
                            public void onProviderDisabled(String provider) {

                            }
                        };

                        if(Build.VERSION.SDK_INT < 23){
                            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,0,0,locationListener);
                        }
                        else{
                            if(ContextCompat.checkSelfPermission(LostAndFoundActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
                                ActivityCompat.requestPermissions(LostAndFoundActivity.this,new String[] {Manifest.permission.ACCESS_FINE_LOCATION},1);
                            }
                            else{
                                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,0,0,locationListener);
                                Location lastLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                                if(lastLocation != null){
                                    printLocation(lastLocation);
                                }
                                else{
                                    lastLocation = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                                    printLocation(lastLocation);
                                }
                            }
                        }

                        return null;
                    }
                })
                .withOnExitAction(new Function1<ProximityAttachment, Unit>() {
                    @Override
                    public Unit invoke(ProximityAttachment attachment) {
                        Log.i("app", "Bye bye, come visit us again on the 1st floor");

                        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
                        locationListener = new LocationListener() {
                            @Override
                            public void onLocationChanged(Location location) {

                            }

                            @Override
                            public void onStatusChanged(String provider, int status, Bundle extras) {

                            }

                            @Override
                            public void onProviderEnabled(String provider) {

                            }

                            @Override
                            public void onProviderDisabled(String provider) {

                            }
                        };

                        if(Build.VERSION.SDK_INT < 23){
                            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,0,0,locationListener);
                        }
                        else{
                            if(ContextCompat.checkSelfPermission(LostAndFoundActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
                                ActivityCompat.requestPermissions(LostAndFoundActivity.this,new String[] {Manifest.permission.ACCESS_FINE_LOCATION},1);
                            }
                            else{
                                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,0,0,locationListener);
                                Location lastLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                                if(lastLocation != null){
                                    printLocation(lastLocation);
                                }
                                else{
                                    lastLocation = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                                    printLocation(lastLocation);
                                }
                            }
                        }
                        return null;
                    }

                }).create();

        this.proximityObserver.addProximityZones(zone1);

        this.proximityObserver.start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }

}
