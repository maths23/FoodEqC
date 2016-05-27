package com.ecp_project.carriere_eung.foodeqc.Entity;

/**
 * Created by Matthieu on 19/05/2016.
 *
 * Enumère les différents types d'items
 * base : produits de base
 * local :  ?
 * imported : produits créés par les utilisateurs
 *
 */
public enum ItemType {
    base,local,imported;

    @Override
    public String toString() {
        String returnValue = "";
        switch (this){
            case base:
                returnValue = "base";
                break;
            case local:
                returnValue = "local";
                break;
            case imported:
                returnValue = "imported";
                break;
        }
        return returnValue;
    }
}
