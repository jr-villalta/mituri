package com.example.mituri;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mituri.Adaptador.AdaptadorSitioTuristico;
import com.example.mituri.Clases.SitioTuristico;
import com.example.mituri.Clases.Usuario;
import com.example.mituri.Modelo.ModPaises;
import com.example.mituri.Modelo.ModRegiones;
import com.example.mituri.Modelo.ServiceAPI;
import com.example.mituri.ServiceUtils.ApiDireccion;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MisSitiosActivity extends AppCompatActivity {

    private DatabaseReference databaseReference;

    TextView userName;
    ImageView userImage;

    public FirebaseUser currentUser;
    private TextView NombreSitio;
    private Spinner Sp_Pais, Sp_Region;
    private String Nombre;
    private String Pais;
    private String Region;
    private String Code;

    private ListView Lv_Sitio;
    private ServiceAPI ServicioPaises;
    private ServiceAPI ServicioRegiones;

    private Usuario usuario = new Usuario();
    private ArrayList<SitioTuristico> ListSitio = new ArrayList<>();
    private ArrayList<String> ListaPaises = new ArrayList<String>();
    private ArrayList<String> ListaCodePaises = new ArrayList<String>();
    private ArrayList<String> ListaRegiones = new ArrayList<String>();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mis_sitios);

        NombreSitio = findViewById(R.id.SearchBarInput);
        userName = (TextView) findViewById(R.id.textViewUsuario);
        userImage = (ImageView) findViewById(R.id.profile_image);
        Lv_Sitio = (ListView) findViewById(R.id.ListSitios);
        Sp_Pais = findViewById(R.id.SpBuscarPais);
        Sp_Region = findViewById(R.id.SpBuscarRegion);
        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference.child(MainActivity.TBL_Usuarios).child(currentUser.getUid()).addValueEventListener(getUsuario);
        databaseReference.child(MainActivity.TBL_SitioTuristico).addValueEventListener(CargarSitios);

        CargarPaises();
        updateUI(currentUser);

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

        //FUNCION DE SELECCION DEL SPINNER DE PAISES
        Sp_Pais.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                //SE ASIGNA EL VALOR A LA VARIABLE SEGUN LA POSICION
                if (!ListaPaises.get(i).equals(ListaPaises.get(0))){
                    Pais = ListaPaises.get(i);
                    Code = ListaCodePaises.get(i);

                    databaseReference.child(MainActivity.TBL_SitioTuristico).addValueEventListener(CargarSitiosPorPais);
                }
                //LLAMADO A LA FUNCION
                CargarRegiones();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        //FUNCION DE SELECCION DEL SPINNER DE REGIONES
        Sp_Region.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (!ListaRegiones.get(i).equals(ListaRegiones.get(0))){
                    Region = ListaRegiones.get(i);
                    databaseReference.child(MainActivity.TBL_SitioTuristico).addValueEventListener(CargarSitiosPorRegion);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


    }

    public void Buscar (View view){

        if (NombreSitio != null){
            Nombre = NombreSitio.getText().toString();
            databaseReference.child(MainActivity.TBL_SitioTuristico).addValueEventListener(CargarSitiosPorNombre);
        }else {
            Toast.makeText(MisSitiosActivity.this, "Sitio Con Nombre "+Nombre+" No Encontrado", Toast.LENGTH_LONG).show();
        }

    }

    public void LimpiarFiltro(View view){
        Pais = null;
        Code = " ";
        Region = null;
        NombreSitio.setText(null);
        CargarPaises();

        ListaRegiones.clear();
        ArrayAdapter<String> adapter = new ArrayAdapter<>(MisSitiosActivity.this, android.R.layout.simple_dropdown_item_1line, ListaRegiones);
        Sp_Region.setAdapter(adapter);

        databaseReference.child(MainActivity.TBL_SitioTuristico).addValueEventListener(CargarSitios);
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
                Pais = null;
                Code = null;
                Region = null;
                NombreSitio.setText(null);
                ListSitio.clear();
                for (DataSnapshot items : snapshot.getChildren()) {
                    SitioTuristico Sitio = items.getValue(SitioTuristico.class);
                    ListSitio.add(Sitio);
                }
                AdaptadorSitioTuristico AdaptadorSitio = new AdaptadorSitioTuristico(ListSitio, getApplicationContext());
                Lv_Sitio.setAdapter(AdaptadorSitio);
            }
        }

        @Override
        public void onCancelled(@NonNull DatabaseError error) {

        }
    };

    public ValueEventListener CargarSitiosPorNombre = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot snapshot) {
            if (snapshot.exists()){
                ListSitio.clear();
                for (DataSnapshot items : snapshot.getChildren()) {
                    SitioTuristico Sitio = items.getValue(SitioTuristico.class);

                    assert Sitio != null;
                    if (Sitio.getNombre().equals(Nombre)){
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

    public ValueEventListener CargarSitiosPorPais = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot snapshot) {
            if (snapshot.exists()){
                ListSitio.clear();
                for (DataSnapshot items : snapshot.getChildren()) {
                    SitioTuristico Sitio = items.getValue(SitioTuristico.class);

                    assert Sitio != null;
                    if (Sitio.getPais().equals(Pais)){
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

    public ValueEventListener CargarSitiosPorRegion = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot snapshot) {
            if (snapshot.exists()){
                ListSitio.clear();
                for (DataSnapshot items : snapshot.getChildren()) {
                    SitioTuristico Sitio = items.getValue(SitioTuristico.class);

                    assert Sitio != null;
                    if (Sitio.getPais().equals(Pais)){
                        if (Sitio.getRegion().equals(Region)){
                            ListSitio.add(Sitio);
                        }
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

    public void CargarPaises(){

        //ASIGNACION DE SERVICION
        ServicioPaises = ApiDireccion.getServicePais();
        //PROCESO PARA OBTENER LA INFORMACION DE LA API
        ServicioPaises.getDatos().enqueue(new Callback<List<ModPaises>>() {
            @Override
            public void onResponse(Call<List<ModPaises>> call, Response<List<ModPaises>> response) {
                //VERIFICA SI HAY DATOS
                if (response.isSuccessful()) {
                    ListaPaises.add("Seleccione un Pais");
                    ListaCodePaises.add(" ");
                    //CICLO FOREACH PARA LA OBTENCION DE DATOS
                    for (ModPaises ItemPais : response.body()) {
                        //ALMACENA LOS DATOS EN LAS LISTA DE LOS ARRAY
                        ListaPaises.add(ItemPais.name);
                        ListaCodePaises.add(ItemPais.code);
                    }

                    //SE ASIGNAN LOS DATOS AL SPINNER DE PAISES
                    ArrayAdapter<String> adapter = new ArrayAdapter<>(MisSitiosActivity.this,
                            android.R.layout.simple_dropdown_item_1line, ListaPaises);
                    Sp_Pais.setAdapter(adapter);
                }
            }

            @Override
            public void onFailure(Call<List<ModPaises>> call, Throwable t) {
                //MUESTRA MENSAJE EN CONSOLA DEL FALLO
                Log.d("Respuesta", "Fail " + t);
            }
        });

    }

    //FUNCION DE CONSUMO DE API DE LAS REGIONES
    public void CargarRegiones() {
        //LIMPIA LA LISTA DE LAS REGIONES
        ListaRegiones.clear();

        //ASIGNACION DE SERVICION
        ServicioRegiones = ApiDireccion.getServiceRegion();
        //PROCESO PARA OBTENER LA INFORMACION DE LA API
        ServicioRegiones.find(Code).enqueue(new Callback<List<ModRegiones>>() {
            @Override
            public void onResponse(Call<List<ModRegiones>> call, Response<List<ModRegiones>> response) {

                //VERIFICA SI HAY DATOS
                if (response.isSuccessful()) {
                    ListaRegiones.add("Seleccione una Region");
                    //CICLO FOREACH PARA LA OBTENCION DE DATOS
                    for (ModRegiones ItemRegion : response.body()) {
                        //ALMACENA LOS DATOS EN LAS LISTA DEL ARRAY
                        ListaRegiones.add(ItemRegion.region);

                    }
                    //SE ASIGNAN LOS DATOS AL SPINNER DE REGIONES
                    ArrayAdapter<String> adapter = new ArrayAdapter<>(MisSitiosActivity.this,
                            android.R.layout.simple_dropdown_item_1line, ListaRegiones);
                    Sp_Region.setAdapter(adapter);
                }
            }

            @Override
            public void onFailure(Call<List<ModRegiones>> call, Throwable t) {
                //MUESTRA MENSAJE EN CONSOLA DEL FALLO
                Log.d("Respuesta", "Fail " + t);
            }
        });
    }

    public void Agregar(View view){
        startActivity(new Intent(MisSitiosActivity.this, AddPost.class));
    }
    public void Home(View view) { startActivity(new Intent(MisSitiosActivity.this, Home.class));}

    private void updateUI(FirebaseUser currentUser) {
        if(currentUser != null){
            userName.setText(currentUser.getEmail());
            if(currentUser.getPhotoUrl() != null){
                Picasso.get().load(currentUser.getPhotoUrl()).into(userImage);
            }else{
                Picasso.get().load("https://www.looper.com/img/gallery/fans-are-absolutely-loving-the-return-of-doc-ock-in-the-spider-man-no-way-home-trailer/l-intro-1629817758.jpg").into(userImage);
            }
        }else{
            userName.setText("Invitado");
            Picasso.get().load("https://png.pngitem.com/pimgs/s/130-1300400_user-hd-png-download.png").into(userImage);

        }
    }

}