package com.ecp_project.carriere_eung.foodeqc.Entity;

/**
 * Created by Matthieu on 19/05/2016.
 *
 * Classe représentant le lien entre un produit composé et les différents produits de base qui le composent
 * Proportion must be between 0 and 100, and represents the mass proportion of an ingredient in an item
 */
public class Ingredient {
    private int id = -1;
    private Item item;
    private double proportion;
    private ComposedItem composedItem;

    public Ingredient(Item item, int proportion,ComposedItem composedItem) throws IllegalArgumentException{
        if (proportion>100 || proportion<0){
            throw new IllegalArgumentException("Proportion must be between 0 and 100");
        }
        else{
            this.item = item;
            this.proportion = proportion;
            this.composedItem = composedItem;
        }

    }
    public Ingredient(int id,Item item, int proportion,ComposedItem composedItem) throws IllegalArgumentException{
        if (proportion>100 || proportion<0){
            throw new IllegalArgumentException("Proportion must be between 0 and 100");
        }
        else{
            this.id = id;
            this.item = item;
            this.proportion = proportion;
            this.composedItem = composedItem;
        }

    }

    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
    }

    public double getProportion() {
        return proportion;
    }

    public void setProportion(double proportion) {
        this.proportion = proportion;
    }

    public int getId() { return id;}

    public ComposedItem getComposedItem() {
        return composedItem;
    }

    public void setComposedItem(ComposedItem composedItem) {
        this.composedItem = composedItem;
    }

    public String getName(){
        return this.getItem().getName();
    }

}
