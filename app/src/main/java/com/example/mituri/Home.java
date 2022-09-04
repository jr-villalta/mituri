package com.example.mituri;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;
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
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class Home extends AppCompatActivity {

    TextView userName;
    ImageView userImage;
    public Dialog popUp;
    DatabaseReference databaseReference;
    private FirebaseAuth mAuth;

    private ListView Lv_Sitio;
    private ArrayList<SitioTuristico> ListSitio = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        Lv_Sitio = (ListView) findViewById(R.id.ListSitios);


        //Slider IMG
        ImageSlider imageSlider = findViewById(R.id.slider);

        List<SlideModel> slideModels = new ArrayList<>();
        slideModels.add(new SlideModel("https://www.adslzone.net/app/uploads-adslzone.net/2019/04/borrar-fondo-imagen.jpg", ScaleTypes.FIT));//Aqui se agregan los datos a la lista
        imageSlider.setImageList(slideModels, ScaleTypes.FIT);

        //Sesion Firebase
        databaseReference = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();

        databaseReference.child(MainActivity.TBL_SitioTuristico).addValueEventListener(CargarSitios);

        userName = (TextView) findViewById(R.id.textViewUsuario);
        userImage = (ImageView) findViewById(R.id.profile_image);

        updateUI(user);

        popUp = new Dialog(this);

        //Pop-up
        userImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth = FirebaseAuth.getInstance();
                FirebaseUser user = mAuth.getCurrentUser();

                popUp.setContentView(R.layout.pop_up);
                ImageView img = (ImageView)popUp.findViewById(R.id.popupimg);
                TextView userEmail = (TextView)popUp.findViewById(R.id.user_email);
                Button btnCerrar = (Button) popUp.findViewById(R.id.logoutbtn);

                if(user != null){
                    userEmail.setText(user.getEmail());
                    if(user.getPhotoUrl() != null){
                        Picasso.get().load(user.getPhotoUrl()).into(img);
                    }else{
                        Picasso.get().load("https://uybor.uz/borless/uybor/img/user-images/user_no_photo_300x300.png").into(img);
                    }

                }else{
                    userEmail.setText("Inicia sesion");
                    Picasso.get().load("https://png.pngitem.com/pimgs/s/130-1300400_user-hd-png-download.png").into(img);
                    btnCerrar.setText("Iniciar Sesion");
                }

                popUp.show();
                btnCerrar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mAuth = FirebaseAuth.getInstance();
                        FirebaseUser user = mAuth.getCurrentUser();

                        if(user != null){
                            logout();
                            updateUI(user);
                            startActivity(new Intent(Home.this,Home.class));
                        }else{
                            startActivity(new Intent(Home.this,LoginActivity.class));
                        }
                        popUp.dismiss();
                    }
                });
            }
        });

        //accion cuando selecciona un sitio de la lista
        Lv_Sitio.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                //Obtiene los datos del seleccionado
                Intent intent = new Intent(getApplicationContext(), Home.class);
                intent.putExtra("IDBlog", ListSitio.get(i).getIDBlog());
                intent.putExtra("Nombre", ListSitio.get(i).getNombre());
                intent.putExtra("Pais", ListSitio.get(i).getPais());
                intent.putExtra("Region", ListSitio.get(i).getRegion());
                intent.putExtra("Coordenadas", ListSitio.get(i).getCoordenadas());
                intent.putExtra("Descripcion", ListSitio.get(i).getDescripcion());
                intent.putExtra("Foto", ListSitio.get(i).getFoto());

                //startActivity(intent);
                Toast.makeText(Home.this, "Datos almacenados"+ListSitio.get(i).getNombre(), Toast.LENGTH_LONG).show();
            }
        });

    }

    private void updateUI(FirebaseUser currentUser) {
        if(currentUser != null){
            userName.setText(currentUser.getEmail());
            if(currentUser.getPhotoUrl() != null){
                Picasso.get().load(currentUser.getPhotoUrl()).into(userImage);
            }else{
                Picasso.get().load("https://uybor.uz/borless/uybor/img/user-images/user_no_photo_300x300.png").into(userImage);
            }
        }else{
            userName.setText("Invitado");
            Picasso.get().load("https://png.pngitem.com/pimgs/s/130-1300400_user-hd-png-download.png").into(userImage);

        }
    }

    public void logout(){
        FirebaseAuth.getInstance().signOut();
    }

    public ValueEventListener CargarSitios = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot snapshot) {
            if (snapshot.exists()){
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

    public void Agregar(View view){
        startActivity(new Intent(Home.this, AddPost.class));
    }
}