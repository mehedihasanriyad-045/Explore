package com.example.myapplication;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

public class contactAdapter extends ArrayAdapter<contacts> {

    private Activity context;
    private List<contacts> contactsList;
    private TextView name,phone;

    public contactAdapter(Activity context, List<contacts> contactsList) {
        super(context, R.layout.contact_list, contactsList);
        this.context = context;
        this.contactsList = contactsList;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {

        LayoutInflater layoutInflater = context.getLayoutInflater();
        View view = layoutInflater.inflate(R.layout.contact_list,null, true);

        contacts fontacts = contactsList.get(position);
        name = view.findViewById(R.id.conName);
        phone = view.findViewById(R.id.conNumber);

        name.setText(fontacts.getName());
        phone.setText(fontacts.getPhone());

        return view;
    }
}
