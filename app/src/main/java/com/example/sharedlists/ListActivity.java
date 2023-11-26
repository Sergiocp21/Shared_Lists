package com.example.sharedlists;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class ListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        // Get intent data
        Intent intent = getIntent();
        String title = intent.getStringExtra("TITLE");
        String content = intent.getStringExtra("CONTENT");

        // Show title
        TextView titleTextView = findViewById(R.id.titleTextView);
        titleTextView.setText(title);

        // Show content
        TextView contentTextView = findViewById(R.id.contentTextView);
        contentTextView.setText(content);
    }
}