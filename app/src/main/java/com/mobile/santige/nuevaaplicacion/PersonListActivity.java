package com.mobile.santige.nuevaaplicacion;

import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class PersonListActivity extends AppCompatActivity {

    List<Persona> listaPersonas;
    private PersonaAdapter myAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person_list);

        Intent intent = getIntent();
        String countPersons = intent.getStringExtra(PersonSelectionActivity.COUNTPERSONS);

        int numPersons;

        try {
            numPersons = Integer.parseInt(countPersons);
        } catch (Exception e) {
            numPersons = 10;
        }

        listaPersonas = new ArrayList<Persona>();

        for (int i = 0; i < numPersons; i++) {
            Persona p = new Persona();
            p.setNombre("Persona " + i );
            p.setListID(i);
            if ( i == 1){
                List<Gasto> g = new ArrayList<Gasto>();
                g.add(new Gasto("Tu vieja", 250.0));
                g.add(new Gasto("Tu vieja2", 300.0));
                p.setGastos(g);
            }
            listaPersonas.add(p);
        }

        updateView(listaPersonas);

    }

    private void updateView(List<Persona> personas) {
        ListView list = new ListView(this);

        if (myAdapter!=null){
            list.setAdapter(myAdapter);
        }else{
            myAdapter = new PersonaAdapter(this,personas);
        }

        setContentView(list);
    }

    public void onResume() {
        super.onResume();
        Intent i = getIntent();

        Persona p = (Persona) getIntent().getSerializableExtra("editedPerson");
        if (p!=null) {
            listaPersonas.set(p.getListID(), p);
        }

        updateView(listaPersonas);
    }

    public void viewPersonActivity(View view, Persona p) {
        Intent intent = new Intent(this, PersonDetail_Activity.class);
        intent.putExtra("person", p);
        startActivity(intent);
    }

    private class PersonaAdapter extends ArrayAdapter<Persona> {
        public PersonaAdapter(Context context, List<Persona> personas) {
            super(context, -1, -1, personas);
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            LinearLayout listLayout = new LinearLayout(PersonListActivity.this);
            listLayout.setOrientation(LinearLayout.VERTICAL);
            listLayout.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            listLayout.setId(5000);

            /*LinearLayout listLayout = (LinearLayout) findViewById(R.id.listaPersonasLayout);
            listLayout.removeAllViews();*/

            final Persona persona = super.getItem(position);

            final TextView listText = new TextView(PersonListActivity.this);
            listText.setId(5001);

            listText.setText(persona.getNombre());
            listLayout.addView(listText);

            int _id = 5001;
            if (persona.getGastos()!=null){
                LinearLayout gastosLayout = new LinearLayout(PersonListActivity.this);
                gastosLayout.setOrientation(LinearLayout.VERTICAL);
                listLayout.setLayoutParams(new AbsListView.LayoutParams(AbsListView.LayoutParams.WRAP_CONTENT, AbsListView.LayoutParams.WRAP_CONTENT));
                _id++;
                listLayout.setId(_id);

                Double monto = 0.0;
                int cantidad = persona.getGastos().size();
                for ( Gasto g : persona.getGastos()){
                    monto+= g.getMonto();
                }

                final TextView listDescripGasto = new TextView(PersonListActivity.this);
                _id++;
                listDescripGasto.setId(_id);
                listDescripGasto.setTextColor(Color.BLUE);
                if (cantidad==1) {
                    listDescripGasto.setText( "(Compro " + cantidad + " cosa y gasto $ " + monto + ")"  );
                }else{
                    listDescripGasto.setText( "(Compro " + cantidad + " cosas y gasto $ " + monto + ")"  );
                }

                gastosLayout.addView(listDescripGasto, ViewGroup.LayoutParams.MATCH_PARENT);

                listLayout.addView(gastosLayout);
            }

            listLayout.setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View v) {
                  viewPersonActivity(v, persona);
              }
            });

            return listLayout;
        }


    }
}
