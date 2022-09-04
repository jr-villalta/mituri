package com.example.mituri;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class RecoveryActivity extends AppCompatActivity {
    private TextInputEditText txtCorreoRecover;
    private Button btnRestablecer;
    private TextView btnRegresarInicio;
    private ProgressBar progressBar;

    FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recovery);
        txtCorreoRecover = findViewById(R.id.txtCorreoRecover);
        btnRestablecer = findViewById(R.id.btnRestablecer);
        btnRegresarInicio = findViewById(R.id.btnRegresarInicio);
        progressBar = findViewById(R.id.progressBar);

        mAuth = FirebaseAuth.getInstance();

        btnRestablecer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                restablecerContra();
            }
        });

        btnRegresarInicio.setOnClickListener(view -> {
            finish();
        });
    }

    private void restablecerContra(){
        String email = txtCorreoRecover.getText().toString();

        if (TextUtils.isEmpty(email)){
            txtCorreoRecover.setError("El correo no puede estar vacío\n");
            txtCorreoRecover.requestFocus();
            return;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            txtCorreoRecover.setError("Por favor proporcione un correo electrónico válido\n");
            txtCorreoRecover.requestFocus();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);
        mAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    Toast.makeText(RecoveryActivity.this, "Verifica tu correo", Toast.LENGTH_SHORT).show();
                    finish();
                }else{
                    Toast.makeText(RecoveryActivity.this, "Vuelve a intentarlo, algo salió mal", Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.INVISIBLE);
                }
            }
        });
    }
}