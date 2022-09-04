package com.example.mituri;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class Home extends AppCompatActivity {

    TextView userName;
    ImageView userImage;
    public Dialog popUp;
    DatabaseReference databaseReference;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        //Slider IMG
        ImageSlider imageSlider = findViewById(R.id.slider);

        List<SlideModel> slideModels = new ArrayList<>();
        slideModels.add(new SlideModel("https://www.adslzone.net/app/uploads-adslzone.net/2019/04/borrar-fondo-imagen.jpg", ScaleTypes.FIT));//Aqui se agregan los datos a la lista
        imageSlider.setImageList(slideModels, ScaleTypes.FIT);

        //Sesion Firebase
        databaseReference = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();

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
}