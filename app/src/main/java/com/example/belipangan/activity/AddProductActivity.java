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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
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

public class AddProductActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    EditText etNama, etDeksripsi, etHarga, etNoTelpon, etLokasi, etBerat, etStok, etMinPemesanan;
    Spinner spKategori;
    Uri imageUri;
    ImageView ivProduct;
    FirebaseAuth mAuth;
    FirebaseUser user;
    FirebaseDatabase database;
    FirebaseStorage storage;
    StorageReference storageReference;
    DatabaseReference dbReference;
    Product product;
    List<String> listKategori;
    String simpanIsClick;

    String nama, deskripsi, noTelpon, lokasi, kategori, uID, imgUri, cekUri;
    int harga, stok, berat, minPemesanan;
    boolean isValid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);
        imgUri = "URI_IMAGE";
        cekUri = "cek";

        database = FirebaseDatabase.getInstance();
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();


        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        listKategori = new ArrayList<>();

        listKategori.add("Sayur");
        listKategori.add("Buah");
        listKategori.add("Makanan Pokok");

        instansiasiView();

        if(spKategori != null){
            spKategori.setOnItemSelectedListener(this);
        }

        ArrayAdapter<CharSequence> adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, listKategori);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        if(spKategori != null){
            spKategori.setAdapter(adapter);
        }
    }

    public void tambahProduct(View view) {
        isValid = validasi();
        if(isValid){
            simpanIsClick = "diklik";
            uID = user.getUid();
            dbReference = database.getReference("UnverifiedProducts");

            product = new Product(nama, deskripsi, noTelpon, lokasi, uID, kategori, harga, imgUri, stok, berat, minPemesanan);

            String productId = dbReference.push().getKey();
            dbReference.child(productId).setValue(product);

            Intent selesai = new Intent(AddProductActivity.this, MainActivitySeller.class);
            selesai.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK + Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(selesai);
        }
    }

    public void batal(View view) {
        Intent batal = new Intent(AddProductActivity.this, MainActivitySeller.class);
        startActivity(batal);
    }

    public boolean validasi(){
        nama = etNama.getText().toString().trim();
        String hargas = etHarga.getText().toString().trim();
        lokasi = etLokasi.getText().toString().trim();
        deskripsi = etDeksripsi.getText().toString().trim();
        noTelpon = etNoTelpon.getText().toString().trim();
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
        else if(lokasi.length() == 0){
            etLokasi.setError("Lokasi harus di isi!");
            return false;
        }
        else if(deskripsi.length() == 0){
            etDeksripsi.setError("Deskripsi produk harus di isi!");
            return false;
        }
        else if(noTelpon.length() == 0){
            etNoTelpon.setError("Nomor telpon harus di isi!");
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
        }else if(cekUri != imgUri){
            Log.d("cek_uri", cekUri);
            Log.d("img_uri", imgUri);
            Toast.makeText(this, "Harus upload foto", Toast.LENGTH_SHORT).show();
            return false;
        }else {
            harga = Integer.parseInt(hargas);
            stok = Integer.parseInt(stoks);
            minPemesanan = Integer.parseInt(minPemesanans);
            berat = Integer.parseInt(berats);
            if(stok == 0){
                etStok.setError("Stok tidak boleh 0");
                return false;
            }else if(minPemesanan == 0){
                etMinPemesanan.setError("Minimum pemesanan tidak boleh 0");
                return false;
            }else if(stok < minPemesanan){
                etStok.setError("Stok tidak boleh kurang dari minimum pemesanan");
                return false;
            }else if(berat == 0){
                etBerat.setError("berat tidak boleh 0");
                return false;
            }else{
                return  true;
            }

        }
    }

    public void chooseImage(View view) {
        Intent upImage = new Intent();
        upImage.setType("image/*");
        upImage.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(upImage, 1);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==1 && resultCode==RESULT_OK && data != null && data.getData() != null){
            imageUri = data.getData();
            imgUri = imageUri.toString()+ LocalDateTime.now().toString();
            Log.d("img_uri_time", imgUri);
            uploadImage();
        }
    }

    private void uploadImage() {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Uploading image...");
        progressDialog.show();

        String productId = UUID.randomUUID().toString();
        StorageReference riversRef = storageReference.child("images/" + user.getUid()).child(imgUri);

        riversRef.putFile(imageUri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        progressDialog.dismiss();
                        Snackbar.make(findViewById(android.R.id.content), "Image Uploaded", Snackbar.LENGTH_SHORT).show();
                        riversRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                imgUri = uri.toString();
                                cekUri = imgUri;

                                Picasso.get()
                                        .load(imageUri)
                                        .placeholder(R.drawable.ic_image)
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

    private void instansiasiView(){
        etNama = findViewById(R.id.etNamaProduct);
        etHarga = findViewById(R.id.etHargaProduct);
        etLokasi = findViewById(R.id.etAlamatProduct);
        etDeksripsi = findViewById(R.id.etDeskripsiProduct);
        spKategori = findViewById(R.id.spinnerKategori);
        etNoTelpon = findViewById(R.id.etNoTelpon);
        ivProduct = findViewById(R.id.ivProduk);
        etStok = findViewById(R.id.etStok);
        etBerat = findViewById(R.id.etBerat);
        etMinPemesanan = findViewById(R.id.etMinPemesanan);
    }

    private boolean cekGambarBaru(){
        boolean bool;
        if(imgUri.equals("URI_IMAGE")){
            bool = false;
        }else{
            bool = true;
        }
        return bool;
    }

    @Override
    protected void onStop() {
        boolean cekGambar = cekGambarBaru();
        if(cekGambar){
            if(simpanIsClick != "diklik"){
                StorageReference storRef = FirebaseStorage.getInstance().getReferenceFromUrl(imgUri);
                storRef.delete();
            }
        }
        super.onStop();

    }
}