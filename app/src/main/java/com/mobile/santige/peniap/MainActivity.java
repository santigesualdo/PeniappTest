package com.mobile.santige.peniap;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.*;
import android.widget.Button;
import android.widget.TextView;


public class MainActivity extends Activity implements View.OnClickListener {

    String msg = "Android";
    private Integer personasCount=2;
    private GrupoPersonas grupoPersonas;

    public static Typeface gothamBold;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler(this));

        setContentView(R.layout.activity_main);

        TextView tv = (TextView) findViewById(R.id.textTitulo);
        gothamBold  = Typeface.createFromAsset(getAssets(), "Gotham-Bold.otf");
        tv.setTypeface(gothamBold);

        Button comenzar = (Button) findViewById(R.id.comenzar);

        comenzar.setOnClickListener(this);

        Button verPenias = (Button) findViewById(R.id.ver_peñas);
        verPenias.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(v.getContext(), VerPeniasActivity.class);
                startActivity(i);
            }
        });

        comenzar.setTypeface(gothamBold);
        verPenias.setTypeface(gothamBold);

        /*ShowcaseView.Builder builder_ = new ShowcaseView.Builder(this)
                .setTarget( new ViewTarget( findViewById(R.id.ver_peñas)) )
                //.setContentTitle("Bienvenidos a PEÑAPP!!")
                //.setContentText("Pulsa empezar para iniciar una peña nueva!")
                //.set
                .setStyle(R.style.CustomShowcaseTheme2)

                .hideOnTouchOutside();


        builder_.build();*/



    }

    @Override
    protected void onStart(){
        super.onStart();
    }

    @Override
    protected void onResume(){
        super.onResume();
    }

    @Override
    protected void onPause(){
        super.onPause();
    }

    @Override
    protected void onStop(){
        super.onStop();
    }

    @Override
    protected void onDestroy(){
        super.onResume();
    }

    @Override
    public void onClick(View v) {

        incializarPersonas();

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
            p.setNombre("Persona " + i );
            p.setListID(i);
            grupoPersonas.get_listaPersonas().add(p);
        }
    }
}
