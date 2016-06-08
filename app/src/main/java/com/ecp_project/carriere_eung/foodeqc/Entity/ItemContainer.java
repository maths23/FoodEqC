package com.ecp_project.carriere_eung.foodeqc.Entity;

import java.util.List;

/**
 * Created by Matthieu on 08/06/2016.
 */
public class ItemContainer {
    public List<Item> items;

    public ItemContainer(){}

    public List<Item> getItems() {
        return items;
    }

    public void setItems(List<Item> items) {
        this.items = items;
    }
}
