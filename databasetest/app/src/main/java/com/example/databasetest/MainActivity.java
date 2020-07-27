package com.example.databasetest;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class MainActivity extends AppCompatActivity {
    private Connection con;
    private Statement st;
    private ResultSet rs;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        try {
            Class.forName("com.mysql.jdbc.Driver");
            System.out.println("1번오류");
            con = DriverManager.getConnection("jdbc:mysql://localhost:5555/tutorial?characterEncoding=UTF-8&serverTimezone=UTC", "geoseong", "1234");
            System.out.println("2번오류");
            st=con.createStatement();
            System.out.println("3번오류");
            rs=st.executeQuery("select*from memo");
            while(rs.next()) {
                String print="id= "+rs.getString("_id");
                print+=", wdate= "+rs.getString("wdate");
                print+=", subject= "+rs.getString("subject");
                print+=", subject= "+rs.getString("subject");
                print+=", content= "+rs.getString("content");
                System.out.println(print);
            }
        }catch (Exception e){
            System.out.println("연결오류"+e.getMessage());
        }
    }
}
