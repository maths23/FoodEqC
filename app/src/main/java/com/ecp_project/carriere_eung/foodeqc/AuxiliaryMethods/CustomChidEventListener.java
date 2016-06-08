package com.ecp_project.carriere_eung.foodeqc.AuxiliaryMethods;

import android.content.Context;
import android.util.Log;

import com.ecp_project.carriere_eung.foodeqc.Entity.Item;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;

import java.util.List;

/**
 * Created by Matthieu on 08/06/2016.
 */
public class CustomChidEventListener implements ChildEventListener {
    private String searchTerm;
    private List<Item> itemsFromFirebase;
    Context context;
    private Boolean finish = false;
    private int count = 0;
    private int numberOfNodes = 0;

    public CustomChidEventListener(String searchTerm, List<Item> itemsFromFirebase,Context context) {
        this.searchTerm = searchTerm;
        this.itemsFromFirebase = itemsFromFirebase;
        this.context = context;
    }

    public List<Item> getItemsFromFirebase() {
        return itemsFromFirebase;
    }

    public Boolean getFinish() {
        return finish;
    }

    @Override
    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
        Item item = dataSnapshot.getValue(Item.class);
        if (numberOfNodes ==0){
            numberOfNodes = (int) (dataSnapshot.getChildrenCount()-1);
        }
        Log.d("Number", ""+ dataSnapshot.getChildrenCount());

        Log.d("read firebase" ,"items lenght is " + itemsFromFirebase.size());

        if(searchTerm.length()>1){
            if (item.getName().contains(searchTerm)){
                this.itemsFromFirebase.add(item);
                Log.d("read firebase",item.toString(context) +"\n items lenght is " + itemsFromFirebase.size());
                Log.d("read firebase","item name" + item.getName());
            }
        }
        count ++;
        if (count == numberOfNodes){
            finish = true;
        }
        Log.d("read firebase" ,"items lenght is, after search, " + itemsFromFirebase.size());
    }

    @Override
    public void onChildChanged(DataSnapshot dataSnapshot, String s) {

    }

    @Override
    public void onChildRemoved(DataSnapshot dataSnapshot) {

    }

    @Override
    public void onChildMoved(DataSnapshot dataSnapshot, String s) {

    }

    @Override
    public void onCancelled(DatabaseError databaseError) {

    }
}
