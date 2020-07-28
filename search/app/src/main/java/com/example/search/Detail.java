package com.example.search;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

public class Detail extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        Intent intent=getIntent();
        String strtitle=intent.getStringExtra("title");
        String strlink=intent.getStringExtra("link");

        TextView title=findViewById(R.id.title3);
        title.setText(Html.fromHtml(strtitle));

        WebView web=findViewById(R.id.web);
        web.setWebViewClient(new Myewbview());
        web.loadUrl(strlink);
    }

    public class Myewbview extends WebViewClient{
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            return super.shouldOverrideUrlLoading(view, url);
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
        }
    }
}
