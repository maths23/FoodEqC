package com.ecp_project.carriere_eung.foodeqc.Entity;

import android.util.Log;

import java.util.ArrayList;
import com.ecp_project.carriere_eung.foodeqc.Entity.ProcessingCost;

/**
 * Created by Matthieu on 19/05/2016.
 *
 * Classe représentant un Item composé, c'est à dire un item
 * qui ne figure pas dans la liste de base et qui a été ajouté par l'Utilisateur
 */
public class ComposedItem extends Item{

    private ArrayList<Ingredient> ingredients;
    //Standard cost is assumed to be 20% :
    //this mean that Item CO2Equivalent will be sum of the ingredients' cost * 1,2
    private ProcessingCost cost = ProcessingCost.standard;

    //ne dois pas être utilisé avec des types "base".
    public ComposedItem(String name,ItemType type, ArrayList<Ingredient> ingredients, ProcessingCost cost) {
        super(name, 0, type);
        this.ingredients = ingredients;
        this.cost = cost;
        this.setCo2Equivalent(computeCO2Equivalent());


    }


    public ComposedItem(String name,ItemType type, ArrayList<Ingredient> ingredients) {
        super(name, 0, type);
        this.ingredients = ingredients;
        this.setCo2Equivalent(computeCO2Equivalent());

    }

    public ComposedItem(int id, String name, ItemType type, ArrayList<Ingredient> ingredients, ProcessingCost cost) {
        super(id, name, 0, type);
        this.ingredients = ingredients;
        this.cost = cost;
        this.setCo2Equivalent(computeCO2Equivalent());

    }

    public ComposedItem(int id, String name, ItemType type, ArrayList<Ingredient> ingredients) {
        super(id, name, 0, type);
        this.ingredients = ingredients;
        this.setCo2Equivalent(computeCO2Equivalent());

    }

    public double computeCO2Equivalent(){
        double result=0;
        if (this.ingredients.size()>0){
            for (Ingredient ingredient:ingredients){
                result += ingredient.getItem().getCo2Equivalent()*ingredient.getProportion()/100;
            }
        }
        double test = 1.2;
        ProcessingCost test2 = ProcessingCost.standard;
        Log.e("Composed","testing equals "+( test == test2.getMalusFactor()));
        Log.e("Composed","testing equals bis "+( test == 1.2));
        Log.e("Composed",""+result);
        if (this.getCost() == null){
            Log.e("Composed","c'est ça");
        }
        Log.e("Composed",this.cost.toString());
        return result*this.getCost().getMalusFactor();
    }

    public ArrayList<Ingredient> getIngredients() {
        return ingredients;
    }

    public void setIngredients(ArrayList<Ingredient> ingredients) {
        this.ingredients = ingredients;
    }


    //used to compute CO2equivalent of a composedItem :
    //equals to the sum of its ingredients * this malus


    public ProcessingCost getCost() {
        return cost;
    }

    public void setCost(ProcessingCost cost) {
        this.cost = cost;
    }
}
