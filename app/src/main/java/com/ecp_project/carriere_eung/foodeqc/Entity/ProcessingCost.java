package com.ecp_project.carriere_eung.foodeqc.Entity;

/**
 * Created by Matthieu on 19/05/2016.
 *
 * This class represents how much CO2 is emitted when ingredients are processed to create the item
 */
public enum ProcessingCost {
    low,standard,high;

    public double getMalusFactor(){
        // On met la valeur à 1 par défault
        double returnValue=1;
        switch (this){
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

    public static ProcessingCost toProcessingCost(double cost) {
        ProcessingCost returnValue = standard;
        if (Double.compare(cost,1.1)==0)
            returnValue = ProcessingCost.low;
        if (Double.compare(cost,1.2)==0)
            returnValue = standard;
        if (Double.compare(cost,1.3)==0)
            returnValue = high;
        return returnValue;

    }
}

