package com.mobile.santige.nuevaaplicacion;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
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
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static android.graphics.Color.WHITE;

public class PersonListActivity extends Activity {

    final static int idTopLayout = Menu.FIRST + 100,
            idBack = Menu.FIRST + 101,
            idBotLayout = Menu.FIRST + 102,
            bottomMenuHeight = 75;


    private PersonaAdapter myAdapter;

    static public List<Persona> listaPersonas;
    static public double montoTotal;

    int numPersons;
    TextView seekBarValue;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        numPersons = MainActivity.personasCount;

        listaPersonas = MainActivity._listaPersonas;

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

        //Create our top content holder
        RelativeLayout global_panel = new RelativeLayout(this);
        global_panel.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT));
        global_panel.setBackgroundColor(Color.WHITE);
        global_panel.setGravity(Gravity.FILL);

        // +++++++++++++ TOP COMPONENT: the header
        RelativeLayout ibMenu = new RelativeLayout(this);
        ibMenu.setId(idTopLayout);

        RelativeLayout.LayoutParams topParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        topParams.addRule(RelativeLayout.ALIGN_PARENT_TOP);
        global_panel.addView(ibMenu, topParams);

        // cancel button in ibMenu
        int nTextH = 18;
        TextView m_bCancel = new TextView(this);
        m_bCancel.setId(idBack);
        m_bCancel.setText("¿Quienes gastaron?");
        nTextH = 18;
        m_bCancel.setTextSize(TypedValue.COMPLEX_UNIT_SP, nTextH);
        m_bCancel.setTypeface(Typeface.create("arial", Typeface.BOLD));
        RelativeLayout.LayoutParams lpbCancel =
                new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        lpbCancel.addRule(RelativeLayout.CENTER_IN_PARENT);
        ibMenu.addView(m_bCancel, lpbCancel);

        // +++++++++++++ BOTTOM COMPONENT: the footer
        LinearLayout ibMenuBot = new LinearLayout(this);
        ibMenuBot.setId(idBotLayout);
        ibMenuBot.setBackgroundColor(WHITE);
        ibMenuBot.setBackgroundResource(R.drawable.border);
        //ibMenuBot.setMinimumHeight(bottomMenuHeight);
        // LinearLayout.LayoutParams ibMenuBotParams = (LinearLayout.LayoutParams) ibMenuBot.getLayoutParams();
        //ibMenuBotParams. = 2;

        RelativeLayout.LayoutParams botParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        //botParams.addRule(RelativeLayout.CENTER_IN_PARENT);
        botParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        global_panel.addView(ibMenuBot, botParams);

        // textview in ibMenu : card holder
        TextView cTVBot = new TextView(this);
        cTVBot.setText("Monto total gastado: ");
        cTVBot.setTextSize(TypedValue.COMPLEX_UNIT_SP,16);
        cTVBot.setTypeface(Typeface.create("arial", Typeface.BOLD));
        RelativeLayout.LayoutParams lpcTVBot = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        //lpcTVBot.addRule(RelativeLayout.CENTER_IN_PARENT);
        //lpcTVBot.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
        lpcTVBot.setMargins(20,0,20,0);

        // botton
        Button bottomButton = new Button(this);
        bottomButton.setText("Listo!");
        bottomButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (montoTotal > 0) {
                    if (personas.size()>1){
                        Intent intent = new Intent(v.getContext(), PersonSelectionActivity.class);
                        startActivity(intent);
                    }else{
                        Toast.makeText(v.getContext(), "Para calcular gastos, al menos tiene que haber 2 personas.", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(v.getContext(), "No hay gastos. No es posible continuar.", Toast.LENGTH_SHORT).show();
                }
            }
        });
        RelativeLayout.LayoutParams lpcButton = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        //lpcButton.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        //lpcButton.addRule(RelativeLayout.ALIGN_BOTTOM);

        ibMenuBot.addView(cTVBot, lpcTVBot);
        ibMenuBot.addView(bottomButton, lpcButton);

        // +++++++++++++ MIDDLE COMPONENT: all our GUI content
        LinearLayout midLayout = new LinearLayout(this);
        midLayout.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
        midLayout.setBackgroundColor(WHITE);
        midLayout.setOrientation(LinearLayout.VERTICAL);

        Button addPerson = new Button(this);
        addPerson.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        addPerson.setText("Agregar Persona con Gastos");
        addPerson.setTextSize(TypedValue.COMPLEX_UNIT_SP,14);
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
        midLayout.addView(addPerson);

        RelativeLayout.LayoutParams midParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
        midParams.addRule(RelativeLayout.ABOVE, ibMenuBot.getId());
        midParams.addRule(RelativeLayout.BELOW, ibMenu.getId());
        global_panel.addView(midLayout, midParams);
        //scroll - so our content will be scrollable between the header and the footer
        ScrollView vscroll = new ScrollView(this);
        vscroll.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
        vscroll.setFillViewport(true);
        midLayout.addView(vscroll);

        //panel in scroll: add all controls/ objects to this layout
        LinearLayout m_panel = new LinearLayout(this);
        m_panel.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        m_panel.setOrientation(LinearLayout.VERTICAL);
        vscroll.addView(m_panel);

        ListView list = new ListView(this);
        if (myAdapter != null) {
            list.setAdapter(myAdapter);
        } else {
            myAdapter = new PersonaAdapter(this, personas);
        }

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
            cTVBot.setText("Monto total gastado: $" + df.format(montoTotal));
        } else {
            cTVBot.setText("Monto total gastado: $0.00");
        }


        m_panel.addView(list);
        setContentView(global_panel);

    }

    public void onResume() {
        super.onResume();
        updateView(listaPersonas);
    }

    private void showEditPersonNameDialog(final View v, final Persona persona) {
        LayoutInflater li = LayoutInflater.from(v.getContext());
        View promptsView = li.inflate(R.layout.dialog_edit_nombre, null);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                v.getContext());

        // set prompts.xml to alertdialog builder
        alertDialogBuilder.setView(promptsView);

        final EditText descrip = (EditText) promptsView.findViewById(R.id.inputDescripName);
        descrip.setText(R.string.nombre_descrip);
        descrip.setTextSize(TypedValue.COMPLEX_UNIT_SP,16);

        final EditText nombreDescrip = (EditText) promptsView.findViewById(R.id.inputDescripName);
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
        monto.setText(R.string.monto_descrip);
        monto.setTextSize(TypedValue.COMPLEX_UNIT_SP,16);

        final EditText gastoMont = (EditText) promptsView.findViewById(R.id.inputMontoGasto);
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

            final LinearLayout listLayout = new LinearLayout(PersonListActivity.this);
            listLayout.setOrientation(LinearLayout.VERTICAL);
            listLayout.setId(5000);
            listLayout.setWeightSum(2);

            listLayout.setBackgroundResource(R.drawable.border);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
            //params.setMargins(20,20,20,20);
            listLayout.setLayoutParams(params);
            listLayout.setPadding(10,10,10,10);


            if (listLayout != null) {

                final Persona persona = super.getItem(position);

                final TextView listText = new TextView(PersonListActivity.this);
                listText.setId(5001);

                LinearLayout personaEditLay = new LinearLayout(PersonListActivity.this);
                // linearLayoutBut.setPadding(5,5,5,5);
                personaEditLay.setWeightSum(2);
                personaEditLay.setOrientation(LinearLayout.HORIZONTAL);
                personaEditLay.setBackgroundColor(WHITE);
                listLayout.addView(personaEditLay);

                listText.setText(persona.getNombre());
                listText.setBackgroundColor(Color.TRANSPARENT);

                listText.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_START);
                listText.setGravity(Gravity.CENTER_VERTICAL);
                listText.setTextSize(TypedValue.COMPLEX_UNIT_SP,16);
                LinearLayout.LayoutParams param2 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.MATCH_PARENT);
                param2.weight=1.8f;
                listText.setLayoutParams(param2);

                final ImageButton but = new ImageButton(PersonListActivity.this);
                but.setBackgroundColor(Color.TRANSPARENT);
                but.setImageResource(R.drawable.penc);
                LinearLayout.LayoutParams param3 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.MATCH_PARENT);
                param3.weight=0.2f;
                but.setLayoutParams(param3);

                but.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showEditPersonNameDialog(v, persona);
                    }
                });

                personaEditLay.addView(but);
                personaEditLay.addView(listText);

                listLayout.setBackgroundColor(WHITE);

                int _id = 5001;
                final LinearLayout gastosLayout = new LinearLayout(PersonListActivity.this);
                gastosLayout.setOrientation(LinearLayout.VERTICAL);

                final TextView listDescripGasto = new TextView(PersonListActivity.this);

                if (persona.getGastos() != null) {
                    Double monto = 0.0;
                    int cantidad = persona.getGastos().size();
                    for (Gasto g : persona.getGastos()) {
                        monto += g.getMonto();
                    }


                    _id++;
                    listDescripGasto.setId(_id);
                    listDescripGasto.setBackgroundColor(WHITE);
                    listDescripGasto.setTextColor(Color.BLACK);
                    listDescripGasto.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                    if (cantidad == 1) {
                        listDescripGasto.setText("(Compro " + cantidad + " cosa y gasto $ " + monto + ")");
                    } else {
                        listDescripGasto.setText("(Compro " + cantidad + " cosas y gasto $ " + monto + ")");
                    }

                    //gastosLayout.setLayoutParams(new AbsListView.LayoutParams(AbsListView.LayoutParams.WRAP_CONTENT, AbsListView.LayoutParams.WRAP_CONTENT));
                    gastosLayout.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                    gastosLayout.addView(listDescripGasto, ViewGroup.LayoutParams.MATCH_PARENT);

                    listLayout.addView(gastosLayout);
                }

                LinearLayout linearLayoutBut = new LinearLayout(PersonListActivity.this);
                // linearLayoutBut.setPadding(5,5,5,5);
                linearLayoutBut.setWeightSum(2);
                linearLayoutBut.setOrientation(LinearLayout.HORIZONTAL);
                linearLayoutBut.setBackgroundColor(WHITE);
                listLayout.addView(linearLayoutBut);

                LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,  LinearLayout.LayoutParams.MATCH_PARENT);
                param.weight=1.0f;

                Button bNuevoGasto = new Button(this.getContext());
                bNuevoGasto.setText("Agregar Gasto");
                bNuevoGasto.setTextSize(TypedValue.COMPLEX_UNIT_SP,12);
                bNuevoGasto.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v)
                    {
                        showNewGastoDialog(v, persona);
                    }
                });


                Button removePerson = new Button(this.getContext());
                removePerson.setText("Quitar Persona");
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
