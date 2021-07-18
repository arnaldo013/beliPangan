package com.example.belipangan.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.belipangan.activity.ProductDetailBuyerActivity;
import com.example.belipangan.R;
import com.example.belipangan.model.Product;
import com.squareup.picasso.Picasso;

import java.text.NumberFormat;
import java.util.Collection;
import java.util.LinkedList;
import java.util.Locale;

public class HomeBuyerAdapter extends RecyclerView.Adapter<HomeBuyerAdapter.HomeBuyer> implements Filterable{
    private LinkedList<Product> list;
    private LayoutInflater iAdapter;
    private LinkedList<Product> productListAll;

    public HomeBuyerAdapter(Context context, LinkedList<Product> list){
        this.iAdapter = LayoutInflater.from(context);
        this.list = list;
        this.productListAll = new LinkedList<>(list);
    }

    @NonNull
    @Override
    public HomeBuyerAdapter.HomeBuyer onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = iAdapter.inflate(R.layout.adapter_home_buyer, parent, false);
        return new HomeBuyer(view, this);
    }

    @Override
    public void onBindViewHolder(@NonNull HomeBuyerAdapter.HomeBuyer holder, int position) {
        Product product;
        product = list.get(position);

        String nama = product.getNama();
        int harga = product.getHarga();

        Locale localeID = new Locale("in", "ID");
        NumberFormat formatRupiah = NumberFormat.getCurrencyInstance(localeID);

        holder.tvNama.setText(nama);
        holder.tvHarga.setText(formatRupiah.format(harga));

        Picasso.get()
                .load(Uri.parse(product.getImgUri()))
                .placeholder(R.drawable.ic_image)
                .fit()
                .into(holder.ivProduct);

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public Filter getFilter() {
        return filter;
    }

    public Filter filter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            LinkedList<Product> filteredList = new LinkedList<>();

            if(charSequence.toString().isEmpty()){
                filteredList.addAll(productListAll);
            }else{
                for (Product prdct : productListAll){
                    if(prdct.getNama().toLowerCase().contains(charSequence.toString().toLowerCase())){
                        filteredList.add(prdct);
                    }
                }
            }

            FilterResults filterResults = new FilterResults();
            filterResults.values = filteredList;
            return filterResults;
        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            list.clear();
            list.addAll((Collection<? extends Product>) filterResults.values);
            notifyDataSetChanged();
        }
    };

    public class HomeBuyer extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView tvNama, tvHarga;
        ImageView ivProduct;
        HomeBuyerAdapter homeBuyerAdapter;


        public HomeBuyer(@NonNull View itemView, HomeBuyerAdapter adapter) {
            super(itemView);
            tvNama = itemView.findViewById(R.id.tvNamaProductBuyer);
            tvHarga = itemView.findViewById(R.id.tvHargaProductBuyer);
            ivProduct = itemView.findViewById(R.id.ivProductBuyer);
            homeBuyerAdapter = adapter;

            ivProduct.setOnClickListener(this);

        }

        @Override
        public void onClick(View view) {
            int posisi = getLayoutPosition();

            Product prdct = list.get(posisi);

            Intent toProductDetail = new Intent(view.getContext(), ProductDetailBuyerActivity.class);
            toProductDetail.putExtra("EXTRA_KEY", list.get(posisi).getKey());
            toProductDetail.putExtra("EXTRA_PRODUCT", prdct);

            view.getContext().startActivity(toProductDetail);
        }
    }
}
