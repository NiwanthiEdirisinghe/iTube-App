package com.example.itubeapp;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PlayListActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private PlayListAdapter adapter;
    private List<PlayListItem> playlistItems;
    private DBHelper dbHelper;
    private SessionManager sessionManager;
    private ImageButton backButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_list);

        dbHelper = new DBHelper(this);
        sessionManager = new SessionManager(this);

        recyclerView = findViewById(R.id.playlist_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        playlistItems = new ArrayList<>();
        adapter = new PlayListAdapter(playlistItems);
        recyclerView.setAdapter(adapter);

        loadPlaylistItems();

        adapter.setOnItemClickListener(new PlayListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(String videoUrl) {
                String videoId = extractYouTubeId(videoUrl);

                if (!videoId.isEmpty()) {
                    Intent intent = new Intent(PlayListActivity.this, PlayerActivity.class);
                    intent.putExtra("video_id", videoId);
                    startActivity(intent);
                }
            }
        });

        backButton = findViewById(R.id.back_button);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PlayListActivity.this, HomeActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void loadPlaylistItems() {
        long userId = sessionManager.getUserId();
        Cursor cursor = dbHelper.getUserPlaylist(userId);

        playlistItems.clear();

        if (cursor != null && cursor.moveToFirst()) {
            do {
                int idColumnIndex = cursor.getColumnIndex("id");
                int urlColumnIndex = cursor.getColumnIndex("video_url");

                if (idColumnIndex != -1 && urlColumnIndex != -1) {
                    long id = cursor.getLong(idColumnIndex);
                    String url = cursor.getString(urlColumnIndex);

                    playlistItems.add(new PlayListItem(id, url));
                }
            } while (cursor.moveToNext());

            cursor.close();
        }

        adapter.notifyDataSetChanged();
    }

    private static String extractYouTubeId(String url) {
        String pattern = "(?<=watch\\?v=|youtu.be\\/|embed\\/)[^#\\&\\?\\n]*";
        Pattern compiledPattern = Pattern.compile(pattern);
        Matcher matcher = compiledPattern.matcher(url);

        if (matcher.find()) {
            return matcher.group();
        }
        return "";
    }

}