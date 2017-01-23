package com.mobile.santige.nuevaaplicacion;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import java.util.List;

public class PersonSelectionActivity extends Activity implements View.OnClickListener{

    TextView seekBarValue;
    private Integer countPersons;
    private GrupoPersonas grupoPersonas;
    private List<Persona> listaPersonas;
    private double montoTotal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person_selection);

        Bundle bundleObject = getIntent().getExtras();
        grupoPersonas = (GrupoPersonas) bundleObject.getSerializable("array_personas");
        listaPersonas = grupoPersonas.get_listaPersonas();
        montoTotal = getIntent().getExtras().getDouble("monto_total");

        Button aceptar = (Button) findViewById(R.id.button2);
        aceptar.setOnClickListener(this);

        SeekBar seekBar = (SeekBar) findViewById(R.id.seekBar);

        seekBarValue = (TextView) findViewById(R.id.seekbarvalue);
        seekBarValue.setText(Integer.toString(listaPersonas.size()));
        seekBarValue.setTextSize(TypedValue.COMPLEX_UNIT_SP,16);

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener(){
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                seekBarValue.setText(String.valueOf(progress));
                if (progress<=listaPersonas.size()){
                    seekBarValue.setText(Integer.toString(listaPersonas.size()));
                }
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });
    }

    @Override
    public void onClick(View v) {
        try{
            countPersons = Integer.parseInt(seekBarValue.getText().toString());
        }catch (Exception e){
            countPersons = listaPersonas.size();
        }

        Intent intent = new Intent(this, ResultActivity.class );

        intent.putExtra("monto_total", montoTotal);
        intent.putExtra("count_persons", countPersons);

        Bundle bundleObject = new Bundle();

        GrupoPersonas groupToSend = new GrupoPersonas();
        groupToSend.set_listaPersonas(listaPersonas);
        bundleObject.putSerializable("array_personas", groupToSend);
        intent.putExtras(bundleObject);

        startActivity(intent);
    }

    @Override
    public void onBackPressed(){
        Intent intent = new Intent(this, PersonListActivity.class);
        Bundle b = new Bundle();

        b.putSerializable("array_personas", grupoPersonas);
        intent.putExtras(b);

        startActivity(intent);
    }
}
