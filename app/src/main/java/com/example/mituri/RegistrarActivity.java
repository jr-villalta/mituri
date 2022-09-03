package com.example.mituri;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class RegistrarActivity extends AppCompatActivity {

    TextInputEditText txtCorreo;
    TextInputEditText txtContrasea;
    TextView btnIngresarAqui;
    TextView btnRegistrarse;

    TextInputEditText txtNombre;
    TextInputEditText txtApellido;

    FirebaseAuth mAuth;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrar);

        txtCorreo = findViewById(R.id.txtCorreo);
        txtContrasea = findViewById(R.id.txtContrasea);
        btnIngresarAqui = findViewById(R.id.btnIngresarAqui);
        btnRegistrarse = findViewById(R.id.btnRegistrarse);

        txtNombre = findViewById(R.id.txtNombre);
        txtApellido = findViewById(R.id.txtApellido);

        mAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();

        btnRegistrarse.setOnClickListener(view ->{
            createUser();
        });

        btnIngresarAqui.setOnClickListener(view ->{
            finish();
        });
    }

    private void createUser(){
        String name = txtNombre.getText().toString();
        String lastname = txtApellido.getText().toString();
        String email = txtCorreo.getText().toString();
        String password = txtContrasea.getText().toString();

        if(TextUtils.isEmpty(name)){
            txtNombre.setError("El Nombre no puede estar vacío\n");
            txtNombre.requestFocus();
        }else if (TextUtils.isEmpty(lastname)) {
            txtApellido.setError("El Apellido no puede estar vacío\n");
            txtApellido.requestFocus();
        }else if (TextUtils.isEmpty(email)){
            txtCorreo.setError("El correo no puede estar vacío\n");
            txtCorreo.requestFocus();
        }else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            txtCorreo.setError("Por favor proporcione un correo electrónico válido\n");
            txtCorreo.requestFocus();
        }else if (TextUtils.isEmpty(password)){
            txtContrasea.setError("La contraseña no puede estar vacía\n");
            txtContrasea.requestFocus();
        }else{
            mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()){
                        Map<String, Object> map = new HashMap<>();
                        map.put("Nombre",name);
                        map.put("Apellido",lastname);
                        map.put("Correo",email);
                        map.put("Tipo_Usuario",0);

                        String idUsuario = mAuth.getCurrentUser().getUid();

                        databaseReference.child("Usuarios").child(idUsuario).setValue(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task2) {
                                if (task2.isSuccessful()){
                                    Toast.makeText(RegistrarActivity.this, "Usuario registrado con éxito\n", Toast.LENGTH_SHORT).show();
                                    mAuth.signOut();
                                    finish();
                                }else {
                                    Toast.makeText(RegistrarActivity.this, "No se pudieron crear los datos correctamente\n" + task2.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        });

                    }else{
                        txtContrasea.setText("");
                        Toast.makeText(RegistrarActivity.this, "Error de registro\n: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }
}