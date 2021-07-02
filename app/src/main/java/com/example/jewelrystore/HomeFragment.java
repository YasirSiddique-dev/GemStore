package com.example.jewelrystore;

import android.animation.Animator;
import android.content.Intent;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
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
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.SearchView;
import android.widget.Toast;


import com.example.jewelrystore.Models.Products;
import com.example.jewelrystore.Prevalent.Prevalent;
import com.example.jewelrystore.ViewHolder.ProductViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import jp.wasabeef.recyclerview.adapters.SlideInLeftAnimationAdapter;


/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {

    public HomeFragment() {
        // Required empty public constructor
    }
    private DatabaseReference productRef;
    private RecyclerView recyclerView;
    private Query query;
    RecyclerView.LayoutManager layoutManager;
    private CircleImageView profile;
    private SearchView searchView;
    private Boolean exit = false;
    private int flag=0;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =inflater.inflate(R.layout.fragment_home, container, false);

        profile=view.findViewById(R.id.profile_image2);
        productRef= FirebaseDatabase.getInstance().getReference().child("Products");
        Picasso.get().load(Prevalent.currentonlineUsers.getImage()).into(profile);
        recyclerView=view.findViewById(R.id.recycler_menu);
        searchView=view.findViewById(R.id.product_search);
        recyclerView.setHasFixedSize(true);
        layoutManager=new LinearLayoutManager(getContext(),RecyclerView.HORIZONTAL,false);
        recyclerView.setLayoutManager(layoutManager);
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

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Intent intent=new Intent(getActivity(),SearchActivity.class);
                intent.putExtra("Search_query",query);
                startActivity(intent);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
               return false;
            }
        });

        return view;
    }



    @Override
    public void onStart() {

        super.onStart();

        Picasso.get().load(Prevalent.currentonlineUsers.getImage()).into(profile);

        FirebaseRecyclerOptions<Products> options=
                new FirebaseRecyclerOptions.Builder<Products>()
                .setQuery(productRef,Products.class)
                .build();


        final FirebaseRecyclerAdapter<Products, ProductViewHolder> adapter=
                            new FirebaseRecyclerAdapter<Products, ProductViewHolder>(options) {
                                @Override
                                protected void onBindViewHolder(@NonNull ProductViewHolder holder, int position, @NonNull final Products model) {
                                    holder.txtProductName.setText(model.getPname());
                                    holder.txtProductDesc.setText(model.getDescription());
                                    holder.txtProductPrice.setText("Rs."+model.getPrice());
                                    Picasso.get().load(model.getImage()).into(holder.imgProductPic);

                                    holder.itemView.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            Intent intent=new Intent(getActivity(),ProductDetailActivity.class);
                                            intent.putExtra("pid",model.getPid());
                                            startActivity(intent);
                                        }
                                    });
                                }

                                @NonNull
                                @Override
                                public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                                    View view=LayoutInflater.from(parent.getContext()).inflate(R.layout.product_item_layout,parent,false);
                                    ProductViewHolder holder=new ProductViewHolder(view);
                                    return holder;

                                }
                };

        recyclerView.setAdapter(adapter);
        adapter.startListening();
    }


}
