package com.example.belipangan.fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.belipangan.R;
import com.example.belipangan.activity.ChangePasswordActivity;
import com.example.belipangan.activity.EditBuyerAccount;
import com.example.belipangan.model.Buyer;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.LinkedList;

public class BuyerAccountFragment extends Fragment implements View.OnClickListener {
    TextView tvEmail, tvTelpon, tvAlamat, tvNama, editProfile, tvUbahPass;
    ImageView ivProfile;
    View view;
    FirebaseDatabase db;
    DatabaseReference dbReference;
    FirebaseUser fUser;
    LinkedList<Buyer> list;
    Buyer buyer;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_buyer_account, container, false);
        db = FirebaseDatabase.getInstance();
        list = new LinkedList<>();
        init();
        getUser();
        editProfile.setOnClickListener(this);
        tvUbahPass.setOnClickListener(this);
        return view;
    }

    private void setView(String nama, String email, String telpon) {
        tvNama.setText(nama);
        tvTelpon.setText(telpon);
        tvEmail.setText(email);

    }

    private void getUser() {
        fUser = FirebaseAuth.getInstance().getCurrentUser();

        dbReference = db.getReference("Buyers");

        dbReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot != null){
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                        Buyer buyer = snapshot.getValue(Buyer.class);
                        list.add(buyer);
                    }

                    for(int i=0; i<list.size(); i++){
                        if(list.get(i).getEmail().equalsIgnoreCase(fUser.getEmail())){
                            buyer = list.get(i);
                            setView(buyer.getNama(), buyer.getEmail(), buyer.getNoTelpon());

                            if(buyer.getImgUri() != null){
                                setProfilImage(buyer.getImgUri());
                            }
                        }
                    }


                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



    }

    private void setProfilImage(String imgUri) {
        Uri uri = Uri.parse(imgUri);
        Glide.with(view.getContext())
                .load(uri)
                .circleCrop()
                .into(ivProfile);
    }

    private void init(){
        tvEmail =  view.findViewById(R.id.buyer_email);
//        tvAlamat = view.findViewById(R.id.buyer_address);
        tvTelpon = view.findViewById(R.id.buyer_number);
        tvNama = view.findViewById(R.id.buyer_name);
        ivProfile = view.findViewById(R.id.buyer_image);
        editProfile = view.findViewById(R.id.editProfilBuyer);
        tvUbahPass = view.findViewById(R.id.tvUbahPass);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.editProfilBuyer:

                Intent intent = new Intent(view.getContext(), EditBuyerAccount.class);
                intent.putExtra("EXTRA_BUYER", buyer);
                view.getContext().startActivity(intent);
                break;
            case R.id.tvUbahPass:
//                Intent intents = new Intent(view.getContext(), ChangePasswordActivity.class);
//                view.getContext().startActivity(intents);
                FirebaseAuth auth = FirebaseAuth.getInstance();
                auth.sendPasswordResetEmail(fUser.getEmail());
                Toast.makeText(view.getContext(), "Link ganti password sudah dikirim, cek email anda", Toast.LENGTH_SHORT).show();
                break;
        }

    }
}