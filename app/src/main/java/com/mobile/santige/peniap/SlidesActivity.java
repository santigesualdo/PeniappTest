package com.mobile.santige.peniap;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import com.github.paolorotolo.appintro.AppIntro2;
import com.github.paolorotolo.appintro.AppIntroFragment;

import java.util.List;


public class SlidesActivity extends AppIntro2 {

    public Activity mActivity;

    private List<Persona> listaPersonas;
    private GrupoPersonas grupoPersonas;
    private double montoTotal;
    private String nombrePenia;
    int numPersons;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle bundleObject = getIntent().getExtras();

        grupoPersonas = (GrupoPersonas) bundleObject.getSerializable("array_personas");
        listaPersonas = grupoPersonas.get_listaPersonas();
        numPersons = getIntent().getIntExtra("personasCount",0);

        addSlide(AppIntroFragment.newInstance("Ayudin", "Peñapp te ayuda a calcular gastos dentro de una peña con dos o mas gastos.", 0, getResources().getColor(R.color.backColor)));
        addSlide(AppIntroFragment.newInstance("Datos de la Peña", "Ingresa el nombre de la peña.", 0, getResources().getColor(R.color.backColor)));
        addSlide(AppIntroFragment.newInstance("Datos de Personas", "Ingresa el nombre de la persona y el total que gasto.", 0, getResources().getColor(R.color.backColor)));
        addSlide(AppIntroFragment.newInstance("Agregar Amigos", "Puedes agregar todos los que quieras.", 0, getResources().getColor(R.color.backColor)));
        addSlide(AppIntroFragment.newInstance("Eliminar Amigos", "Si te equivocaste puedes borrar amigos.", 0, getResources().getColor(R.color.backColor)));
        addSlide(AppIntroFragment.newInstance("Continuar.", "Cuando ya cargaste todo lo anterior, podes continuar.", 0, getResources().getColor(R.color.backColor)));
        addSlide(AppIntroFragment.newInstance("Ingresar personas sin gastos.", "Toca la barra para indicar cuantos amigos participaron y que no tuvieron gastos.", 0, getResources().getColor(R.color.backColor)));
        addSlide(AppIntroFragment.newInstance("Listo!.", "La peña ya casi esta lista, presiona continuar para ver resultados.", 0, getResources().getColor(R.color.backColor)));
        addSlide(AppIntroFragment.newInstance("Resultados.", "Puedes guardar y compartir los resultados por Whatsapp.", 0, getResources().getColor(R.color.backColor)));

        //setSkipText("Omitir");
        //setDoneText("Listo.");

        // OPTIONAL METHODS
        // Override bar/separator color.
        // setBarColor(Color.parseColor("#3F51B5"));
        // setSeparatorColor(Color.parseColor("#2196F3"));

        // Hide Skip/Done button.
        showSkipButton(true);
        setProgressButtonEnabled(true);
    }

    void iniciarPenia(){
        Intent intent = new Intent(this, PersonListActivity.class );
        Bundle b = new Bundle();

        b.putSerializable("array_personas", grupoPersonas);
        intent.putExtras(b);
        intent.putExtra("personasCount", numPersons);
        startActivity(intent);
    }

    @Override
    public void onSkipPressed(Fragment currentFragment) {
        super.onSkipPressed(currentFragment);
        iniciarPenia();
    }

    @Override
    public void onDonePressed(Fragment currentFragment) {
        super.onDonePressed(currentFragment);
        iniciarPenia();
    }

    @Override
    public void onSlideChanged(@Nullable Fragment oldFragment, @Nullable Fragment newFragment) {
        super.onSlideChanged(oldFragment, newFragment);
        // Do something when the slide changes.
    }

}
