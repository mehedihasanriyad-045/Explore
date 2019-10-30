package com.example.myapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class MyAdpater extends RecyclerView.Adapter<MyAdpater.MyViewHolder>
{

    private Context context;
    private List<PlacesDesc> placesDescList;

    public MyAdpater(Context context, List<PlacesDesc> placesDescList) {
        this.context = context;
        this.placesDescList = placesDescList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.dhaka,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        PlacesDesc placesDesc = placesDescList.get(position);
        holder.placesname.setText(placesDesc.getImagename()+":");
        Picasso.get().load(placesDesc.getImageurl()).fit().centerCrop().into(holder.imageView);


    }

    @Override
    public int getItemCount() {
        return placesDescList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView placesname;
        ImageView imageView;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.place_images);
            placesname = itemView.findViewById(R.id.place_name);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int postion = getAdapterPosition();

                }
            });

        }
    }

}
