package com.example.mituri;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    public static final String TBL_SitioTuristico = "SitioTuristico";
    public static final String TBL_Usuarios = "Usuarios";

    public static final String Foto_NuevoBlog = "https://firebasestorage.googleapis.com/v0/b/mituri-4793b.appspot.com/o/Lugar.png?alt=media&token=ac734149-7e88-4b38-9d37-ceccf53ea20f";

    private Button btnlogout, btnAdd;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnlogout = findViewById(R.id.btnSalir);
        btnAdd = findViewById(R.id.btnAgregar);
        mAuth = FirebaseAuth.getInstance();

        btnlogout.setOnClickListener(view ->{
            mAuth.signOut();
            startActivity(new Intent(MainActivity.this, Home.class));
        });

        btnAdd.setOnClickListener(view -> {
            startActivity(new Intent(MainActivity.this, CRUD_SitioTuristico.class));
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser user = mAuth.getCurrentUser();
        if (user == null){
            startActivity(new Intent(MainActivity.this, Home.class));
        }
    }

}