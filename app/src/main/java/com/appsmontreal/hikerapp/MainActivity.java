package com.appsmontreal.hikerapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;

import java.text.DecimalFormat;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    private LocationManager locationManager;
    private LocationListener locationListener;
    private Location lastKnownLocation;
    private double latitude;
    private double longitude;
    private double accuracy;
    private double altitude;
    private TextView latitudeTextView;
    private TextView longitudeTextView;
    private TextView accuracyTextView;
    private TextView altitudeTextView;
    private TextView addressTextView;


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        latitudeTextView = findViewById(R.id.latitudeTextView);
        longitudeTextView = findViewById(R.id.longitudeTextView);
        accuracyTextView = findViewById(R.id.accuracyTextView);
        altitudeTextView = findViewById(R.id.altitudeTextView);
        addressTextView = findViewById(R.id.addressTextView);
        searchHikerLocation();
    }


    private void searchHikerLocation() {
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @SuppressLint("MissingPermission")
            @Override
            public void onLocationChanged(Location location) {
//                Toast.makeText(MainActivity.this,"New Position",Toast.LENGTH_LONG).show();
                getLocation(location);
            }

            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {

            }

            @Override
            public void onProviderEnabled(String s) {

            }

            @Override
            public void onProviderDisabled(String s) {

            }
        };

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},1);
        } else {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,0,0,locationListener);
        }
    }

    private void getLocation(Location location) {
        latitude = location.getLatitude();
        longitude = location.getLongitude();
        accuracy = location.getAccuracy();
        altitude = location.getAltitude();

        // Add a marker in userPosition and move the camera
        Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
        try {
            List<Address> listAddresses = geocoder.getFromLocation(latitude,longitude,1);

            if (listAddresses != null && listAddresses.size() > 0) {
                String anAddress = "";
                if (listAddresses.get(0).getThoroughfare() != null) {
                    anAddress += listAddresses.get(0).getThoroughfare() + ",\n\n ";
                }


                if (listAddresses.get(0).getLocality() != null) {
                    anAddress += listAddresses.get(0).getLocality() + ",\n\n ";
                }

                if (listAddresses.get(0).getAdminArea() != null) {
                    anAddress += listAddresses.get(0).getAdminArea();
                }

                Log.i("Location Data ===> ", listAddresses.get(0).toString());

                latitudeTextView.setText(Double.toString(Math.round(latitude)));
                longitudeTextView.setText(Double.toString(Math.round(longitude)));
                accuracyTextView.setText(Double.toString(Math.round(accuracy)));
                altitudeTextView.setText(Double.toString(Math.round(altitude)));
                addressTextView.setText(anAddress);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
