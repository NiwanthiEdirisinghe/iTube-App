package com.example.itubeapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.itubeapp.PlayListItem;
import com.example.itubeapp.R;

import java.util.List;

public class PlayListAdapter extends RecyclerView.Adapter<PlayListAdapter.ViewHolder> {
    private List<PlayListItem> items;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(String videoUrl);
    }

    public PlayListAdapter(List<PlayListItem> items) {
        this.items = items;
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.play_list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        PlayListItem item = items.get(position);
        holder.urlTextView.setText(item.getUrl());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onItemClick(item.getUrl());
            }
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView urlTextView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            urlTextView = itemView.findViewById(R.id.url_text_view);
        }
    }
}