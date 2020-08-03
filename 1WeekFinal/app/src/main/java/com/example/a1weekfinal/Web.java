package com.example.a1weekfinal;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

public class Web extends AppCompatActivity {
    ProgressBar prog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);
        Intent intent = getIntent();
        prog=findViewById(R.id.progress);
        String url = intent.getStringExtra("url");
        String title = intent.getStringExtra("title");
        WebView web = findViewById(R.id.web);
        getSupportActionBar().setTitle(title);

        web.setWebViewClient(new Myweb());
        web.loadUrl(url);


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }

    public class Myweb extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
            return super.shouldOverrideUrlLoading(view, request);
        }

        @Override
        public void onPageFinished(WebView view, String url) {//페이지 로딩 끝났을때
            super.onPageFinished(view, url);
            prog.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId())
        {
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}

