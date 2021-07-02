package com.example.jewelrystore;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.jewelrystore.Models.Users;
import com.example.jewelrystore.Prevalent.Prevalent;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rey.material.widget.CheckBox;

import io.paperdb.Paper;

public class SignInActivity extends AppCompatActivity {
    private EditText edit_email,edit_pass;
    private Button btn_login,btn_admin,btn_users;
    private ProgressDialog progressDialog;
    private String parentDbName="Users";
    private CheckBox checkBox_rememberMe;
    TextView textView1,textView2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FirebaseApp.initializeApp(this);
        setContentView(R.layout.activity_sign_in);
        edit_email=findViewById(R.id.edit_email);
        edit_pass=findViewById(R.id.edit_pass);
        btn_login=findViewById(R.id.btn_login);
        btn_admin=findViewById(R.id.btn_admin);
        btn_users=findViewById(R.id.btn_users);
        textView1=findViewById(R.id.txt_extra);
        textView2=findViewById(R.id.txt_extra2);
        progressDialog=new ProgressDialog(this);
        checkBox_rememberMe=(CheckBox)findViewById(R.id.remember_me);
        Paper.init(this);
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginUser();
            }
        });
        btn_admin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    btn_admin.setVisibility(View.INVISIBLE);
                    textView1.setVisibility(View.INVISIBLE);
                    checkBox_rememberMe.setVisibility(View.INVISIBLE);
                    btn_users.setVisibility(View.VISIBLE);
                    textView2.setVisibility(View.INVISIBLE);
                    btn_login.setText("Login Admin");
                    parentDbName="Admins";

            }
        });
        btn_users.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btn_admin.setVisibility(View.VISIBLE);
                textView1.setVisibility(View.VISIBLE);
                checkBox_rememberMe.setVisibility(View.VISIBLE);
                textView2.setVisibility(View.VISIBLE);
                btn_users.setVisibility(View.INVISIBLE);
                btn_login.setText("Log In");
                parentDbName="Users";
            }
        });

        String UserPhoneKey=Paper.book().read(Prevalent.UserPhoneKey);
        String UserPasswordKey=Paper.book().read(Prevalent.UserPasswordKey);
        if (UserPhoneKey != null && UserPasswordKey != null)
        {
            progressDialog.setTitle("Pleae wait..");
            progressDialog.setMessage("Please wait while we are checking the account");
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();
            if (!TextUtils.isEmpty(UserPhoneKey) && !TextUtils.isEmpty(UserPasswordKey))
            {
                allowAccessToAccount(UserPhoneKey,UserPasswordKey);
            }
        }

    }

    private void LoginUser() {
        String phone=edit_email.getText().toString();
        String pass=edit_pass.getText().toString();

        if (TextUtils.isEmpty(phone))
        {
            Toast.makeText(this, "Please Enter Your Phone Number", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(pass))
        {
            Toast.makeText(this, "Please Enter your password", Toast.LENGTH_SHORT).show();
        }
        else
        {
            progressDialog.setTitle("Login Account");
            progressDialog.setMessage("Please wait while we are checking the credential.");
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();

            allowAccessToAccount(phone,pass);
        }
    }

    private void allowAccessToAccount(final String phone, final String pass) {
        if (checkBox_rememberMe.isChecked())
        {
            Paper.book().write(Prevalent.UserPhoneKey,phone);
            Paper.book().write(Prevalent.UserPasswordKey,pass);
        }


        final DatabaseReference Rootref;
        Rootref= FirebaseDatabase.getInstance().getReference();
        Rootref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.child(parentDbName).child(phone).exists())
                {
                    Users users=dataSnapshot.child(parentDbName).child(phone).getValue(Users.class) ;
                    if (users.getPhone().equals(phone)){

                        if (users.getPassword().equals(pass))
                        {
                            if (parentDbName.equals("Admins"))
                            {
                                Toast.makeText(SignInActivity.this, "Welcome Admin you are Logged in Successfully..", Toast.LENGTH_SHORT).show();
                                progressDialog.dismiss();
                                Intent intent=new Intent(SignInActivity.this,AdminMainActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                Prevalent.currentonlineUsers=users;
                                startActivity(intent);
                            }
                            else if (parentDbName.equals("Users"))
                            {
                                Toast.makeText(SignInActivity.this, "Logged in Successfully..", Toast.LENGTH_SHORT).show();
                                progressDialog.dismiss();
                                Intent intent=new Intent(SignInActivity.this,HomeActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                Prevalent.currentonlineUsers=users;
                                startActivity(intent);
                                finish();
                            }


                        }
                        else {
                            progressDialog.dismiss();
                            Toast.makeText(SignInActivity.this, "Password is Incorrect", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
                else
                {
                    Toast.makeText(SignInActivity.this, "Account with this "+phone+"Doesn't Exist", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                    Toast.makeText(SignInActivity.this, "You need to create account first ", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void moveToOther(View view) {
        Intent intent=new Intent(SignInActivity.this,SignUpActivity.class);
        startActivity(intent);
    }
}
