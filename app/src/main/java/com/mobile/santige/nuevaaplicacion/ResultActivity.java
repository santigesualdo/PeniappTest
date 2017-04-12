package com.mobile.santige.nuevaaplicacion;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.Settings;
import android.support.annotation.NonNull;
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
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
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

    Boolean peniaGuardada;

    final static int idBotLayout = Menu.FIRST + 102,
            idTopLayout = Menu.FIRST + 100,
            bottomMenuHeight = 75;

    static String textPersonaSinGasto = "las personas sin gastos";

    DatabaseHandler db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler(this));

        peniaGuardada = false;

        db = DatabaseHandler.getInstance(this);

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

    private void updateView( final List<Persona> personas){
        setContentView(R.layout.activity_result);

        Button guardarButton = (Button) findViewById( R.id.guardarButton);
        guardarButton.setText("Guardar Peña");
        guardarButton.setTypeface(MainActivity.gothamBold);
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams )guardarButton.getLayoutParams();
        params.weight = 1;
        guardarButton.setLayoutParams(params);
        guardarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Penia penia = getPenia();
                Integer id = db.addPenia(penia).intValue();
                if (id!= -1) {
                    penia.setId(id);
                    for ( Persona p : personas){
                        if ((p.getGastos()!=null) && (!p.getNombre().equals(textPersonaSinGasto))){
                            p.setPeniaId( penia.getId());
                            Integer personId = db.addPerson(p,penia).intValue();
                            for (Gasto g: p.getGastos()){
                                db.addGasto(g, personId);
                            }
                        }
                    }
                    Toast.makeText(v.getContext(),"La peña fue guardada con exito.", Toast.LENGTH_SHORT).show();
                    peniaGuardada = true;
                }
            }

            @NonNull
            private Penia getPenia() {
                Calendar cal = Calendar.getInstance();
                Date currentLocalTime = cal.getTime();

                DateFormat date = new SimpleDateFormat("yyyy/MM/dd HH:mm");

                String localTime = date.format(currentLocalTime);

                Penia penia = new Penia();
                penia.setMonto(montoTotal);
                penia.setFecha(localTime);
                penia.setCountPersons(cantPersonas);
                penia.setActiva(1);
                return penia;
            }
        });

        Button bottomButton = (Button) findViewById( R.id.compartirButton);
        bottomButton.setTypeface(MainActivity.gothamBold);
        bottomButton.setText("Compartir");
        LinearLayout.LayoutParams params2 = (LinearLayout.LayoutParams )bottomButton.getLayoutParams();
        params2.weight = 1;
        bottomButton.setLayoutParams(params2);
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

        TextView textBottom = (TextView) findViewById(R.id.textoBottom);
        textBottom.setTypeface(MainActivity.gothamBold);
        textBottom.setText("Monto total gastado: $" + montoTotal);
        textBottom.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16.0F);

        Button inicioButtom = (Button) findViewById( R.id.volverInicioButton);
        inicioButtom.setTypeface(MainActivity.gothamBold);
        inicioButtom.setText(R.string.volver_al_menu);
        inicioButtom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Intent intent = new Intent(v.getContext(), MainActivity.class);
                if (peniaGuardada){
                    startActivity(intent);
                }else{
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

                    AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                    builder.setMessage(R.string.preguntaVerificacion)
                            .setPositiveButton("Si", dialogClickListener)
                            .setNegativeButton("No", dialogClickListener)
                            .show();
                }


            }
        });

        LinearLayout m_panel = new LinearLayout(this);
        m_panel.setOrientation(LinearLayout.VERTICAL);
        ScrollView vscroll = (ScrollView) findViewById(R.id.v_scroll);
        vscroll.addView(m_panel);
        ListView list = new ListView(this);
        if (resPerAdapter != null) {
            list.setAdapter(resPerAdapter);
        } else {
            resPerAdapter = new ResultPersonasAdapter(this, personas);
        }

        m_panel.addView(list);
    }

    private class ResultPersonasAdapter extends ArrayAdapter<Persona>{
        public ResultPersonasAdapter(Context context, List<Persona> personas){
            super(context,-1,-1, personas);
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            LinearLayout listLayout = new LinearLayout(ResultActivity.this);
            listLayout.setOrientation(LinearLayout.VERTICAL);
            Integer id = 5000;
            listLayout.setId(id);
            if (listLayout != null) {
                final Persona persona = super.getItem(position);

                final TextView listText = new TextView(ResultActivity.this);
                id++;
                listText.setId(id);

                listText.setText(persona.getNombre());
                listText.setTypeface(MainActivity.gothamBold);
                listText.setPadding(10,10, 10, 10);
                listText.setGravity(Gravity.CENTER_HORIZONTAL);
                listText.setBackground(getResources().getDrawable(R.drawable.button_subtittle2_shape));
                listText.setTextSize(TypedValue.COMPLEX_UNIT_SP,14);
/*                Random rnd = new Random();
                int color = Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));
                listLayout.setBackgroundColor(color);*/
                listText.setTextColor(Color.WHITE);

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
