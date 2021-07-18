package com.example.belipangan.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.belipangan.R;
import com.example.belipangan.model.Product;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.text.NumberFormat;
import java.util.Locale;

public class ProductDetailBuyerActivity extends AppCompatActivity {

    FirebaseAuth mAuth;
    FirebaseUser fUser;
    Intent intent;
    String nama, deskripsi, kategori, key, alamat, uid, noTelpon, namaToko;
    Uri imgUri;
    int harga, berat, pemesananMinimum, stok;
    DatabaseReference dbReference, dbReference2;
    StorageReference storageReference;
    ImageView ivProduk;
    TextView tvdesk, tvHarga, tvNama, tvKategori, tvMinPesanan, tvBerat, tvToko, tvStok, tvAlamat;
    Product product;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail_buyer);

        mAuth = FirebaseAuth.getInstance();
        fUser = mAuth.getCurrentUser();

        instansiasiView();
        intent = getIntent();
        getIntentData();

        dbReference = FirebaseDatabase.getInstance().getReference("Product").child(fUser.getUid()).child(key);
        storageReference = FirebaseStorage.getInstance().getReferenceFromUrl(imgUri.toString());

        getUser();

    }

    private void getUser() {
        dbReference2 = FirebaseDatabase.getInstance().getReference("Sellers").child(uid);

        dbReference2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                namaToko = dataSnapshot.child("nama").getValue().toString();
                setView();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void instansiasiView(){
        ivProduk = findViewById(R.id.ivDetailProduct);
        tvdesk = findViewById(R.id.tvDeskripsiProduct);
        tvHarga = findViewById(R.id.tvHargaProductDetail);
        tvNama = findViewById(R.id.tvNamaProductDetail);
        tvKategori = findViewById(R.id.tvKategori);
        tvMinPesanan = findViewById(R.id.tvPemesananMin);
        tvBerat = findViewById(R.id.tvBeratProduct);
        tvToko = findViewById(R.id.namaToko);
        tvStok = findViewById(R.id.tvStokProductDetail);
        tvAlamat = findViewById(R.id.tvAlamat);
    }

    private void getIntentData(){
        product = (Product)intent.getSerializableExtra("EXTRA_PRODUCT");
        nama = product.getNama();
        harga = product.getHarga();
        deskripsi = product.getDeskripsi();
        kategori = product.getKategori();
        imgUri = Uri.parse(product.getImgUri());
        key = intent.getStringExtra("EXTRA_KEY");
        alamat = product.getAlamat();
        uid = product.getuID();
        noTelpon = product.getNoTelpon();
        berat = product.getBerat();
        pemesananMinimum = product.getMinPemesanan();
        stok = product.getStok();
    }

    private void setView(){
        Locale localeID = new Locale("in", "ID");
        NumberFormat formatRupiah = NumberFormat.getCurrencyInstance(localeID);

        tvHarga.setText(formatRupiah.format((double) harga));
        tvNama.setText(nama);
        tvdesk.setText(deskripsi);
        tvKategori.setText(kategori);
        tvBerat.setText(String.valueOf(berat));
        tvMinPesanan.setText(String.valueOf(pemesananMinimum));
        tvToko.setText(namaToko);
        tvStok.setText("Stok " + String.valueOf(product.getStok()));
        tvAlamat.setText(alamat);

        Picasso.get()
                .load(imgUri)
                .placeholder(R.drawable.ic_image)
                .fit()
                .into(ivProduk);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        menu.setGroupVisible(R.id.product_menu_group, false);
        menu.setGroupVisible(R.id.edit_menu_product, false);
        menu.setGroupVisible(R.id.group_logout, false);

        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_utama, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.menuLogout:
                mAuth.signOut();
                Intent intent = new Intent(this, LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK + Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                break;

        }
        return super.onOptionsItemSelected(item);
    }

    public void actionBeli(View view) {
        Intent beli = new Intent(this, ProductOrderActivity.class);
        beli.putExtra("EXTRA_PRODUCT", product);
        beli.putExtra("EXTRA_KEY", key);
        startActivity(beli);
    }
}