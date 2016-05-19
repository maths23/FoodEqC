package com.ecp_project.carriere_eung.foodeqc.Entity;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by eung on 19/05/16.
 *
 * Classe représentant un repas
 */
public class Repas {

    private String nom;
    private String description;
    private Date date;


    private ArrayList<ItemRepas> elements;

    private int co2Equivalent;

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public ArrayList<ItemRepas> getElements() {
        return elements;
    }

    public void setElements(ArrayList<ItemRepas> elements) {
        this.elements = elements;
    }

    public void addElement(ItemRepas element) {
        this.elements.add(element);
    }

    public int getCo2Equivalent() {
        return co2Equivalent;
    }

    public void setCo2Equivalent(int co2Equivalent) {
        this.co2Equivalent = co2Equivalent;
    }
}
