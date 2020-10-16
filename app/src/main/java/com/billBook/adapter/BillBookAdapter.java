package com.billBook.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.billBook.R;
import com.billBook.databinding.ItemBillBookBinding;
import com.billBook.model.BillBookModel;
import com.bumptech.glide.Glide;

import java.util.List;

public class BillBookAdapter extends RecyclerView.Adapter<BillBookAdapter.BillBookViewHolder> {

    private Context mContext;
    private List<BillBookModel> alBillBook;

    public BillBookAdapter(Context mContext, List<BillBookModel> alBillBook) {
        this.mContext = mContext;
        this.alBillBook = alBillBook;
    }

    @NonNull
    @Override
    public BillBookViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_bill_book, parent, false);
        return new BillBookViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BillBookViewHolder holder, int position) {
        BillBookModel model = alBillBook.get(position);

        holder.binding.tvProductName.setText(model.getTrackName());

        Glide.with(mContext).load(model.getArtworkUrl100())
                .error(R.drawable.images)
                .placeholder(R.drawable.images)
                .into(holder.binding.ivProductImage);
    }

    @Override
    public int getItemCount() {
        return alBillBook.size();
    }

    class BillBookViewHolder extends RecyclerView.ViewHolder {
        ItemBillBookBinding binding;

        BillBookViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = DataBindingUtil.bind(itemView);
        }
    }
}