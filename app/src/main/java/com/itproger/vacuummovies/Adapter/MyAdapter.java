package com.itproger.vacuummovies.Adapter;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.itproger.vacuummovies.Constant;
import com.itproger.vacuummovies.Film;
import com.itproger.vacuummovies.Fragments.FilmDetailFragment;
import com.itproger.vacuummovies.R;

import java.util.ArrayList;
import java.util.List;


public class MyAdapter extends RecyclerView.Adapter<MyViewHolder> {
    private Context context;
    private List<Film> filmList;

    public MyAdapter(Context context, List<Film> filmList) {
        this.context = context;
        this.filmList = filmList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_item, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Glide.with(context).load(filmList.get(position).getDataImage()).into(holder.recImage);
        holder.recCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Получаем текущую позицию элемента
                int adapterPosition = holder.getAdapterPosition();

                // Проверяем, чтобы позиция была корректной
                if (adapterPosition != RecyclerView.NO_POSITION) {
                    // Получаем объект фильма по текущей позиции
                    Film film = filmList.get(adapterPosition);

                    // Создаем фрагмент и передаем в него данные о фильме
                    FilmDetailFragment filmDetailFragment = new FilmDetailFragment();
                    Bundle bundle = new Bundle();
                    bundle.putString(Constant.NAME, film.getName());
                    bundle.putString(Constant.DATAIMAGE, film.getDataImage());
                    filmDetailFragment.setArguments(bundle);

                    // Заменяем текущий фрагмент на FilmDetailFragment
                    ((AppCompatActivity) context).getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.frame_layout, filmDetailFragment)
                            .addToBackStack(null)
                            .commit();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return filmList != null ? filmList.size() : 0;
    }

    public void searhFilmList(ArrayList<Film> searchList) {
        filmList = searchList;
        notifyDataSetChanged();
    }
}

class MyViewHolder extends RecyclerView.ViewHolder {
    ImageView recImage;
    CardView recCard;


    public MyViewHolder(@NonNull View itemView) {
        super(itemView);

        recCard = itemView.findViewById(R.id.recCard);
        recImage = itemView.findViewById(R.id.recImage);
    }
}