package com.example.tripplanner;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class ChatAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final List<ChatMessage> data;

    public ChatAdapter(List<ChatMessage> data) {
        this.data = data;
    }

    @Override
    public int getItemViewType(int pos) {
        return data.get(pos).getType();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        int layout = viewType == ChatMessage.SENT
                ? R.layout.item_chat_sent
                : R.layout.item_chat_received;
        View v = LayoutInflater.from(parent.getContext()).inflate(layout, parent, false);
        return new RecyclerView.ViewHolder(v) {};
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int pos) {
        TextView tv = holder.itemView.findViewById(R.id.messageText);
        tv.setText(data.get(pos).getMessage());
    }

    @Override
    public int getItemCount() {
        return data.size();
    }
}
