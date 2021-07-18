package com.example.belipangan.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.belipangan.R;
import com.example.belipangan.model.Order;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.NumberFormat;
import java.util.Locale;

public class PendingOrderDetailActivity extends AppCompatActivity {

    TextView tvNamaProduk, tvNamaCus, tvHarga, tvQty, tvAlamat, tvIdOrder, tvStatus;
    Button btnApprove, btnCancel;
    FirebaseUser fUser;
    Order order;
    Intent intent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pending_order_detail);

        intent = getIntent();
        init();
        getData(intent);
        setView(order);

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


    public void approveAction(View view) {

        DatabaseReference db = FirebaseDatabase.getInstance().getReference("approvalOrders")
                .child(fUser.getUid());

        order.setStatus("Approve");

        db.child(order.getIdOrder()).setValue(order);

        DatabaseReference db2 = FirebaseDatabase.getInstance().getReference("unapprovalOrders")
                .child(fUser.getUid()).child(order.getIdOrder());

        db2.removeValue();

        Intent intent = new Intent(this, SellerPendingOrderActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);



    }

    public void cancelAction(View view) {
        DatabaseReference db = FirebaseDatabase.getInstance().getReference("unapprovalOrders")
                .child(fUser.getUid()).child(order.getIdOrder());

        db.removeValue();

        Intent intent = new Intent(this, SellerPendingOrderActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);

    }
}