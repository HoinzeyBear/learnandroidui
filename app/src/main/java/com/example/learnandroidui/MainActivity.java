package com.example.learnandroidui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.learnandroidui.animation.AnimationActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
    }

    public void toKotlinCustomComponents(View view) {
        Intent intent = new Intent(this, KotlinCCActivity.class);
        startActivity(intent);
    }

    public void toAnimations(View view) {
        Intent intent = new Intent(this, AnimationActivity.class);
        startActivity(intent);
    }
}
