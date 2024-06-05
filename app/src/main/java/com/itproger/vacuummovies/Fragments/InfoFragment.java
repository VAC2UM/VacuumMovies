package com.itproger.vacuummovies.Fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.preference.PreferenceManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.itproger.vacuummovies.R;

public class InfoFragment extends Fragment {
    TextView creatorText;
    TextView usindDesc;
    TextView personalDesc;
    ImageView githubBtn;
    SharedPreferences sharedPref;
    boolean isSuperUser;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_info, container, false);

        creatorText = rootView.findViewById(R.id.creator);
        githubBtn = rootView.findViewById(R.id.github_btn);
        usindDesc = rootView.findViewById(R.id.usingDesc);
        personalDesc = rootView.findViewById(R.id.personalDesc);

        sharedPref = getContext().getSharedPreferences("LoginPrefs", Context.MODE_PRIVATE);

        isSuperUser = sharedPref.getBoolean("isSuperUser", false);

        githubBtn.setOnClickListener(view -> {
            String url = "https://github.com/VAC2UM/VacuumMovies";
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            startActivity(intent);
        });
        creatorText.setText("Создатель приложения:\nстудент РТУ МИРЭА Голованев Никита");
        usindDesc.setText("На главной странице вы увидите список фильмов, которые обозначены соответствующим постером. Сверху есть строка поиска фильма по его названию. Нажав на постер, вы перейдете на страницу с информацией о данном фильме.\n" +
                "В левом нижнем углу вы увидите значок YouTube, нажав на который вы сможете посмотреть трейлер к данному фильму.");

        if (isSuperUser) {
            personalDesc.setText("Вы являетесь админом.\nВам доступен новый функционал:\n1) На странице вашего профиля в правом нижнем углу есть кнопка для добавления фильма в БД\n2) Перейдя на страницу с фильмом в правом нижнем углу будет выпадающий список, нажав на который у вас будет выбор (редактировать или удалить фильм)");
        } else {
            personalDesc.setText("Вы обычный пользователь.\nВам доступен только вышеуказанный функционал.");
        }
        return rootView;
    }
}