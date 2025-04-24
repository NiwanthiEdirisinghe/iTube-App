package com.example.itubeapp;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class PlayerActivity extends AppCompatActivity {
    private WebView webView;
    private ImageButton backButton;

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);

        String videoId = getIntent().getStringExtra("video_id");

        webView = findViewById(R.id.webView);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setDomStorageEnabled(true);
        webView.setWebChromeClient(new WebChromeClient());

        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if (url.startsWith("https://www.youtube.com") ||
                        url.startsWith("http://www.youtube.com") ||
                        url.startsWith("youtube.com") ||
                        url.startsWith("https://m.youtube.com")) {
                    return false; // Let the system handle YouTube links
                }
                view.loadUrl(url);
                return true;
            }
        });

        String embedUrl = "https://www.youtube.com/embed/" + videoId + "?autoplay=1";
        String html = "<!DOCTYPE html><html><head>" +
                "<meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">" +
                "<style>body { margin: 0; padding: 0; } " +
                "iframe { width: 100%; height: 100vh; border: none; }</style>" +
                "</head><body>" +
                "<iframe src=\"" + embedUrl + "\" frameborder=\"0\" " +
                "allow=\"accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture\" " +
                "allowfullscreen></iframe>" +
                "</body></html>";

        webView.loadDataWithBaseURL("https://www.youtube.com", html, "text/html", "utf-8", null);

        backButton = findViewById(R.id.back_button);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (webView.canGoBack()) {
            webView.goBack();
        } else {
            super.onBackPressed();
        }
    }
}