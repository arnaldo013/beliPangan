package com.example.belipangan.fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.belipangan.activity.EditBuyerAccount;
import com.example.belipangan.activity.EditSellerAccount;
import com.example.belipangan.activity.LoginActivity;
import com.example.belipangan.R;
import com.example.belipangan.model.Buyer;
import com.example.belipangan.model.Seller;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.LinkedList;

public class HomeFragment extends Fragment implements View.OnClickListener {
    FirebaseAuth mAuth;
    TextView tvEmail, tvTelpon, tvAlamat, tvNama, editProfile, tvUbahPass;
    ImageView ivProfile;
    View view;
    FirebaseDatabase db;
    DatabaseReference dbReference;
    FirebaseUser fUser;
    LinkedList<Seller> list;
    Seller seller;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        mAuth = FirebaseAuth.getInstance();

        view = inflater.inflate(R.layout.fragment_home, container, false);
        db = FirebaseDatabase.getInstance();
        list = new LinkedList<>();
        init();
        getUser();
        editProfile.setOnClickListener(this);
        tvUbahPass.setOnClickListener(this);
        return view;
    }

    @Override
    public void onPrepareOptionsMenu(@NonNull Menu menu) {
        menu.setGroupVisible(R.id.product_menu_group, false);
        menu.setGroupVisible(R.id.edit_menu_product, false);

        super.onPrepareOptionsMenu(menu);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.menu_utama, menu);
        menu.setGroupVisible(R.id.product_menu_group, false);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.menuLogout:
                mAuth.signOut();
                Intent intent = new Intent(getContext(), LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK + Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                break;
            case R.id.menuAdd:
                Toast.makeText(getContext(), "add ditekan", Toast.LENGTH_SHORT).show();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setView(String nama, String email, String telpon, String alamat) {
        tvNama.setText(nama);
        tvTelpon.setText(telpon);
        tvEmail.setText(email);
        tvAlamat.setText(alamat);

    }

    private void getUser() {
        fUser = FirebaseAuth.getInstance().getCurrentUser();

        dbReference = db.getReference("Sellers");

        dbReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot != null){
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                        Seller seller = snapshot.getValue(Seller.class);
                        list.add(seller);
                    }

                    for(int i=0; i<list.size(); i++){
                        if(list.get(i).getEmail().equalsIgnoreCase(fUser.getEmail())){
                            seller = list.get(i);
                            setView(seller.getNama(), seller.getEmail(), seller.getNoTelpon(), seller.getAlamat());

                            if (seller.getImgUri() != null) {
                                setProfilImage(seller.getImgUri());
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
        tvEmail =  view.findViewById(R.id.seller_email);
        tvAlamat = view.findViewById(R.id.seller_address);
        tvTelpon = view.findViewById(R.id.seller_number);
        tvNama = view.findViewById(R.id.seller_name);
        ivProfile = view.findViewById(R.id.seller_image);
        editProfile = view.findViewById(R.id.etProfilSeller);
        tvUbahPass = view.findViewById(R.id.tvUbahPass);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.etProfilSeller:

                Intent intent = new Intent(view.getContext(), EditSellerAccount.class);
                intent.putExtra("EXTRA_SELLER", seller);
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