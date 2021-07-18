package com.example.belipangan.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.belipangan.R;
import com.example.belipangan.activity.AddProductRequestActivity;
import com.example.belipangan.model.Product;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

public class RequestAddDetailActivity extends AppCompatActivity {
    TextView tvNama, tvHarga, tvMin, tvStok, tvDes, tvBerat, tvKategori;
    ImageView ivProduct;
    Intent intent;
    Product product;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_add_detail);

        intent = getIntent();
        getIntentData(intent);

        init();


    }

    private void getIntentData(Intent intent) {
        product = (Product) intent.getSerializableExtra("EXTRA_PRODUCT");
    }

    private void init() {
        tvNama = findViewById(R.id.namaProduct);
        tvBerat = findViewById(R.id.berat);
        tvHarga = findViewById(R.id.hargaProducts);
        tvMin = findViewById(R.id.minPemesanan);
        tvStok = findViewById(R.id.stok);
        tvDes = findViewById(R.id.deskripsiProduct);
        tvKategori = findViewById(R.id.kategori);
        ivProduct = findViewById(R.id.ivProduk);

        tvNama.setText(product.getNama());
        tvBerat.setText(String.valueOf(product.getBerat()));
        tvHarga.setText(String.valueOf(product.getHarga()));
        tvMin.setText(String.valueOf(product.getMinPemesanan()));
        tvDes.setText(product.getDeskripsi());
        tvStok.setText(String.valueOf(product.getStok()));
        tvKategori.setText(product.getKategori());

        Picasso.get()
                .load(Uri.parse(product.getImgUri()))
                .placeholder(R.drawable.ic_image)
                .fit()
                .into(ivProduct);
    }

    public void verifikasi(View view) {
        DatabaseReference db =FirebaseDatabase.getInstance().getReference("Product").child(product.getuID());

        db.child(product.getKey()).setValue(product);

        DatabaseReference db2 = FirebaseDatabase.getInstance().getReference("UnverifiedProducts")
                .child(product.getKey());

        db2.removeValue();

        Intent intent = new Intent(this, AddProductRequestActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);

    }

    public void cancel(View view) {
        DatabaseReference db = FirebaseDatabase.getInstance().getReference("UnverifiedProducts")
                .child(product.getKey());

        db.removeValue();

        Intent intent = new Intent(this, AddProductRequestActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }
}