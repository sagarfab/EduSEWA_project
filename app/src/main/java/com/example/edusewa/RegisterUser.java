package com.example.edusewa;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;


public class RegisterUser extends AppCompatActivity implements View.OnClickListener{
    private TextView heading, registerUser;
    private EditText editTextFullName, editTextStandard, editTextEmail, editTextPassword;
    private ProgressBar progressBar;

    private FirebaseAuth mAuth;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_user);
        mAuth = FirebaseAuth.getInstance();

        heading = (TextView) findViewById(R.id.heading);
        heading.setOnClickListener(this);
        registerUser = (Button)findViewById(R.id.registerUser);
        registerUser.setOnClickListener(this);
        editTextFullName = (EditText) findViewById(R.id.name);
        editTextStandard = (EditText) findViewById(R.id.Standard);
        editTextEmail = (EditText)findViewById(R.id.email);
        editTextPassword = (EditText)findViewById(R.id.password);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);



    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case    R.id.heading:
                startActivity(new Intent(this,MainActivity.class));
                break;
            case R.id.registerUser:
                registerUser();
                break;


        }

    }

    private void registerUser() {
        String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();
        String fullName = editTextFullName.getText().toString().trim();
        String standard = editTextStandard.getText().toString().trim();

        if(fullName.isEmpty()){
            editTextFullName.setError("Full name is requires");
            editTextFullName.requestFocus();
            return;

        }
        if(standard.isEmpty()){
            editTextStandard.setError("Class is requires");
            editTextStandard.requestFocus();
            return;

        }
        if(email.isEmpty()){
            editTextEmail.setError("Email is requires");
            editTextEmail.requestFocus();
            return;

        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            editTextEmail.setError("Enter valid Email");
            editTextEmail.requestFocus();
            return;


        }
        if(password.isEmpty()){
            editTextPassword.setError("Password is requires");
            editTextPassword.requestFocus();
            return;

        }
        progressBar.setVisibility(View.VISIBLE);
        mAuth.createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            User user = new User(fullName, standard, email);
                            FirebaseDatabase.getInstance().getReference("Users")
                                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                    .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()){
                                        Toast.makeText(RegisterUser.this, "Registeration Successfull", Toast.LENGTH_SHORT).show();

                                        progressBar.setVisibility(View.GONE);

                                    }else{
                                        Toast.makeText(RegisterUser.this, "Failed to Register, Try again", Toast.LENGTH_SHORT).show();
                                        progressBar.setVisibility(View.GONE);
                                    }

                                }
                            });

                        }else {
                            Toast.makeText(RegisterUser.this, "Failed to Register", Toast.LENGTH_SHORT).show();
                        }
                    }
                });



    }
}