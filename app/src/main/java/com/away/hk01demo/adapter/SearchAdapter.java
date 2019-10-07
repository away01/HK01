package com.away.hk01demo.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.away.hk01demo.R;
import com.away.hk01demo.table.SearchTable;
import com.away.hk01demo.utils.ImageUtils;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.VerViewHolder> {

    private ArrayList<SearchTable> data;
    private Context context;
    private ItemClickCallBack clickCallBack;

    public void setClickCallBack(ItemClickCallBack clickCallBack) {
        this.clickCallBack = clickCallBack;
    }

    public interface ItemClickCallBack {
        void onItemClick(int pos);
    }

    public SearchAdapter(ArrayList<SearchTable> data, Context context) {
        this.data = data;
        this.context = context;
    }

    public void setData(ArrayList<SearchTable> data) {
        this.data = data;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public VerViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new VerViewHolder(LayoutInflater.from(context).inflate(R.layout.item_ver, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull VerViewHolder verViewHolder, int i) {
        if (data.get(i).getCategory() == null) return;
        verViewHolder.tvDesc.setText("" + data.get(i).getCategory());
        if (TextUtils.isEmpty(data.get(i).getIconUrl()) || data.get(i).getIconUrl() == null) return;
        ImageUtils.setRoundCornerImageView(context, data.get(i).getIconUrl(), R.drawable.icon_default, verViewHolder.iv);

        verViewHolder.tvTitle.setText("" + data.get(i).getTitle());
        verViewHolder.tvNum.setVisibility(View.INVISIBLE);
        verViewHolder.ratingBar.setVisibility(View.INVISIBLE);
        verViewHolder.tvCount.setVisibility(View.INVISIBLE);

        verViewHolder.itemView.setOnClickListener(v -> {
            if (clickCallBack == null) return;
            clickCallBack.onItemClick(i);
        });
    }

    @Override
    public int getItemCount() {
        return data == null ? 0 : data.size();
    }

    static class VerViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_num)
        TextView tvNum;
        @BindView(R.id.iv)
        ImageView iv;
        @BindView(R.id.tv_title)
        TextView tvTitle;
        @BindView(R.id.tv_desc)
        TextView tvDesc;
        @BindView(R.id.ratingBar)
        RatingBar ratingBar;
        @BindView(R.id.tv_count)
        TextView tvCount;

        public VerViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

}
