package com.ecp_project.carriere_eung.foodeqc.Entity;

import android.widget.Toast;

import com.ecp_project.carriere_eung.foodeqc.Exception.ItemNotFoundException;

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
    private RepasType repasType;


    private ArrayList<ItemRepas> elements;

    private int co2Equivalent;


    public Repas(String nom, String description, Date date, RepasType repasType) {
        this.nom = nom;
        this.description = description;
        this.date = date;
        this.repasType = repasType;

        this.elements = new ArrayList<>();
        this.co2Equivalent = 0;
    }

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
        this.co2Equivalent += element.getItem().getCo2Equivalent();
    }

    public void removeElement(ItemRepas element) throws ItemNotFoundException {
        if (this.elements.contains(element)) {
            this.elements.remove(element);
            this.co2Equivalent -= element.getItem().getCo2Equivalent();
        }
        else {
            throw new ItemNotFoundException();
        }
    }

    public int getCo2Equivalent() {
        return co2Equivalent;
    }

    public RepasType getRepasType() {
        return repasType;
    }

    public void setRepasType(RepasType repasType) {
        this.repasType = repasType;
    }
}
