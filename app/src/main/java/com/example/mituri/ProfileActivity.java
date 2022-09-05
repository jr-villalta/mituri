package com.example.mituri;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mituri.Clases.Usuario;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class ProfileActivity extends AppCompatActivity {

    private TextInputEditText txtNombrePerfil;
    private TextInputEditText txtApellidoPerfil;
    private TextView btnModificarPerfil;

    private Usuario usuario = new Usuario();
    private FirebaseAuth mAuth;
    private DatabaseReference databaseReference;
    private FirebaseUser user;

    private TextView btnRegresarHome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        txtNombrePerfil = findViewById(R.id.txtNombrePerfil);
        txtApellidoPerfil = findViewById(R.id.txtApellidoPerfil);
        btnModificarPerfil = findViewById(R.id.btnModificarPerfil);
        btnRegresarHome = findViewById(R.id.btnRegresarHome);

        mAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();
        user = mAuth.getCurrentUser();
        databaseReference.child("Usuarios").child(mAuth.getUid()).addValueEventListener(getUsuario);

        txtNombrePerfil.setText(usuario.getNombre());
        txtApellidoPerfil.setText(usuario.getApellido());

        btnModificarPerfil.setOnClickListener(view ->{
            modificarUsuario();
        });

        btnRegresarHome.setOnClickListener(view -> {
            finish();
        });
    }

    private void modificarUsuario(){

        String name = txtNombrePerfil.getText().toString();
        String lastname = txtApellidoPerfil.getText().toString();

        if(TextUtils.isEmpty(name)){
            txtNombrePerfil.setError("El Nombre no puede estar vacío\n");
            txtNombrePerfil.requestFocus();
        }else if (TextUtils.isEmpty(lastname)) {
            txtApellidoPerfil.setError("El Apellido no puede estar vacío\n");
            txtApellidoPerfil.requestFocus();
        }else{
            Map<String, Object> map = new HashMap<>();
            map.put("Nombre",name);
            map.put("Apellido",lastname);

            databaseReference.child("Usuarios").child(user.getUid()).updateChildren(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    Toast.makeText(ProfileActivity.this, "Datos modificados correctamente", Toast.LENGTH_SHORT).show();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(ProfileActivity.this, "Algo salió mal", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    public ValueEventListener getUsuario = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot snapshot) {
            if(snapshot.exists()){
                usuario = snapshot.getValue(Usuario.class);
                usuario.setIDUsuario(user.getUid());
                txtNombrePerfil.setText(usuario.getNombre());
                txtApellidoPerfil.setText(usuario.getApellido());
            }
        }

        @Override
        public void onCancelled(@NonNull DatabaseError error) {

        }
    };
}