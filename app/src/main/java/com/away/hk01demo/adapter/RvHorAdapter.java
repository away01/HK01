package com.away.hk01demo.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.away.hk01demo.R;
import com.away.hk01demo.bean.entry.EntryBean;
import com.away.hk01demo.common.OnItemClickListener;
import com.away.hk01demo.utils.ImageUtils;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RvHorAdapter extends RecyclerView.Adapter<RvHorAdapter.HorViewHolder> {

    private ArrayList<EntryBean> data;
    private Context context;

    public RvHorAdapter(ArrayList<EntryBean> data, Context context) {
        this.data = data;
        this.context = context;
    }

    public void setData(ArrayList<EntryBean> data) {
        this.data = data;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public HorViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new HorViewHolder(LayoutInflater.from(context).inflate(R.layout.item_hor, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull HorViewHolder horViewHolder, int i) {
        if (data.get(i).category == null) return;
        horViewHolder.tvDesc.setText("" + data.get(i).category.attributes.label);
        if (data.get(i).link == null || data.get(i).title == null) return;
        ImageUtils.setRoundCornerImageView(context, data.get(i).link.get(1).attributes.href
                , R.drawable.icon_default, horViewHolder.iv);
        horViewHolder.tvTitle.setText("" + data.get(i).title.label);

        horViewHolder.itemView.setOnClickListener(v -> {
            if (listener == null) return;
            listener.onItemClick(horViewHolder.itemView, i);
        });
    }

    @Override
    public int getItemCount() {
        return data == null ? 0 : data.size();
    }

    static class HorViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.iv)
        ImageView iv;
        @BindView(R.id.tv_title)
        TextView tvTitle;
        @BindView(R.id.tv_desc)
        TextView tvDesc;

        public HorViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    private OnItemClickListener listener;

    public void setListener(OnItemClickListener listener) {
        this.listener = listener;
    }
}
