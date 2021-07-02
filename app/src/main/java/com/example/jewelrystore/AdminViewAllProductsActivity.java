package com.example.jewelrystore;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.jewelrystore.Models.Products;
import com.example.jewelrystore.ViewHolder.ProductViewHolder4;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

public class AdminViewAllProductsActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    private DatabaseReference productRef;
    private ImageView back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_view_all_products);
        recyclerView=findViewById(R.id.admin_view_all);
        back=findViewById(R.id.close_btn4);
        layoutManager=new LinearLayoutManager(this,RecyclerView.VERTICAL,false);
        recyclerView.setLayoutManager(layoutManager);
        productRef= FirebaseDatabase.getInstance().getReference().child("Products");
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseRecyclerOptions<Products>options=
                new FirebaseRecyclerOptions.Builder<Products>()
                .setQuery(productRef,Products.class)
                .build();
        FirebaseRecyclerAdapter<Products, ProductViewHolder4> adapter=
                new FirebaseRecyclerAdapter<Products, ProductViewHolder4>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull ProductViewHolder4 holder, int position, @NonNull final Products model)
                    {
                        holder.item_name.setText(model.getPname());
                        holder.item_desc.setText(model.getDescription());
                        Picasso.get().load(model.getImage()).into(holder.item_pic);
                        holder.item_delete.setOnClickListener(new View.OnClickListener()
                        {
                            @Override
                            public void onClick(View v)
                            {
                                DatabaseReference productListRef= FirebaseDatabase.getInstance().getReference().child("Products");
                                productListRef.child(model.getPid())
                                        .removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task)
                                    {
                                        Toast.makeText(AdminViewAllProductsActivity.this, "Product removed Successfully", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        });
                    }
                    @NonNull
                    @Override
                    public ProductViewHolder4 onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.product_item_layout_4,parent,false);
                        ProductViewHolder4 productViewHolder4=new ProductViewHolder4(view);
                        return productViewHolder4;
                    }
                };
        recyclerView.setAdapter(adapter);
        adapter.startListening();

    }
}
