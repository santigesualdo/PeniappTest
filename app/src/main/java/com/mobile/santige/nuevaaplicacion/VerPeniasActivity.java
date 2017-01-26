package com.mobile.santige.nuevaaplicacion;

import android.app.Activity;
import android.content.Context;
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

import java.text.DecimalFormat;
import java.util.List;

import static android.graphics.Color.WHITE;

public class VerPeniasActivity extends Activity {

    List<Penia> listaPenias;

    private PeniaAdapter myAdapter;

    final static int idTopLayout = Menu.FIRST + 100,
            idBack = Menu.FIRST + 101,
            idBotLayout = Menu.FIRST + 102,
            bottomMenuHeight = 75;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ver_penias);

        DatabaseHandler db = DatabaseHandler.getInstance(this);
        listaPenias = db.getAllPenias();

        if (listaPenias.size()==0){
            Toast.makeText(this, "No hay peñas guardadas.", Toast.LENGTH_SHORT).show();
        }

        updateView(listaPenias);

    }

    private void updateView(final List<Penia> listaPenias) {
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

        // +++++++++++++ MIDDLE COMPONENT: all our GUI content
        LinearLayout midLayout = new LinearLayout(this);
        midLayout.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
        midLayout.setBackgroundColor(WHITE);
        midLayout.setOrientation(LinearLayout.VERTICAL);

        Button addPerson = new Button(this);
        addPerson.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        addPerson.setText("Agregar Persona con Gastos");
        addPerson.setTextSize(TypedValue.COMPLEX_UNIT_SP,14);
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
            myAdapter = new VerPeniasActivity.PeniaAdapter(this, listaPenias);
        }

        m_panel.addView(list);
        setContentView(global_panel);

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

            listLayout.setBackgroundResource(R.drawable.border);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
            //params.setMargins(20,20,20,20);
            listLayout.setLayoutParams(params);
            listLayout.setPadding(10,10,10,10);


            if (listLayout != null) {

                final Penia penia = super.getItem(position);

                final TextView listText = new TextView(VerPeniasActivity.this);
                listText.setId(5001);

                LinearLayout personaEditLay = new LinearLayout(VerPeniasActivity.this);
                // linearLayoutBut.setPadding(5,5,5,5);
                personaEditLay.setWeightSum(2);
                personaEditLay.setOrientation(LinearLayout.HORIZONTAL);
                personaEditLay.setBackgroundColor(WHITE);
                listLayout.addView(personaEditLay);

                listText.setText(penia.getFecha());
                listText.setBackgroundColor(Color.TRANSPARENT);

                listText.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_START);
                listText.setGravity(Gravity.CENTER_VERTICAL);
                listText.setTextSize(TypedValue.COMPLEX_UNIT_SP,16);
                LinearLayout.LayoutParams param2 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.MATCH_PARENT);
                param2.weight=1.8f;
                listText.setLayoutParams(param2);

                final ImageButton but = new ImageButton(VerPeniasActivity.this);
                but.setBackgroundColor(Color.TRANSPARENT);
                but.setImageResource(R.drawable.penc);
                LinearLayout.LayoutParams param3 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.MATCH_PARENT);
                param3.weight=0.2f;
                but.setLayoutParams(param3);

                personaEditLay.addView(but);
                personaEditLay.addView(listText);

                listLayout.setBackgroundColor(WHITE);

                int _id = 5001;
                final LinearLayout gastosLayout = new LinearLayout(VerPeniasActivity.this);
                gastosLayout.setOrientation(LinearLayout.VERTICAL);

                final TextView listDescripGasto = new TextView(VerPeniasActivity.this);

                /*if (persona.getGastos() != null) {
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
                }*/

                LinearLayout linearLayoutBut = new LinearLayout(VerPeniasActivity.this);
                // linearLayoutBut.setPadding(5,5,5,5);
                linearLayoutBut.setWeightSum(2);
                linearLayoutBut.setOrientation(LinearLayout.HORIZONTAL);
                linearLayoutBut.setBackgroundColor(WHITE);
                listLayout.addView(linearLayoutBut);

                LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,  LinearLayout.LayoutParams.MATCH_PARENT);
                param.weight=1.0f;

                return listLayout;
            }

            return null;
        }
    }
}
