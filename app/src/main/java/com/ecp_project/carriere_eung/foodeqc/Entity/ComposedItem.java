package com.ecp_project.carriere_eung.foodeqc.Entity;

import java.util.ArrayList;

/**
 * Created by Matthieu on 19/05/2016.
 */
public class ComposedItem extends Item{

    private ArrayList<Ingredient> ingredients;
    //Standard cost is assumed to be 20% :
    //this mean that Item CO2Equivalent will be sum of the ingredients' cost * 1,2
    private ProcessingCost cost = ProcessingCost.standard;

    public ComposedItem(String name, ArrayList<Ingredient> ingredients, ProcessingCost cost) {
        super(name, 0, ItemType.local);
        this.ingredients = ingredients;
        this.cost = cost;
    }


    public ComposedItem(String name, ArrayList<Ingredient> ingredients) {
        super(name, 0, ItemType.local);
        this.ingredients = ingredients;
        this.setCo2Equivalent(computeCO2Equivalent());

    }



    public double computeCO2Equivalent(){
        double result=-1;
        if (this.ingredients.size()>0){
            for (Ingredient ingredient:ingredients){
                result += ingredient.getItem().getCo2Equivalent()*ingredient.getProportion()*100;
            }
        }

        return result*this.getMalusFactor();
    }

    public ArrayList<Ingredient> getIngredients() {
        return ingredients;
    }

    public void setIngredients(ArrayList<Ingredient> ingredients) {
        this.ingredients = ingredients;
    }

    //used to compute CO2equivalent of a composedItem :
    //equals to the sum of its ingredients * this malus
    public double getMalusFactor(){
        double returnValue=0;
        switch (this.cost){
            case low:
                returnValue = 1.1;
                break;
            case standard:
                returnValue = 1.2;
                break;
            case high:
                returnValue = 1.3;
                break;
        }
        return returnValue;
    }
}
