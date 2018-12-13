package com.android16_team.caro_project;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MessageAdapter extends ArrayAdapter<String> {

    private List<Boolean> modes = new ArrayList<>();

    public MessageAdapter(Context context, int resource, List<String> objects) {
        super(context, resource, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = ((Activity)getContext()).getLayoutInflater();
        View row = null;
        if(modes.get(position))
            row = inflater.inflate(R.layout.from_message, null);
        else
            row = inflater.inflate(R.layout.to_message, null);

        TextView message = row.findViewById(R.id.txtMessage);
        message.setText(getItem(position));
        return row;
    }

    public void add(String object, Boolean isFrom) {
        super.add(object);
        modes.add(isFrom);
    }
}
