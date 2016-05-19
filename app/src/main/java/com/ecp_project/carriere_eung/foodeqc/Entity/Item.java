package com.ecp_project.carriere_eung.foodeqc.Entity;

import java.util.ArrayList;

/**
 * Created by Matthieu on 19/05/2016.
 */
public class Item {

    private int id;
    private static int idKey =0;
    private String name;
    private int co2Equivalent;
    private ItemType type;


    public Item(String name, int co2Equivalent, ItemType type) {
        this.id = idKey;
        idKey += 1;
        this.name = name;
        this.co2Equivalent = co2Equivalent;
        this.type = type;

    }
}
