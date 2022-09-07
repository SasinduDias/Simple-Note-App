package com.example.notes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

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
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class SignupActivity extends AppCompatActivity {

    TextView tv_login,tv_login_screen;
    RelativeLayout bt_signup;
    Snackbar snackbar;
    ConstraintLayout constraintLayout;
    EditText et_username,et_email,et_contact_no,et_password;

    private FirebaseAuth mAuth;
    String UserId;
    FirebaseFirestore db;

    String passwordPattern="[a-zA-Z0-9\\\\!\\\\@\\\\#\\\\$]{8,24}";
    String emailPattern="^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
    String phonePattern="^[+]?[0-9]{10,13}$";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);


//        tv_login=findViewById(R.id.tv_login_screen);
        bt_signup=findViewById(R.id.btn_sign_up);
        et_email=findViewById(R.id.edt_email);
        et_username=findViewById(R.id.edt_user);
        et_contact_no=findViewById(R.id.edt_contact_number);
        et_password=findViewById(R.id.et_password);
        tv_login_screen=findViewById(R.id.tv_login_screen);

        mAuth = FirebaseAuth.getInstance();

//        acces to database
        db = FirebaseFirestore.getInstance();

        String sign_up_text="<font>Already have an account?</font> <font color=#E26912><b>SIGN IN</b></font>";
        tv_login_screen.setText(Html.fromHtml(sign_up_text));

        //        go to the login page
        tv_login_screen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SignupActivity.this, logActivity.class));
                finish();

            }
        });

        bt_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateFields();
            }
        });

    }


    private void validateFields() {
        //do validation here
        String user_name,email,phone,password;
        user_name=et_username.getText().toString();
        email=et_email.getText().toString();
        phone=et_contact_no.getText().toString();
        password=et_password.getText().toString();

        if(!(email.isEmpty()) && !(password.isEmpty()) && !(phone.isEmpty())){
            if(email.matches(emailPattern)){
                if(password.matches(passwordPattern)){
                    if(phone.matches(phonePattern)) {
                        Toast.makeText(SignupActivity.this, "Successful", Toast.LENGTH_SHORT).show();
//                    LogUser(email,password);
                        saveUser(user_name, email, phone, password);
                    }else{
                        et_contact_no.setError("enter phone no correctly");
                    }

                }else{
                    et_password.setError("Password should have minimum 8 letters");
                }
            }else{
                et_email.setError("Email is incorrect");
            }

        }else{
            Toast.makeText(SignupActivity.this, "Please fill the fields " , Toast.LENGTH_SHORT).show();

        }


    }


    private void saveUser(String user_name, String email, String phone, String password) {
//        snackbar = Snackbar.make(constraintLayout,"Post Uploaded please wait",Snackbar.LENGTH_LONG).setAction("Dismiss", new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                snackbar.dismiss();
//            }
//        });
//        snackbar.setActionTextColor(Color.CYAN);
//        snackbar.show();


        mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                //add data to the firebase store
                UserId=mAuth.getCurrentUser().getUid();

                DocumentReference documentReference=db.collection("USER").document(UserId);
                Map<String, Object> user = new HashMap<>();
                user.put("USER NAME", user_name);
                user.put("EMAIL", email);
                user.put("PHONE", phone);

                documentReference.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(SignupActivity.this, "Login Success Data Saved" , Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(SignupActivity.this,MainActivity.class));
                        finish();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(SignupActivity.this, "Login Success Data not Saved" , Toast.LENGTH_SHORT).show();

                    }
                });

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(SignupActivity.this, "Login not Success" , Toast.LENGTH_SHORT).show();

            }
        });

    }


}