package com.example.mituri;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MasInformacionActivity extends AppCompatActivity {
    public FirebaseUser currentUser;

    private ImageView img;
    private TextView TxtNombre, TxtPais, TxtRegion, TxtDescripcion, TxtMediaEstrellas;
    private Button btnVerMapa,btnCalificar;
    private RatingBar ratingBar;
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
        btnVerMapa = findViewById(R.id.BtnMapaMasInfo);
        btnCalificar = findViewById(R.id.btnCalificar);
        ratingBar = findViewById(R.id.ratingBar);
        TxtMediaEstrellas = findViewById(R.id.mediaEstrellas);

        TxtNombre.setText(getIntent().getStringExtra("Nombre"));
        TxtPais.setText("Pais: " + getIntent().getStringExtra("Pais"));
        TxtRegion.setText("Region: " + getIntent().getStringExtra("Region"));
        TxtDescripcion.setText(getIntent().getStringExtra("Descripcion"));

        IDBlog = getIntent().getStringExtra("IDBlog");
        Coordenadas = getIntent().getStringExtra("Coordenadas");

        Foto = getIntent().getStringExtra("Foto");

        Glide.with(getApplicationContext()).load(Foto).into(img);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference reference = database.getReference(MainActivity.TBL_Calificaciones);
        currentUser = FirebaseAuth.getInstance().getCurrentUser();

        if(currentUser == null){
            btnCalificar.setVisibility(View.INVISIBLE);
        }


        reference.child(IDBlog).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                float valorMedio = 0;
                int contador = 0;
                for (DataSnapshot child : snapshot.getChildren()) {
                    valorMedio += Float.parseFloat(child.getValue().toString());
                    contador++;
                }
                valorMedio /= contador;
                ratingBar.setRating(valorMedio);
                TxtMediaEstrellas.setText("["+valorMedio+"]");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        btnVerMapa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String[] coordenada = Coordenadas.split("-");
                Uri location = Uri.parse("geo:" + coordenada[0] + ",-" + coordenada[1] + "?z=14"); // z param is zoom level
                //Toast.makeText(MasInformacionActivity.this, location.toString(), Toast.LENGTH_SHORT).show();
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, location);
                startActivity(mapIntent);
            }
        });

        btnCalificar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                view = getLayoutInflater().inflate(R.layout.layout_calificar_flotante,null);
                builder.setView(view);
                Dialog dialog = builder.create();
                dialog.show();

                ImageView cerrar = view.findViewById(R.id.close);
                cerrar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });

                RatingBar ratingBar2 = view.findViewById(R.id.ratingBar2);
                TextView texto = view.findViewById(R.id.estrellas);
                Button valorar = view.findViewById(R.id.valorar);

                reference.child(IDBlog).child(currentUser.getUid()).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DataSnapshot> task) {
                        //Toast.makeText(MasInformacionActivity.this, ""+task.getResult().getValue(), Toast.LENGTH_SHORT).show();
                        String valor = task.getResult().getValue().toString();
                        ratingBar2.setRating(Float.parseFloat(valor));
                    }
                });


                ratingBar2.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
                    @Override
                    public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
                        texto.setText("["+v+"]");
                    }
                });

                valorar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        double rating = ratingBar2.getRating();
                        if(rating > 0){
                            reference.child(IDBlog).child(currentUser.getUid()).setValue(rating);
                            Toast.makeText(MasInformacionActivity.this, "Gracias por valorar el sitio", Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                        }else {
                            Toast.makeText(MasInformacionActivity.this, "Ingrese una valoracion para continuar", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });

    }

    public void Home(View view) { startActivity(new Intent(MasInformacionActivity.this, Home.class));}
    public void AddPost(View view) { startActivity(new Intent(MasInformacionActivity.this, AddPost.class));}
    public void Profile(View view) { startActivity(new Intent(MasInformacionActivity.this, ProfileActivity.class)); }
}