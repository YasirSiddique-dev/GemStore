package com.example.jewelrystore.ViewHolder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.jewelrystore.Interfaces.ItemClickListener;
import com.example.jewelrystore.R;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProductViewHolder3 extends RecyclerView.ViewHolder implements View.OnClickListener
{
    public TextView item_name,item_desc,item_quant2;
    public CircleImageView item_pic;
    public ItemClickListener itemClickListener;
    public ProductViewHolder3(@NonNull View itemView) {
        super(itemView);

        item_pic=itemView.findViewById(R.id.item_pic2);
        item_name=itemView.findViewById(R.id.item_name2);
        item_desc=itemView.findViewById(R.id.item_desc2);
        item_quant2=itemView.findViewById(R.id.item_quant2);
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
