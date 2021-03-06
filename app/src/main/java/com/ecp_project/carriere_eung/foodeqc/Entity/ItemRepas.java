package com.ecp_project.carriere_eung.foodeqc.Entity;

/**
 * Created by eung on 19/05/16.
 *
 * Classe représentant un item compris dans un repas.
 * Il permet ainsi de spécifier la quantité (en gramme) de l'item compris dans le repas)
 */
public class ItemRepas {

    private int id;
    private Item item;
    private int poids;


    public ItemRepas(Item item, int poids) {
        this.id = -1;
        this.item = item;
        this.poids = poids;
    }

    public ItemRepas(int id,Item item, int poids) {
        this.id = id;
        this.item = item;
        this.poids = poids;
    }

    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
    }

    public int getPoids() {
        return poids;
    }

    public void setPoids(int poids) {
        this.poids = poids;
    }

    public double getCo2Equivalent() {
        return item.getCo2Equivalent() * poids /100;
    }

    public int getId() {
        return this.id;
    }

}
