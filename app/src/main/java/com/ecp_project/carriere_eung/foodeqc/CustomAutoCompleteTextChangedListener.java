package com.ecp_project.carriere_eung.foodeqc;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.ArrayAdapter;

import com.ecp_project.carriere_eung.foodeqc.AddNewItemActivity;
import com.ecp_project.carriere_eung.foodeqc.MainActivity;

/**
 * Created by Matthieu on 27/05/2016.
 */
public class CustomAutoCompleteTextChangedListener implements TextWatcher {

    public static final String TAG = "CustomAutoCompleteTextChangedListener.java";
    Context context;

    public CustomAutoCompleteTextChangedListener(Context context){
        this.context = context;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence userInput, int start, int before, int count) {
        Log.e("Input","User input: "+ userInput);
        AddNewItemActivity addNewItemActivity = ((AddNewItemActivity) context);

        addNewItemActivity.item = addNewItemActivity.db.getItemsFromDb(userInput.toString());


        // update the adapater
        addNewItemActivity.myAdapter.notifyDataSetChanged();
        addNewItemActivity.myAdapter = new ArrayAdapter<String>(addNewItemActivity, android.R.layout.simple_dropdown_item_1line, addNewItemActivity.item);
        addNewItemActivity.myAutoComplete.setAdapter(addNewItemActivity.myAdapter);

    }

    @Override
    public void afterTextChanged(Editable s) {

    }
}
