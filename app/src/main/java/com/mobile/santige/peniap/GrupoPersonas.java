package com.mobile.santige.peniap;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by CPU2007 on 23/01/2017.
 */

public class GrupoPersonas implements Serializable {

    private List<Persona> _listaPersonas;

    public GrupoPersonas(){
        _listaPersonas = new ArrayList<Persona>();
    }

    public List<Persona> get_listaPersonas() {
        return _listaPersonas;
    }

    public void set_listaPersonas(List<Persona> _listaPersonas) {
        this._listaPersonas = _listaPersonas;
    }
}
