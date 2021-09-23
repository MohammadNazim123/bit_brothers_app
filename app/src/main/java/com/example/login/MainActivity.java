package com.example.login;


import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity {

    private EditText email,password,username;
    private Button login;
    private TextView gotosignup;
    FirebaseAuth Auth;
    FirebaseUser mUser;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        username = findViewById(R.id.UserName);
        email=findViewById(R.id.Email);
        password =findViewById(R.id.Password);
        gotosignup = findViewById(R.id.GotoSignUp);
        login = findViewById(R.id.Login);
        Auth = FirebaseAuth.getInstance();



        String text="Create an Account ? SIGN UP";
        SpannableString spannableString = new SpannableString(text);
        ClickableSpan clickableSpan=new ClickableSpan() {
            @Override
            public void onClick(@NonNull View widget) {
                Intent intent = new Intent(getApplicationContext(), SignUpActivity.class);
                startActivity(intent);
                finish();
            }
        };
        spannableString.setSpan(clickableSpan,20,27, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        gotosignup.setText(spannableString);
        gotosignup.setClickable(true);
        gotosignup.setMovementMethod(LinkMovementMethod.getInstance());
        sharedPreferences = getSharedPreferences("username", Context.MODE_PRIVATE);
        String Username = sharedPreferences.getString("user", "");
        if (!Username.equals("")) {
            startActivity(new Intent(getApplicationContext(),HomeActivity.class));
        }


        mUser = FirebaseAuth.getInstance().getCurrentUser();

        if (mUser != null) {
            startActivity(new Intent(getApplicationContext(),HomeActivity.class));
        }



        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String Email = email.getText().toString();
                String Password =password.getText().toString();
                String Username = username.getText().toString();

                if (TextUtils.isEmpty(Username)) {
                    username.setError("Username is empty");
                }

                if (TextUtils.isEmpty(Email))
                {
                    email.setError("Email is Empty");
                }
                if (TextUtils.isEmpty(Password)) {
                    password.setError("Password is Empty");

                }
                if (!Patterns.EMAIL_ADDRESS.matcher(Email).matches())
                {
                    email.setError("Invalid Email");
                }
                if (Password.length() < 6) {
                    Toast.makeText(getApplicationContext(), "Password should not be less than 6", Toast.LENGTH_SHORT).show();
                } else {
                    DatabaseReference ref = FirebaseDatabase.getInstance().getReference("User");
                    ref.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            String email = snapshot.child(Username).child("email").getValue().toString();
                            String password1 = snapshot.child(Username).child("password").getValue().toString();
                            if (email.equals(Email) && password1.equals(Password)) {
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.putString("user", "UserName");
                                editor.apply();
                                startActivity(new Intent(getApplicationContext(), HomeActivity.class));
                            } else {
                                Toast.makeText(getApplicationContext(),"Login Failed",Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });


                }


            }
        });


    }
}