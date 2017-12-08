package com.hci.project.cerebro;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Malavika Ramprasad on 12/5/2017.
 */

public class TutorsAdapter extends BaseAdapter {
    Activity context;
    String title[];
    String description[];

    public TutorsAdapter(Activity activity, String[] title, String[] description) {
        this.context = activity;
        this.title = title;
        this.description = description;
    }

    @Override
    public int getCount() {
        return title.length;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    private class ViewHolder {
        TextView txtViewTitle;
        TextView txtViewDescription;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        LayoutInflater inflater = context.getLayoutInflater();

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.list_item, null);
            holder = new ViewHolder();
            holder.txtViewTitle = (TextView) convertView.findViewById(R.id.row_element_name);
            holder.txtViewDescription = (TextView) convertView.findViewById(R.id.row_element_rating);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.txtViewTitle.setText(title[position]);
        holder.txtViewDescription.setText(description[position]);

        return convertView;
    }
}
