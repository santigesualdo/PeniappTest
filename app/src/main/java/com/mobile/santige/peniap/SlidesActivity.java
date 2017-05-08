package com.mobile.santige.peniap;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import com.github.paolorotolo.appintro.AppIntro;
import com.github.paolorotolo.appintro.AppIntroFragment;

import java.util.List;


public class SlidesActivity extends AppIntro {

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

        addSlide(AppIntroFragment.newInstance(getString(R.string.tip1) , getString(R.string.tip1value), 0, getResources().getColor(R.color.backColor)));
        addSlide(AppIntroFragment.newInstance(getString(R.string.tip2) , getString(R.string.tip2value), 0, getResources().getColor(R.color.backColor)));
        addSlide(AppIntroFragment.newInstance(getString(R.string.tip3) , getString(R.string.tip3value), 0, getResources().getColor(R.color.backColor)));
        addSlide(AppIntroFragment.newInstance(getString(R.string.tip4) , getString(R.string.tip4value), 0, getResources().getColor(R.color.backColor)));
        addSlide(AppIntroFragment.newInstance(getString(R.string.tip5) , getString(R.string.tip5value), 0, getResources().getColor(R.color.backColor)));
        addSlide(AppIntroFragment.newInstance(getString(R.string.tip6) , getString(R.string.tip6value), 0, getResources().getColor(R.color.backColor)));
        addSlide(AppIntroFragment.newInstance(getString(R.string.tip7) , getString(R.string.tip7value), 0, getResources().getColor(R.color.backColor)));
        addSlide(AppIntroFragment.newInstance(getString(R.string.tip8) , getString(R.string.tip8value), 0, getResources().getColor(R.color.backColor)));
        addSlide(AppIntroFragment.newInstance(getString(R.string.tip9) , getString(R.string.tip9value), 0, getResources().getColor(R.color.backColor)));

        setSkipText("Omitir");
        setDoneText("Empezar");

        // OPTIONAL METHODS
        // Override bar/separator color.
         setBarColor(getResources().getColor(R.color.buttonPressedColor));
         setSeparatorColor(Color.WHITE);

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
