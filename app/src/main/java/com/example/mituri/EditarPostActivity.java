package com.example.mituri;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.mituri.Clases.SitioTuristico;
import com.example.mituri.Clases.Usuario;
import com.example.mituri.Modelo.ModPaises;
import com.example.mituri.Modelo.ModRegiones;
import com.example.mituri.Modelo.ServiceAPI;
import com.example.mituri.ServiceUtils.ApiDireccion;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditarPostActivity extends AppCompatActivity {

    public FirebaseStorage storage;
    public StorageReference Storareference;
    public DatabaseReference Datareference;
    public FirebaseAuth mAuth;
    public FirebaseUser currentUser;

    private TextView tvUbication;
    private EditText TxtNombre, TxtDescripcion;
    private Spinner Sp_Pais, Sp_Region;

    private Usuario usuario = new Usuario();
    public Uri UrlImage;
    private ImageView Img;
    private String Pais;
    private String Region;
    private String Code;
    private String ID;

    private ServiceAPI ServicioPaises;
    private ServiceAPI ServicioRegiones;

    private ArrayList<String> ListaPaises = new ArrayList<String>();
    private ArrayList<String> ListaCodePaises = new ArrayList<String>();
    private ArrayList<String> ListaRegiones = new ArrayList<String>();

    private Button btnGPS, btnGuardar;
    private String Foto;

    private AdView mAdView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_post);

        Img = findViewById(R.id.ImgEditar);
        TxtNombre = findViewById(R.id.txtNombreLugarEditar);
        TxtDescripcion = findViewById(R.id.txtDescripcionrEditar);
        tvUbication = findViewById(R.id.locationtvEditar);

        Sp_Pais = findViewById(R.id.SpCrearPaisEditar);
        Sp_Region = findViewById(R.id.SpCrearRegionEditar);

        btnGPS = (Button)findViewById(R.id.gpsbtn);
        btnGuardar = findViewById(R.id.btnGuardar);

        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();

        storage = FirebaseStorage.getInstance();
        Storareference = storage.getReference();

        Datareference = FirebaseDatabase.getInstance().getReference();

        Datareference.child(MainActivity.TBL_Usuarios).child(currentUser.getUid()).addValueEventListener(getUsuario);

        ID = getIntent().getStringExtra("IDBlog");
        TxtNombre.setText(getIntent().getStringExtra("Nombre"));
        TxtDescripcion.setText(getIntent().getStringExtra("Descripcion"));
        tvUbication.setText(getIntent().getStringExtra("Coordenadas"));
        Pais = getIntent().getStringExtra("Pais");
        Code = getIntent().getStringExtra("Code");
        Region = getIntent().getStringExtra("Region");
        Foto = getIntent().getStringExtra("Foto");

        Glide.with(getApplicationContext()).load(Foto).into(Img);

        CargarPaises();
        CargarRegiones();
        Permisos();




        //FUNCION DE SELECCION DEL SPINNER DE PAISES
        Sp_Pais.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                //SE ASIGNA EL VALOR A LA VARIABLE SEGUN LA POSICION
                if (!ListaPaises.get(i).equals(ListaPaises.get(0))){
                    Pais = ListaPaises.get(i);
                    Code = ListaCodePaises.get(i);
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
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        btnGPS.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Localizacion();
            }
        });

        btnGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!TxtNombre.getText().toString().isEmpty() && !tvUbication.getText().toString().isEmpty() &&
                        !TxtDescripcion.getText().toString().isEmpty()) {
                    if (Pais != null && Region != null){
                        if (!tvUbication.getText().toString().equals("Tus Coordenadas")){

                            if (UrlImage != null) {
                                GuardarImagen();
                            } else {
                                Guardar();
                            }

                        }else{
                            Toast.makeText(EditarPostActivity.this, "Se debe colocar unas Coordenadas", Toast.LENGTH_LONG).show();
                        }
                    }else{
                        Toast.makeText(EditarPostActivity.this, "Seleccione un Pais y una Region", Toast.LENGTH_LONG).show();
                    }
                }else{
                    Toast.makeText(EditarPostActivity.this, "Llene todos los campos", Toast.LENGTH_LONG).show();
                }

            }
        });

    }

    public ValueEventListener getUsuario = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot snapshot) {
            if(snapshot.exists()){
                usuario = snapshot.getValue(Usuario.class);
                usuario.setIDUsuario(currentUser.getUid());
            }
        }

        @Override
        public void onCancelled(@NonNull DatabaseError error) {

        }
    };

    public void CargarFoto(View view) {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/");
        startActivityForResult(intent, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK){
            UrlImage = data.getData();

            Glide.with(getApplicationContext()).load(UrlImage).into(Img);
        }
    }

    public void CargarPaises(){

        //ASIGNACION DE SERVICION
        ServicioPaises = ApiDireccion.getServicePais();
        //PROCESO PARA OBTENER LA INFORMACION DE LA API
        ServicioPaises.getDatos().enqueue(new Callback<List<ModPaises>>() {
            @Override
            public void onResponse(Call<List<ModPaises>> call, Response<List<ModPaises>> response) {
                //VERIFICA SI HAY DATOS
                if (response.isSuccessful()) {
                    ListaPaises.add(Pais);
                    ListaCodePaises.add(" ");
                    //CICLO FOREACH PARA LA OBTENCION DE DATOS
                    for (ModPaises ItemPais : response.body()) {
                        //ALMACENA LOS DATOS EN LAS LISTA DE LOS ARRAY
                        ListaPaises.add(ItemPais.name);
                        ListaCodePaises.add(ItemPais.code);
                    }

                    //SE ASIGNAN LOS DATOS AL SPINNER DE PAISES
                    ArrayAdapter<String> adapter = new ArrayAdapter<>(EditarPostActivity.this,
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
                    ListaRegiones.add(Region);
                    //CICLO FOREACH PARA LA OBTENCION DE DATOS
                    for (ModRegiones ItemRegion : response.body()) {
                        //ALMACENA LOS DATOS EN LAS LISTA DEL ARRAY
                        ListaRegiones.add(ItemRegion.region);

                    }
                    //SE ASIGNAN LOS DATOS AL SPINNER DE REGIONES
                    ArrayAdapter<String> adapter = new ArrayAdapter<>(EditarPostActivity.this,
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

    public void Localizacion(){

        LocationManager locationManager = (LocationManager) EditarPostActivity.this.
                getSystemService(Context.LOCATION_SERVICE);

        LocationListener locationListener = new LocationListener() {
            public void onLocationChanged(Location location) {
                tvUbication.setText(""+location.getLatitude()+" "+location.getLongitude());
            }

            public void onStatusChanged(String provider, int status, Bundle extras) {}

            public void onProviderEnabled(String provider) {}

            public void onProviderDisabled(String provider) {}

        };
        int permisionCheck = ContextCompat.checkSelfPermission(EditarPostActivity.this,
                Manifest.permission.ACCESS_FINE_LOCATION);
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0,
                0,locationListener);

    }

    public void Permisos(){

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

    public void GuardarImagen(){
        //SE CREA OBJETO, SE INDIA DONDE SE GUARDARA
        StorageReference file = Storareference.child("SitiosTuristicos").child(UrlImage.getLastPathSegment());
        //SE AGREGA LA URL DE LA IMAGEN
        UploadTask subir = file.putFile(UrlImage);

        Task<Uri> uriTask = subir.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
            @Override
            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                if (!task.isSuccessful()){
                    throw task.getException();
                }
                return file.getDownloadUrl();
            }
        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                Uri URLDescarga = task.getResult();
                Foto = URLDescarga.toString();
                Guardar();
            }
        });
    }

    public void Guardar(){

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference reference =database.getReference(MainActivity.TBL_SitioTuristico);

        SitioTuristico Sitio = new SitioTuristico();
        Sitio.setIDBlog(ID);
        Sitio.setNombre(TxtNombre.getText().toString());
        Sitio.setPais(Pais);
        Sitio.setCode(Code);
        Sitio.setRegion(Region);
        Sitio.setCoordenadas(tvUbication.getText().toString());
        Sitio.setDescripcion(TxtDescripcion.getText().toString());
        Sitio.setFoto(Foto);
        Sitio.setUsuario(usuario);

        reference.child(String.valueOf(ID)).setValue(Sitio);
        Toast.makeText(this, "Se Agrego Correctamente", Toast.LENGTH_LONG).show();

        startActivity(new Intent(EditarPostActivity.this, MisSitiosActivity.class));

    }

}