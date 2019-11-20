package com.example.myapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Calendar;
import java.util.List;

public class Event_Comment_Adapter extends RecyclerView.Adapter<Event_Comment_Adapter.EventCommentViewHolder> {

    private Context context;
    private List<eventcomment> commentList;

    public Event_Comment_Adapter(Context context, List<eventcomment> commentList) {
        this.context = context;
        this.commentList = commentList;
    }

    @NonNull
    @Override
    public EventCommentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View row = LayoutInflater.from(context).inflate(R.layout.event_comment_sample,parent,false);
        return new EventCommentViewHolder(row);
    }

    @Override
    public void onBindViewHolder(@NonNull EventCommentViewHolder holder, int position) {

        holder.userName.setText(commentList.get(position).getUser());
        holder.comment.setText(commentList.get(position).getComment());
        holder.date.setText(commentList.get(position).getTime());

    }

    @Override
    public int getItemCount() {
        return commentList.size();
    }

    public class EventCommentViewHolder extends RecyclerView.ViewHolder {

        TextView userName,comment,date;

        public EventCommentViewHolder(@NonNull View itemView) {

            super(itemView);

            userName = itemView.findViewById(R.id.event_comment_username);
            comment = itemView.findViewById(R.id.event_comment_content);
            date = itemView.findViewById(R.id.event_date);
        }
    }
}