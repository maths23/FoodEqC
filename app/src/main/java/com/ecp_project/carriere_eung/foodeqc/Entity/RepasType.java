package com.ecp_project.carriere_eung.foodeqc.Entity;

/**
 * Created by eung on 21/05/16.
 * Enumeration représentant les différents type de repas : petit-dej, dejeuner, dinner, en-cas
 */
public enum RepasType {
    breakfast,lunch,dinner,snack;


    private static final String KEY_BREAKFAST = "breakfast";
    private static final String KEY_LUNCH = "lunch";
    private static final String KEY_DINNER = "dinner";
    private static final String KEY_SNACK = "snack";

    @Override
    public String toString() {
        String result = "";
        switch (this) {
            case breakfast:
                result =  KEY_BREAKFAST;
                break;
            case lunch:
                result = KEY_LUNCH;
                break;
            case dinner:
                result = KEY_DINNER;
                break;
            case snack:
                result = KEY_SNACK;
                break;
        }
        return result;
    }

    public static RepasType stringToRepasType(String string) {
        RepasType result = null;
        switch (string) {
            case KEY_BREAKFAST:
                return breakfast;
            case KEY_LUNCH:
                result = lunch;
                break;
            case KEY_DINNER:
                result = dinner;
                break;
            case KEY_SNACK:
                result = snack;
                break;
        }
        return result;
    }


}

