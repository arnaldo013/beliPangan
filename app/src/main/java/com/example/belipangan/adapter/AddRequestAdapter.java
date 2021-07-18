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
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.belipangan.R;
import com.example.belipangan.activity.RequestAddDetailActivity;
import com.example.belipangan.model.Product;
import com.squareup.picasso.Picasso;

import java.util.LinkedList;

public class AddRequestAdapter extends RecyclerView.Adapter<AddRequestAdapter.PendingOrder> {
    private LinkedList<Product> list;
    private LayoutInflater iAdapter;

    public AddRequestAdapter(Context context, LinkedList<Product> list){
        this.list = list;
        this.iAdapter = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public PendingOrder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = iAdapter.inflate(R.layout.adapter_add_request, parent, false);
        return new PendingOrder(view, this);
    }

    @Override
    public void onBindViewHolder(@NonNull PendingOrder holder, int position) {
        Product product = list.get(position);
        String namaProduk = product.getNama();
        int harga = product.getHarga();
        int berat = product.getBerat();

        holder.tvNama.setText(namaProduk);
        holder.tvHarga.setText(String.valueOf(harga));
        holder.tvBerat.setText(String.valueOf(berat));

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

    public class PendingOrder extends RecyclerView.ViewHolder implements View.OnClickListener {
        AddRequestAdapter adapter;
        TextView tvNama, tvHarga, tvBerat;
        CardView cv;
        ImageView ivProduct;

        public PendingOrder(@NonNull View itemView, AddRequestAdapter adapter) {
            super(itemView);
            this.adapter = adapter;

            tvNama = itemView.findViewById(R.id.tvNamaProduct);
            tvHarga = itemView.findViewById(R.id.tvTotalHarga);
            tvBerat = itemView.findViewById(R.id.tvBeratProduct);
            cv = itemView.findViewById(R.id.cvAddRequest);
            ivProduct = itemView.findViewById(R.id.ivProductOrder);

            cv.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            Intent intent = new Intent(view.getContext(), RequestAddDetailActivity.class);
            intent.putExtra("EXTRA_PRODUCT", list.get(getLayoutPosition()));
            view.getContext().startActivity(intent);
        }
    }
}
