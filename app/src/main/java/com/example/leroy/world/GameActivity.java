package com.example.leroy.world;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class GameActivity extends AppCompatActivity {
    private DatabaseReference mDatabase;
    private TextView nameTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        Intent intent = getIntent();

        nameTextView = (TextView) findViewById(R.id.nameTextView);
        nameTextView.setText(intent.getStringExtra("name"));

    }

    @Override
    protected void onStart() {
        mDatabase = FirebaseDatabase.getInstance().getReference();

        super.onStart();
    }
}
