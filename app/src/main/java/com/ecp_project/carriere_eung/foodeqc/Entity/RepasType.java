package com.ecp_project.carriere_eung.foodeqc.Entity;

/**
 * Created by eung on 21/05/16.
 * Enumeration représentant les différents type de repas : petit-dej, dejeuner, dinner, en-cas
 */
public enum RepasType {
    breakfast,lunch,dinner,snack;

    @Override
    public String toString() {
        String result = "";
        switch (this) {
            case breakfast:
                result =  "breakfast";
                break;
            case lunch:
                result = "lunch";
                break;
            case dinner:
                result = "dinner";
                break;
            case snack:
                result = "snack";
                break;
            default:
        }
        return result;
    }

    public static RepasType stringToRepasType(String string) {
        RepasType result = null;
        switch (string) {
            case "breakfast":
                return breakfast;
            case "lunch":
                result = lunch;
                break;
            case "dinner":
                result = dinner;
                break;
            case "snack":
                result = snack;
                break;
            default:
        }
        return result;
    }
}

