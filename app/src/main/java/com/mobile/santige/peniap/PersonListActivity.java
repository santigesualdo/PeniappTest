package com.mobile.santige.peniap;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.text.InputType;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class PersonListActivity extends Activity {

    final static int idTopLayout = Menu.FIRST + 100,
            idBack = Menu.FIRST + 101,
            idBotLayout = Menu.FIRST + 102,
            bottomMenuHeight = 75;


    private PersonaAdapter myAdapter;

    private List<Persona> listaPersonas;
    private GrupoPersonas grupoPersonas;
    private double montoTotal;
    private String nombrePenia;

    int numPersons;
    TextView seekBarValue;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler(this));

        Bundle bundleObject = getIntent().getExtras();

        grupoPersonas = (GrupoPersonas) bundleObject.getSerializable("array_personas");
        listaPersonas = grupoPersonas.get_listaPersonas();
        numPersons = getIntent().getIntExtra("personasCount",0);

        updateView(listaPersonas);
    }

    @Override
    public void onBackPressed() {

        final Intent intent = new Intent(this, MainActivity.class);

        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which){
                    case DialogInterface.BUTTON_POSITIVE:
                        startActivity(intent);
                        break;

                    case DialogInterface.BUTTON_NEGATIVE:
                        //No button clicked
                        break;
                }
            }
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Si vuelve atras se perderan los cambios realizados. ¿Continuar?")
                .setNegativeButton("No", dialogClickListener)
                .setPositiveButton("Si", dialogClickListener)
                .show();
    }

    private void updateView(final List<Persona> personas) {

        setContentView(R.layout.activity_person_list);

        TextView tv = (TextView) findViewById(R.id.textTitulo);
        tv.setTypeface(MainActivity.gothamBold);

        TextView textNombrePenia = (TextView) findViewById(R.id.textNombrePenia);
        textNombrePenia.setBackgroundResource(R.drawable.button_subtittle_shape);
        textNombrePenia.setTypeface(MainActivity.gothamBold);

        textNombrePenia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            showEditPeniaName(v);
            }
        });

        //Create our top content holder
        RelativeLayout global_panel = (RelativeLayout) findViewById(R.id.global_panel);
        // +++++++++++++ TOP COMPONENT: the header
        RelativeLayout ibMenu = (RelativeLayout) findViewById(R.id.topMenu);

        TextView titleGastos = (TextView) findViewById(R.id.titleGastos);
        titleGastos.setTypeface(MainActivity.gothamBold);
        //titleGastos.setTextSize(TypedValue.COMPLEX_UNIT_SP, nTextH);

        titleGastos.setBackgroundResource(R.drawable.button_subtittle_shape);

        // +++++++++++++ BOTTOM COMPONENT: the footer
        LinearLayout ibMenuBot = (LinearLayout) findViewById(R.id.ibMenuBot);

        // textview in ibMenu : card holder
        TextView cTVBot = (TextView) findViewById(R.id.cTVBot);
        cTVBot.setTypeface(MainActivity.gothamBold);

        // botton
        Button bottomButton = (Button) findViewById(R.id.bottomButton);
        bottomButton.setTypeface(MainActivity.gothamBold);


        bottomButton.setText("Continuar");
        bottomButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (montoTotal > 0) {
                    if (personas.size()>1){
                        Intent intent = new Intent(v.getContext(), PersonSelectionActivity.class);

                        GrupoPersonas personasGroup = new GrupoPersonas();
                        personasGroup.set_listaPersonas(personas);
                        Bundle b = new Bundle();
                        b.putSerializable("array_personas", personasGroup);
                        intent.putExtras(b);
                        intent.putExtra("monto_total", montoTotal);
                        intent.putExtra("nombre_penia", nombrePenia);

                        startActivity(intent);
                    }else{
                        Toast.makeText(v.getContext(), "Para calcular gastos, al menos tiene que haber 2 personas.", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(v.getContext(), "No hay gastos. No es posible continuar.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // +++++++++++++ MIDDLE COMPONENT: all our GUI content
        LinearLayout midLayout = (LinearLayout) findViewById(R.id.midLayout);

        Button addPerson = (Button) findViewById(R.id.addPerson);
        addPerson.setTypeface(MainActivity.gothamBold);

        addPerson.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Persona newP = new Persona();
                if (personas.size()>0){
                    Persona anteriorP  = personas.get(personas.size()-1);
                    newP.setListID(anteriorP.getListID()+1);
                }else{
                    newP.setListID(0);
                }
                newP.setNombre("Persona "+ newP.getListID());
                personas.add(newP);
                updateView(personas);
            }
        });
        //scroll - so our content will be scrollable between the header and the footer
        ScrollView vscroll = (ScrollView) findViewById(R.id.vscroll);

        //panel in scroll: add all controls/ objects to this layout
        LinearLayout m_panel = new LinearLayout(this);
        LinearLayout.LayoutParams mpanelParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        m_panel.setOrientation(LinearLayout.VERTICAL);
        m_panel.setLayoutParams(mpanelParams);
        vscroll.addView(m_panel);

        ListView list = new ListView(this);
        if (myAdapter != null) {
            list.setAdapter(myAdapter);
        } else {
            myAdapter = new PersonaAdapter(this, personas);
        }

        list.setDivider(getResources().getDrawable(R.drawable.list_view_divider));
        list.setDividerHeight(3);

        montoTotal = 0;
        for (Persona pe : personas) {
            if (pe.getGastos() != null) {
                for (Gasto g : pe.getGastos()) {
                    montoTotal += g.getMonto();
                }
            }
        }

        DecimalFormat df = new DecimalFormat("#.00");

        if (montoTotal > 0) {
            cTVBot.setText("GASTOS: $" + df.format(montoTotal));
        } else {
            cTVBot.setText("GASTOS: $0.00");
        }

        m_panel.addView(list);
    }

    public void onResume() {
        super.onResume();
        updateView(listaPersonas);
    }

    private void showEditPeniaName(final View v){
        LayoutInflater li = LayoutInflater.from(v.getContext());
        View promptsView = li.inflate(R.layout.dialog_edit_nombre_penia, null);

        final TextView text = (TextView) v;
        text.setTypeface(MainActivity.gothamBold);

        /*EditText inputDescripName = (EditText) findViewById(R.id.inputDescripName);
        inputDescripName.setTypeface(MainActivity.gothamBold);*/

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                v.getContext());

        // set prompts.xml to alertdialog builder
        alertDialogBuilder.setView(promptsView);

        final EditText nombreDescrip = (EditText) promptsView.findViewById(R.id.inputDescripName);
        nombreDescrip.setTypeface(MainActivity.gothamBold);
        nombreDescrip.setText(R.string.nombre_penia);
        nombreDescrip.setText("");
        nombreDescrip.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (nombreDescrip.getText().toString().equals("-")){
                    nombreDescrip.setText("");
                }
                return false;
            }
        });

        alertDialogBuilder
                .setCancelable(false)
                .setTitle("Ingresar Nombre.")
                .setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {
                                String desc = nombreDescrip.getText().toString();

                                if (desc.equals("")){
                                    Toast.makeText(  v.getContext() ,"No ingresaste un nombre.", Toast.LENGTH_LONG ).show();
                                    nombreDescrip.requestFocus();
                                    return;
                                }

                                nombrePenia = nombreDescrip.getText().toString();
                                text.setText(nombrePenia);
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

    private void showEditPersonNameDialog(final View v, final Persona persona) {
        LayoutInflater li = LayoutInflater.from(v.getContext());
        View promptsView = li.inflate(R.layout.dialog_edit_nombre, null);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                v.getContext());

        // set prompts.xml to alertdialog builder
        alertDialogBuilder.setView(promptsView);

        final EditText descrip = (EditText) promptsView.findViewById(R.id.inputDescripName);
        descrip.setTypeface(MainActivity.gothamBold);
        descrip.setText(R.string.nombre_descrip);
        descrip.setTextSize(TypedValue.COMPLEX_UNIT_SP,16);

        final EditText nombreDescrip = (EditText) promptsView.findViewById(R.id.inputDescripName);
        nombreDescrip.setTypeface(MainActivity.gothamBold);
        nombreDescrip.setText(R.string.nombre_descrip);
        nombreDescrip.setText("");
        nombreDescrip.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (nombreDescrip.getText().toString().equals("-")){
                    nombreDescrip.setText("");
                }
                return false;
            }
        });

        alertDialogBuilder
                .setCancelable(false)
                .setTitle("Ingresar Nombre.")
                .setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {
                                String desc = nombreDescrip.getText().toString();

                                if (desc.equals("")){
                                    Toast.makeText(  v.getContext() ,"No ingresaste un nombre.", Toast.LENGTH_LONG ).show();
                                    nombreDescrip.requestFocus();
                                    return;
                                }

                                persona.setNombre(nombreDescrip.getText().toString());
                                listaPersonas.set(persona.getListID(), persona);
                                updateView(listaPersonas);
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

    private void showNewGastoDialog(final View v, final Persona persona) {
        LayoutInflater li = LayoutInflater.from(v.getContext());
        View promptsView = li.inflate(R.layout.dialog_new_gasto, null);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                v.getContext());

        // set prompts.xml to alertdialog builder
        alertDialogBuilder.setView(promptsView);

        final TextView monto = (TextView) promptsView.findViewById(R.id.textMontoGasto);
        monto.setTypeface(MainActivity.gothamBold);
        monto.setText(R.string.monto_descrip);
        monto.setTextSize(TypedValue.COMPLEX_UNIT_SP,16);

        final EditText gastoMont = (EditText) promptsView.findViewById(R.id.inputMontoGasto);
        gastoMont.setTypeface(MainActivity.gothamBold);
        gastoMont.setInputType(InputType.TYPE_CLASS_NUMBER);
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
                                String monto = gastoMont.getText().toString();
                                Double montoNumero;
                                try{
                                    montoNumero = Double.parseDouble(monto);
                                }catch (Exception e){
                                    Toast.makeText( v.getContext() ,"No ingresaste un numero para el importe, proba de nuevo.", Toast.LENGTH_LONG ).show();
                                    gastoMont.requestFocus();
                                    return;
                                }
                                Gasto g = new Gasto( "" , montoNumero );

                                List<Gasto> gastos = persona.getGastos();
                                if (gastos== null) {
                                    gastos = new ArrayList<Gasto>();
                                }
                                gastos.add(g);
                                persona.setGastos(gastos);
                                listaPersonas.set(persona.getListID(), persona);
                                updateView(listaPersonas);
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

    private class PersonaAdapter extends ArrayAdapter<Persona> {
        public PersonaAdapter(Context context, List<Persona> personas) {
            super(context, -1, -1, personas);
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {

            Integer id = 5000;

            final LinearLayout listLayout = new LinearLayout(PersonListActivity.this);
            listLayout.setOrientation(LinearLayout.VERTICAL);
            listLayout.setId(id);
            listLayout.setWeightSum(2);

            listLayout.setBackgroundResource(R.drawable.border);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
            listLayout.setLayoutParams(params);
            listLayout.setPadding(5,5,5,5);


            if (listLayout != null) {

                final Persona persona = super.getItem(position);

                LinearLayout personaEditLay = new LinearLayout(PersonListActivity.this);
                personaEditLay.setOrientation(LinearLayout.HORIZONTAL);
                personaEditLay.setWeightSum(2);
                personaEditLay.setBackground(getResources().getDrawable(R.drawable.button_subtittle_shape));
                LinearLayout.LayoutParams params0 = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                params0.setMargins(0,0,0,5);
                listLayout.addView(personaEditLay, params0);

                final TextView listText = new TextView(PersonListActivity.this);
                id = 5001;
                listText.setId(id);
                listText.setTypeface(MainActivity.gothamBold);

                listText.setText(persona.getNombre());
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    listText.setBackground(getResources().getDrawable(R.drawable.gradient));
                }
                listText.setBackgroundColor(Color.TRANSPARENT);
                listText.setTextColor(Color.BLACK);
                listText.setTextSize(TypedValue.COMPLEX_UNIT_SP,16);
                listText.setGravity(Gravity.RIGHT);
                //listText.setBackground(getResources().getDrawable(R.drawable.button_subtittle_shape));
                LinearLayout.LayoutParams params1 = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                params1.weight = 1.7f;
                params1.setMargins(0,0,0,0);

                ImageButton butEliminarPenia = new ImageButton(getContext());
                butEliminarPenia.setMaxHeight(30);
                butEliminarPenia.setBackgroundColor(Color.TRANSPARENT);
                butEliminarPenia.setImageResource(R.drawable.penc);
                LinearLayout.LayoutParams params2 = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.MATCH_PARENT);
                params2.gravity = Gravity.LEFT;
                params2.setMargins(5,0,5,0);
                params2.weight = 0.3f;

                butEliminarPenia.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showEditPersonNameDialog(v, persona);
                    }
                });

                personaEditLay.addView(listText, params1);
                personaEditLay.addView(butEliminarPenia,params2);

                listLayout.setBackgroundColor(getResources().getColor(R.color.backgroundGlobalColor));

                int _id = 5001;
                final LinearLayout gastosLayout = new LinearLayout(PersonListActivity.this);
                gastosLayout.setOrientation(LinearLayout.VERTICAL);

                final TextView listDescripGasto = new TextView(PersonListActivity.this);
                listDescripGasto.setTypeface(MainActivity.gothamBold);

                if (persona.getGastos() == null) {
                    List<Gasto> gastos = new ArrayList<Gasto>();
                    gastos.add(new Gasto("", 0.0));
                    persona.setGastos(gastos);
                }


                Double monto = 0.0;
                for (Gasto g : persona.getGastos()) {
                    monto += g.getMonto();
                }

                _id++;
                listDescripGasto.setId(_id);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    listDescripGasto.setBackground(getResources().getDrawable(R.drawable.gradient2));
                }
                listDescripGasto.setBackgroundColor(getResources().getColor(R.color.buttonPressedColor));
                listDescripGasto.setTextColor(Color.WHITE);
                listDescripGasto.setGravity(Gravity.CENTER_HORIZONTAL);
                listDescripGasto.setText(" Gastó $ " + monto );

                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.MATCH_PARENT);
                lp.setMargins(0,0,0,5);

                gastosLayout.setGravity(Gravity.CENTER_HORIZONTAL);
                gastosLayout.addView(listDescripGasto, lp);

                listLayout.addView(gastosLayout);

                LinearLayout linearLayoutBut = new LinearLayout(PersonListActivity.this);
                linearLayoutBut.setWeightSum(2);
                linearLayoutBut.setOrientation(LinearLayout.HORIZONTAL);
                linearLayoutBut.setBackgroundColor(Color.TRANSPARENT);
                listLayout.addView(linearLayoutBut);

                LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,  LinearLayout.LayoutParams.MATCH_PARENT);
                param.setMargins(5,2,5,5);
                param.weight=1.0f;

                Button bNuevoGasto = new Button(this.getContext());
                bNuevoGasto.setTypeface(MainActivity.gothamBold);
                bNuevoGasto.setText("Agregar Gasto");

                bNuevoGasto.setTextSize(TypedValue.COMPLEX_UNIT_SP,12);

                bNuevoGasto.setBackgroundResource(R.drawable.button_subtittle2_shape_round);


                bNuevoGasto.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v)
                    {
                        showNewGastoDialog(v, persona);
                    }
                });


                Button removePerson = new Button(this.getContext());
                removePerson.setTypeface(MainActivity.gothamBold);
                removePerson.setText("Quitar Persona");

                removePerson.setBackgroundResource(R.drawable.button_subtittle2_shape_round);
                removePerson.setTextSize(TypedValue.COMPLEX_UNIT_SP,12);
                removePerson.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        PersonaAdapter.super.remove(persona);
                    }
                });

                linearLayoutBut.addView(bNuevoGasto, param);
                linearLayoutBut.addView(removePerson, param);

                return listLayout;
            }

            return null;
        }
    }

}
