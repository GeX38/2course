package com.example.myapplication;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private TextView titleTextView, genreTextView, yearTextView, directorTextView, actorsTextView, ratingTextView;
    private Button randomMovieButton;
    private JSONArray moviesArray;
    private List<Integer> movieIndices;
    private int lastRandomMovieIndex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        titleTextView = findViewById(R.id.titleTextView);
        genreTextView = findViewById(R.id.genreTextView);
        yearTextView = findViewById(R.id.yearTextView);
        directorTextView = findViewById(R.id.directorTextView);
        actorsTextView = findViewById(R.id.actorsTextView);
        ratingTextView = findViewById(R.id.ratingTextView);
        randomMovieButton = findViewById(R.id.randomMovieButton);

        try {

            InputStream is = getResources().openRawResource(R.raw.movies);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();

            String json = new String(buffer, "UTF-8");
            JSONObject jsonObject = new JSONObject(json);
            moviesArray = jsonObject.getJSONArray("movies");
        } catch (Exception e) {
            e.printStackTrace();
        }


        movieIndices = new ArrayList<>();
        for (int i = 0; i < moviesArray.length(); i++) {
            movieIndices.add(i);
        }
        Collections.shuffle(movieIndices);

        randomMovieButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int randomIndex = generateRandomIndex();

                try {

                    JSONObject randomMovie = moviesArray.getJSONObject(randomIndex);
                    lastRandomMovieIndex++;

                    String title = randomMovie.getString("title");
                    String genre = randomMovie.getString("genre");
                    int year = randomMovie.getInt("year");
                    String director = randomMovie.getString("director");
                    JSONArray actorsArray = randomMovie.getJSONArray("actors");
                    double rating = randomMovie.getDouble("rating");


                    titleTextView.setText("Название: " + title);
                    genreTextView.setText("Жанр: " + genre);
                    yearTextView.setText("Год: " + year);
                    directorTextView.setText("Режиссер: " + director);


                    StringBuilder actorsStringBuilder = new StringBuilder("Актеры: ");
                    for (int i = 0; i < actorsArray.length(); i++) {
                        actorsStringBuilder.append(actorsArray.getString(i));
                        if (i < actorsArray.length() - 1) {
                            actorsStringBuilder.append(", ");
                        }
                    }
                    actorsTextView.setText(actorsStringBuilder.toString());

                    ratingTextView.setText("Рейтинг: " + rating);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private int generateRandomIndex() {
        int arrayLength = movieIndices.size();


        if (lastRandomMovieIndex >= arrayLength) {
            Collections.shuffle(movieIndices);
            lastRandomMovieIndex = 0;
        }

        int randomIndex = movieIndices.get(lastRandomMovieIndex);
        return randomIndex;
    }
}
