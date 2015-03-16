package com.example.earthquake;


import android.content.Context;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;


public class EqAdapter extends BaseAdapter {

    List<GeoJson.Seisme> seismes;
    LayoutInflater inflater;

    public EqAdapter(){}

    private class ViewHolder {
        TextView eqTitle;
        TextView eqTime;
        TextView eqMag;
        ImageView magImage;
    }

    public EqAdapter(Context context, List<GeoJson.Seisme> objects) {
        inflater = LayoutInflater.from(context);
        this.seismes = objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.activity_eq_adapter, null);
            holder.eqTitle = (TextView) convertView.findViewById(R.id.eqTitle);
            holder.eqTime = (TextView) convertView.findViewById(R.id.eqTime);
            holder.eqMag = (TextView) convertView.findViewById(R.id.eqMag);
            holder.magImage = (ImageView) convertView.findViewById(R.id.magImg);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        GeoJson.Seisme seisme = seismes.get(position);
        holder.eqTitle.setText(seisme.getTitle());
        holder.eqTime.setText(seisme.getStandardTime());
        holder.eqMag.setText(seisme.getMagString());
        holder.magImage.setImageResource(seisme.getMagImg());
        return convertView;
    }

    @Override
    public int getCount() {
        return seismes.size();
    }

    @Override
    public GeoJson.Seisme getItem(int position) {
        return seismes.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
}

