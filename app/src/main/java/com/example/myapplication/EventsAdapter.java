package com.example.myapplication;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseUser;
import com.squareup.picasso.Picasso;

import java.util.List;

public class EventsAdapter extends RecyclerView.Adapter<EventsAdapter.EventViewHolder>{


    private Context context;
    private List<EventImage> eventList;

    FirebaseUser firebaseUser;




    public EventsAdapter(Context context, List<EventImage> eventList) {
        this.context = context;
        this.eventList = eventList;
    }

    @NonNull
    @Override
    public EventViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.activity_event_sample, viewGroup, false);

        return new EventViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EventViewHolder myViewHolder, int position) {


        EventImage eventImage = eventList.get(position);
        //EventImage eventImage = eventList.get(position);
        myViewHolder.tourplace.setText(eventImage.getPlace());
        Picasso.get().load(eventImage.getImageName()).fit().centerCrop().into(myViewHolder.tour_photo);
        myViewHolder.duration.setText(eventImage.getDuration());
        myViewHolder.cost.setText(eventImage.getAmount());
        myViewHolder.date.setText("Starting : " + eventImage.getDate());
        myViewHolder.postTime.setText(eventImage.getPostTime());
        myViewHolder.name.setText(eventImage.name);

    }

    @Override
    public int getItemCount() {
        return eventList.size();
    }

    public class EventViewHolder extends RecyclerView.ViewHolder {
        ImageView tour_photo;
        TextView duration, date, cost, tourplace, postTime, name;

        public EventViewHolder(@NonNull View itemView) {
            super(itemView);

            tour_photo = itemView.findViewById(R.id.cardImageView);
            tourplace = itemView.findViewById(R.id.cardPlaceName);
            duration = itemView.findViewById(R.id.CardDuration);
            date = itemView.findViewById(R.id.cardStartingDate);
            cost = itemView.findViewById(R.id.cardTourCost);
            postTime = itemView.findViewById(R.id.postTime);
            name = itemView.findViewById(R.id.cardHostName);
        }
    }
}