package com.ecp_project.carriere_eung.foodeqc.Entity;


/**
 * Created by Matthieu on 19/05/2016.
 *
 * Classe générale représentant un produit.
 *
 */
public class Item {


    private int id;
    private static int idKey =0;
    private String name;

    private double co2Equivalent;

    private ItemType type;


    public Item(String name, double co2Equivalent, ItemType type) {
        this.id = idKey;
        idKey += 1;
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

    @Override
    public String toString() {
        return name + "has an equivalent of " + co2Equivalent + " gCO2/100g and is " + type.toString();
    }
}
