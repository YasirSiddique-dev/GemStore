package com.example.jewelrystore;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.example.jewelrystore.Models.Products;
import com.example.jewelrystore.Prevalent.Prevalent;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class ProductDetailActivity extends AppCompatActivity {
    private ImageView product_detail_Image,btn_close;
    private TextView product_detail_name,product_detail_desc,product_detail_price;
    private Button add_to_cart;
    private ElegantNumberButton numberButton;
    private String productId="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);

        product_detail_Image=findViewById(R.id.product_image_detail);
        product_detail_name=findViewById(R.id.product_name_detail);
        product_detail_desc=findViewById(R.id.product_desc_detail_data);
        product_detail_price=findViewById(R.id.product_price_detail);
        numberButton=findViewById(R.id.card2);
        btn_close=findViewById(R.id.close_btn6);
        add_to_cart=findViewById(R.id.btn_add_to_cart);

        productId=getIntent().getStringExtra("pid");
        getProductDetails(productId);

        btn_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        add_to_cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addingToCartList();
            }
        });
    }

    private void addingToCartList() {
        final String saverCurrentTime,saveCurrentDate;

        Calendar calForDate=Calendar.getInstance();
        SimpleDateFormat currentDate=new SimpleDateFormat("MMM dd, yyyy");
        saveCurrentDate=currentDate.format(calForDate.getTime());

        SimpleDateFormat currentTime=new SimpleDateFormat("HH:mm:ss a");
        saverCurrentTime=currentTime.format(calForDate.getTime());

        final DatabaseReference cartListRef=FirebaseDatabase.getInstance().getReference().child("Cart List");
        final HashMap<String,Object> cartMap=new HashMap<>();

        cartMap.put("pid",productId);
        cartMap.put("pname",product_detail_name.getText().toString());
        cartMap.put("price",product_detail_price.getText().toString());
        cartMap.put("date",saveCurrentDate);
        cartMap.put("time",saverCurrentTime);
        cartMap.put("quantity",numberButton.getNumber());
        cartMap.put("discount","");


        cartListRef.child("User View").child(Prevalent.currentonlineUsers.getPhone())
                .child("Products")
                .child(productId)
                .updateChildren(cartMap)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful())
                        {
                            cartListRef.child("Admin View").child(Prevalent.currentonlineUsers.getPhone())
                                    .child("Products")
                                    .child(productId)
                                    .updateChildren(cartMap)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task)
                                        {
                                            if (task.isSuccessful())
                                            {

                                                Toast.makeText(ProductDetailActivity.this, "Added to Cart List", Toast.LENGTH_SHORT).show();
                                                finish();
                                            }
                                        }
                                    });
                        }
                    }
                });
    }

    private void getProductDetails(String productId) {
        DatabaseReference productRef= FirebaseDatabase.getInstance().getReference().child("Products");
        productRef.child(productId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists())
                {
                    Products products=dataSnapshot.getValue(Products.class);
                    product_detail_name.setText(products.getPname());
                    product_detail_desc.setText(products.getDescription());
                    product_detail_price.setText(products.getPrice());
                    Picasso.get().load(products.getImage()).into(product_detail_Image);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
