package com.mobile.santige.peniap;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.*;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

public class MainActivity extends Activity implements View.OnClickListener {

    String msg = "Android";
    private Integer personasCount=2;
    private GrupoPersonas grupoPersonas;

    public static Typeface gothamBold;

    SharedPreferences prefs = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        prefs = getSharedPreferences("com.mobile.santige.peniap", MODE_PRIVATE);

        Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler(this));

        this.requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.activity_main);



        TextView tv = (TextView) findViewById(R.id.textTitulo);
        gothamBold  = Typeface.createFromAsset(getAssets(), "Gotham-Bold.otf");
        tv.setTypeface(gothamBold);

        Button comenzar = (Button) findViewById(R.id.comenzar);
        Button comousar = (Button) findViewById(R.id.comousar);

        comenzar.setOnClickListener(this);

        Button verPenias = (Button) findViewById(R.id.ver_peñas);
        verPenias.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(v.getContext(), VerPeniasActivity.class);
                startActivity(i);
            }
        });

        comousar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                empezarSlides();
            }
        });

        incializarPersonas();

        comenzar.setTypeface(gothamBold);
        verPenias.setTypeface(gothamBold);
        comousar.setTypeface(gothamBold);

    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void empezarSlides() {
        Intent i = new Intent(this.getBaseContext(), SlidesActivity.class);

        Bundle b = new Bundle();
        b.putSerializable("array_personas", grupoPersonas);
        i.putExtras(b);
        i.putExtra("personasCount", personasCount);

        startActivity(i);

    }

    @Override
    public void onClick(View v) {
        empezarPenia();
    }

    public void empezarPenia(){

        Intent intent = new Intent(this, PersonListActivity.class );
        Bundle b = new Bundle();

        b.putSerializable("array_personas", grupoPersonas);
        intent.putExtras(b);
        intent.putExtra("personasCount", personasCount);
        startActivity(intent);
    }

    @Override
    public void onBackPressed(){
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    public void incializarPersonas() {

        grupoPersonas = new GrupoPersonas();
        for (int i = 0 ; i< personasCount; i++){
            Persona p = new Persona();
            p.setNombre(getString(R.string.nombreidentificador) +" "+ i );
            p.setListID(i);
            grupoPersonas.get_listaPersonas().add(p);
        }
    }

}
