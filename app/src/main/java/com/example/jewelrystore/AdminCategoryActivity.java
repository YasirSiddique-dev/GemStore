package com.example.jewelrystore;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

public class AdminCategoryActivity extends AppCompatActivity {
    private CardView ring,earring,necklaces,bracelet;
    private CardView nosepin,bangles,tiara,anklets;
    private ImageView img_ring,img_bracelet,img_earings,img_necklaces,img_nosepin,img_bangles,img_tiara,img_anklets;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_category);
        ring=findViewById(R.id.ring);
        earring=findViewById(R.id.earring);
        necklaces=findViewById(R.id.neckleass);
        bracelet=findViewById(R.id.bracelet);
        nosepin=findViewById(R.id.nose_rings);
        bangles=findViewById(R.id.bangles);
        tiara=findViewById(R.id.tiara);
        anklets=findViewById(R.id.anklets);

        img_anklets=findViewById(R.id.img_anklets);
        img_bangles=findViewById(R.id.img_bangles);
        img_bracelet=findViewById(R.id.img_bracelet);
        img_earings=findViewById(R.id.img_earring);
        img_necklaces=findViewById(R.id.img_necklaces);
        img_nosepin=findViewById(R.id.img_nosepin);
        img_ring=findViewById(R.id.img_ring);
        img_tiara=findViewById(R.id.image_tiara);

        Picasso.get().load(R.drawable.ring_img).into(img_ring);
        Picasso.get().load(R.drawable.anklet_img).into(img_anklets);
        Picasso.get().load(R.drawable.bangles_img).into(img_bangles);
        Picasso.get().load(R.drawable.bracelet_image).into(img_bracelet);
        Picasso.get().load(R.drawable.earring_img).into(img_earings);
        Picasso.get().load(R.drawable.necklace_img).into(img_necklaces);
        Picasso.get().load(R.drawable.nosepin_img).into(img_nosepin);
        Picasso.get().load(R.drawable.tiara_img).into(img_tiara);
        ring.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminCategoryActivity.this,AdminAddNewProdcutActivity.class);
                intent.putExtra("category","rings");
                startActivity(intent);
            }
        });
        earring.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminCategoryActivity.this,AdminAddNewProdcutActivity.class);
                intent.putExtra("category","earings");
                startActivity(intent);
            }
        });
        necklaces.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminCategoryActivity.this,AdminAddNewProdcutActivity.class);
                intent.putExtra("category","necklaces");
                startActivity(intent);
            }
        });
        bracelet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminCategoryActivity.this,AdminAddNewProdcutActivity.class);
                intent.putExtra("category","bracelet");
                startActivity(intent);
            }
        });
        nosepin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminCategoryActivity.this,AdminAddNewProdcutActivity.class);
                intent.putExtra("category","nose_pins");
                startActivity(intent);
            }
        });
        anklets.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminCategoryActivity.this,AdminAddNewProdcutActivity.class);
                intent.putExtra("category","anklets");
                startActivity(intent);
            }
        });
        bangles.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminCategoryActivity.this,AdminAddNewProdcutActivity.class);
                intent.putExtra("category","bangles");
                startActivity(intent);
            }
        });
        tiara.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminCategoryActivity.this,AdminAddNewProdcutActivity.class);
                intent.putExtra("category","tiara");
                startActivity(intent);
            }
        });
    }
}
