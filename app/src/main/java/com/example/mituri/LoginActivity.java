package com.example.mituri;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {
    private TextInputEditText txtCorreoLogin;
    private TextInputEditText txtContraseaLogin;
    private Button btnLogin;
    private TextView btnRegistrateAqui;
    private TextView btnRecuperar;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        txtCorreoLogin = findViewById(R.id.txtCorreoLogin);
        txtContraseaLogin = findViewById(R.id.txtContraseaLogin);
        btnRegistrateAqui = findViewById(R.id.btnRegistrateAqui);
        btnRecuperar = findViewById(R.id.btnRecuperar);
        btnLogin = findViewById(R.id.btnLogin);

        mAuth = FirebaseAuth.getInstance();

        btnLogin.setOnClickListener(view -> {
            loginUser();
        });

        btnRegistrateAqui.setOnClickListener(view ->{
            startActivity(new Intent(LoginActivity.this, RegistrarActivity.class));
        });

        btnRecuperar.setOnClickListener(view -> {
            startActivity(new Intent(LoginActivity.this, RegistrarActivity.class));
        });
        btnRecuperar.setOnClickListener(view -> {
            startActivity(new Intent(LoginActivity.this, RecoveryActivity.class));
        });
    }

        private void loginUser(){
        String email = txtCorreoLogin.getText().toString();
        String password = txtContraseaLogin.getText().toString();

        if (TextUtils.isEmpty(email)){
            txtCorreoLogin.setError("El correo no puede estar vacío\n");
            txtCorreoLogin.requestFocus();
        }else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            txtCorreoLogin.setError("Por favor proporcione un correo electrónico válido\n");
            txtCorreoLogin.requestFocus();
        }else if (TextUtils.isEmpty(password)){
            txtContraseaLogin.setError("La contraseña no puede estar vacía\n");
            txtContraseaLogin.requestFocus();
        }else{
            mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()){
                        Toast.makeText(LoginActivity.this, "El usuario inició sesión con éxito\n", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(LoginActivity.this, Home.class));
                    }else{
                        Toast.makeText(LoginActivity.this, "Error de inicio de sesión\n: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }
}