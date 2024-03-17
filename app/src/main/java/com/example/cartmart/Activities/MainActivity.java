package com.example.cartmart.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cartmart.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {
    Button btn_login;
    TextView txt_signUp, txt_title;
    EditText et_email, et_pass;
    String st_email, st_pass;
    CheckBox cb_keep_signIn;
    FirebaseAuth firebaseAuth;
    ProgressBar progressBar;
    @Override
    public void onStart(){
        super.onStart();
        progressBar.setVisibility(View.VISIBLE);
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        if (firebaseUser != null){
            progressBar.setVisibility(View.GONE);
            Intent intent = new Intent(MainActivity.this, Nevigation_Bar.class);
            startActivity(intent);
            finish();
        } else {
            progressBar.setVisibility(View.GONE);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        firebaseAuth = FirebaseAuth.getInstance();
        linkViewsLogin();
        setTextColor();
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressBar.setVisibility(view.VISIBLE);
                getValuesFromET();
            }
        });
        txt_signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, SignUp.class);
                startActivity(intent);
            }
        });


    }
    //following function is get values from edit texts in string and checking if the data is complete and then moving to home page
    void getValuesFromET(){
        st_email = et_email.getText().toString().trim();
        st_pass = et_pass.getText().toString().trim();

        if (st_email.isEmpty() || st_pass.isEmpty()){
            Toast.makeText(this, "Please Enter Complete Info", Toast.LENGTH_SHORT).show();
            progressBar.setVisibility(View.GONE);
        }
        else {
            matchDataFromFirebase();

        }

    }

    private void matchDataFromFirebase() {
        firebaseAuth.signInWithEmailAndPassword(st_email, st_pass)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        progressBar.setVisibility(View.GONE);
                        if (task.isSuccessful()){
                            progressBar.setVisibility(View.GONE);
//                            if (!cb_keep_signIn.isChecked()){
//                                FirebaseAuth.getInstance().signOut();
//                            }
                            Intent intent = new Intent(MainActivity.this, Nevigation_Bar.class);
                            startActivity(intent);
                            finish();
                        }
                        else {
                            Toast.makeText(MainActivity.this, task.getException().getMessage().toString(), Toast.LENGTH_SHORT).show();
                            progressBar.setVisibility(View.GONE);
                        }
                    }
                });
    }

    //following function setting the color of title and signup text views
    void setTextColor(){
        String signUp = "<font color=#FFFFFFFF>Don't have account | </font><font color=#FF9900>Sign Up</font>";
        txt_signUp.setText(Html.fromHtml(signUp));
        Html.escapeHtml(signUp);
        String title = "<font color=#FF000000>C</font><font color=#FF000000>art</font> <font color=#FF000000>M</font><font color=#FF000000>art</font>";
        txt_title.setText(Html.fromHtml(title));
        Html.escapeHtml(title);
    }
    //following function is linking the views
    void linkViewsLogin(){
        btn_login = findViewById(R.id.btn_login);
        txt_signUp = findViewById(R.id.txt_signUp);
        txt_title = findViewById(R.id.txt_title);
        et_email = findViewById(R.id.et_email);
        et_pass = findViewById(R.id.et_pass);
        cb_keep_signIn = findViewById(R.id.cb_keep_signIn);
        progressBar = findViewById(R.id.progressBar);
    }

}