package com.mobile.santige.nuevaaplicacion;

import java.io.Serializable;

public class Penia implements Serializable {

    private Integer id;
    private Double monto;
    private String fecha;
    private Integer countPersons;
    private Integer activa;

    public Penia( Integer id, Double monto, String fecha, Integer countPersons, Integer activa){
        this.id = id;
        this.monto = monto;
        this.fecha = fecha;
        this.countPersons = countPersons;
        this.activa = activa;
    }
    public Penia(){};

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Double getMonto() {
        return monto;
    }

    public void setMonto(Double monto) {
        this.monto = monto;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public Integer getCountPersons() {
        return countPersons;
    }

    public void setCountPersons(Integer countPersons) {
        this.countPersons = countPersons;
    }

    public Integer getActiva() {
        return activa;
    }

    public void setActiva(Integer activa) {
        this.activa = activa;
    }
}
