package com.hci.project.cerebro;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Tejas Sarma on 12/3/2017.
 */

public class TutorListViewAdapter extends BaseAdapter {

    Activity context;
    List<SubmitQuestion> questions;

    public TutorListViewAdapter(Activity context, List<SubmitQuestion> questions) {
        super();
        this.context = context;
        this.questions = questions;
    }

    @Override
    public int getCount() {
        return questions.size();
    }

    @Override
    public Object getItem(int i) {
        return questions.get(i);
    }

//    public int getItemObjectId(int i) { return questions.get(i).getId(); }

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

        holder.txtViewTitle.setText(questions.get(position).getDescription());
        holder.txtViewDescription.setText(questions.get(position).getDescription());

        return convertView;
    }
}
