package com.mobile.santige.peniap;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Persona implements Serializable {

    private Integer id;
    private Integer peniaId;
    private String nombre;
    private List<Gasto> gastos;
    private int listID;
    private String mensajeSalida;

    public void Persona(){
        gastos = new ArrayList<Gasto>();
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public List<Gasto> getGastos() {
        return gastos;
    }

    public void setGastos(List<Gasto> gastos) {
        this.gastos = gastos;
    }

    public int getListID() {
        return listID;
    }

    public void setListID(int listID) {
        this.listID = listID;
    }

    public String getMensajeSalida() { return mensajeSalida; }

    public void setMensajeSalida(String mensajeSalida) {
        this.mensajeSalida = mensajeSalida;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getPeniaId() {
        return peniaId;
    }

    public void setPeniaId(Integer peniaId) {
        this.peniaId = peniaId;
    }
}
