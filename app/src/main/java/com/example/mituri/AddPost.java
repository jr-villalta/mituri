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
import android.widget.*;


public class AddPost extends AppCompatActivity {

    Button btnGPS;
    TextView tvUbication;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_post);

        tvUbication = (TextView)findViewById(R.id.locationtv);
        btnGPS = (Button)findViewById(R.id.gpsbtn);

        //Aqui empieza el desvergue de la ubi
        btnGPS.setOnClickListener(new View.OnClickListener(){
           @Override
           public void onClick(View view){

               LocationManager locationManager = (LocationManager) AddPost.this.getSystemService(Context.LOCATION_SERVICE);

                LocationListener locationListener = new LocationListener() {
                    public void onLocationChanged(Location location) {
                        tvUbication.setText(""+location.getLatitude()+" "+location.getLongitude());
                    }

                    public void onStatusChanged(String provider, int status, Bundle extras) {}

                    public void onProviderEnabled(String provider) {}

                    public void onProviderDisabled(String provider) {}

                };
                int permisionCheck = ContextCompat.checkSelfPermission(AddPost.this,
                        Manifest.permission.ACCESS_FINE_LOCATION);
                locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0,0,locationListener);

           }
        }); //aqui termina los otros son permisos para que el usuario pueda entrar

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