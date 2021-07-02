package com.example.jewelrystore;

import android.content.Intent;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;


/**
 * A simple {@link Fragment} subclass.
 */
public class CategoryFragment extends Fragment {

    public CategoryFragment() {
        // Required empty public constructor
    }


    private CardView ring,earring,necklaces,bracelet;
    private CardView nose_pin,bangles,tiara,anklets;
    private ImageView img_ring,img_bracelet,img_earings,img_necklaces,img_nosepin,img_bangles,img_tiara,img_anklets;
    private int flag=0;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view=inflater.inflate(R.layout.fragment_category, container, false);
        ring=view.findViewById(R.id.ring_user);
        earring=view.findViewById(R.id.earring_user);
        necklaces=view.findViewById(R.id.neckleass_user);
        bracelet=view.findViewById(R.id.bracelet_user);
        nose_pin=view.findViewById(R.id.nose_rings_user);
        bangles=view.findViewById(R.id.bangles_user);
        tiara=view.findViewById(R.id.tiara_user);
        anklets=view.findViewById(R.id.anklets_user);

        img_anklets=view.findViewById(R.id.img_anklets_user);
        img_bangles=view.findViewById(R.id.img_bangles_user);
        img_bracelet=view.findViewById(R.id.img_bracelet_user);
        img_earings=view.findViewById(R.id.img_earring_user);
        img_necklaces=view.findViewById(R.id.img_necklaces_user);
        img_nosepin=view.findViewById(R.id.img_nosepin_user);
        img_ring=view.findViewById(R.id.img_ring_user);
        img_tiara=view.findViewById(R.id.image_tiara_user);

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
                Intent intent=new Intent(getActivity(),UserCategoryActivity.class);
                intent.putExtra("category","rings");
                startActivity(intent);
            }
        });
        bracelet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getActivity(),UserCategoryActivity.class);
                intent.putExtra("category","bracelet");
                startActivity(intent);
            }
        });
        earring.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getActivity(),UserCategoryActivity.class);
                intent.putExtra("category","earings");
                startActivity(intent);
            }
        });
        necklaces.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getActivity(),UserCategoryActivity.class);
                intent.putExtra("category","necklaces");
                startActivity(intent);
            }
        });
        nose_pin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getActivity(),UserCategoryActivity.class);
                intent.putExtra("category","nose_pins");
                startActivity(intent);
            }
        });
        anklets.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getActivity(),UserCategoryActivity.class);
                intent.putExtra("category","anklets");
                startActivity(intent);
            }
        });
        bangles.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getActivity(),UserCategoryActivity.class);
                intent.putExtra("category","bangles");
                startActivity(intent);
            }
        });
        tiara.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getActivity(),UserCategoryActivity.class);
                intent.putExtra("category","tiara");
                startActivity(intent);
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
}
