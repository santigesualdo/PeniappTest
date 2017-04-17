package com.mobile.santige.peniap;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.List;
import java.util.Locale;

import static android.graphics.Color.TRANSPARENT;
import static android.graphics.Color.WHITE;

public class VerPeniasActivity extends Activity {

    List<Penia> listaPenias;

    private PeniaAdapter myAdapter;

    final static int idTopLayout = Menu.FIRST + 100,
            idBack = Menu.FIRST + 101,
            idBotLayout = Menu.FIRST + 102,
            bottomMenuHeight = 75;

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

        // Global Panel
        RelativeLayout global_panel = (RelativeLayout) findViewById(R.id.global_panel);

        // Top Component
        RelativeLayout ibMenuTop = (RelativeLayout) findViewById(R.id.ibMenuTop);

        // Text in top
        TextView m_bCancel = (TextView) findViewById(R.id.m_bCancel);
        m_bCancel.setTypeface(MainActivity.gothamBold);

        // Bottom component
        LinearLayout ibMenuBot = (LinearLayout) findViewById(R.id.ibMenuBot);

        Double montoTotalP = 0.0;
        for ( Penia penia : listaPenias){
            montoTotalP+=penia.getMonto();
        }
        DecimalFormat df = new DecimalFormat("0", DecimalFormatSymbols.getInstance(Locale.ENGLISH));
        df.setMaximumFractionDigits(2); //340 = DecimalFormat.DOUBLE_FRACTION_DIGITS
        df.setMinimumFractionDigits(2);//FractionDigits(2);

        TextView cTVBot = (TextView) findViewById(R.id.cTVBot);
        cTVBot.setTypeface(MainActivity.gothamBold);
        cTVBot.setText( cTVBot.getText().toString() + listaPenias.size()  );

        // +++++++++++++ MIDDLE COMPONENT: all our GUI content
        LinearLayout ibMenuMiddle = (LinearLayout) findViewById(R.id.ibMenuMiddle);

        Button addPenia = (Button) findViewById(R.id.buttonAddPenia);
        addPenia.setTypeface(MainActivity.gothamBold);
        addPenia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(v.getContext(), MainActivity.class);
                startActivity(i);
            }
        });

        //scroll - so our content will be scrollable between the header and the footer
        ScrollView vscroll =(ScrollView) findViewById(R.id.ScrollView);

        //panel in scroll: add all controls/ objects to this layout
        LinearLayout m_panel = (LinearLayout) findViewById(R.id.scrollPanel);

        ListView list = new ListView(this);
        list.setDivider(getResources().getDrawable(R.drawable.list_view_divider));
        list.setDividerHeight(3);

        if (myAdapter != null) {
            list.setAdapter(myAdapter);
        } else {
            myAdapter = new VerPeniasActivity.PeniaAdapter(this, listaPenias);
            list.setAdapter(myAdapter);
        }

        m_panel.addView(list);


    }

    private class PeniaAdapter extends ArrayAdapter<Penia> {
        public PeniaAdapter(Context context, List<Penia> penias) {
            super(context, -1, -1, penias);
        }
        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {

            final LinearLayout listLayout = new LinearLayout(VerPeniasActivity.this);
            listLayout.setOrientation(LinearLayout.VERTICAL);
            listLayout.setId(5000);
            listLayout.setWeightSum(2);
            listLayout.setBackgroundColor(Color.TRANSPARENT);

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
            //params.setMargins(20,20,20,20);
            listLayout.setLayoutParams(params);
            listLayout.setPadding(10,10,10,10);



            if (listLayout != null) {

                final Penia penia = super.getItem(position);

                LinearLayout personaEditLay = new LinearLayout(VerPeniasActivity.this);
                // linearLayoutBut.setPadding(5,5,5,5);
                personaEditLay.setWeightSum(3);
                personaEditLay.setOrientation(LinearLayout.HORIZONTAL);

                personaEditLay.setBackgroundColor(Color.WHITE);
                // Titulo penia
                final TextView tituloText = new TextView(VerPeniasActivity.this);
                tituloText.setId(5001);
                tituloText.setTypeface(MainActivity.gothamBold);
                tituloText.setText("AD");
                tituloText.setBackgroundResource(R.drawable.shape_verpenia_result);
                tituloText.setGravity(Gravity.CENTER);
                tituloText.setTextSize(TypedValue.COMPLEX_UNIT_SP,16);
                LinearLayout.LayoutParams param2 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.MATCH_PARENT);
                param2.weight=1.8f;
                tituloText.setLayoutParams(param2);

                // Boton eliminar peña
                final ImageButton butEliminarPenia = new ImageButton(VerPeniasActivity.this);
                butEliminarPenia.setBackgroundColor(Color.TRANSPARENT);
                butEliminarPenia.setImageResource(R.drawable.elim);
                LinearLayout.LayoutParams param3 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.MATCH_PARENT);
                param3.weight=0.2f;
                butEliminarPenia.setLayoutParams(param3);
                butEliminarPenia.setOnClickListener(new View.OnClickListener() {
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

                personaEditLay.addView(butEliminarPenia);
                personaEditLay.addView(tituloText);

                LinearLayout personaDataLay = new LinearLayout(VerPeniasActivity.this);
                personaDataLay.setWeightSum(2);
                personaDataLay.setOrientation(LinearLayout.VERTICAL);
                personaDataLay.setBackgroundColor(Color.TRANSPARENT);

                final TextView fechaText = new TextView(VerPeniasActivity.this);
                fechaText.setId(6001);
                fechaText.setTypeface(MainActivity.gothamBold);
                fechaText.setTextColor(Color.WHITE);
                fechaText.setText(penia.getFecha());
                fechaText.setGravity(Gravity.RIGHT);
                personaDataLay.addView(fechaText);



                TextView cantidadPersonasTxt = new TextView(this.getContext());
                cantidadPersonasTxt.setBackgroundColor(Color.WHITE);
                cantidadPersonasTxt.setTypeface(MainActivity.gothamBold);
                cantidadPersonasTxt.setText("Amigos: " + penia.getCountPersons());
                cantidadPersonasTxt.setTextSize(TypedValue.COMPLEX_UNIT_SP,14);
                cantidadPersonasTxt.setGravity(Gravity.CENTER_HORIZONTAL);

                TextView totalText = new TextView(this.getContext());
                totalText.setBackgroundColor(Color.WHITE);
                totalText.setTypeface(MainActivity.gothamBold);
                DecimalFormat df = new DecimalFormat("0", DecimalFormatSymbols.getInstance(Locale.ENGLISH));
                df.setMaximumFractionDigits(2);
                df.setMinimumFractionDigits(2);
                totalText.setText("Total Gastado: $ "+ df.format(penia.getMonto()));
                totalText.setTextSize(TypedValue.COMPLEX_UNIT_SP,14);
                totalText.setGravity(Gravity.CENTER_HORIZONTAL);

                personaDataLay.addView(cantidadPersonasTxt);

                List<Persona> personasEnPenia = db.getPersonasByPenia(penia.getId());
                Integer id = 7000;
                TextView persona = new TextView(this.getContext());
                persona.setTypeface(MainActivity.gothamBold);
                persona.setId(id++);
                persona.setText( "Gastó cada uno: $" + df.format(penia.getMonto()/penia.getCountPersons()) );
                persona.setTextSize(TypedValue.COMPLEX_UNIT_SP,14);
                persona.setBackgroundColor(Color.WHITE);
                persona.setGravity(Gravity.CENTER_HORIZONTAL);

                personaDataLay.addView(totalText);
                personaDataLay.addView(persona);
                for (Persona p : personasEnPenia ){
                    if (p.getGastos() != null){
                        Double gastoTotalPorPersona = 0.0;
                        for (Gasto g : p.getGastos()){
                            gastoTotalPorPersona+= g.getMonto();
                        }
                        persona = new TextView(this.getContext());
                        persona.setTypeface(MainActivity.gothamBold);
                        persona.setBackgroundColor(Color.WHITE);
                        persona.setId(id++);
                        persona.setText( p.getNombre() + " - $" + df.format(gastoTotalPorPersona));
                        persona.setTextSize(TypedValue.COMPLEX_UNIT_SP,14);
                        persona.setGravity(Gravity.CENTER_HORIZONTAL);
                        personaDataLay.addView(persona);
                    }
                }

                listLayout.setBackgroundColor(TRANSPARENT);

                int _id = 5001;
                final LinearLayout gastosLayout = new LinearLayout(VerPeniasActivity.this);
                gastosLayout.setOrientation(LinearLayout.VERTICAL);

                LinearLayout linearLayoutBut = new LinearLayout(VerPeniasActivity.this);
                linearLayoutBut.setWeightSum(2);
                linearLayoutBut.setOrientation(LinearLayout.HORIZONTAL);
                linearLayoutBut.setBackgroundColor(WHITE);
                listLayout.addView(linearLayoutBut);

                LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,  LinearLayout.LayoutParams.MATCH_PARENT);
                param.weight=1.0f;

                listLayout.addView(personaEditLay);
                listLayout.addView(personaDataLay);

                return listLayout;
            }

            return null;
        }
    }
}
