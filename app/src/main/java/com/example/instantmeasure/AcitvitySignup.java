package com.example.instantmeasure;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;


public class AcitvitySignup extends AppCompatActivity {

/////////////////////////////////////////////////////////////////
    //Set display name for each user
    ///////////////////////////////////////
        public EditText emailId, passwd;
        Button btnSignUp,signIn;



        FirebaseAuth firebaseAuth;
        TextView err;
        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_signup);
            firebaseAuth = FirebaseAuth.getInstance();
            emailId = findViewById(R.id.loginemail);
            passwd = findViewById(R.id.loginpswd);
            btnSignUp = findViewById(R.id.btnlogin);
            signIn = findViewById(R.id.btnsignup);
            err = findViewById(R.id.tv_errli);

            btnSignUp.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    System.out.println("========================btn");

                    String emailID = emailId.getText().toString();
                    String paswd = passwd.getText().toString();
                    if (emailID.isEmpty()) {
                        System.out.println("inside if");

                        emailId.setError("Provide your Email first!");
                        emailId.requestFocus();
                    } else if (paswd.isEmpty()) {
                        System.out.println("inside 1st elseif");

                        passwd.setError("Set your password");
                        passwd.requestFocus();
                    } else if (emailID.isEmpty() && paswd.isEmpty()) {
                        System.out.println("inside 2 elseif");

                        emailId.setError("Provide your Email first!");
                        emailId.requestFocus();

                        passwd.setError("Set your password");
                        passwd.requestFocus();

                        Toast.makeText(AcitvitySignup.this, "Fields Empty!", Toast.LENGTH_SHORT).show();
                    } else if (!(emailID.isEmpty() && paswd.isEmpty())) {
                        System.out.println("inside 3 elseif");
                        firebaseAuth.createUserWithEmailAndPassword(emailID, paswd).addOnCompleteListener(AcitvitySignup.this, new OnCompleteListener() {
                            @Override
                            public void onComplete(@NonNull Task task) {
                                System.out.println("out of if");

                                if (!task.isSuccessful()) {

                                    err.setText("Sign Up Not Sucessfull :"+task.getException().getMessage());
                                    System.out.println("inside  succ if");




                                } else {


                                    startActivity(new Intent(AcitvitySignup.this, Home.class));
                                    System.out.println("in else");




                                }
                            }
                        });
                    } else {
                        Toast.makeText(AcitvitySignup.this, "Error", Toast.LENGTH_SHORT).show();
                    }
                }
            });
            signIn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Intent I = new Intent(AcitvitySignup.this, ActivityLogin.class);
                    startActivity(I);
                }
            });
        }



    }



