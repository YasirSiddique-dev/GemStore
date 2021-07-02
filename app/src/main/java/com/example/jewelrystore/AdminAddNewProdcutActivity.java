package com.example.jewelrystore;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class AdminAddNewProdcutActivity extends AppCompatActivity {
    private  String CategoryName,pName,pDesc,pPrice,saveCurrentDate,saveCurrentTime;
    private EditText product_name,product_desc,product_price;
    private Button btn_add_product;
    private ImageView product_image;
    private static final int GalleryPick=1;
    private Uri ImageUri;
    private ProgressDialog progressDialog;
    private String productRandomKey,downloadImageUrl;
    private StorageReference prodcutImagesRef;
    private DatabaseReference productRef;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_add_new_prodcut);
        CategoryName=getIntent().getExtras().get("category").toString();
        prodcutImagesRef= FirebaseStorage.getInstance().getReference().child("Product Images");
        productRef =FirebaseDatabase.getInstance().getReference().child("Products");
        product_name=findViewById(R.id.product_title);
        product_desc=findViewById(R.id.product_desc);
        product_price=findViewById(R.id.product_price);
        product_image=findViewById(R.id.prodcut_image);
        btn_add_product=findViewById(R.id.btn_add_product);
        progressDialog=new ProgressDialog(this);
        product_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGallery();
            }
        });
        btn_add_product.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ValidateProductData();
            }
        });
    }

    private void ValidateProductData() {
        pName=product_name.getText().toString();
        pDesc=product_desc.getText().toString();
        pPrice=product_price.getText().toString();

        if (ImageUri==null)
        {
            Toast.makeText(this, "Product Image is Mandatory...!", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(pName))
        {
            Toast.makeText(this, "Product Name is Mandatory...!", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(pDesc))
        {
            Toast.makeText(this, "Product Description is Mandatory...!", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(pPrice))
        {
            Toast.makeText(this, "Product Price is Mandatory...!", Toast.LENGTH_SHORT).show();
        }
        else
        {
            StoreProductInformation();
        }
    }

    private void StoreProductInformation() {
        progressDialog.setTitle("Adding New Product");
        progressDialog.setMessage("Please wait while we are adding the new product");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        Calendar calendar=Calendar.getInstance();
        SimpleDateFormat currentDate  =new SimpleDateFormat("MM dd, yyyy");
        saveCurrentDate =currentDate.format(calendar.getTime());
        SimpleDateFormat currentTime  =new SimpleDateFormat("HH:mm:ss a");
        saveCurrentTime =currentTime.format(calendar.getTime());
        productRandomKey= saveCurrentDate + saveCurrentTime;

        final StorageReference filePath=prodcutImagesRef.child(ImageUri.getLastPathSegment()+productRandomKey+".jpg");
        final UploadTask uploadTask=filePath.putFile(ImageUri);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                String message=e.toString();
                Toast.makeText(AdminAddNewProdcutActivity.this, "Error : "+message, Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Toast.makeText(AdminAddNewProdcutActivity.this, "Product Image Uploaded Successfully", Toast.LENGTH_SHORT).show();
                Task<Uri> uriTask=uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                    @Override
                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                        if (!task.isSuccessful())
                        {
                            throw task.getException();
                        }

                        return filePath.getDownloadUrl();
                    }
                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        if (task.isSuccessful())
                        {
                            downloadImageUrl=task.getResult().toString();
                            Toast.makeText(AdminAddNewProdcutActivity.this, "Got the product Image Url", Toast.LENGTH_SHORT).show();
                            saveProductInfoToDatabase();
                        }
                    }
                });
            };
        });
    }

    private void saveProductInfoToDatabase() {
        final HashMap<String,Object> productMap=new HashMap<>();
        productMap.put("pid",productRandomKey);
        productMap.put("date",saveCurrentDate);
        productMap.put("time",saveCurrentTime);
        productMap.put("description",pDesc);
        productMap.put("image",downloadImageUrl);
        productMap.put("category",CategoryName);
        productMap.put("price",pPrice);
        productMap.put("pname",pName);

        productRef.child(productRandomKey).updateChildren(productMap)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful())
                        {
                            progressDialog.dismiss();
                            Toast.makeText(AdminAddNewProdcutActivity.this, "Product added Successfully..", Toast.LENGTH_SHORT).show();
                            Intent intent=new Intent(AdminAddNewProdcutActivity.this,AdminCategoryActivity.class);
                            startActivity(intent);
                        }
                        else
                        {
                            String message =task.getException().toString();
                            Toast.makeText(AdminAddNewProdcutActivity.this, "Error :"+message, Toast.LENGTH_SHORT).show();
                        }
                    }
                });

    }

    private void openGallery() {
        Intent galleryIntent=new Intent();
        galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent,GalleryPick);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==GalleryPick && resultCode==RESULT_OK && data!=null)
        {
            ImageUri=data.getData();
            product_image.setImageURI(ImageUri);

        }
    }
}
