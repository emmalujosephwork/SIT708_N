package com.example.tripplanner;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ChatListAdapter extends RecyclerView.Adapter<ChatListAdapter.ChatViewHolder> {

    private List<ChatListItem> chats;
    private OnChatClickListener listener;

    public interface OnChatClickListener {
        void onChatClick(String chatId);
    }

    public ChatListAdapter(List<ChatListItem> chats, OnChatClickListener listener) {
        this.chats = chats;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ChatViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(android.R.layout.simple_list_item_2, parent, false);
        return new ChatViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ChatViewHolder holder, int position) {
        ChatListItem chat = chats.get(position);
        holder.title.setText("Chat " + (position + 1));
        holder.subtitle.setText(chat.getLast_message());

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onChatClick(chat.getChat_id());
            }
        });
    }

    @Override
    public int getItemCount() {
        return chats.size();
    }

    static class ChatViewHolder extends RecyclerView.ViewHolder {
        TextView title;
        TextView subtitle;

        ChatViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(android.R.id.text1);
            subtitle = itemView.findViewById(android.R.id.text2);
        }
    }
}
