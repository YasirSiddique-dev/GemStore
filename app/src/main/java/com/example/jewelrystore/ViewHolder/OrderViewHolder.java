package com.example.jewelrystore.ViewHolder;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.jewelrystore.Interfaces.ItemClickListener;
import com.example.jewelrystore.R;

public class OrderViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    private ItemClickListener itemClickListener;
    public TextView txt_Order_id,txt_Order_Date,txt_Order_price,txt_Order_status,txt_shipment_status;
    public OrderViewHolder(@NonNull View itemView)
    {
        super(itemView);
        txt_Order_id=itemView.findViewById(R.id.order_Id);
        txt_Order_Date=itemView.findViewById(R.id.order_date);
        txt_Order_price=itemView.findViewById(R.id.order_price);
        txt_Order_status=itemView.findViewById(R.id.order_confirmation);
        txt_shipment_status=itemView.findViewById(R.id.order_shipment);
    }

    @Override
    public void onClick(View v)
    {
        itemClickListener.onClick(v,getAdapterPosition(),false);
    }
    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

}
