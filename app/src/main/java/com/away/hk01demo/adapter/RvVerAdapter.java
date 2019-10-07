package com.away.hk01demo.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.away.hk01demo.R;
import com.away.hk01demo.bean.entry.EntryBean;
import com.away.hk01demo.table.LookupTable;
import com.away.hk01demo.utils.ImageUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RvVerAdapter extends RecyclerView.Adapter<RvVerAdapter.VerViewHolder> {

    private ArrayList<EntryBean> data;
    private Context context;
    private ItemClickCallBack clickCallBack;

    private List<LookupTable> tables;

    public void setClickCallBack(ItemClickCallBack clickCallBack) {
        this.clickCallBack = clickCallBack;
    }

    public interface ItemClickCallBack {
        void onItemClick(int pos);
    }

    public RvVerAdapter(ArrayList<EntryBean> data, Context context) {
        this.data = data;
        this.context = context;
    }

    public void setData(ArrayList<EntryBean> data) {
        this.data = data;
        notifyDataSetChanged();
    }

    public void setData(List<LookupTable> lookupTables) {
        this.tables = lookupTables;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public VerViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new VerViewHolder(LayoutInflater.from(context).inflate(R.layout.item_ver, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull VerViewHolder verViewHolder, int i) {
        if (data.get(i).category == null) return;
        verViewHolder.tvDesc.setText("" + data.get(i).category.attributes.label);
        if (data.get(i).link == null || data.get(i).title == null) return;
        if ((i + 1) % 2 == 1) {
            //奇数
            ImageUtils.setRoundCornerImageView(context, data.get(i).link.get(1).attributes.href, R.drawable.icon_default, verViewHolder.iv);
        } else {
            ImageUtils.setRoundImageView(context, data.get(i).link.get(1).attributes.href, R.drawable.icon_default, verViewHolder.iv);
        }
        verViewHolder.tvTitle.setText("" + data.get(i).title.label);
        verViewHolder.tvNum.setText("" + (i + 1));

        if (tables == null || tables.size() == 0) return;
        verViewHolder.ratingBar.setRating((float) tables.get(i).averageUserRating);
        verViewHolder.tvCount.setText("(" + tables.get(i).getUserRatingCount() + ")");

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
