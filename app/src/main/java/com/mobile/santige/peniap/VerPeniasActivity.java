package com.mobile.santige.peniap;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.List;
import java.util.Locale;

public class VerPeniasActivity extends Activity {

    List<Penia> listaPenias;

    private PeniaAdapter myAdapter;

    DatabaseHandler db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler(this));

        db = DatabaseHandler.getInstance(this);
        listaPenias = db.getAllPenias();

        if (listaPenias.size()==0){
            Toast.makeText(this, "No hay peñas guardadas.", Toast.LENGTH_SHORT).show();
        }

        updateView(listaPenias);

    }

    private void updateView(final List<Penia> listaPenias) {
        //Create our top content holder
        setContentView(R.layout.activity_ver_penias);

        // Text in top
        TextView m_bCancel = (TextView) findViewById(R.id.m_bCancel);
        m_bCancel.setTypeface(MainActivity.gothamBold);

        Double montoTotalP = 0.0;
        for ( Penia penia : listaPenias){
            montoTotalP+=penia.getMonto();
        }
        DecimalFormat df = new DecimalFormat("0", DecimalFormatSymbols.getInstance(Locale.ENGLISH));
        df.setMaximumFractionDigits(2); //340 = DecimalFormat.DOUBLE_FRACTION_DIGITS
        df.setMinimumFractionDigits(2);//FractionDigits(2);

        TextView cTVBot = (TextView) findViewById(R.id.cTVBot);
        cTVBot.setTypeface(MainActivity.gothamBold);
        cTVBot.setText( cTVBot.getText().toString() + ' '+ listaPenias.size()  );

        Button addPenia = (Button) findViewById(R.id.buttonAddPenia);
        addPenia.setTypeface(MainActivity.gothamBold);
        addPenia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(v.getContext(), MainActivity.class);
                startActivity(i);
            }
        });

        //panel in scroll: add all controls/ objects to this layout
        LinearLayout m_panel = (LinearLayout) findViewById(R.id.scrollPanel);

        ListView list = new ListView(this);
        if (myAdapter != null) {
            list.setAdapter(myAdapter);
        } else {
            myAdapter = new PeniaAdapter(this, listaPenias);
            list.setAdapter(myAdapter);
        }

        m_panel.addView(list);
    }

    private class PeniaAdapter extends ArrayAdapter<Penia> {
        public PeniaAdapter(Context context, List<Penia> penias) {
            super(context, -1, -1, penias);
        }

        private void addViewLine(ViewGroup viewGroup) {
            View line = new View(this.getContext());
            LinearLayout.LayoutParams para = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 1);
            para.setMargins(2,2,2,2);
            line.setBackgroundColor(Color.BLACK);
            viewGroup.addView(line,para );
        }
        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {

            final LinearLayout listLayout = new LinearLayout(getContext());
            listLayout.setOrientation(LinearLayout.VERTICAL);
            listLayout.setId(5000);
            listLayout.setWeightSum(2);
            listLayout.setBackgroundColor(Color.TRANSPARENT);

            AbsListView.LayoutParams params = new AbsListView.LayoutParams(AbsListView.LayoutParams.MATCH_PARENT, AbsListView.LayoutParams.MATCH_PARENT);
            listLayout.setLayoutParams(params);

            if (listLayout != null) {

                final Penia penia = super.getItem(position);

                // personaEditLay, nombre de penia, boton para eliminar
                final LinearLayout personaEditLay = new LinearLayout(VerPeniasActivity.this);
                personaEditLay.setPadding(0,0,0,0);
                personaEditLay.setWeightSum(2);
                personaEditLay.setOrientation(LinearLayout.HORIZONTAL);
                personaEditLay.setBackgroundColor(Color.WHITE);

                // personaDataLay, donde estan los datos
                final LinearLayout personaDataLay = new LinearLayout(VerPeniasActivity.this);
                personaDataLay.setWeightSum(1);
                personaDataLay.setOrientation(LinearLayout.VERTICAL);
                personaDataLay.setBackgroundColor(Color.WHITE);
                final LinearLayout.LayoutParams lpPersonaDataLay = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
                lpPersonaDataLay.setMargins(0,5,0,5);

                final LinearLayout buttonsLayout = new LinearLayout(VerPeniasActivity.this);
                buttonsLayout.setBackgroundColor(Color.TRANSPARENT);
                final LinearLayout tittleLayout = new LinearLayout(VerPeniasActivity.this);
                tittleLayout.setBackgroundColor(Color.TRANSPARENT);
                final LinearLayout.LayoutParams buttonsTittleParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
                buttonsTittleParams.setMargins(0,5,0,5);
                buttonsTittleParams.weight = 1.5f;

                // Titulo penia
                final TextView tituloText = new TextView(VerPeniasActivity.this);
                tituloText.setId(5001);
                tituloText.setTypeface(MainActivity.gothamBold);
                tituloText.setText(penia.getNombre());
                tituloText.setBackgroundResource(R.drawable.shape_verpenia_result);
                tituloText.setGravity(Gravity.CENTER);
                tituloText.setTextSize(TypedValue.COMPLEX_UNIT_SP,16);

                LinearLayout.LayoutParams paramTitulo = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.MATCH_PARENT);
                paramTitulo.gravity = Gravity.LEFT;
                paramTitulo.setMargins(20,0,0,0);

                LinearLayout.LayoutParams paramButons = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.MATCH_PARENT);
                paramButons.setMargins(5,0,5,0);
                paramButons.gravity = Gravity.CENTER;

                // Boton eliminar peña
                final ImageButton butHideShow = new ImageButton(VerPeniasActivity.this);
                butHideShow.setBackgroundColor(Color.TRANSPARENT);
                butHideShow.setImageResource(R.drawable.desple);

                final ImageButton butDeletePenia = new ImageButton(VerPeniasActivity.this);
                butDeletePenia.setBackgroundColor(Color.TRANSPARENT);
                butDeletePenia.setImageResource(R.drawable.elim);

                final ImageButton butVerPeniaResult = new ImageButton(VerPeniasActivity.this);
                butVerPeniaResult.setBackgroundColor(Color.TRANSPARENT);
                butVerPeniaResult.setImageResource(R.drawable.busc);

                personaEditLay.addView(buttonsLayout);
                    buttonsLayout.addView(butHideShow, paramButons);
                    buttonsLayout.addView(butDeletePenia, paramButons);
                    buttonsLayout.addView(butVerPeniaResult, paramButons);
                personaEditLay.addView(tittleLayout, buttonsTittleParams);
                    tittleLayout.addView(tituloText, paramTitulo);

                // fecha
                TextView fechaText = new TextView(VerPeniasActivity.this);
                fechaText.setId(6001);
                fechaText.setTextSize(TypedValue.COMPLEX_UNIT_SP,14);
                fechaText.setTypeface(MainActivity.gothamBold);
                fechaText.setTextColor(Color.WHITE);
                fechaText.setBackgroundColor(getResources().getColor(R.color.backgroundGlobalColor));
                fechaText.setText(penia.getFecha());
                fechaText.setGravity(Gravity.RIGHT);
                personaDataLay.addView(fechaText);

                // cant personas
                TextView cantidadPersonasTxt = new TextView(VerPeniasActivity.this);
                cantidadPersonasTxt.setBackgroundColor(Color.WHITE);
                cantidadPersonasTxt.setTypeface(MainActivity.gothamBold);
                cantidadPersonasTxt.setText("Amigos: " + penia.getCountPersons());
                cantidadPersonasTxt.setTextSize(TypedValue.COMPLEX_UNIT_SP,14);
                cantidadPersonasTxt.setGravity(Gravity.LEFT);
                personaDataLay.addView(cantidadPersonasTxt);

                addViewLine(personaDataLay);

                // total gastado
                TextView totalText = new TextView(this.getContext());
                totalText.setBackgroundColor(Color.WHITE);
                totalText.setTypeface(MainActivity.gothamBold);
                DecimalFormat df = new DecimalFormat("0", DecimalFormatSymbols.getInstance(Locale.ENGLISH));
                df.setMaximumFractionDigits(2);
                df.setMinimumFractionDigits(2);
                totalText.setText("Total Gastado: $ "+ df.format(penia.getMonto()));
                totalText.setTextSize(TypedValue.COMPLEX_UNIT_SP,14);
                totalText.setGravity(Gravity.LEFT);

                // listado de penias guardadas
                List<Persona> personasEnPenia = db.getPersonasByPenia(penia.getId());
                Integer id = 7000;

                Integer cant = personasEnPenia.size();

                Log.d("Cantidad de Personas", cant.toString());

                // gastos de cada uno
                TextView gastoCadaUno = new TextView(this.getContext());
                gastoCadaUno.setTypeface(MainActivity.gothamBold);
                gastoCadaUno.setId(id++);
                gastoCadaUno.setText( "Gastó cada uno: $" + df.format(penia.getMonto()/penia.getCountPersons()) );
                gastoCadaUno.setTextSize(TypedValue.COMPLEX_UNIT_SP,14);
                gastoCadaUno.setBackgroundColor(Color.WHITE);
                gastoCadaUno.setGravity(Gravity.LEFT);

                personaDataLay.addView(totalText);
                addViewLine(personaDataLay);
                personaDataLay.addView(gastoCadaUno);
                addViewLine(personaDataLay);

                Integer i = 1;
                for (Persona p : personasEnPenia ){
                    if (p.getGastos() != null){
                        Double gastoTotalPorPersona = 0.0;
                        for (Gasto g : p.getGastos()){
                            // calculo monto total
                            gastoTotalPorPersona+= g.getMonto();
                        }
                        // gasto de cada persona

                        TextView persona = new TextView(this.getContext());
                        persona.setTypeface(MainActivity.gothamBold);
                        persona.setBackgroundColor(Color.WHITE);
                        persona.setId(id++);
                        persona.setText( p.getNombre() + " - $" + df.format(gastoTotalPorPersona));
                        persona.setTextSize(TypedValue.COMPLEX_UNIT_SP,14);
                        persona.setGravity(Gravity.LEFT);
                        personaDataLay.addView(persona);
                        if (i++ != personasEnPenia.size()) {
                            addViewLine(personaDataLay);
                        }
                    }
                }

                butDeletePenia.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(final View v) {
                        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                switch (which){
                                    case DialogInterface.BUTTON_POSITIVE:
                                        PeniaAdapter.super.remove(penia);
                                        DatabaseHandler db = DatabaseHandler.getInstance(v.getContext());
                                        db.deletePenia(penia);
                                        updateView(listaPenias);
                                        break;
                                    case DialogInterface.BUTTON_NEGATIVE:
                                        break;
                                }
                            }
                        };

                        AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                        builder.setMessage("¿Esta seguro de eliminar esta peña?")
                                .setNegativeButton("No", dialogClickListener)
                                .setPositiveButton("Si", dialogClickListener)
                                .show();
                    }
                });
                butHideShow.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(final View v) {

                        if (personaDataLay.getVisibility() != View.GONE){
                            personaDataLay.setVisibility(View.GONE);
                            lpPersonaDataLay.setMargins(0,0,0,0);
                        }else{
                            personaDataLay.setVisibility(View.VISIBLE);
                            lpPersonaDataLay.setMargins(0,0,0,10);
                        }
                    }
                });

                butVerPeniaResult.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(v.getContext(), ResultActivity.class );
                        intent.putExtra("monto_total", penia.getMonto());
                        intent.putExtra("count_persons",  penia.getCountPersons());
                        intent.putExtra("nombre_penia", penia.getNombre());
                        intent.putExtra("guardarPeniaBut", false);
                        List<Persona> listaPersonas = db.getPersonasByPenia(penia.getId());
                        GrupoPersonas personasGroup = new GrupoPersonas();
                        personasGroup.set_listaPersonas(listaPersonas);
                        Bundle b = new Bundle();
                        b.putSerializable("array_personas", personasGroup);
                        intent.putExtras(b);
                        startActivity(intent);
                    }
                });

                listLayout.addView(personaEditLay);
                listLayout.addView(personaDataLay,lpPersonaDataLay);

                return listLayout;
            }

            return null;
        }
    }


}
