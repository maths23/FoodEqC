package com.ecp_project.carriere_eung.foodeqc.AuxiliaryMethods;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.StringTokenizer;

/**
 * Created by Matthieu on 24/05/2016.
 */
public class AddNewItemAuxiliary {

    public static int getIndexOfMapsContaining(ArrayList<HashMap<String,String>> list, String ingredientName){
        int returnValue =0;
        if (listContainsMap(list, ingredientName)){
            int index =0;
            for (HashMap map:list){
                if (map.containsValue(ingredientName)){
                    returnValue = index;

                }
                index +=1;
            }
        }


        return returnValue;
    }

    public static boolean listContainsMap(ArrayList<HashMap<String,String>> list, String ingredientName){
        boolean returnValue = false;
        for (HashMap map:list){
            if (map.containsValue(ingredientName)){
                returnValue = true;
            }
        }
        return returnValue;
    }
}
