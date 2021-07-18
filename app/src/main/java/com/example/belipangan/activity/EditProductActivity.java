package com.example.belipangan.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.belipangan.R;
import com.example.belipangan.model.Product;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class EditProductActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    String nama, deskripsi, kategori, key, uid;
    int harga, berat, stok, pemesananMinimum;
    Uri imgUri, imgUriBaru;
    String uriImage = "URI_IMAGE";
    Intent intent;
    Spinner spKategori;
    List<String> listKategori;
    boolean isValid;
    Product product;
    DatabaseReference dbReference;
    StorageReference storageReference;
    String fotoIsClick, simpanEdit;

    EditText etNama, etDeksripsi, etHarga, etBerat, etStok, etMinPemesanan;
    ImageView ivProduct;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_product);



        intent = getIntent();
        getIntentData(intent);

        storageReference = FirebaseStorage.getInstance().getReference();
        listKategori = new ArrayList<>();
        instansiasiView();

        addListKategori(listKategori);

        ArrayAdapter<CharSequence> adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, listKategori);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spKategori.setAdapter(adapter);
        spKategori.setOnItemSelectedListener(this);

        setViewData();
    }

    private void instansiasiView() {
        etNama = findViewById(R.id.etNamaProduct);
        etDeksripsi = findViewById(R.id.etDeskripsiProduct);
        etHarga = findViewById(R.id.etHargaProduct);
        etBerat = findViewById(R.id.etBerat);
        etStok = findViewById(R.id.etStok);
        etMinPemesanan = findViewById(R.id.etMinPemesanan);
        spKategori = findViewById(R.id.spinnerKategori);
        ivProduct = findViewById(R.id.ivProduk);
    }

    private void setViewData(){
        etNama.setText(nama);
        etDeksripsi.setText(deskripsi);
        etHarga.setText(String.valueOf(harga));
        etBerat.setText(String.valueOf(berat));
        etStok.setText(String.valueOf(stok));
        etMinPemesanan.setText(String.valueOf(pemesananMinimum));

        Picasso.get()
                .load(imgUri)
                .placeholder(R.drawable.ic_image)
                .fit()
                .into(ivProduct);
    }

    private void getIntentData(Intent intent){
        product = (Product)intent.getSerializableExtra("EXTRA_PRODUCT");
        nama = product.getNama();
        harga = product.getHarga();
        deskripsi = product.getDeskripsi();
        kategori = product.getKategori();
        imgUri = Uri.parse(product.getImgUri());
        key = intent.getStringExtra("EXTRA_KEY");
        uid = product.getuID();
        berat = product.getBerat();
        pemesananMinimum = product.getMinPemesanan();
        stok = product.getStok();
    }

    private boolean validasi(){
        nama = etNama.getText().toString().trim();
        deskripsi = etDeksripsi.getText().toString().trim();
        String hargas = etHarga.getText().toString().trim();
        String stoks = etStok.getText().toString().trim();
        String minPemesanans = etMinPemesanan.getText().toString().trim();
        String berats = etBerat.getText().toString().trim();

        if(nama.length() == 0){
            etNama.setError("Nama produk harus di isi!");
            return false;
        }
        else if(hargas.length() == 0){
            etHarga.setError("Harga produk harus di isi!");
            return false;
        }
        else if(deskripsi.length() == 0){
            etDeksripsi.setError("Deskripsi produk harus di isi!");
            return false;
        }else if (stoks.length() == 0){
            etStok.setError("Stok tidak boleh 0");
            return false;
        }else if (minPemesanans.length() == 0){
            etMinPemesanan.setError("Minimum pemesanan tidak boleh 0");
            return false;
        }else if (berats.length() == 0){
            etBerat.setError("Berat tidak boleh 0");
            return  false;
        }else {
            harga = Integer.parseInt(hargas);
            stok = Integer.parseInt(stoks);
            pemesananMinimum = Integer.parseInt(minPemesanans);
            berat = Integer.parseInt(berats);
            if (stok == 0) {
                etStok.setError("Stok tidak boleh 0");
                return false;
            } else if (pemesananMinimum == 0) {
                etMinPemesanan.setError("Minimum pemesanan tidak boleh 0");
                return false;
            } else if (stok < pemesananMinimum) {
                etStok.setError("Stok tidak boleh kurang dari minimum pemesanan");
                return false;
            } else if (berat == 0) {
                etBerat.setError("berat tidak boleh 0");
                return false;
            } else {
                return true;
            }
        }
    }

    private void addListKategori(List<String> list){
        list.add(0, kategori);
        list.add("Buah");
        list.add("Sayur");
        list.add("Makanan Pokok");
    }

    public void chooseImage(View view) {
        fotoIsClick = "diklik";
        Log.d("klik", fotoIsClick);
        Intent upImage = new Intent();
        upImage.setType("image/*");
        upImage.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(upImage, 1);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 1 && resultCode == RESULT_OK && data != null && data.getData() != null){
            imgUriBaru = data.getData();
            uriImage = imgUriBaru.toString() + LocalDateTime.now().toString();
            uploadImage();
        }
    }

    private void uploadImage() {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Uploading image...");
        progressDialog.show();

        String productId = UUID.randomUUID().toString();
        StorageReference riversRef = storageReference.child("images/" + uid).child(uriImage);

        riversRef.putFile(imgUriBaru)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        progressDialog.dismiss();
                        Snackbar.make(findViewById(android.R.id.content), "Image Uploaded", Snackbar.LENGTH_SHORT).show();
                        riversRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                uriImage = uri.toString();

                                Picasso.get()
                                        .load(imgUriBaru)
                                        .fit()
                                        .into(ivProduct);

                            }
                        });
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        progressDialog.dismiss();
                        Toast.makeText(getApplicationContext(), "Failed to upload", Toast.LENGTH_SHORT).show();
                    }
                }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                double precentage = (100 * snapshot.getBytesTransferred() / snapshot.getTotalByteCount());
                progressDialog.setMessage("Progress: " + (int) precentage + "%");

            }
        });
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        kategori = adapterView.getItemAtPosition(i).toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    public void simpanEdit(View view) {
        isValid = validasi();
        if(isValid){
//            DatabaseReference dbReference = FirebaseDatabase.getInstance().getReference("Product").child(uid).child(key);
            DatabaseReference dbReference = FirebaseDatabase.getInstance().getReference("UnverifiedEditProducts");
            boolean gambarBaru = cekGambarBaru();
            simpanEdit = "diklik";

            if(gambarBaru){
                product.setImgUri(uriImage);
//                dbReference.child("imgUri").setValue(uriImage);
//                StorageReference storageRef = FirebaseStorage.getInstance().getReferenceFromUrl(imgUri.toString());
//                storageRef.delete();
            }

            product.setNama(nama);
            product.setBerat(berat);
            product.setDeskripsi(deskripsi);
            product.setKategori(kategori);
            product.setHarga(harga);
            product.setMinPemesanan(pemesananMinimum);
            product.setStok(stok);

            dbReference.child(product.getKey()).setValue(product);



//            dbReference.child("berat").setValue(berat);
//            dbReference.child("nama").setValue(nama);
//            dbReference.child("deskripsi").setValue(deskripsi);
//            dbReference.child("kategori").setValue(kategori);
//            dbReference.child("harga").setValue(harga);
//            dbReference.child("berat").setValue(berat);
//            dbReference.child("minPemesanan").setValue(pemesananMinimum);

            Intent finish = new Intent (this, MainActivitySeller.class);
            finish.putExtra("EXTRA_PRODUCT", product);
            finish.putExtra("EXTRA_KEY", key);
            finish.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK + Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(finish);
        }
    }

    private boolean cekGambarBaru(){
        boolean bool;
        if(uriImage == "URI_IMAGE"){
            bool = false;
        }else{
            bool = true;
        }
        return bool;
    }

    @Override
    protected void onStop() {
        super.onStop();
        boolean cekGambar = cekGambarBaru();

        if(!cekGambar){
            if (fotoIsClick != "diklik"){
                EditProductActivity.this.finish();
            }
        }else if (cekGambar) {
            if (simpanEdit != "diklik") {
                StorageReference storageRef = FirebaseStorage.getInstance().getReferenceFromUrl(uriImage);
                storageRef.delete();
            }
        }
    }
}