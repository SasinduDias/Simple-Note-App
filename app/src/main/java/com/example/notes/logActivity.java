package com.example.notes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class logActivity extends AppCompatActivity {

    EditText et_email,et_password;
    RelativeLayout btn_login;
    TextView tv_sign_up,reset;
    String passwordPattern="[a-zA-Z0-9\\\\!\\\\@\\\\#\\\\$]{8,24}";
    String emailPattern="^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

    FirebaseAuth mAuth;
    String Uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log);

        et_email=findViewById(R.id.edt_email);
        et_password=findViewById(R.id.edt_password);
        btn_login=findViewById(R.id.btn_login);
        tv_sign_up=findViewById(R.id.tv_signup);
        reset=findViewById(R.id.reset);

        mAuth = FirebaseAuth.getInstance();

        if(mAuth.getCurrentUser()!=null){
            startActivity(new Intent(logActivity.this,MainActivity.class));
            finish();
        }

        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(logActivity.this,ResetPasswordActivity.class));

            }
        });

        String sign_up_text="<font>Don't have an account?</font> <font color=#E26912><b> SIGNUP</b></font>";
        tv_sign_up.setText(Html.fromHtml(sign_up_text));

        tv_sign_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(logActivity.this, SignupActivity.class));
                finish();

            }
        });


        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email,password;
                email=et_email.getText().toString().trim();
                password=et_password.getText().toString().trim();
//                Toast.makeText(log1Activity.this, "E mail is " + email + "" + "Password is" + password , Toast.LENGTH_SHORT).show();

                validateFielde(email,password);

            }
        });

    }

    private void validateFielde(String email, String password) {

        if(!(email.isEmpty()) && !(password.isEmpty())){
            if(email.matches(emailPattern)){
                if(password.matches(passwordPattern)){
                    LogUser(email,password);

                }else{
                    et_password.setError("Password should have minimum 8 letters");
                }
            }else{
                et_email.setError("Email is incorrect");
            }

        }else{
            Toast.makeText(logActivity.this, "Please fill the fields " , Toast.LENGTH_SHORT).show();

        }
    }

    private void LogUser(String email, String password) {
        //firebase,API
        mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    Toast.makeText(logActivity.this, "Login Successful", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(logActivity.this,MainActivity.class));
                    finish();
                }else{
                    Toast.makeText(logActivity.this, "Error occurred", Toast.LENGTH_SHORT).show();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(logActivity.this, "Connection Error", Toast.LENGTH_SHORT).show();
                System.out.println("error is =================================" + e);
            }
        });

    }


    }
