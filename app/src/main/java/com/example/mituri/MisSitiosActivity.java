package com.example.mituri;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mituri.Adaptador.AdaptadorSitioTuristico;
import com.example.mituri.Clases.SitioTuristico;
import com.example.mituri.Clases.Usuario;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Objects;

public class MisSitiosActivity extends AppCompatActivity {

    TextView userName;
    DatabaseReference databaseReference;
    public FirebaseUser currentUser;

    private ListView Lv_Sitio;

    private Usuario usuario = new Usuario();
    private ArrayList<SitioTuristico> ListSitio = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mis_sitios);

        userName = (TextView) findViewById(R.id.textViewUsuario);
        Lv_Sitio = (ListView) findViewById(R.id.ListSitios);
        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference.child(MainActivity.TBL_Usuarios).child(currentUser.getUid()).addValueEventListener(getUsuario);
        databaseReference.child(MainActivity.TBL_SitioTuristico).addValueEventListener(CargarSitios);

        Lv_Sitio.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                Intent intent = new Intent(getApplicationContext(), MasInformacion_MisSitiosActivity.class);
                intent.putExtra("IDBlog", ListSitio.get(i).getIDBlog());
                intent.putExtra("Nombre", ListSitio.get(i).getNombre());
                intent.putExtra("Pais", ListSitio.get(i).getPais());
                intent.putExtra("Region", ListSitio.get(i).getRegion());
                intent.putExtra("Coordenadas", ListSitio.get(i).getCoordenadas());
                intent.putExtra("Descripcion", ListSitio.get(i).getDescripcion());
                intent.putExtra("Foto", ListSitio.get(i).getFoto());
                startActivity(intent);

            }
        });

    }

    public ValueEventListener getUsuario = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot snapshot) {
            if(snapshot.exists()){
                usuario = snapshot.getValue(Usuario.class);
                usuario.setIDUsuario(currentUser.getUid());
                userName.setText(usuario.getNombre());
            }
        }
        @Override
        public void onCancelled(@NonNull DatabaseError error) {

        }
    };

    public ValueEventListener CargarSitios = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot snapshot) {
            if (snapshot.exists()){
                ListSitio.clear();
                for (DataSnapshot items : snapshot.getChildren()) {
                    SitioTuristico Sitio = items.getValue(SitioTuristico.class);
                    assert Sitio != null;
                    if (Objects.equals(currentUser.getUid(), Sitio.getUsuario().getIDUsuario())){
                        ListSitio.add(Sitio);
                    }
                }
                AdaptadorSitioTuristico AdaptadorSitio = new AdaptadorSitioTuristico(ListSitio, getApplicationContext());
                Lv_Sitio.setAdapter(AdaptadorSitio);
            }
        }

        @Override
        public void onCancelled(@NonNull DatabaseError error) {

        }
    };

    public void Agregar(View view){
        startActivity(new Intent(MisSitiosActivity.this, AddPost.class));
    }
    public void Home(View view) { startActivity(new Intent(MisSitiosActivity.this, Home.class));}

}