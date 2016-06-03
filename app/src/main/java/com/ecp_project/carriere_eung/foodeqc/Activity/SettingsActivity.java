package com.ecp_project.carriere_eung.foodeqc.Activity;

import android.app.DialogFragment;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.ecp_project.carriere_eung.foodeqc.ConfirmDialogFragment;
import com.ecp_project.carriere_eung.foodeqc.DatabaseHandler;
import com.ecp_project.carriere_eung.foodeqc.R;

/**
 * Created by Matthieu on 02/06/2016.
 */
public class SettingsActivity extends AppCompatActivity implements ConfirmDialogFragment.ConfirmProportionDialogListener {
    DatabaseHandler db;
    Button restaure;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_new_item);
        restaure = (Button)findViewById(R.id.buttonRestaure);
        restaure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

    }

    @Override
    public void onDialogPositiveClick(DialogFragment dialog) {
        db =new DatabaseHandler(getApplication());
        db.restaureBaseDatabase();
    }

    @Override
    public void onDialogNegativeClick(DialogFragment dialog) {

    }
}
