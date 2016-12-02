package com.mobile.santige.nuevaaplicacion;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
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

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import static com.mobile.santige.nuevaaplicacion.R.string.Gastos;
import static java.lang.System.in;

public class PersonDetail_Activity extends AppCompatActivity  {

    Persona detailedPerson;
    final Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person_detail);
        Intent i = getIntent();
        detailedPerson = (Persona)i.getSerializableExtra("person");

        final EditText e = (EditText)findViewById(R.id.editText);
        e.setText(detailedPerson.getNombre());
        e.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                e.setText("");
            }
        });


        // Boton volver al formulario anterior.
        final Button volver = (Button) findViewById(R.id.butVolver);
        volver.setText("Ok");
        volver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent returnIntent=new Intent( volver.getContext() , PersonListActivity.class);
                EditText edit = (EditText) findViewById(R.id.editText);
                if (edit.getText().toString() != null && !edit.getText().toString().equals("")){
                    detailedPerson.setNombre(edit.getText().toString());
                }

                returnIntent.putExtra("editedPerson", detailedPerson);
                returnIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(returnIntent);
            }
        });

        // Boton nuevo gasto
        Button nuevoGasto = (Button) findViewById(R.id.butNuevoGasto);
        nuevoGasto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                LayoutInflater li = LayoutInflater.from(v.getContext());
                View promptsView = li.inflate(R.layout.dialog_new_gasto, null);

                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                        context);

                // set prompts.xml to alertdialog builder
                alertDialogBuilder.setView(promptsView);

                final TextView descrip = (TextView) promptsView.findViewById(R.id.textTitleGasto);
                descrip.setText(R.string.gasto_descrip);
                descrip.setTextSize(16);
                final TextView monto = (TextView) promptsView.findViewById(R.id.textMontoGasto);
                monto.setText(R.string.monto_descrip);
                monto.setTextSize(16);

                final EditText gastoDescrip = (EditText) promptsView.findViewById(R.id.inputDescripGasto);
                gastoDescrip.setText("");
                gastoDescrip.setOnKeyListener(new View.OnKeyListener() {
                    @Override
                    public boolean onKey(View v, int keyCode, KeyEvent event) {
                        if (gastoDescrip.getText().toString().equals("-")){
                            gastoDescrip.setText("");
                        }
                        return false;
                    }
                });


                final EditText gastoMont = (EditText) promptsView.findViewById(R.id.inputMontoGasto);
                gastoMont.setText("");
                gastoMont.setOnKeyListener(new View.OnKeyListener() {
                    @Override
                    public boolean onKey(View v, int keyCode, KeyEvent event) {
                        if (gastoMont.getText().toString().equals("")){
                            gastoMont.setText("");
                        }
                        return false;
                    }
                });

                alertDialogBuilder
                        .setCancelable(false)
                        .setTitle("Ingresar gasto.")
                        .setPositiveButton("OK",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,int id) {
                                        String desc = gastoDescrip.getText().toString();
                                        String monto = gastoMont.getText().toString();
                                        Double montoNumero;
                                        try{
                                            montoNumero = Double.parseDouble(monto);
                                        }catch (Exception e){
                                            Toast.makeText( context ,"No ingresaste un numero para el importe, proba de nuevo.", Toast.LENGTH_LONG ).show();
                                            gastoMont.requestFocus();
                                            return;
                                        }

                                        if (desc.equals("")){
                                            Toast.makeText( context ,"No ingresaste un nombre para el gasto, intenta de nuevo.", Toast.LENGTH_LONG ).show();
                                            gastoDescrip.requestFocus();
                                            return;
                                        }

                                        Gasto g = new Gasto( desc , Double.parseDouble(monto) );

                                        List<Gasto> gastos = detailedPerson.getGastos();
                                        if (gastos== null) {
                                            gastos = new ArrayList<Gasto>();
                                        }
                                        gastos.add(g);
                                        detailedPerson.setGastos(gastos);
                                        showGastos();
                                    }
                                })
                        .setNegativeButton("Cancel",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,int id) {
                                        dialog.cancel();
                                    }
                                });

                // create alert dialog
                AlertDialog alertDialog = alertDialogBuilder.create();

                // show it
                alertDialog.show();
            }
        });

        showGastos();
    }

    private void showGastos() {
        if (detailedPerson.getGastos()!= null && detailedPerson.getGastos().size()>0) {
            LinearLayout personGastos = (LinearLayout) findViewById(R.id.personGastosLinearLayout);
            personGastos.removeAllViews();

            for( Gasto g : detailedPerson.getGastos()){
                TextView gastoDescrip = new TextView(this);
                gastoDescrip.setText(g.getDescripcion());
                TextView gastoMonto = new TextView(this);
                gastoMonto.setText("$ "+g.getMonto().toString());
                personGastos.addView(gastoDescrip);
                personGastos.addView(gastoMonto);
            }
        }
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
