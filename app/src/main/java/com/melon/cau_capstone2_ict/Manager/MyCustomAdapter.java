package com.melon.cau_capstone2_ict.Manager;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.melon.cau_capstone2_ict.R;

public class MyCustomAdapter extends ArrayAdapter<String> {
    Context context;

    public MyCustomAdapter(Context context, int textViewResourceId, String[] objects) {
        super(context, textViewResourceId, objects);
        this.context = context;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }

    public View getCustomView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View row = inflater.inflate(R.layout.color_row, parent, false);
        ImageView label = (ImageView) row.findViewById(R.id.image_color);

        switch (position) {
            case 0:
                label.setImageResource(R.drawable.black);
                break;
            case 1:
                label.setImageResource(R.drawable.gray);
                break;
            case 2:
                label.setImageResource(R.drawable.white);
                break;
            case 3:
                label.setImageResource(R.drawable.blue);
                break;
            case 4:
                label.setImageResource(R.drawable.l_blue);
                break;
            case 5:
                label.setImageResource(R.drawable.green);
                break;
            case 6:
                label.setImageResource(R.drawable.l_green);
                break;
            case 7:
                label.setImageResource(R.drawable.red);
                break;
            case 8:
                label.setImageResource(R.drawable.pink);
                break;
            case 9:
                label.setImageResource(R.drawable.orange);
                break;
            case 10:
                label.setImageResource(R.drawable.yellow);
                break;
            case 11:
                label.setImageResource(R.drawable.purple);
                break;
        }
        return row;
    }
}