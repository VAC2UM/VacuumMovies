package com.itproger.vacuummovies.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.itproger.vacuummovies.Film;
import com.itproger.vacuummovies.R;

import java.util.ArrayList;

public class MyAdapter extends BaseAdapter {

    private ArrayList<Film> filmList;
    private Context context;
    LayoutInflater layoutInflater;

    public MyAdapter(ArrayList<Film> filmList, Context context) {
        this.filmList = filmList;
        this.context = context;
    }

    @Override
    public int getCount() {
        return filmList.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if (layoutInflater == null) {
            layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }
        if (view == null) {
            view = layoutInflater.inflate(R.layout.grid_item, null);
        }
        ImageView gridImage = view.findViewById(R.id.gridImage);

        Glide.with(context).load(filmList.get(i).getDataImage()).into(gridImage);
        return view;
    }
}
