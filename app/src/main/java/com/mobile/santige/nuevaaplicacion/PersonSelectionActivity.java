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

import static com.mobile.santige.nuevaaplicacion.MainActivity.personasCount;

public class PersonSelectionActivity extends Activity implements View.OnClickListener{

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
        seekBarValue.setText("2");
        seekBarValue.setTextSize(TypedValue.COMPLEX_UNIT_SP,16);

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener(){
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                seekBarValue.setText(String.valueOf(progress));
                if (progress<=2){
                    seekBarValue.setText("2");
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
            COUNTPERSONS = seekBarValue.getText().toString();
        }catch (Exception e){
            COUNTPERSONS = "2";
        }

        Intent intent = new Intent(this, ResultActivity.class );
        startActivity(intent);
    }

    @Override
    public void onBackPressed(){
        Intent intent = new Intent(this, PersonListActivity.class);
        startActivity(intent);
    }
}
