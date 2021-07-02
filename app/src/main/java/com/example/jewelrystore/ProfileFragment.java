package com.example.jewelrystore;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.Handler;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jewelrystore.Models.Users;
import com.example.jewelrystore.Prevalent.Prevalent;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;
import io.paperdb.Paper;


/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileFragment extends Fragment {

    public ProfileFragment() {
        // Required empty public constructor
    }
    TextView name,email,phone;
    CircleImageView profile;
    Button btn_update,btn_logout;
    private int flag=0;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view=inflater.inflate(R.layout.fragment_profile, container, false);
        Paper.init(getActivity());
        name=view.findViewById(R.id.name);
        email=view.findViewById(R.id.email);
        profile=view.findViewById(R.id.profile_image);
        phone=view.findViewById(R.id.phone);
        btn_update=view.findViewById(R.id.update_profile);
        btn_logout=view.findViewById(R.id.logout);
        name.setText(Prevalent.currentonlineUsers.getFirst_Name()+" "+Prevalent.currentonlineUsers.getLast_Name());
        email.setText(Prevalent.currentonlineUsers.getEmail());
        phone.setText(Prevalent.currentonlineUsers.getPhone());

        DatabaseReference UserRef= FirebaseDatabase.getInstance().getReference().child("Users");
        {
            UserRef.child(Prevalent.currentonlineUsers.getPhone()).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists())
                    {
                        if (dataSnapshot.exists()){
                            if (dataSnapshot.hasChild("image"))
                            {
                                Users users=dataSnapshot.getValue(Users.class);
                                Picasso.get().load(users.getImage()).into(profile);

                            }
                            else
                            {
                                profile.setImageResource(R.drawable.profile_pic);

                            }

                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }


        btn_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Paper.book().destroy();
                Intent intent=new Intent(getActivity(),SignInActivity.class);
                startActivity(intent);
            }
        });
        btn_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getActivity(),UpdateProfileActivity.class);
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
