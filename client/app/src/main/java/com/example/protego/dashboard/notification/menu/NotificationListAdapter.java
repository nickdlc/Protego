package com.example.protego.dashboard.notification.menu;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.protego.R;
import com.example.protego.web.schemas.firestore.Notification;

import java.util.ArrayList;

public class NotificationListAdapter extends RecyclerView.Adapter<NotificationListAdapter.ViewHolder> {
    private ArrayList<Notification> notifications;
    private RecyclerViewClickListener listener;

    public NotificationListAdapter(ArrayList<Notification> notifications, RecyclerViewClickListener listener) {
        this.notifications = notifications;
        this.listener = listener;
    }

    @NonNull
    @Override
    public NotificationListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Log.d("NotificationListAdapter", "onCreateViewHolder");
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_notifications, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull NotificationListAdapter.ViewHolder holder, int position) {
        Log.d("NotificationListAdapter", "onBindViewHolder");
        String notification = notifications.get(position).getMsg();
        holder.tvNotification.setText(notification);
    }

    @Override
    public int getItemCount() {
        return notifications.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView tvNotification;

        public ViewHolder(@NonNull View view) {
            super(view);
            Log.d("NotificationListAdapter.ViewHolder", "constructor");
            tvNotification = view.findViewById(R.id.tvNotification);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            try {
                listener.onClick(view, getAdapterPosition());
            } catch(Exception e) {
                Log.d("Adapter", "No Click");
            };
        }
    }

    public interface RecyclerViewClickListener {
        void onClick(View view, int position);
    }
}
