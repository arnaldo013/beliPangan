package com.example.belipangan.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.belipangan.activity.BuyerFinishOrderActivity;
import com.example.belipangan.activity.BuyerPendingOrderActivity;
import com.example.belipangan.activity.BuyerProgressOrderActivity;
import com.example.belipangan.activity.LoginActivity;
import com.example.belipangan.R;
import com.google.firebase.auth.FirebaseAuth;

public class BuyerOrderFragment extends Fragment implements View.OnClickListener {
    FirebaseAuth mAuth;
    Button btnPending, btnProgress, btnFinish;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mAuth = FirebaseAuth.getInstance();

        View view = inflater.inflate(R.layout.fragment_buyer_order, container, false);

        btnPending = view.findViewById(R.id.btnPendingOrder);
        btnProgress = view.findViewById(R.id.btnProgressOrder);
        btnFinish = view.findViewById(R.id.btnFinishedOrder);

        btnPending.setOnClickListener(this);
        btnProgress.setOnClickListener(this);
        btnFinish.setOnClickListener(this);

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

    @Override
    public void onClick(View view) {
        Intent intent;
        switch (view.getId()){
            case R.id.btnPendingOrder:
                intent = new Intent(view.getContext(), BuyerPendingOrderActivity.class);
                startActivity(intent);
                break;
            case R.id.btnProgressOrder:
                intent = new Intent(view.getContext(), BuyerProgressOrderActivity.class);
                startActivity(intent);
                break;
            case R.id.btnFinishedOrder:
                intent = new Intent(view.getContext(), BuyerFinishOrderActivity.class);
                startActivity(intent);
                break;
        }
    }
}