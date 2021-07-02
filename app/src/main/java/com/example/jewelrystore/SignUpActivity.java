package com.example.jewelrystore;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.text.InputType;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class SignUpActivity extends AppCompatActivity {
    EditText fname,lname,email_field,phone_field,pass_field;
    Button btn_create;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        fname=findViewById(R.id.fname_field);
        lname=findViewById(R.id.lname_field);
        email_field=findViewById(R.id.email_field);
        phone_field=findViewById(R.id.phone_field);
        pass_field=findViewById(R.id.confirm_pass_field);
        btn_create=findViewById(R.id.btn_create);
        progressDialog=new ProgressDialog(this);


        btn_create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(phone_field.getText().toString())){
                    Toast.makeText(SignUpActivity.this, "Please write your phone number", Toast.LENGTH_SHORT).show();
                }
                else if (TextUtils.isEmpty(fname.getText().toString())&& TextUtils.isEmpty(lname.getText().toString())){
                    Toast.makeText(SignUpActivity.this, "Please write your Name", Toast.LENGTH_SHORT).show();
                }
                else if (TextUtils.isEmpty(email_field.getText().toString())){
                    Toast.makeText(SignUpActivity.this, "Please write your Email", Toast.LENGTH_SHORT).show();
                }
                else if (TextUtils.isEmpty(pass_field.getText().toString())){
                    Toast.makeText(SignUpActivity.this, "Please write your Password", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Intent intent=new Intent(SignUpActivity.this,VerificationActivity.class);
                    intent.putExtra("phone",phone_field.getText().toString());
                    intent.putExtra("fname",fname.getText().toString());
                    intent.putExtra("lname",lname.getText().toString());
                    intent.putExtra("pass",pass_field.getText().toString());
                    intent.putExtra("email",email_field.getText().toString());
                    startActivity(intent);
                }
            }
        });
    }

    private void createAccount() {
        String first_name=fname.getText().toString();
        String last_name=lname.getText().toString();
        String email=email_field.getText().toString();
        String phone=phone_field.getText().toString();
        String pass=pass_field.getText().toString();


           progressDialog.setTitle("Create Account");
           progressDialog.setMessage("Please wait while we are checking the credential.");
           progressDialog.setCanceledOnTouchOutside(false);
           progressDialog.show();

          validatePhoneNumber(first_name,last_name,phone,email,pass);

    }

    private void validatePhoneNumber(final String first_name, final String last_name, final String phone, final String email, final String pass) {

        final DatabaseReference RootRef;

        RootRef= FirebaseDatabase.getInstance().getReference();
        RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (!(dataSnapshot.child("Users").child(phone).exists()))
                {
                    HashMap<String,Object> userDataMap=new HashMap<>();
                    userDataMap.put("Phone",phone);
                    userDataMap.put("Password",pass);
                    userDataMap.put("First_Name",first_name);
                    userDataMap.put("Last_Name",last_name);
                    userDataMap.put("Email",email);

                    RootRef.child("Users").child(phone).updateChildren(userDataMap)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful())
                                    {
                                        Toast.makeText(SignUpActivity.this, "Congratulation, your account has been created", Toast.LENGTH_SHORT).show();
                                        progressDialog.dismiss();
                                        Intent intent=new Intent(SignUpActivity.this,SignInActivity.class);
                                        startActivity(intent);
                                    }
                                    else
                                    {
                                        progressDialog.dismiss();
                                        Toast.makeText(SignUpActivity.this, "Network Error..Please try again after some time ", Toast.LENGTH_SHORT).show();
                                    }

                                }
                            });

                }
                else
                {
                    Toast.makeText(SignUpActivity.this, "This "+phone+" Already Exist", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                    Toast.makeText(SignUpActivity.this, "please try again with using another phone number", Toast.LENGTH_SHORT).show();
                    Intent intent=new Intent(SignUpActivity.this,MainActivity.class);
                    startActivity(intent);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    public void SignIn(View view) {
        Intent intent=new Intent(SignUpActivity.this,SignInActivity.class);
        startActivity(intent);
    }

}
