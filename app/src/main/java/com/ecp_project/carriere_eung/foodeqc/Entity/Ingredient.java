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
    private String composedItemName;
    private double proportion;


    public Ingredient(Item item, String composedItemName, double proportion) throws IllegalArgumentException{
        if (proportion>100 || proportion<0){
            throw new IllegalArgumentException("Proportion must be between 0 and 100");
        }
        else{
            this.item = item;
            this.proportion = proportion;
            this.composedItemName = composedItemName;
        }

    }
    public Ingredient(int id, Item item, String composedItem, double proportion) throws IllegalArgumentException{
        if (proportion>100 || proportion<0){
            throw new IllegalArgumentException("Proportion must be between 0 and 100");
        }
        else{
            this.id = id;
            this.item = item;
            this.proportion = proportion;
            this.composedItemName = composedItem;
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

    public String getComposedItemName() {
        return composedItemName;
    }

    public void setComposedItemName(String composedItemName) {
        this.composedItemName = composedItemName;
    }

    public String getName(){
        return this.getItem().getName();
    }

}
