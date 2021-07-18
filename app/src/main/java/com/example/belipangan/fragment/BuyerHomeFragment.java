package com.example.belipangan.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.belipangan.R;
import com.example.belipangan.adapter.HomeBuyerAdapter;
import com.example.belipangan.model.Product;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.LinkedList;

public class BuyerHomeFragment extends Fragment {
    View rootView;
    RecyclerView rvHomeBuyer;
    LinkedList<Product> list;
    ArrayList<String> listKey;
    FirebaseDatabase database;
    FirebaseDatabase database2;
    DatabaseReference dbReference;
    DatabaseReference dbReference2;
    Product product;
    HomeBuyerAdapter homeBuyerAdapter;

    SearchView searchView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_buyer_home, container, false);

        searchView = rootView.findViewById(R.id.searchView);

        list = new LinkedList<>();
        listKey = new ArrayList<>();

        database = FirebaseDatabase.getInstance();
        database2 = FirebaseDatabase.getInstance();

        getListKey();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                homeBuyerAdapter.getFilter().filter(newText);
                return false;
            }
        });

        return rootView;
    }

    private void getListKey(){
        dbReference = database.getReference("Product");

        dbReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                list.clear();
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    String key = snapshot.getKey();
                    listKey.add(key);
                }

                if (listKey.size() > 0){
                    getListProduct();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    private void getListProduct(){
        list.clear();
        for (int i =0; i<listKey.size(); i++){
            dbReference2 = database2.getReference("Product").child(listKey.get(i));
            dbReference2.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                        Log.i("DEBUG", "Masuk data snapshot");

                        product = snapshot.getValue(Product.class);
                        product.setKey(snapshot.getKey());
                        Product prdct = product;
                        list.add(prdct);
                    }
                    rvHomeBuyer = rootView.findViewById(R.id.rvHomeBuyer);
                    homeBuyerAdapter = new HomeBuyerAdapter(rootView.getContext(), list);
                    rvHomeBuyer.setLayoutManager(new GridLayoutManager(rootView.getContext(), 2));
                    rvHomeBuyer.setAdapter(homeBuyerAdapter);


                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
    }

}