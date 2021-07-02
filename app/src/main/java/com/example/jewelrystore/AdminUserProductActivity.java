package com.example.jewelrystore;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.jewelrystore.Models.Products;
import com.example.jewelrystore.Models.TestModel;
import com.example.jewelrystore.ViewHolder.ProductViewHolder;
import com.example.jewelrystore.ViewHolder.ProductViewHolder2;
import com.example.jewelrystore.ViewHolder.ProductViewHolder3;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class AdminUserProductActivity extends AppCompatActivity {
    private String order_id;
    private DatabaseReference productRef;
    private RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    private String name,desc,image;
    private ImageView back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_user_product);
        back=findViewById(R.id.close_btn3);
        order_id=getIntent().getStringExtra("OrderId");
        productRef= FirebaseDatabase.getInstance().getReference().child("Orders").child("Admin View");
        recyclerView=findViewById(R.id.adminUserProduct);
        layoutManager=new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                finish();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseRecyclerOptions<TestModel> options=
                new FirebaseRecyclerOptions.Builder<TestModel>()
                .setQuery(productRef.child("Orders").child(order_id).child("Products"),TestModel.class)
                .build();
        FirebaseRecyclerAdapter<TestModel, ProductViewHolder3> adapter
                = new FirebaseRecyclerAdapter<TestModel, ProductViewHolder3>(options) {
            @Override
            protected void onBindViewHolder(@NonNull final ProductViewHolder3 holder, int position, @NonNull final TestModel model)
            {

                DatabaseReference productRef= FirebaseDatabase.getInstance().getReference().child("Products");
                productRef.child(model.getPid()).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        Products products=dataSnapshot.getValue(Products.class);
                        holder.item_name.setText(products.getPname());
                        holder.item_desc.setText(products.getDescription());
                        holder.item_quant2.setText("Quantity "+model.getQuantity());
                        Picasso.get().load(products.getImage()).into(holder.item_pic);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });


            }

            @NonNull
            @Override
            public ProductViewHolder3 onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view=LayoutInflater.from(parent.getContext()).inflate(R.layout.product_item_layout3,parent,false);
                ProductViewHolder3 productViewHolder3=new ProductViewHolder3(view);
                return productViewHolder3;
            }
        };
        recyclerView.setAdapter(adapter);
        adapter.startListening();
    }


}
