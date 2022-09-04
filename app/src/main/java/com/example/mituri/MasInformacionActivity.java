package com.example.mituri;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.mituri.Clases.Usuario;

public class MasInformacionActivity extends AppCompatActivity {

    private ImageView img;
    private TextView TxtNombre, TxtPais, TxtRegion, TxtDescripcion;

    private String IDBlog;
    private String Coordenadas;
    private String Foto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mas_informacion);

        TxtNombre = findViewById(R.id.TxtNombreMasInfo);
        TxtPais = findViewById(R.id.TxtPaisMasInfo);
        TxtRegion = findViewById(R.id.TxtRegionMasInfo);
        TxtDescripcion = findViewById(R.id.TxtDescripcionMasInfo);
        img = findViewById(R.id.imgMasInfo);

        TxtNombre.setText(getIntent().getStringExtra("Nombre"));
        TxtPais.setText("Pais: "+getIntent().getStringExtra("Pais"));
        TxtRegion.setText("Region: "+getIntent().getStringExtra("Region"));
        TxtDescripcion.setText(getIntent().getStringExtra("Descripcion"));

        IDBlog = getIntent().getStringExtra("IDBlog");
        Coordenadas = getIntent().getStringExtra("Coordenadas");

        Foto = getIntent().getStringExtra("Foto");

        Glide.with(getApplicationContext()).load(Foto).into(img);


    }
}