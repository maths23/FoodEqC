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

    private static final String KEY_BASE = "base";
    private static final String KEY_LOCAL = "local";
    private static final String KEY_IMPORTED = "imported";

    @Override
    public String toString() {
        String returnValue = "";
        switch (this){
            case base:
                returnValue = KEY_BASE;
                break;
            case local:
                returnValue = KEY_LOCAL;
                break;
            case imported:
                returnValue = KEY_IMPORTED;
                break;
        }
        return returnValue;
    }

    public static ItemType toItemType(String typeString){
        ItemType type =null;
        switch (typeString){
            case KEY_BASE:
                type=ItemType.base;
                break;
            case KEY_LOCAL:
                type=local;
                break;
            case KEY_IMPORTED:
                type=imported;
                break;
        }
        return type;
    }
}
