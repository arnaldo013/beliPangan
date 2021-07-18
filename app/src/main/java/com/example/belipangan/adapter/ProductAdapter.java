package com.example.belipangan.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.belipangan.activity.ProductDetailActivity;
import com.example.belipangan.R;
import com.example.belipangan.model.Product;
import com.squareup.picasso.Picasso;

import java.text.NumberFormat;
import java.util.LinkedList;
import java.util.Locale;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ViewProduct>{
    LayoutInflater iAdapter;
    LinkedList<Product> listProduct;

    public ProductAdapter(Context context, LinkedList<Product> list){
        iAdapter = LayoutInflater.from(context);
        listProduct = list;
    }

    @NonNull
    @Override
    public ProductAdapter.ViewProduct onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = iAdapter.inflate(R.layout.item_product_grid, parent, false);
        return new ViewProduct(view, this);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductAdapter.ViewProduct holder, int position) {
        Product product = new Product();
        product = listProduct.get(position);

        String nama = product.getNama();
        String harga = String.valueOf(product.getHarga());
        String deskripsi = product.getDeskripsi();
        Uri imgUri = Uri.parse(product.getImgUri());

        Locale localeID = new Locale("in", "ID");
        NumberFormat formatRupiah = NumberFormat.getCurrencyInstance(localeID);

        holder.tvNama.setText(nama);
//        holder.tvDeskrispi.setText(deskripsi);
        holder.tvHarga.setText(formatRupiah.format(Integer.parseInt(harga)));

        Picasso.get()
                .load(imgUri)
                .placeholder(R.drawable.ic_image)
                .fit()
                .into(holder.ivProduct);

    }

    @Override
    public int getItemCount() {
        return listProduct.size();
    }

    public class ViewProduct extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView tvNama, tvHarga;
//        tvDeskrispi;
        ImageView ivProduct;
        ProductAdapter productAdapter;

        public ViewProduct(@NonNull View itemView, ProductAdapter adapter) {
            super(itemView);
            tvNama = itemView.findViewById(R.id.tvNamaProduct);
            tvHarga = itemView.findViewById(R.id.tvHargaProduct);
//            tvDeskrispi = itemView.findViewById(R.id.tvDeskripsiProduct);
            ivProduct = itemView.findViewById(R.id.ivProduct);
            productAdapter = adapter;

            ivProduct.setOnClickListener(this);
        }


        @Override
        public void onClick(View view) {
            int posisi = getLayoutPosition();

            Product prdct = listProduct.get(posisi);

            Intent toProductDetail = new Intent(view.getContext(), ProductDetailActivity.class);
            toProductDetail.putExtra("EXTRA_KEY", listProduct.get(posisi).getKey());
            toProductDetail.putExtra("EXTRA_PRODUCT", prdct);

            view.getContext().startActivity(toProductDetail);

        }
    }


}
