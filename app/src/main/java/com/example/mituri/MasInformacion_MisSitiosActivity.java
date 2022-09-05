package com.example.mituri;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.bumptech.glide.Glide;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MasInformacion_MisSitiosActivity extends AppCompatActivity {

    private ImageView img;
    private TextView TxtNombre, TxtPais, TxtRegion, TxtDescripcion;
    private Button btnVerMapa, btnEditar,btnEliminar;

    private String IDBlog;
    private String Code;
    private String Coordenadas;
    private String Foto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mas_informacion_mis_sitios);

        TxtNombre = findViewById(R.id.TxtNombreMasInfo);
        TxtPais = findViewById(R.id.TxtPaisMasInfo);
        TxtRegion = findViewById(R.id.TxtRegionMasInfo);
        TxtDescripcion = findViewById(R.id.TxtDescripcionMasInfo);
        img = findViewById(R.id.imgMasInfo);
        btnVerMapa = findViewById(R.id.BtnMapaMasInfo);
        btnEditar = findViewById(R.id.BtnEditar);
        btnEliminar = findViewById(R.id.btnEliminar);

        TxtNombre.setText(getIntent().getStringExtra("Nombre"));
        TxtPais.setText("Pais: "+getIntent().getStringExtra("Pais"));
        TxtRegion.setText("Region: "+getIntent().getStringExtra("Region"));
        TxtDescripcion.setText(getIntent().getStringExtra("Descripcion"));

        IDBlog = getIntent().getStringExtra("IDBlog");
        Code = getIntent().getStringExtra("Code");
        Coordenadas = getIntent().getStringExtra("Coordenadas");

        Foto = getIntent().getStringExtra("Foto");

        Glide.with(getApplicationContext()).load(Foto).into(img);

        btnVerMapa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String[] coordenada = Coordenadas.split("-");
                Uri location = Uri.parse("geo:"+coordenada[0]+",-"+coordenada[1]+"?z=14"); // z param is zoom level
                Log.d("Coordenadas",""+location);
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, location);
                startActivity(mapIntent);
            }
        });

        btnEliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                builder.setCancelable(true);
                builder.setTitle("Eliminar");
                String mensaje = "Deseas eliminar el registro? ";
                builder.setMessage(mensaje);
                builder.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        FirebaseDatabase database = FirebaseDatabase.getInstance();
                        DatabaseReference reference =database.getReference(MainActivity.TBL_SitioTuristico);
                        reference.child(IDBlog).removeValue();
                        finish();
                        Toast.makeText(getApplicationContext(), "Registro eliminado con exito", Toast.LENGTH_SHORT).show();
                    }
                });
                builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast.makeText(view.getContext(), "Operacion cancelada con exito", Toast.LENGTH_LONG).show();
                    }
                });
                builder.create();
                builder.show();
            }
        });
        btnEditar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(getApplicationContext(), EditarPostActivity.class);
                intent.putExtra("Activity", "MasInformacion");
                intent.putExtra("IDBlog", IDBlog);
                intent.putExtra("Nombre", getIntent().getStringExtra("Nombre"));
                intent.putExtra("Pais", getIntent().getStringExtra("Pais"));
                intent.putExtra("Code", getIntent().getStringExtra("Code"));
                intent.putExtra("Region", getIntent().getStringExtra("Region"));
                intent.putExtra("Coordenadas", Coordenadas);
                intent.putExtra("Descripcion", getIntent().getStringExtra("Descripcion"));
                intent.putExtra("Foto", Foto);
                startActivity(intent);

            }
        });
    }

    public void MiSitios(View view) { startActivity(new Intent(MasInformacion_MisSitiosActivity.this, MisSitiosActivity.class)); }
    public void Agregar(View view){ startActivity(new Intent(MasInformacion_MisSitiosActivity.this, AddPost.class)); }
    public void Profile(View view) { startActivity(new Intent(MasInformacion_MisSitiosActivity.this, ProfileActivity.class)); }
}