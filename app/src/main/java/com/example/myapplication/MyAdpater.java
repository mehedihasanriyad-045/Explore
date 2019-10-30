package com.example.myapplication;

import android.content.Context;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.lang.annotation.Documented;
import java.util.List;

public class MyAdpater extends RecyclerView.Adapter<MyAdpater.MyViewHolder>
{

    private Context context;
    private List<PlacesDesc> placesDescList;
    private OnItemClickListener listener;

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

    public class MyViewHolder extends RecyclerView.ViewHolder  implements View.OnClickListener, View.OnCreateContextMenuListener, MenuItem.OnMenuItemClickListener {
        TextView placesname;
        ImageView imageView;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.place_images);
            placesname = itemView.findViewById(R.id.place_name);
            itemView.setOnClickListener(this);
            itemView.setOnCreateContextMenuListener(this);

        }

        @Override
        public void onClick(View v) {
            if(listener != null)
            {
                int position = getAdapterPosition();

                if(position !=  RecyclerView.NO_POSITION)
                {
                    listener.onItemClick(position);
                }
            }
        }

        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {

            menu.setHeaderTitle("Choose an action");
            MenuItem Delete = menu.add(menu.NONE,1,1,"Delete");

            Delete.setOnMenuItemClickListener(this);


        }

        @Override
        public boolean onMenuItemClick(MenuItem item) {

            int position = getAdapterPosition();

            if(position !=  RecyclerView.NO_POSITION)
            {
                switch (item.getItemId())
                {
                    case 1:
                        listener.DELETE(position);
                        return true;

                }
            }

            return false;
        }
    }

    public interface OnItemClickListener {

        void onItemClick(int position);

        void DELETE(int position);



    }

    public void setOnItemClickListener(OnItemClickListener listener){

        this.listener = listener;
    }


}
