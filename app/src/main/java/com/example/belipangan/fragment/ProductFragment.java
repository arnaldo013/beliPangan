package com.example.belipangan.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.example.belipangan.activity.AddProductActivity;
import com.example.belipangan.activity.LoginActivity;
import com.example.belipangan.R;
import com.example.belipangan.adapter.ProductAdapter;
import com.example.belipangan.model.Product;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.LinkedList;

public class ProductFragment extends Fragment {
    ProductAdapter adapter;
    RecyclerView rvProduk;
    LinkedList<Product> list;
    FirebaseAuth mAuth;
    FirebaseUser user;
    Product product;

    FirebaseDatabase database;
    DatabaseReference databaseReference;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_product, container, false);
        setHasOptionsMenu(true);
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();

        list = new LinkedList<>();


        database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference("Product").child(user.getUid());

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                list.clear();
                if(dataSnapshot != null){
                    for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                        product = snapshot.getValue(Product.class);
                        product.setKey(snapshot.getKey());
                        Product prdct = product;
                        list.add(prdct);

                    }

                    rvProduk = rootView.findViewById(R.id.rvProduct);
                    adapter = new ProductAdapter(rootView.getContext(), list);
                    rvProduk.setLayoutManager(new GridLayoutManager(rootView.getContext(), 2));
                    rvProduk.setAdapter(adapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        return rootView;
    }

    @Override
    public void onPrepareOptionsMenu(@NonNull Menu menu) {
        menu.setGroupVisible(R.id.edit_menu_product, false);
        super.onPrepareOptionsMenu(menu);

    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.menu_utama, menu);
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
                Intent addProduct = new Intent(getContext(), AddProductActivity.class);
                startActivity(addProduct);
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}