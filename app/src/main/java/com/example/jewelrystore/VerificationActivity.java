package com.example.jewelrystore;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.arch.core.executor.TaskExecutor;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskExecutors;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;

public class VerificationActivity extends AppCompatActivity {
    EditText otp_Code;
    Button btn_submit;
    ProgressBar progressBar;
    String verificationCodeBySystem;
    private String phone,fname,lname,pass,email;
    private ProgressDialog progressDialog;
    private TextView sendAgain;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verification);

        otp_Code=findViewById(R.id.edit_verify_code);
        btn_submit=findViewById(R.id.btn_submit_code);
        progressBar=findViewById(R.id.progress_code);
        progressBar.setVisibility(View.GONE);
        sendAgain=findViewById(R.id.send_again);
        phone=getIntent().getExtras().get("phone").toString();
        email=getIntent().getExtras().get("email").toString();
        pass=getIntent().getExtras().get("pass").toString();
        fname=getIntent().getExtras().get("fname").toString();
        lname=getIntent().getExtras().get("lname").toString();
        progressDialog=new ProgressDialog(this);
        sendAgain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Toast.makeText(VerificationActivity.this, "Please wait..", Toast.LENGTH_SHORT).show();
                sendVerificationToUser(phone);
            }
        });

        sendVerificationToUser(phone);
        Toast.makeText(this, "please wait..", Toast.LENGTH_SHORT).show();
        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String code=otp_Code.getText().toString();
                if (code.isEmpty() || code.length()<6)
                {
                    Toast.makeText(VerificationActivity.this, "Wrong OTP Code", Toast.LENGTH_SHORT).show();
                }
                progressBar.setVisibility(View.VISIBLE);
                verifyCode(code);
            }
        });
    }

    private void sendVerificationToUser(String phone) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                "+92"+phone,
                60,
                TimeUnit.SECONDS,
                TaskExecutors.MAIN_THREAD,
                mCallbacks);
    }

    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks= new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

        @Override
        public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);
            verificationCodeBySystem=s;
        }

        @Override
        public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
            String code=phoneAuthCredential.getSmsCode();
            if (code!=null)
            {
                progressBar.setVisibility(View.VISIBLE);
                verifyCode(code);
            }
        }

        @Override
        public void onVerificationFailed(@NonNull FirebaseException e) {
            Toast.makeText(VerificationActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    };

    private void verifyCode(String code) {
        PhoneAuthCredential credential=PhoneAuthProvider.getCredential(verificationCodeBySystem,code);
        SignInUser(credential);
    }

    private void SignInUser(PhoneAuthCredential credential) {

        FirebaseAuth firebaseAuth=FirebaseAuth.getInstance();
        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(VerificationActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful())
                        {
                            createAccount();
                        }
                        else 
                        {
                            Toast.makeText(VerificationActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void createAccount() {
        progressDialog.setTitle("Create Account");
        progressDialog.setMessage("Please wait while we are checking the credential.");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();
        validatePhoneNumber(fname,lname,phone,email,pass);
    }

    private void validatePhoneNumber(final String first_name, final String last_name, final String phone, final String email, final String pass)
    {
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
                                        Toast.makeText(VerificationActivity.this, "Congratulation, your account has been created", Toast.LENGTH_SHORT).show();
                                        progressDialog.dismiss();
                                        Intent intent=new Intent(VerificationActivity.this,SignInActivity.class);
                                        startActivity(intent);
                                    }
                                    else
                                    {
                                        progressDialog.dismiss();
                                        Toast.makeText(VerificationActivity.this, "Network Error..Please try again after some time ", Toast.LENGTH_SHORT).show();
                                    }

                                }
                            });

                }
                else
                {
                    Toast.makeText(VerificationActivity.this, "This "+phone+" Already Exist", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                    Toast.makeText(VerificationActivity.this, "please try again with using another phone number", Toast.LENGTH_SHORT).show();

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

}
