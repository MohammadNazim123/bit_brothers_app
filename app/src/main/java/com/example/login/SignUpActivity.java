package com.example.login;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.EnumMap;
import java.util.regex.Pattern;

public class SignUpActivity extends AppCompatActivity {
    private EditText email,password,repassword,username;
    private Button signup;
    FirebaseAuth Auth;
    FirebaseUser mUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        email=findViewById(R.id.Email);
        username = findViewById(R.id.UserName);
        password =findViewById(R.id.Password);
        repassword=findViewById(R.id.RePassword);
        signup = findViewById(R.id.SignUp);
        Auth = FirebaseAuth.getInstance();

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String Email = email.getText().toString();
                String Password =password.getText().toString();
                String RePasword= repassword.getText().toString();
                String UserName = username.getText().toString();

                if (TextUtils.isEmpty(UserName)) {
                    username.setError("Username is Empty");
                }
                if (TextUtils.isEmpty(Email))
                {
                    email.setError("Email is Empty");
                }
                if (TextUtils.isEmpty(Password)) {
                    password.setError("Password is Empty");

                }
                if (TextUtils.isEmpty(RePasword)) {
                    repassword.setError("Re-Password is Empty");
                }
                if (Password.length() < 6) {
                    Toast.makeText(getApplicationContext(), "Password should not be less than 6", Toast.LENGTH_SHORT).show();

                }

                if (!Patterns.EMAIL_ADDRESS.matcher(Email).matches())
                {
                    email.setError("Invalid Email");
                }
                if (!Password.equals(RePasword)) {
                    Toast.makeText(getApplicationContext(),"Password does not match",Toast.LENGTH_SHORT).show();
                }
                else {

                    Auth.createUserWithEmailAndPassword(Email, Password)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        User user  = new User(Email,Password,UserName);
                                        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("User");
                                        ref.child(UserName).setValue(user);
                                        startActivity(new Intent(getApplicationContext(), HomeActivity.class));
                                        Toast.makeText(getApplicationContext(), "Sign Up Successfull", Toast.LENGTH_SHORT).show();

                                    } else {
                                        Toast.makeText(getApplicationContext(), "Sign Up Failed", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });

                }
            }
        });
    }
}


class User {
    String email;
    String password;
    String username;

    public class user{
    }

    public User(String email, String password,String username) {
        this.email = email;
        this.password = password;
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
