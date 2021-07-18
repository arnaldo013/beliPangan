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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.belipangan.R;
import com.example.belipangan.model.Seller;
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

import java.time.LocalDateTime;
import java.util.UUID;

public class EditSellerAccount extends AppCompatActivity implements View.OnClickListener {
    Intent intent;
    EditText etNama, etAlamat, etTelpon;
    Seller seller;
    TextView fotoSeller;
    Intent upImage;
    Uri imageUri;
    String imgUri;
    FirebaseStorage storage;
    StorageReference storageReference;
    FirebaseUser user;
    ImageView ivProfil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_seller_account);

        intent = getIntent();
        init();
        getData(intent);


        user = FirebaseAuth.getInstance().getCurrentUser();
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        fotoSeller.setOnClickListener(this);

        if(seller.getImgUri() != null){
            Log.d("VALID", seller.getImgUri());

            Uri u = Uri.parse(seller.getImgUri());
            Glide.with(EditSellerAccount.this)
                    .load(u)
                    .circleCrop()
                    .into(ivProfil);
        }
    }

    public void simpanEditSeller(View view) {
        boolean isValid = validasi();

        if (isValid) {
            Log.d("VALID", etNama.getText().toString().trim());
            FirebaseUser fUser = FirebaseAuth.getInstance().getCurrentUser();
            DatabaseReference db = FirebaseDatabase.getInstance().getReference("Sellers").child(fUser.getUid());

            if(imgUri != null){
                db.child("imgUri").setValue(imgUri);
            }
            db.child("nama").setValue(etNama.getText().toString().trim());
            db.child("alamat").setValue(etAlamat.getText().toString().trim());
            db.child("noTelpon").setValue(etTelpon.getText().toString().trim());

            Intent intent = new Intent(this, MainActivitySeller.class);
            startActivity(intent);
        }

    }

    private void init() {
        etNama = findViewById(R.id.etNamaSeller);
        etTelpon = findViewById(R.id.etTelpSeller);
        fotoSeller = findViewById(R.id.tvFotoSeller);
        ivProfil = findViewById(R.id.ivEditSeller);
        etAlamat = findViewById(R.id.etAlamatSeller);
    }

    private void getData(Intent intent) {
        seller = (Seller) intent.getSerializableExtra("EXTRA_SELLER");

        etNama.setText(seller.getNama());
        etTelpon.setText(seller.getNoTelpon());
        etAlamat.setText(seller.getAlamat());

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.tvFotoSeller:
                upImage = new Intent();
                upImage.setType("image/*");
                upImage.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(upImage, 1);
                break;
        }
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

                                Glide.with(EditSellerAccount.this)
                                        .load(imageUri)
                                        .circleCrop()
                                        .into(ivProfil);

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

    private boolean validasi(){
        String nama = etNama.getText().toString().trim();
        String telpon = etTelpon.getText().toString().trim();
        String alamat = etAlamat.getText().toString().trim();

        if(nama.length() == 0){
            etNama.setError("Nama tidak boleh kosong");
            return false;
        }else  if(telpon.length() == 0){
            etTelpon.setError("No. Telpon tidak boleh kosong");
            return false;
        }else if(alamat.length() == 0){
            etAlamat.setError("Alamat tidak boleh kosong");
            return  false;
        }else{
            return  true;
        }
    }
}