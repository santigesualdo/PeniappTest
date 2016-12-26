package com.mobile.santige.nuevaaplicacion;

import android.content.Intent;
import android.os.Bundle;
import android.view.*;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Button;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    String msg = "Android";
    public static Integer personasCount=2;
    public static List<Persona> _listaPersonas;

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

        _listaPersonas = new ArrayList<Persona>();
        for (int i = 0 ; i< personasCount; i++){
            Persona p = new Persona();
            p.setNombre("Persona " + i );
            p.setListID(i);
            _listaPersonas.add(p);
        }

        Intent intent = new Intent(this, PersonListActivity.class );
        startActivity(intent);
    }
}
