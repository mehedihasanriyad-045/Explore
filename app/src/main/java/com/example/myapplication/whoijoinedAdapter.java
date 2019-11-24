package com.example.myapplication;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import java.util.List;

public class whoijoinedAdapter extends ArrayAdapter<Join> {

    private Activity context;
    private List<Join> joinList;
    private TextView name,email;


    public whoijoinedAdapter(Activity context, List<Join> joinList) {
        super(context, R.layout.contact_list, joinList);
        this.context = context;
        this.joinList = joinList;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {

        LayoutInflater layoutInflater = context.getLayoutInflater();
        View view = layoutInflater.inflate(R.layout.contact_list,null, true);

        Join who = joinList.get(position);
        name = view.findViewById(R.id.conName);
        email = view.findViewById(R.id.conNumber);

        name.setText(who.getName());
        email.setText(who.getEmail());

        return view;
    }
}
