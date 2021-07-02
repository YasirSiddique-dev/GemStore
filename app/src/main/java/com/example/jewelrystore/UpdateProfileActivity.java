package com.example.jewelrystore;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jewelrystore.Models.Users;
import com.example.jewelrystore.Prevalent.Prevalent;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class UpdateProfileActivity extends AppCompatActivity {
    private TextView update,change_profile;
    private EditText u_fname,u_lname,u_phone,u_email;
    private CircleImageView up_profile_pic;
    private Uri imageUri;
    private String myUrl="";
    private StorageTask uploadTask;
    private ImageView close;
    private StorageReference storageReference_profile;
    private String checker="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_profile);
        storageReference_profile= FirebaseStorage.getInstance().getReference().child("Profile Picture");

        update=findViewById(R.id.update);
        close=findViewById(R.id.close_btn8);
        change_profile=findViewById(R.id.change_profile);
        u_fname=findViewById(R.id.upd_first_name);
        u_lname=findViewById(R.id.upd_last_name);
        u_email=findViewById(R.id.upd_email);
        u_phone=findViewById(R.id.upd_phone);
        u_phone.setEnabled(false);
        up_profile_pic=findViewById(R.id.update_profile_image);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checker.equals("clicked"))
                {
                    userInfoSaved();
                }
                else
                {
                    updateOnlyUserInfo();
                }
            }
        });
        userInfoDisplay(up_profile_pic,u_fname,u_lname,u_email,u_phone);

        change_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checker="clicked";
                CropImage.activity(imageUri)
                        .setAspectRatio(1,1)
                        .start(UpdateProfileActivity.this);
            }
        });
    }

    private void updateOnlyUserInfo() {
        DatabaseReference ref=FirebaseDatabase.getInstance().getReference().child("Users");
        HashMap<String,Object> userMap=new HashMap<>();
        userMap.put("First_Name",u_fname.getText().toString());
        userMap.put("Last_Name",u_lname.getText().toString());
        userMap.put("Email",u_email.getText().toString());
        ref.child(Prevalent.currentonlineUsers.getPhone()).updateChildren(userMap);
        startActivity(new Intent(UpdateProfileActivity.this,UpdateProfileActivity.class));
        Toast.makeText(UpdateProfileActivity.this, "Profile Info Updated Successfully", Toast.LENGTH_SHORT).show();
        finish();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE && resultCode==RESULT_OK)
        {
            CropImage.ActivityResult result=CropImage.getActivityResult(data);
            imageUri=result.getUri();
            up_profile_pic.setImageURI(imageUri);
        }
        else
        {
            Toast.makeText(this,"Error, Try Again.",Toast.LENGTH_SHORT).show();
            startActivity(new Intent(UpdateProfileActivity.this,UpdateProfileActivity.class));
            finish();
        }
    }

    private void userInfoSaved() {
        if (TextUtils.isEmpty(u_fname.getText().toString()))
        {
            Toast.makeText(this, "Please Enter First Name", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(u_lname.getText().toString()))
        {
            Toast.makeText(this, "Please Enter Last Name", Toast.LENGTH_SHORT).show();
        }

        else if (TextUtils.isEmpty(u_email.getText().toString()))
        {
            Toast.makeText(this, "Please Enter Email Address", Toast.LENGTH_SHORT).show();
        }
        else if (checker.equals("clicked"))
        {
            uploadImage();
        }

    }

    private void uploadImage() {
        final ProgressDialog progressDialog=new ProgressDialog(this);
        progressDialog.setTitle("Updating Profile");
        progressDialog.setMessage("Please wait.. while we are updating your Account info");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        if (imageUri != null)
        {
            final StorageReference fileRef=storageReference_profile
                    .child(Prevalent.currentonlineUsers.getPhone()+".jpg");

            uploadTask=fileRef.putFile(imageUri);
            uploadTask.continueWithTask(new Continuation() {
                @Override
                public Object then(@NonNull Task task) throws Exception {
                    if (!task.isSuccessful())
                    {
                        throw task.getException();
                    }
                    return fileRef.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if (task.isSuccessful())
                    {
                        Uri downloadUrl=task.getResult();
                        myUrl=downloadUrl.toString();
                        DatabaseReference ref=FirebaseDatabase.getInstance().getReference().child("Users");
                        HashMap<String,Object> userMap=new HashMap<>();
                        userMap.put("First_Name",u_fname.getText().toString());
                        userMap.put("Last_Name",u_lname.getText().toString());
                        userMap.put("Email",u_email.getText().toString());
                        userMap.put("image",myUrl);
                        ref.child(Prevalent.currentonlineUsers.getPhone()).updateChildren(userMap);
                        progressDialog.dismiss();
                        Toast.makeText(UpdateProfileActivity.this, "Profile Info Updated Successfully", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                    else
                        {
                            progressDialog.dismiss();
                            Toast.makeText(UpdateProfileActivity.this, "Error", Toast.LENGTH_SHORT).show();
                        }
                }
            });
        }
        else
        {
            Toast.makeText(this, "Image is not Selected", Toast.LENGTH_SHORT).show();
        }
    }

    private void userInfoDisplay(final CircleImageView up_profile_pic, final EditText u_fname, final EditText u_lname, final EditText u_email, final EditText u_phone)
    {
        DatabaseReference UserRef= FirebaseDatabase.getInstance().getReference().child("Users");
        {
            UserRef.child(Prevalent.currentonlineUsers.getPhone()).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists())
                    {
                        if (dataSnapshot.exists()){
                            if (dataSnapshot.hasChild("image"))
                            {
                                Users users=dataSnapshot.getValue(Users.class);
                                Picasso.get().load(users.getImage()).into(up_profile_pic);
                                u_fname.setText(users.getFirst_Name());
                                u_lname.setText(users.getLast_Name());
                                u_email.setText(users.getEmail());
                                u_phone.setText(users.getPhone());
                            }
                            else
                            {
                                Users users=dataSnapshot.getValue(Users.class);
                                u_fname.setText(users.getFirst_Name());
                                u_lname.setText(users.getLast_Name());
                                u_email.setText(users.getEmail());
                                u_phone.setText(users.getPhone());
                            }

                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
    }


}
