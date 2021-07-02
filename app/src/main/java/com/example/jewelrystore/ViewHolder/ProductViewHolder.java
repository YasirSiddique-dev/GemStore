package com.example.jewelrystore.ViewHolder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.jewelrystore.Interfaces.ItemClickListener;
import com.example.jewelrystore.R;

public class ProductViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
{
    public TextView txtProductName,txtProductDesc,txtProductPrice;
    public ImageView imgProductPic;
    public ItemClickListener itemClickListener;
    public ProductViewHolder(@NonNull View itemView) {
        super(itemView);

        imgProductPic=itemView.findViewById(R.id.pImage);
        txtProductName=itemView.findViewById(R.id.pname);
        txtProductDesc=itemView.findViewById(R.id.pdesc);
        txtProductPrice=itemView.findViewById(R.id.prod_price);
    }
    public void setItemClickListener(ItemClickListener listener)
    {
        this.itemClickListener=listener;

    }
    @Override
    public void onClick(View v) {
        itemClickListener.onClick(v,getAdapterPosition(),false);
    }
}
