package com.mobile.santige.nuevaaplicacion;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.List;

public class GastoDetail extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gasto_detail);

        Intent i = getIntent();
        Persona persona = (Persona) i.getSerializableExtra("gastos");

        if (persona.getGastos()!=null){

        }

    }
}
