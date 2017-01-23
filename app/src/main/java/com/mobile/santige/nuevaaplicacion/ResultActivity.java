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
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.List;
import java.util.Locale;
import java.util.Random;

public class ResultActivity extends Activity {

    private ResultPersonasAdapter resPerAdapter;
    List<Persona> personas;
    Double montoTotal;
    Integer cantPersonas;
    Double montoPorPera;
    Integer personasSinGasto;

    Persona personaSinGasto;

    String mensajeWhatsapp = "";

    final static int idBotLayout = Menu.FIRST + 102,
            idTopLayout = Menu.FIRST + 100,
            bottomMenuHeight = 75;

    static String textPersonaSinGasto = "las personas sin gastos";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle bundleObject = getIntent().getExtras();
        GrupoPersonas grupoPersonas = (GrupoPersonas) bundleObject.getSerializable("array_personas");

        personas = grupoPersonas.get_listaPersonas();
        montoTotal = getIntent().getExtras().getDouble("monto_total");
        cantPersonas = getIntent().getExtras().getInt("count_persons");
        montoPorPera = montoTotal / cantPersonas;

        personasSinGasto = cantPersonas - personas.size();
        textPersonaSinGasto = "Las " + personasSinGasto + " personas sin gastos";

        mensajeWhatsapp="Resultados de peña: \n";

        if (cantPersonas > personas.size()){
            personaSinGasto = new Persona();
            personaSinGasto.setNombre(textPersonaSinGasto);
            personas.add(personaSinGasto);
        }

        updateView(personas);
    }

    public void onResume() {
        super.onResume();
        updateView(personas);
    }

    @Override
    public void onBackPressed() {
        personas.remove(personaSinGasto);
        GrupoPersonas groupToSend = new GrupoPersonas();
        groupToSend.set_listaPersonas(personas);
        Intent intent = new Intent(this, PersonSelectionActivity.class);
        Bundle bundleObject = new Bundle();
        bundleObject.putSerializable("array_personas", groupToSend);
        intent.putExtras(bundleObject);
        intent.putExtra("monto_total", montoTotal);
        startActivity(intent);
    }

    public static int getContrastColor(int color) {
        double y = (299 * Color.red(color) + 587 * Color.green(color) + 114 * Color.blue(color)) / 1000;
        return y >= 128 ? Color.BLACK : Color.WHITE;
    }

    private void updateView(final List<Persona> personas) {

        RelativeLayout global_panel = new RelativeLayout(this);
        global_panel.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT));
        global_panel.setGravity(Gravity.FILL);

        // +++++++++++++ TOP COMPONENT: the header
        RelativeLayout ibMenu = new RelativeLayout(this);
        ibMenu.setId(idTopLayout);

        RelativeLayout.LayoutParams topParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        topParams.addRule(RelativeLayout.ALIGN_PARENT_TOP);
        global_panel.addView(ibMenu, topParams);

        // +++++++++++++ BOTTOM COMPONENT: the footer
        RelativeLayout ibMenuBot = new RelativeLayout(this);
        ibMenuBot.setId(idBotLayout);
        ibMenuBot.setBackgroundColor(Color.argb(100,0,256,0));
        ibMenuBot.setMinimumHeight(bottomMenuHeight);
        RelativeLayout.LayoutParams botParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        botParams.addRule(RelativeLayout.CENTER_IN_PARENT);
        botParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        global_panel.addView(ibMenuBot, botParams);

        // textview in ibMenu : card holder
        TextView cTVBot = new TextView(this);
        cTVBot.setPadding(0,0,0,0);
        cTVBot.setText("Monto total gastado: ");
        cTVBot.setTextSize(TypedValue.COMPLEX_UNIT_SP,16);
        cTVBot.setTypeface(Typeface.create("arial", Typeface.BOLD));
        RelativeLayout.LayoutParams lpcTVBot = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        lpcTVBot.addRule(RelativeLayout.CENTER_IN_PARENT);
        lpcTVBot.addRule(RelativeLayout.ALIGN_TOP);

        // botton
        Button bottomButton = new Button(this);
        bottomButton.setText("Compartir!");
        bottomButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent whatsappIntent = new Intent(Intent.ACTION_SEND);
                whatsappIntent.setType("text/plain");
                whatsappIntent.setPackage("com.whatsapp");
                for (Persona p : personas){
                    if (p.getMensajeSalida()!=null){
                        mensajeWhatsapp+= p.getMensajeSalida()+"\n";
                    }
                }
                whatsappIntent.putExtra(Intent.EXTRA_TEXT, mensajeWhatsapp + "\nGenerado por Peñapp.");
                try {
                    startActivity(whatsappIntent);
                } catch (android.content.ActivityNotFoundException ex) {
                    Toast.makeText(v.getContext(),"Whatsapp have not been installed.", Toast.LENGTH_SHORT ).show();
                }
            }
        });
        RelativeLayout.LayoutParams lpcButton = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        lpcButton.addRule(RelativeLayout.CENTER_IN_PARENT);
        lpcButton.addRule(RelativeLayout.ALIGN_BOTTOM);

        //ibMenuBot.addView(cTVBot, lpcTVBot);
        ibMenuBot.addView(bottomButton, lpcButton);

        // +++++++++++++ MIDDLE COMPONENT: all our GUI content
        LinearLayout midLayout = new LinearLayout(this);
        midLayout.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
        midLayout.setBackgroundColor(Color.WHITE);
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
        if (resPerAdapter != null) {
            list.setAdapter(resPerAdapter);
        } else {
            resPerAdapter = new ResultPersonasAdapter(this, personas);
        }

        m_panel.addView(list);
        setContentView(global_panel);
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
                listText.setTextSize(TypedValue.COMPLEX_UNIT_SP,16);
                Random rnd = new Random();
                int color = Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));
                listLayout.setBackgroundColor(color);
                listText.setTextColor(getContrastColor(color));

                double acumGastosPersona = 0;
                double montoAPoner= 0;
                if (persona.getGastos()!= null){
                    for (Gasto g : persona.getGastos()) {
                        acumGastosPersona += g.getMonto();
                    }
                }
                montoAPoner= montoPorPera-acumGastosPersona;
                DecimalFormat df = new DecimalFormat("0", DecimalFormatSymbols.getInstance(Locale.ENGLISH));
                df.setMaximumFractionDigits(2); //340 = DecimalFormat.DOUBLE_FRACTION_DIGITS
                df.setMinimumFractionDigits(2);//FractionDigits(2);

                String mensaje="";
                if (persona.getNombre() != textPersonaSinGasto){
                    if (montoAPoner > 0 ) {
                        persona.setMensajeSalida(persona.getNombre() + " debe pagar $" + df.format(Math.abs(montoAPoner)));
                    }else if (montoAPoner<0){
                        persona.setMensajeSalida(persona.getNombre() + " debe recuperar $ " + df.format(Math.abs(montoAPoner)));
                    }else if (montoAPoner == 0){
                        persona.setMensajeSalida(persona.getNombre() + " ya esta derecho de gastos.");
                    }
                }else{
                    persona.setMensajeSalida(persona.getNombre() + " deben pagar $" + df.format(Math.abs(montoPorPera)));
                }

                listText.setText(persona.getMensajeSalida());
                listLayout.addView(listText);

            }
            return listLayout;
        }
    }
}
