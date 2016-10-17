package com.mobile.santige.nuevaaplicacion;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import static java.lang.System.in;

public class PersonDetail_Activity extends AppCompatActivity implements View.OnClickListener {

    Persona detailedPerson;
    List<Gasto> gastos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person_detail);
        Intent i = getIntent();
        detailedPerson = (Persona)i.getSerializableExtra("person");
        gastos = detailedPerson.getGastos();

        EditText e = (EditText)findViewById(R.id.editText);
        e.setText(detailedPerson.getNombre());

        Button volver = (Button) findViewById(R.id.butVolver);
        volver.setOnClickListener(this);

        /*if (gastos!= null && gastos.size()>0) {
            LinearLayout personGastos = (LinearLayout) findViewById(R.id.personGastosLinearLayout);
            personGastos.setLayoutParams(new AbsListView.LayoutParams(AbsListView.LayoutParams.WRAP_CONTENT, AbsListView.LayoutParams.WRAP_CONTENT));

            for( Gasto g : gastos){

            }
        }*/
    }

    @Override
    public void onClick(View v) {
        Intent returnIntent = new Intent(this, PersonListActivity.class);
        EditText edit = (EditText) findViewById(R.id.editText);
        if (edit.getText().toString() != null && !edit.getText().toString().equals("")){
            detailedPerson.setNombre(edit.getText().toString());
        }
        returnIntent.putExtra("editedPerson", detailedPerson);
        returnIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(returnIntent);
    }

    private class MyAdapter extends ArrayAdapter<Gasto> {
        public MyAdapter(Context context, Gasto[] gastos) {
            super(context, -1, -1, gastos);
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            LinearLayout listLayout = new LinearLayout(PersonDetail_Activity.this);
            listLayout.setLayoutParams(new AbsListView.LayoutParams(AbsListView.LayoutParams.WRAP_CONTENT, AbsListView.LayoutParams.WRAP_CONTENT));
            listLayout.setId(6001);

            final TextView listText = new TextView(PersonDetail_Activity.this);
            listText.setId(6002);
            listLayout.addView(listText);
            final Gasto gasto = super.getItem(position);
            listText.setText("$ " + gasto.getMonto() + " en " + gasto.getDescripcion() );

            listLayout.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    if (event.getAction() ==MotionEvent.ACTION_DOWN) {
                        viewGastoActivity(v, gasto);
                    }
                    return true;
                }
            });

            return listLayout;
        }
    }

    public void viewGastoActivity(View view, Gasto g){
        Intent intent = new Intent(this, PersonDetail_Activity.class );
        intent.putExtra("gasto" , g );
        startActivity(intent);
    }

}
