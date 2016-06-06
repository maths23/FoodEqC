package com.ecp_project.carriere_eung.foodeqc;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.ArrayAdapter;

import com.ecp_project.carriere_eung.foodeqc.Activity.AddNewRepasActivity;
import com.ecp_project.carriere_eung.foodeqc.Activity.AddNewRepasActivity;

/**
 * Created by eung on 03/06/16.
 */
public class AddRepasCustomAutoCompleteTextChangedListener implements TextWatcher {
    public static final String TAG = "CustomAutoCompleteTextChangedListener.java";
    Context context;

    public AddRepasCustomAutoCompleteTextChangedListener(Context context){
        this.context = context;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence userInput, int start, int before, int count) {
        Log.e("Input","User input: "+ userInput);
        AddNewRepasActivity addNewRepasActivity = ((AddNewRepasActivity) context);

        //all items can be added to Repas, hence second paramter must be false
        addNewRepasActivity.item = addNewRepasActivity.db.getItemsFromDb(userInput.toString(),false);


        // update the adapater
        addNewRepasActivity.myAdapter.notifyDataSetChanged();
        addNewRepasActivity.myAdapter = new ArrayAdapter<String>(addNewRepasActivity, android.R.layout.simple_dropdown_item_1line, addNewRepasActivity.item);
        addNewRepasActivity.myAutoComplete.setAdapter(addNewRepasActivity.myAdapter);

    }

    @Override
    public void afterTextChanged(Editable s) {

    }
}
