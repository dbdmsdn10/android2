package com.example.search;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

public class Book_detail extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_detail);
        getSupportActionBar().setTitle("도서내용");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);//뒤로가기버튼

        Intent intent=getIntent();
        String strtitle=intent.getStringExtra("title");
        String strprice=intent.getStringExtra("price");
        String strauthor=intent.getStringExtra("author");
        String strdescription=intent.getStringExtra("description");
        String strimage=intent.getStringExtra("image");
        String strpublisher=intent.getStringExtra("publisher");
        String strpubdate=intent.getStringExtra("pubdate");

        TextView subject=findViewById(R.id.subject);
        TextView author=findViewById(R.id.author);
        ImageView bookimg=findViewById(R.id.bookimg);
        TextView price=findViewById(R.id.price);
        TextView publisher=findViewById(R.id.publisher);
        TextView pubdate=findViewById(R.id.pubdate);
        TextView description=findViewById(R.id.description);
        int leng=strprice.length()-1;

        strpubdate= strpubdate.substring(0,4)+"/"+strpubdate.substring(4,6)+"/"+strpubdate.substring(6,8);

        subject.setText(Html.fromHtml(strtitle));
        author.setText(Html.fromHtml(strauthor));
        price.setText(Html.fromHtml(strprice)+"원");
        publisher.setText(Html.fromHtml(strpublisher));
        pubdate.setText(Html.fromHtml(strpubdate));
        description.setText(Html.fromHtml(strdescription));

        Picasso.with(Book_detail.this).load(strimage).into(bookimg);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId()==android.R.id.home){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
