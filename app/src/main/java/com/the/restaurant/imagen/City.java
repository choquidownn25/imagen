package com.the.restaurant.imagen;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

/**
 * Created by choqu_000 on 21/12/2015.
 */
public class City {
    //Atributo
    protected int id;
    protected String name;
    protected String data;
    protected int precio;
    protected Bitmap photo;

    public City(int id, String name, int precio) {
        this.id = id;
        this.name = name;
        this.precio = precio;
    }

    public int getPrecio(){return precio;}

    public void setPrecio(int precio){
        this.precio=precio;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
        try {
            byte[] byteData = Base64.decode(data, Base64.DEFAULT);
            this.photo = BitmapFactory.decodeByteArray(byteData, 0, byteData.length);
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }

    public Bitmap getPhoto() {
        return photo;
    }
}
