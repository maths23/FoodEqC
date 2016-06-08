package com.ecp_project.carriere_eung.foodeqc.Entity;

/**
 * Created by Matthieu on 19/05/2016.
 *
 * Enumère les différents types d'items
 * base : produits de base
 * composed :  ?
 * imported : produits créés par les utilisateurs
 *
 */
public enum ItemType {
    base, composed,imported,created;

    private static final String KEY_BASE = "base";
    private static final String KEY_COMPOSED = "composed";
    private static final String KEY_IMPORTED = "imported";
    private static final String KEY_CREATED = "created";

    @Override
    public String toString() {
        String returnValue = "";
        switch (this){
            case base:
                returnValue = KEY_BASE;
                break;
            case composed:
                returnValue = KEY_COMPOSED;
                break;
            case imported:
                returnValue = KEY_IMPORTED;
                break;
            case created:
                returnValue = KEY_CREATED;
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
            case KEY_COMPOSED:
                type= composed;
                break;
            case KEY_IMPORTED:
                type=imported;
                break;
            case KEY_CREATED:
                type=created;
        }
        return type;
    }
}
