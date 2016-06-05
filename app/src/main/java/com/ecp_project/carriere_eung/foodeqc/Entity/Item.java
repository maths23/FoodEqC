package com.ecp_project.carriere_eung.foodeqc.Entity;


import android.content.Context;

import com.ecp_project.carriere_eung.foodeqc.R;

/**
 * Created by Matthieu on 19/05/2016.
 *
 * Classe générale représentant un produit.
 * L'ID est gérée par la database. Pas d'autoincrémentation à partir d'une clé dans cette classe.
 *
 */
public class Item {


    private int id =-1;
    //private static int idKey =0;
    private String name;

    private double co2Equivalent;

    private ItemType type;


    public Item(int id,String name, double co2Equivalent, ItemType type) {

        this.id = id;
        this.name = name;
        this.co2Equivalent = co2Equivalent;
        this.type = type;

    }

    public Item(String name, double co2Equivalent, ItemType type) {
        this.name = name;
        this.co2Equivalent = co2Equivalent;
        this.type = type;
    }

    public int getId() {
        return id;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getCo2Equivalent() {
        return co2Equivalent;
    }

    public void setCo2Equivalent(double co2Equivalent) {
        this.co2Equivalent = co2Equivalent;
    }

    public ItemType getType() {
        return type;
    }


    public String toString(Context context) {
        return name + context.getString(R.string.itemToString1)+ " " + co2Equivalent + context.getString(R.string.itemToString2)+ " " + type.toString();
    }
}
