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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jewelrystore.Interfaces.ItemClickListener;
import com.example.jewelrystore.Models.Order;
import com.example.jewelrystore.Prevalent.Prevalent;
import com.example.jewelrystore.ViewHolder.OrderViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class NewOrderActivity extends AppCompatActivity {
    private RecyclerView order_list;
    private DatabaseReference orderRef;
    private ImageView back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_order);
        back=findViewById(R.id.close_btn5);
       orderRef=FirebaseDatabase.getInstance().getReference().child("Orders");
       order_list=findViewById(R.id.admin_panel_order_list);
       order_list.setLayoutManager(new LinearLayoutManager(this));
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
        FirebaseRecyclerOptions<Order>options=
                new FirebaseRecyclerOptions.Builder<Order>()
                .setQuery(orderRef.child("Admin View").child("Orders"),Order.class)
                .build();
        FirebaseRecyclerAdapter<Order,AdminOrdersViewHolder>adapter
            = new FirebaseRecyclerAdapter<Order, AdminOrdersViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull final AdminOrdersViewHolder holder, int position, @NonNull final Order model)
            {
                final DatabaseReference db= FirebaseDatabase.getInstance().getReference();
                holder.txt_order_state.setText("Order Status "+model.getState());
                holder.txt_order_conf.setText(model.getConfirmation());
                holder.txt_order_userphone.setText("Phone "+model.getPhone());
                holder.txt_order_user.setText(model.getFirst_Name()+" "+model.getLast_Name());
                holder.txt_order_price.setText("Order Price "+model.getTotal_Amount());
                holder.txt_order_id.setText("Order id "+model.getOrder_ID());
                holder.txt_order_date.setText(model.getDate());
                holder.txt_order_address.setText("Address "+model.getHome_Address()+", "+model.getCity());
                holder.btn_view_product.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent=new Intent(NewOrderActivity.this,AdminUserProductActivity.class);
                        intent.putExtra("OrderId",model.getOrder_ID().toString());
                        startActivity(intent);
                    }
                });
                holder.img_delete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v)
                    {
                        DatabaseReference orderListRef= FirebaseDatabase.getInstance().getReference().child("Orders");
                        orderListRef.child("Admin View")
                                .child("Orders")
                                .child(model.getOrder_ID())
                                .removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task)
                            {
                                DatabaseReference orderListRef= FirebaseDatabase.getInstance().getReference().child("Orders");
                                orderListRef.child(model.getPhone()).child(model.getOrder_ID())
                                        .removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task)
                                    {
                                        Toast.makeText(NewOrderActivity.this, "Order Deleted Successfully", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        });
                    }
                });



                if (model.getState().equals("Not Shipped"))
                {
                    holder.btn_change_state.setVisibility(View.VISIBLE);
                    holder.btn_change_state2.setVisibility(View.INVISIBLE);

                    holder.btn_change_state.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v)
                        {
                            db.child("Orders").child(model.getPhone()).child(model.getDate()+model.getTime()).child("State").setValue("Shipped");
                            db.child("Orders").child("Admin View").child("Orders").child(model.getDate()+model.getTime()).child("State").setValue("Shipped");

                        }
                    });
                    holder.btn_view_product.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent=new Intent(NewOrderActivity.this,AdminUserProductActivity.class);
                            intent.putExtra("OrderId",model.getOrder_ID().toString());
                            startActivity(intent);
                        }
                    });
                }
                else if (model.getState().equals("Shipped"))
                {
                    holder.btn_change_state.setVisibility(View.INVISIBLE);
                    holder.btn_change_state2.setVisibility(View.VISIBLE);
                    holder.btn_change_state2.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v)
                        {
                            db.child("Orders").child(model.getPhone()).child(model.getDate()+model.getTime()).child("State").setValue("Not Shipped");
                            db.child("Orders").child("Admin View").child("Orders").child(model.getDate()+model.getTime()).child("State").setValue("Not Shipped");

                        }
                    });
                }

            }


            @NonNull
            @Override
            public AdminOrdersViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view=LayoutInflater.from(parent.getContext()).inflate(R.layout.order_item_layout2,parent,false);
                return new AdminOrdersViewHolder(view);
            }
        };
        order_list.setAdapter(adapter);
        adapter.startListening();
    }

    public static class AdminOrdersViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
    {
        public TextView txt_order_id,txt_order_date,txt_order_price,txt_order_address;
        public TextView txt_order_user,txt_order_userphone,txt_order_conf,txt_order_state;
        public Button btn_change_state,btn_view_product,btn_change_state2;
        private ImageView img_delete;
        private ItemClickListener itemClickListener;
        public AdminOrdersViewHolder(@NonNull View itemView)
        {
            super(itemView);
            txt_order_id=itemView.findViewById(R.id.order_Id2);
            txt_order_date=itemView.findViewById(R.id.order_date2);
            txt_order_price=itemView.findViewById(R.id.order_price2);
            txt_order_user=itemView.findViewById(R.id.order_user);
            txt_order_userphone=itemView.findViewById(R.id.order_Userphone);
            txt_order_conf=itemView.findViewById(R.id.order_confirmation2);
            txt_order_state=itemView.findViewById(R.id.order_state2);
            btn_change_state=itemView.findViewById(R.id.admin_btn_confirm_shipped);
            btn_change_state2=itemView.findViewById(R.id.admin_btn_confirm_shipped2);
            btn_view_product=itemView.findViewById(R.id.admin_btn_view_products);
            txt_order_address=itemView.findViewById(R.id.order_address2);
            img_delete=itemView.findViewById(R.id.delete_item);
        }

        @Override
        public void onClick(View v)
        {
            itemClickListener.onClick(v,getAdapterPosition(),false);
        }
        public void setItemClickListener(ItemClickListener itemClickListener)
        {
            this.itemClickListener = itemClickListener;
        }
    }
}
