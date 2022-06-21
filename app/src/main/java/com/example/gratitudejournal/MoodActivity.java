package com.example.gratitudejournal;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;

public class MoodActivity extends AppCompatActivity {

    // if mood != null: send an intent straight to compose (because they've already picked an emoji
    // otherwise do all the mood selection stuff

    // if navigating here from the onclick for the edit mood button
    // (which will be a button at the top of the compose activity),
    // set the mood to null before firing the intent

    float x1, x2, y1, y2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mood);
    }

    public boolean onTouchEvent(MotionEvent touchEvent){
        switch(touchEvent.getAction()){
            case MotionEvent.ACTION_DOWN:
                x1 = touchEvent.getX();
                y1 = touchEvent.getY();
                break;
            case MotionEvent.ACTION_UP:
                x2 = touchEvent.getX();
                y2 = touchEvent.getY();

                if(x1 > x2) {
                Intent i = new Intent(MoodActivity.this, ComposeActivity.class);
                startActivity(i);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                Log.i("swiped left", "it worked");

                }
            break;
        }
        return false;
    }

}