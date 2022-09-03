package com.example.mituri;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class CRUD_SitioTuristico extends AppCompatActivity {

    Button btnGPS;
    TextView tvUbication;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crud_sitio_turistico);

        tvUbication = (TextView)findViewById(R.id.locationtv);
        btnGPS = (Button)findViewById(R.id.gpsbtn);


        btnGPS.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){

                LocationManager locationManager = (LocationManager) CRUD_SitioTuristico.this.
                        getSystemService(Context.LOCATION_SERVICE);

                LocationListener locationListener = new LocationListener() {
                    public void onLocationChanged(Location location) {
                        tvUbication.setText(""+location.getLatitude()+" "+location.getLongitude());
                    }

                    public void onStatusChanged(String provider, int status, Bundle extras) {}

                    public void onProviderEnabled(String provider) {}

                    public void onProviderDisabled(String provider) {}

                };
                int permisionCheck = ContextCompat.checkSelfPermission(CRUD_SitioTuristico.this,
                        Manifest.permission.ACCESS_FINE_LOCATION);
                locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0,
                        0,locationListener);

            }
        });

        int permisionCheck = ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION);

        if ( permisionCheck == PackageManager.PERMISSION_DENIED) {
            if ( ActivityCompat.shouldShowRequestPermissionRationale ( this,
                    Manifest.permission.ACCESS_FINE_LOCATION)){
            } else{
                ActivityCompat.requestPermissions (this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        1);
            }
        }

    }




}