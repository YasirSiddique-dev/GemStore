package com.example.jewelrystore;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

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
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

public class ConfirmFinalOrderActivity extends AppCompatActivity {
    private EditText fname,lname,email,phone,home_address,city;
    private Button confirm_order;
    private String total_amount;
    private ArrayList<String> prodList=new ArrayList<>();
    private ArrayList<String> prodquant=new ArrayList<>();
    String product_id,product_quant;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_final_order);
        fname=findViewById(R.id.confirm_order_fname);
        lname=findViewById(R.id.confirm_order_lname);
        email=findViewById(R.id.confirm_order_email);
        phone=findViewById(R.id.confirm_order_phone);
        home_address=findViewById(R.id.confirm_order_h_address);
        city=findViewById(R.id.confirm_order_City);
        confirm_order=findViewById(R.id.btn_confirmOrder);
        prodList=getIntent().getStringArrayListExtra("product_list");
        prodquant=getIntent().getStringArrayListExtra("product_quantity");
        total_amount=getIntent().getStringExtra("total_price");

        fname.setText(Prevalent.currentonlineUsers.getFirst_Name());
        lname.setText(Prevalent.currentonlineUsers.getLast_Name());
        phone.setText(Prevalent.currentonlineUsers.getPhone());
        email.setText(Prevalent.currentonlineUsers.getEmail());

        confirm_order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                check();
            }
        });
    }
    private void check()
    {
        if (TextUtils.isEmpty(fname.getText().toString()))
        {
            Toast.makeText(this, "Please Enter Your First Name", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(lname.getText().toString()))
        {
            Toast.makeText(this, "Please Enter Your Last Name", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(phone.getText().toString()))
        {
            Toast.makeText(this, "Please Enter Your Phone Number", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(email.getText().toString()))
        {
            Toast.makeText(this, "Please Enter Your Email", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(home_address.getText().toString()))
        {
            Toast.makeText(this, "Please Enter Your Home Address", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(city.getText().toString()))
        {
            Toast.makeText(this, "Please enter your city", Toast.LENGTH_SHORT).show();
        }
        else
        {
            confirmOrder();
        }
    }

    private void confirmOrder()
    {
        final String saverCurrentTime,saveCurrentDate;

        Calendar calForDate=Calendar.getInstance();
        final SimpleDateFormat currentDate=new SimpleDateFormat("MM dd, yyyy");
        saveCurrentDate=currentDate.format(calForDate.getTime());

        final SimpleDateFormat currentTime=new SimpleDateFormat("HH:mm:ss a");
        saverCurrentTime=currentTime.format(calForDate.getTime());

        final DatabaseReference orderRef= FirebaseDatabase.getInstance().getReference()
                .child("Orders");


        final HashMap<String,Object> ordersMap=new HashMap<>();
        ordersMap.put("Order_ID",saveCurrentDate+saverCurrentTime);
        ordersMap.put("Total_Amount",total_amount);
        ordersMap.put("Date",saveCurrentDate);
        ordersMap.put("Time",saverCurrentTime);
        ordersMap.put("First_Name",fname.getText().toString());
        ordersMap.put("Last_Name",lname.getText().toString());
        ordersMap.put("Phone",phone.getText().toString());
        ordersMap.put("Email",email.getText().toString());
        ordersMap.put("Home_Address",home_address.getText().toString());
        ordersMap.put("City",city.getText().toString());
        ordersMap.put("State","Not Shipped");
        ordersMap.put("Confirmation","Confirmed Order");

        orderRef.child(Prevalent.currentonlineUsers.getPhone())
                .child(saveCurrentDate+saverCurrentTime)
                .updateChildren(ordersMap)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task)
            {
                orderRef.child("Admin View")
                        .child("Orders")
                        .child(saveCurrentDate+saverCurrentTime)
                        .updateChildren(ordersMap)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                FirebaseDatabase.getInstance().getReference().child("Cart List")
                                        .child("User View")
                                        .child(Prevalent.currentonlineUsers.getPhone())
                                        .removeValue()
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task)
                                            {
                                                if (task.isSuccessful())
                                                {

                                                    final DatabaseReference db= FirebaseDatabase.getInstance().getReference();
                                                    for (int i = 0; i < prodList.size(); i++) {
                                                        product_id=prodList.get(i);
                                                        product_quant=prodquant.get(i);
                                                        final HashMap<String,Object> productMap=new HashMap<>();
                                                        productMap.put("pid",product_id);
                                                        productMap.put("Quantity",product_quant);
                                                        orderRef.child("Admin View").child("Orders").child(saveCurrentDate+saverCurrentTime)
                                                                .child("Products")
                                                                .child(product_id).updateChildren(productMap)
                                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                    @Override
                                                                    public void onComplete(@NonNull Task<Void> task) {
                                                                        Toast.makeText(ConfirmFinalOrderActivity.this, "Your Final Order Has been Placed Successfully", Toast.LENGTH_SHORT).show();
                                                                        finish();
                                                                    }
                                                                });
//
                                                    }

                                                }
                                            }
                                        });
                            }
                        });

            }
        });
    }

}
