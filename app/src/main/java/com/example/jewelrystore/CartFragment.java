package com.example.jewelrystore;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jewelrystore.Models.Cart;
import com.example.jewelrystore.Prevalent.Prevalent;
import com.example.jewelrystore.ViewHolder.CartViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class CartFragment extends Fragment {

    public CartFragment() {
        // Required empty public constructor
    }
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private Button cart_confirm;
    ArrayList<String> productlist=new ArrayList<>();
    ArrayList<String> productquantity=new ArrayList<>();
    private int total_calculatedPrice=0;
    TextView total_price;
    private int flag=0;
    private TextView check_cart;
    private DatabaseReference cartListRef;
    private Query query;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_cart, container, false);
        check_cart=view.findViewById(R.id.check_cart);
        cartListRef= FirebaseDatabase.getInstance().getReference().child("Cart List");
        query=cartListRef.child("User View").child(Prevalent.currentonlineUsers.getPhone()).child("Products");
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                if (dataSnapshot.exists())
                {
                    Log.i("Data Fetching For Cart","Successfull");
                }
                else if (!dataSnapshot.exists())
                {
                    check_cart.setText("No Item in the Cart");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getActivity(), "Error While Fetching Data"+databaseError, Toast.LENGTH_SHORT).show();

            }
        });
        recyclerView=view.findViewById(R.id.cart_list);
        recyclerView.setHasFixedSize(true);
        layoutManager=new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        cart_confirm=view.findViewById(R.id.btn_cart_confirm);
        total_price=view.findViewById(R.id.total_price);
        total_price.setText("Total Price: Rs."+0);
        if (total_price.getText().equals("Total Price: Rs."+0))
        {
            cart_confirm.setText("Calculate Total");
        }
        cart_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cart_confirm.getText().equals("Calculate Total"))
                {
                    total_price.setText("Total Price: Rs."+total_calculatedPrice);
                    cart_confirm.setText("Next");
                }
                else if (cart_confirm.getText().equals("Next"))
                {
                    Toast.makeText(getActivity(), "Please confirm the credential", Toast.LENGTH_SHORT).show();
                    cart_confirm.setText("Calculate Total");
                    Intent intent=new Intent(getActivity(),ConfirmFinalOrderActivity.class);
                    intent.putExtra("total_price",String.valueOf(total_calculatedPrice));
                    intent.putStringArrayListExtra("product_list",productlist);
                    intent.putStringArrayListExtra("product_quantity",productquantity);
                    startActivity(intent);

                }

            }
        });
        view.setFocusableInTouchMode(true);
        view.requestFocus();
        view.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    if (keyCode == KeyEvent.KEYCODE_BACK) {
                        if (flag==1)
                        {
                            getActivity().finish();

                        }
                        else if (flag==0)
                        {
                            Toast.makeText(getActivity(), "Press Back again to Exit.", Toast.LENGTH_SHORT).show();
                            flag=1;
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    flag=0;
                                }
                            }, 3 * 1000);
                        }
                        return true;
                    }
                }
                return false;
            }
        });
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        total_price.setText("Total Price: Rs."+0);

        FirebaseRecyclerOptions<Cart> options=
                new FirebaseRecyclerOptions.Builder<Cart>()
                .setQuery(query,Cart.class)
                .build();

        FirebaseRecyclerAdapter<Cart, CartViewHolder>adapter
                =new FirebaseRecyclerAdapter<Cart, CartViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull CartViewHolder holder, int position, @NonNull final Cart model)
            {
                holder.txtProductQuantity.setText("Quantity: "+model.getQuantity());
                holder.txtProductPrice.setText("Price Rs."+model.getPrice());
                holder.txtProductName.setText(model.getPname());
                productlist.add(model.getPid());
                productquantity.add(model.getQuantity());
                int oneTypeProductPrice=((Integer.valueOf(model.getPrice()))*(Integer.valueOf(model.getQuantity()))) ;
                total_calculatedPrice=total_calculatedPrice+oneTypeProductPrice;
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        CharSequence options[]=new CharSequence[]
                                {
                                  "Edit",
                                  "Remove"
                                };
                        AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());
                        builder.setTitle("Cart Options");

                        builder.setItems(options, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which)
                            {
                                if (which==0)
                                {
                                    Intent intent=new Intent(getActivity(),ProductDetailActivity.class);
                                    intent.putExtra("pid",model.getPid());
                                    startActivity(intent);
                                }
                                else if (which==1)
                                {
                                    cartListRef.child("User View")
                                            .child(Prevalent.currentonlineUsers.getPhone())
                                            .child("Products")
                                            .child(model.getPid())
                                            .removeValue()
                                            .addOnCompleteListener(new OnCompleteListener<Void>()
                                            {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task)
                                                {
                                                    if (task.isSuccessful())
                                                    {
                                                        Toast.makeText(getActivity(), "Item Removed", Toast.LENGTH_SHORT).show();

                                                    }
                                                }
                                            });
                                }
                            }
                        });
                        builder.show();
                    }
                });
            }
            @NonNull
            @Override
            public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
            {
               View view=LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_item,parent,false);
               CartViewHolder holder=new CartViewHolder(view);
               return holder;
            }
        };
        recyclerView.setAdapter(adapter);
        adapter.startListening();

    }
}
