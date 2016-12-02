package com.mobile.santige.nuevaaplicacion;

import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class PersonListActivity extends AppCompatActivity {

    final static int idTopLayout = Menu.FIRST + 100,
            idBack = Menu.FIRST + 101,
            idBotLayout = Menu.FIRST + 102;
    private PersonaAdapter myAdapter;

    static public List<Persona> listaPersonas;
    static public double montoTotal;
    static public double montoPorPera;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        int numPersons = Integer.parseInt(PersonSelectionActivity.personasCount);

        listaPersonas = PersonSelectionActivity._listaPersonas;

        updateView(listaPersonas);
    }

    private void updateView(List<Persona> personas) {

        //Create our top content holder
        RelativeLayout global_panel = new RelativeLayout(this);
        global_panel.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT));
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
        m_bCancel.setText("Lista de Personas:");
        nTextH = 18;
        m_bCancel.setTextSize(nTextH);
        m_bCancel.setTypeface(Typeface.create("arial", Typeface.BOLD));
        RelativeLayout.LayoutParams lpbCancel =
                new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        lpbCancel.addRule(RelativeLayout.CENTER_IN_PARENT);
        ibMenu.addView(m_bCancel, lpbCancel);

        // +++++++++++++ BOTTOM COMPONENT: the footer
        RelativeLayout ibMenuBot = new RelativeLayout(this);
        ibMenuBot.setId(idBotLayout);
        ibMenuBot.setBackgroundColor(Color.argb(100,0,256,0));
        RelativeLayout.LayoutParams botParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        botParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        global_panel.addView(ibMenuBot, botParams);

        // textview in ibMenu : card holder
        TextView cTVBot = new TextView(this);
        cTVBot.setText("Monto total gastado: ");
        cTVBot.setTextSize(16);
        cTVBot.setTypeface(Typeface.create("arial", Typeface.BOLD));
        RelativeLayout.LayoutParams lpcTVBot = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        lpcTVBot.addRule(RelativeLayout.ALIGN_PARENT_LEFT);

        // botton
        Button bottomButton = new Button(this);
        bottomButton.setText("LISTO!");
        bottomButton.setWidth(50);
        bottomButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), ResultActivity.class);
                startActivity(intent);
            }
        });
        RelativeLayout.LayoutParams lpcButton = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        lpcButton.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);

        ibMenuBot.addView(cTVBot, lpcTVBot);
        ibMenuBot.addView(bottomButton, lpcButton);

        // +++++++++++++ MIDDLE COMPONENT: all our GUI content
        LinearLayout midLayout = new LinearLayout(this);
        midLayout.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
        midLayout.setOrientation(LinearLayout.VERTICAL);
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
        montoPorPera = 0;
        for (Persona pe : personas) {
            if (pe.getGastos() != null) {
                for (Gasto g : pe.getGastos()) {
                    montoTotal += g.getMonto();
                }
            }
        }

        DecimalFormat df = new DecimalFormat("#.00");
        montoPorPera = montoTotal / Integer.parseInt(PersonSelectionActivity.personasCount);

        if (montoTotal > 0) {
            cTVBot.setText("Monto por persona: $" + df.format(montoPorPera) + "\nMonto total gastado: $" + montoTotal);
        } else {
            cTVBot.setText("Monto total gastado: $" + df.format(montoTotal));
        }


        m_panel.addView(list);
        setContentView(global_panel);

    }

    public void onResume() {
        super.onResume();

        Persona p = (Persona) getIntent().getSerializableExtra("editedPerson");

        if (p != null) {
            PersonSelectionActivity._listaPersonas.set(p.getListID(), p);
        }

        listaPersonas = PersonSelectionActivity._listaPersonas;

        updateView(listaPersonas);
    }

    public void viewPersonActivity(View view, Persona p) {
        Intent intent = new Intent(this, PersonDetail_Activity.class);
        intent.putExtra("person", p);
        startActivityForResult(intent, 2);
    }


    private class PersonaAdapter extends ArrayAdapter<Persona> {
        public PersonaAdapter(Context context, List<Persona> personas) {
            super(context, -1, -1, personas);
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {

            LinearLayout listLayout = new LinearLayout(PersonListActivity.this);
            listLayout.setOrientation(LinearLayout.VERTICAL);
            listLayout.setId(5000);

            if (listLayout != null) {

                final Persona persona = super.getItem(position);

                final TextView listText = new TextView(PersonListActivity.this);
                listText.setId(5001);

                listText.setText(persona.getNombre());
                listText.setPadding(0, 5, 0, 5);
                listText.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                listText.setTextSize(16);
                Random rnd = new Random();
                int color = Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));
                listLayout.setBackgroundColor(color);
                listLayout.addView(listText);

                int _id = 5001;
                if (persona.getGastos() != null) {
                    LinearLayout gastosLayout = new LinearLayout(PersonListActivity.this);
                    gastosLayout.setOrientation(LinearLayout.VERTICAL);

                    Double monto = 0.0;
                    int cantidad = persona.getGastos().size();
                    for (Gasto g : persona.getGastos()) {
                        monto += g.getMonto();
                    }

                    final TextView listDescripGasto = new TextView(PersonListActivity.this);
                    _id++;
                    listDescripGasto.setId(_id);
                    listDescripGasto.setBackgroundColor(color);
                    listDescripGasto.setTextColor(Color.BLUE);
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

                listLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        viewPersonActivity(v, persona);
                    }
                });

                return listLayout;
            }

            return null;
        }
    }
}
