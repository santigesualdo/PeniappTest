package com.mobile.santige.nuevaaplicacion;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;
import java.util.Random;

public class ResultActivity extends AppCompatActivity {

    private ResultPersonasAdapter resPerAdapter;
    List<Persona> personas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        personas = PersonListActivity.listaPersonas;

        LinearLayout global_panel = new LinearLayout(this);
        global_panel.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
        global_panel.setGravity(Gravity.FILL);
        global_panel.setBackgroundColor(Color.YELLOW);

        updateView(personas);
    }

    public void onResume() {
        super.onResume();
        updateView(personas);
    }

    private void updateView(List<Persona> personas) {
        ListView list = new ListView(this);
        if (resPerAdapter != null) {
            list.setAdapter(resPerAdapter);
        } else {
            resPerAdapter = new ResultPersonasAdapter(this, personas);
        }

        setContentView(list);
    }

    private class ResultPersonasAdapter extends ArrayAdapter<Persona>{
        public ResultPersonasAdapter(Context context, List<Persona> personas){
            super(context,-1,-1, personas);
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            LinearLayout listLayout = new LinearLayout(ResultActivity.this);
            listLayout.setOrientation(LinearLayout.VERTICAL);
            listLayout.setId(5000);
            if (listLayout != null) {
                final Persona persona = super.getItem(position);

                final TextView listText = new TextView(ResultActivity.this);
                listText.setId(5001);

                listText.setText(persona.getNombre());
                listText.setPadding(0, 5, 0, 5);
                listText.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                listText.setTextSize(16);
                Random rnd = new Random();
                int color = Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));
                listLayout.setBackgroundColor(color);

                 /*if (persona.getGastos()==null){
                    listText.setText(persona.getNombre() + " no tuvo gastos. Debe poner: $" + PersonListActivity.montoPorPera);
                    LinearLayout.LayoutParams lpcButton = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.MATCH_PARENT);
                    listLayout.addView(listText, lpcButton);
                 }*/
            }
            return listLayout;
        }
    }
}
