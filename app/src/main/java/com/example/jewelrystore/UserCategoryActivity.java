package com.example.jewelrystore;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jewelrystore.Models.Products;
import com.example.jewelrystore.ViewHolder.ProductViewHolder;
import com.example.jewelrystore.ViewHolder.ProductViewHolder2;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserCategoryActivity extends AppCompatActivity {
    private String CategoryName;
    private DatabaseReference productRef;
    private RecyclerView recyclerView;
    private Query query;
    RecyclerView.LayoutManager layoutManager;
    private CircleImageView profile;
    private ImageView close;
    private TextView check_products;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_category);
        close=findViewById(R.id.close_btn9);
        check_products=findViewById(R.id.check_product_user);
        CategoryName=getIntent().getExtras().get("category").toString();
        productRef= FirebaseDatabase.getInstance().getReference().child("Products");
        query=productRef.orderByChild("category").equalTo(CategoryName);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                if (dataSnapshot.exists())
                {
                    Log.i("Data products","Successfull");
                }
                else
                {
                    check_products.setText("No Product Found");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError)
            {
                Toast.makeText(UserCategoryActivity.this, "Error While Fetching Data", Toast.LENGTH_SHORT).show();
            }
        });
        recyclerView=findViewById(R.id.recycler_menu2);
        layoutManager=new LinearLayoutManager(this,RecyclerView.VERTICAL,false);
        recyclerView.setLayoutManager(layoutManager);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();


        FirebaseRecyclerOptions<Products> options=
                new FirebaseRecyclerOptions.Builder<Products>()
                        .setQuery(query,Products.class)
                        .build();
        FirebaseRecyclerAdapter<Products, ProductViewHolder2> adapter=
                new FirebaseRecyclerAdapter<Products, ProductViewHolder2>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull ProductViewHolder2 holder, int position, @NonNull final Products model) {
                        holder.item_name.setText(model.getPname());
                        holder.item_desc.setText(model.getDescription());
                        Picasso.get().load(model.getImage()).into(holder.item_pic);
                        holder.itemView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent=new Intent(UserCategoryActivity.this,ProductDetailActivity.class);
                                intent.putExtra("pid",model.getPid());
                                startActivity(intent);
                            }
                        });

                    }
                    @NonNull
                    @Override
                    public ProductViewHolder2 onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        View view=LayoutInflater.from(parent.getContext()).inflate(R.layout.product_item_layout2,parent,false);
                        ProductViewHolder2 holder2=new ProductViewHolder2(view);
                        return holder2;
                    }
                };

        recyclerView.setAdapter(adapter);
        adapter.startListening();
    }
}
