package com.mobile.santige.nuevaaplicacion;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import static com.mobile.santige.nuevaaplicacion.MainActivity.personasCount;

public class PersonSelectionActivity extends AppCompatActivity implements View.OnClickListener{

    TextView seekBarValue;
    public static String COUNTPERSONS;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person_selection);

        Button aceptar = (Button) findViewById(R.id.button2);
        aceptar.setOnClickListener(this);

        SeekBar seekBar = (SeekBar) findViewById(R.id.seekBar);
        seekBarValue = (TextView) findViewById(R.id.seekbarvalue);
        seekBarValue.setTextSize(16);

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
        try{
            COUNTPERSONS = seekBarValue.getText().toString();
        }catch (Exception e){
            COUNTPERSONS = "0";
        }

        Intent intent = new Intent(this, ResultActivity.class );
        startActivity(intent);
    }
}
