package com.example.belipangan.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.belipangan.R;
import com.example.belipangan.model.Order;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

public class BuyerProgressDetailActivity extends AppCompatActivity {
    TextView tvNamaProduk, tvNamaCus, tvHarga, tvQty, tvAlamat, tvIdOrder, tvStatus;
    Button btnApprove, btnCancel;
    FirebaseUser fUser;
    Order order;
    Intent intent;
    String stok;
    ArrayList<Integer> listStok;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buyer_progress_detail);

        intent = getIntent();
        init();
        getData(intent);

        DatabaseReference dbProduct = FirebaseDatabase.getInstance().getReference("Product")
                .child(order.getUidSeller()).child(order.getIdProduk());

         listStok = new ArrayList<>();

        dbProduct.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot != null){
                    stok = dataSnapshot.child("stok").getValue().toString();
                    initList(Integer.valueOf(stok));

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        setView(order);

    }

    private void initList(int stok) {
        listStok.add(0, stok);
        Log.d("list stok", String.valueOf(listStok.get(0)));
    }

    private void setView(Order order) {
        Locale localeID = new Locale("in", "ID");
        NumberFormat formatRupiah = NumberFormat.getCurrencyInstance(localeID);

        tvNamaProduk.setText(order.getNamaProduct());
        tvStatus.setText(order.getStatus());
        tvAlamat.setText(order.getAlamatTujuan());
        tvQty.setText(String.valueOf(order.getKuantitas()));
        tvHarga.setText(formatRupiah.format(order.getTotalHarga()));
        tvIdOrder.setText(order.getIdOrder());
        tvNamaCus.setText(order.getNamaPembeli());

    }

    private void getData(Intent intent) {
        order = (Order) intent.getSerializableExtra("EXTRA_ORDER");
    }

    private void init() {
        fUser = FirebaseAuth.getInstance().getCurrentUser();

        tvNamaProduk = findViewById(R.id.tvNamaProduk);
        tvNamaCus = findViewById(R.id.tvNamaCustomer);
        tvHarga = findViewById(R.id.tvHargaProduk);
        tvQty = findViewById(R.id.tvKuantitasProduk);
        tvAlamat = findViewById(R.id.tvAlamatCustomer);
        tvIdOrder = findViewById(R.id.tvIdOrder);
        tvStatus = findViewById(R.id.tvStatusProduk);
        btnApprove = findViewById(R.id.btnApprove);
        btnCancel = findViewById(R.id.btnCancel);
    }

    public void finishOrder(View view) {
        DatabaseReference db = FirebaseDatabase.getInstance().getReference("FinishOrders")
                .child(order.getUidSeller()).child(order.getIdOrder());

        order.setStatus("Finish");



        int stokSekarang = listStok.get(0) - order.getKuantitas();

        Log.d("stok sekarang", String.valueOf(stokSekarang));

        updateStok(stokSekarang);

        db.setValue(order);

        DatabaseReference db2 = FirebaseDatabase.getInstance().getReference("approvalOrders")
                .child(order.getUidSeller()).child(order.getIdOrder());

        db2.removeValue();

        Intent intent = new Intent (this, BuyerProgressOrderActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);



    }

    private void updateStok(int stokSekarang) {
        int stok = stokSekarang;
        DatabaseReference dbProduct = FirebaseDatabase.getInstance().getReference("Product")
                .child(order.getUidSeller()).child(order.getIdProduk()).child("stok");

        dbProduct.setValue(stok).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.d("SUKSES", "Stok berhasil diperbarui");
            }
        });


    }
}