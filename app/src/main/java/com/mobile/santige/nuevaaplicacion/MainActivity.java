package com.mobile.santige.nuevaaplicacion;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.*;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Button;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    String msg = "Android";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button comenzar = (Button) findViewById(R.id.comenzar);
        comenzar.setOnClickListener(this);
        Log.d(msg,"The OnCreate() event. ");
    }

    @Override
    protected void onStart(){
        super.onStart();
        Log.d(msg,"The OnStart() event");
    }

    @Override
    protected void onResume(){
        super.onResume();
        Log.d(msg, "The OnResume() event");
    }

    @Override
    protected void onPause(){
        super.onPause();
        Log.d(msg, "The onPause() event");
    }

    @Override
    protected void onStop(){
        super.onStop();
        Log.d(msg, "The onStop() event");
    }

    @Override
    protected void onDestroy(){
        super.onResume();
        Log.d(msg, "The onDestroy() event");
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(this, PersonSelectionActivity.class );
        startActivity(intent);
    }
}
