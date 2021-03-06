package com.ecp_project.carriere_eung.foodeqc.Entity;

import com.ecp_project.carriere_eung.foodeqc.Exception.ItemNotFoundException;
import com.ecp_project.carriere_eung.foodeqc.R;

import java.util.ArrayList;
import java.util.GregorianCalendar;

/**
 * Created by eung on 19/05/16.
 *
 * Classe représentant un repas
 */
public class Repas {

    private int id;
    private GregorianCalendar date;
    private RepasType repasType;


    private ArrayList<ItemRepas> elements;

    private double co2Equivalent;


    public Repas(GregorianCalendar date, RepasType repasType) {
        this.id = -1;
        this.date = date;
        this.repasType = repasType;

        this.elements = new ArrayList<>();
        this.co2Equivalent = 0;
    }

    public Repas(int id,GregorianCalendar date, RepasType repasType, ArrayList<ItemRepas> elements, double co2Equivalent) {
        this.id = id;
        this.date = date;
        this.repasType = repasType;
        this.elements = elements;
        this.co2Equivalent = co2Equivalent;
    }

    public GregorianCalendar getDate() {
        return date;
    }

    public void setDate(GregorianCalendar date) {
        this.date = date;
    }

    public ArrayList<ItemRepas> getElements() {
        return elements;
    }

    public void setElements(ArrayList<ItemRepas> elements) {

        this.elements = elements;
        updateCO2Equivalent();
    }

    public void addElement(ItemRepas element) {
        this.elements.add(element);
        this.co2Equivalent += element.getCo2Equivalent();
        updateCO2Equivalent();
    }

    public void removeElement(ItemRepas element) throws ItemNotFoundException {
        if (this.elements.contains(element)) {
            this.elements.remove(element);
            updateCO2Equivalent();
        }
        else {
            throw new ItemNotFoundException();
        }
    }

    public double getCo2Equivalent() {
        return co2Equivalent;
    }

    public RepasType getRepasType() {
        return repasType;
    }

    public void setRepasType(RepasType repasType) {
        this.repasType = repasType;
    }

    public int getId() {
        return this.id;
    }

    public void updateCO2Equivalent() {
        double returnValue = 0;
        for (ItemRepas itemRepas: this.elements
             ) {
            returnValue += itemRepas.getCo2Equivalent();
        }
        this.co2Equivalent = returnValue;
    }
}
