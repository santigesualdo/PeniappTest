package com.mobile.santige.nuevaaplicacion;

import java.io.Serializable;

/**
 * Created by CPU2007 on 07/10/2016.
 */

public class Gasto implements Serializable {
    private String descripcion;
    private Double monto;
    private Integer id;
    private Integer idPerson;

    public Gasto(String _descripcion, Double _monto){
        descripcion = _descripcion;
        monto = _monto;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Double getMonto() {
        return monto;
    }

    public void setMonto(Double monto) {
        this.monto = monto;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getIdPerson() {
        return idPerson;
    }

    public void setIdPerson(Integer idPerson) {
        this.idPerson = idPerson;
    }
}
