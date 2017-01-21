package com.example.aarjugoyal.feed_web;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.TextView;

public class DisplayContent extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_content);

        Intent intent = getIntent();
        String email_name = intent.getStringExtra(MainActivity.EMAILNAME);
        TextView textView = new TextView(this);
        textView.setTextSize(40);
        textView.setText(email_name);

        ViewGroup layout = (ViewGroup) findViewById(R.id.activity_display_content);
        layout.addView(textView);

    }
}
