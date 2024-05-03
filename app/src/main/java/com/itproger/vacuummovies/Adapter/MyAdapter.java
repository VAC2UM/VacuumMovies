package com.itproger.vacuummovies.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.itproger.vacuummovies.Film;
import com.itproger.vacuummovies.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class MyAdapter extends ArrayAdapter<Film> {

    public MyAdapter(Context context, ArrayList<Film> filmArrayList) {
        super(context, 0, filmArrayList);
    }


    @Override
    public View getView(int position, @NonNull View convertView, @NonNull ViewGroup parent) {
        View listItemView = convertView;
        if(listItemView == null){
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.grid_item, parent, false);
        }

        Film film = getItem(position);
        ImageView poster = listItemView.findViewById(R.id.gridImage);

        Picasso.get().load(film.getDataImage()).into(poster);

        listItemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO: Сделать переход на страницу с информацией о фильме
                Toast.makeText(getContext(), "Фильм: " + film.getName(), Toast.LENGTH_SHORT).show();
            }
        });
        return listItemView;
    }
}
