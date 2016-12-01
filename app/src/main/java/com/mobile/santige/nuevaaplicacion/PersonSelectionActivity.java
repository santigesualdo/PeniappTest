package com.mobile.santige.nuevaaplicacion;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class PersonSelectionActivity extends AppCompatActivity implements View.OnClickListener{

    TextView seekBarValue;
    public final static String COUNTPERSONS = "com.example.myfirstapp.MESSAGE";
    public static String personasCount;
    public static List<Persona> _listaPersonas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person_selection);

        Button aceptar = (Button) findViewById(R.id.button2);
        aceptar.setOnClickListener(this);

        SeekBar seekBar = (SeekBar) findViewById(R.id.seekBar);
        seekBarValue = (TextView) findViewById(R.id.seekbarvalue);

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener(){
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                seekBarValue.setText(String.valueOf(progress));
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });
    }

    @Override
    public void onClick(View v) {
        personasCount= seekBarValue.getText().toString();
        _listaPersonas = new ArrayList<Persona>();
        for (int i = 0 ; i< Integer.parseInt(personasCount); i++){
            Persona p = new Persona();
            p.setNombre("Persona " + i );
            p.setListID(i);
            _listaPersonas.add(p);
        }

        Intent intent = new Intent(this, PersonListActivity.class );
        startActivity(intent);
    }
}
