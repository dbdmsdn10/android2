package com.example.ex12;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import org.w3c.dom.Text;

public class Website extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_website);

        Intent intent=getIntent();
        String strname=intent.getStringExtra("name");
        String strlink=intent.getStringExtra("link");
        TextView name=findViewById(R.id.sitename);
        name.setText(strname);
        WebView link=findViewById(R.id.siteweb);

        link.setWebViewClient(new Myweb());
        link.loadUrl(strlink);

    }

    public class Myweb extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
            return super.shouldOverrideUrlLoading(view, request);
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
        }
    }
}
