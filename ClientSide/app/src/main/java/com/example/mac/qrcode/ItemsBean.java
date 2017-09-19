package com.example.mac.qrcode;

import java.util.ArrayList;

/**
 * Created by mac on 15/10/16.
 */

public class ItemsBean {
    private double precio;
    private double calorias;
    private String id;
    private String foto;
    ItemsBean(double calorias,double precio,String foto,String id){
        this.calorias=calorias;
        this.id=id;
        this.foto=foto;
        this.precio=precio;
    }
    public String getId(){return id;}
    public double getprecio(){
        return precio;
    }
    public String getFoto(){
        return foto;
    }
    public double getCalorias(){
        return calorias;
    }
    public void setFoto(String foto){
        this.foto=foto;
    }
    public void setPrecio(int precio){
        this.precio=precio;
    }
    public void setCalorias(int calorias){
        this.calorias=calorias;
    }

}
