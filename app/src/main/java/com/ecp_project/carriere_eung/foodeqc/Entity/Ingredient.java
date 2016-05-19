package com.ecp_project.carriere_eung.foodeqc.Entity;

/**
 * Created by Matthieu on 19/05/2016.
 *
 * Classe représentant le lien entre un produit composé et les différents produits de base qui le composent
 * Proportion must be between 0 and 100, and represents the mass proportion of an ingredient in an item
 */
public class Ingredient {
    private Item item;
    private int proportion;

    public Ingredient(Item item, int proportion) throws IllegalArgumentException{
        if (proportion>100 || proportion<0){
            throw new IllegalArgumentException("Proportion must be between 0 and 100");
        }
        else{
            this.item = item;
            this.proportion = proportion;
        }

    }

    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
    }

    public int getProportion() {
        return proportion;
    }

    public void setProportion(int proportion) {
        this.proportion = proportion;
    }
}
