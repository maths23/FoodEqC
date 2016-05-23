package com.ecp_project.carriere_eung.foodeqc;

import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.NumberPicker;
import android.widget.Toast;

import com.ecp_project.carriere_eung.foodeqc.Widget.CustomNumberPicker;

/**
 * Created by Matthieu on 23/05/2016.
 */
public class SetProportionDialog extends DialogFragment {

    public NumberPicker nPicker;

    public interface SetProportionDialogListener {
        public void onDialogPositiveClick(DialogFragment dialog);
        public void onDialogNegativeClick(DialogFragment dialog);
        public void onDialogNeutralClick(DialogFragment dialog,int proportion);
    }

    // Use this instance of the interface to deliver action events
    SetProportionDialogListener mListener;

    // Override the Fragment.onAttach() method to instantiate the NoticeDialogListener
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        // Verify that the host activity implements the callback interface
        try {
            // Instantiate the NoticeDialogListener so we can send events to the host
            mListener = (SetProportionDialogListener) activity;
        } catch (ClassCastException e) {
            // The activity doesn't implement the interface, throw exception
            throw new ClassCastException(activity.toString()
                    + " must implement NoticeDialogListener");
        }
    }

    LayoutInflater inflater;
    View customView;

    public Dialog onCreateDialog(Bundle savedInstanceState){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        inflater = getActivity().getLayoutInflater();

        nPicker = new NumberPicker(getActivity());
        nPicker.setMaxValue(100);

        builder.setTitle(R.string.set_proportion_title);
        //builder.setView(inflater.inflate(R.layout.set_proportion,null));
        builder.setView(nPicker);

        builder.setNegativeButton(R.string.delete_ingredient, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(getActivity(),"negative",Toast.LENGTH_LONG).show();
                mListener.onDialogNegativeClick(SetProportionDialog.this);
            }
        });
        builder.setNeutralButton(R.string.confirm_proportion, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mListener.onDialogNeutralClick(SetProportionDialog.this,nPicker.getValue());

            }
        });
        builder.setPositiveButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mListener.onDialogPositiveClick(SetProportionDialog.this);
            }
        });
        return builder.create();
    }
}
