package com.mobile.santige.nuevaaplicacion;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.*;
import android.util.Log;
import android.widget.Button;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends Activity implements View.OnClickListener {

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

        incializarPersonas();

        Intent intent = new Intent(this, PersonListActivity.class );
        startActivity(intent);
    }

    @Override
    public void onBackPressed(){
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    public static void incializarPersonas() {
        _listaPersonas = new ArrayList<Persona>();
        for (int i = 0 ; i< personasCount; i++){
            Persona p = new Persona();
            p.setNombre("Persona " + i );
            p.setListID(i);
            _listaPersonas.add(p);
        }
    }
}
