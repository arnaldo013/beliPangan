package com.example.belipangan.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.belipangan.activity.BuyerProgressDetailActivity;
import com.example.belipangan.R;
import com.example.belipangan.model.Order;

import java.util.LinkedList;

public class BuyerProgressOrderAdapter extends RecyclerView.Adapter<BuyerProgressOrderAdapter.PendingOrder> {
    private LinkedList<Order> list;
    private LayoutInflater iAdapter;

    public BuyerProgressOrderAdapter(Context context, LinkedList<Order> list){
        this.list = list;
        this.iAdapter = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public PendingOrder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = iAdapter.inflate(R.layout.adapter_buyer_progress_order, parent, false);
        return new PendingOrder(view, this);
    }

    @Override
    public void onBindViewHolder(@NonNull PendingOrder holder, int position) {
        Order order = list.get(position);
        String namaProduk = order.getNamaProduct();
        int harga = order.getTotalHarga();
        String status = order.getStatus();

        holder.tvNama.setText(namaProduk);
        holder.tvHarga.setText(String.valueOf(harga));
        holder.tvStatus.setText(status);

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class PendingOrder extends RecyclerView.ViewHolder implements View.OnClickListener {
        BuyerProgressOrderAdapter adapter;
        TextView tvNama, tvHarga, tvStatus;
        CardView cv;
//        ImageView ivProduct;

        public PendingOrder(@NonNull View itemView, BuyerProgressOrderAdapter adapter) {
            super(itemView);
            this.adapter = adapter;

            tvNama = itemView.findViewById(R.id.tvNamaProduct);
            tvHarga = itemView.findViewById(R.id.tvTotalHarga);
            tvStatus = itemView.findViewById(R.id.tvStatusProduct);
            cv = itemView.findViewById(R.id.cvPending);

            cv.setOnClickListener(this);
//            ivProduct = itemView.findViewById(R.id.ivProductOrder);
        }

        @Override
        public void onClick(View view) {
            Intent intent = new Intent(view.getContext(), BuyerProgressDetailActivity.class);
            intent.putExtra("EXTRA_ORDER", list.get(getLayoutPosition()));
            view.getContext().startActivity(intent);
        }
    }
}
