package com.example.itubeapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HomeActivity extends AppCompatActivity {
    private EditText urlEditText;
    private Button playButton, addToPlaylistButton, myPlaylistButton, signOutButton;;
    private DBHelper dbHelper;
    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        dbHelper = new DBHelper(this);
        sessionManager = new SessionManager(this);

        // Check if user is already logged in
        if (!sessionManager.isLoggedIn()) {
            startActivity(new Intent(HomeActivity.this, LoginActivity.class));
            finish();
            return;
        }

        urlEditText = findViewById(R.id.url_edit_text);
        playButton = findViewById(R.id.play_button);
        addToPlaylistButton = findViewById(R.id.add_to_playlist_button);
        myPlaylistButton = findViewById(R.id.my_playlist_button);
        signOutButton = findViewById(R.id.sign_out_button);

        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String youtubeUrl = urlEditText.getText().toString().trim();

                if (youtubeUrl.isEmpty()) {
                    Toast.makeText(HomeActivity.this, "Please enter a YouTube URL", Toast.LENGTH_SHORT).show();
                    return;
                }

                String videoId = extractYouTubeId(youtubeUrl);

                if (videoId.isEmpty()) {
                    Toast.makeText(HomeActivity.this, "Invalid YouTube URL", Toast.LENGTH_SHORT).show();
                    return;
                }

                Intent intent = new Intent(HomeActivity.this, PlayerActivity.class);
                intent.putExtra("video_id", videoId);
                startActivity(intent);
            }
        });

        addToPlaylistButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String youtubeUrl = urlEditText.getText().toString().trim();

                if (youtubeUrl.isEmpty()) {
                    Toast.makeText(HomeActivity.this, "Please enter a YouTube URL", Toast.LENGTH_SHORT).show();
                    return;
                }

                long userId = sessionManager.getUserId();
                long result = dbHelper.addVideoToPlaylist(userId, youtubeUrl);

                if (result != -1) {
                    Toast.makeText(HomeActivity.this, "Added to playlist", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(HomeActivity.this, "Failed to add to playlist", Toast.LENGTH_SHORT).show();
                }
            }
        });

        myPlaylistButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeActivity.this, PlayListActivity.class));
            }
        });

        signOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signOut();
            }
        });
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

    private void signOut() {
        sessionManager.logout();

        Intent intent = new Intent(HomeActivity.this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK); // This clears all activities and starts LoginActivity
        startActivity(intent);
        finish();
    }
}