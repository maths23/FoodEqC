package com.ecp_project.carriere_eung.foodeqc.AuxiliaryMethods;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.MenuItem;

import com.ecp_project.carriere_eung.foodeqc.Activity.AddNewItemActivity;
import com.ecp_project.carriere_eung.foodeqc.Activity.AddNewItemTypeActivity;
import com.ecp_project.carriere_eung.foodeqc.Activity.AddNewRepasActivity;
import com.ecp_project.carriere_eung.foodeqc.Activity.AddNewRepasTypeActivity;
import com.ecp_project.carriere_eung.foodeqc.Activity.DisplayExistingItemActivity;
import com.ecp_project.carriere_eung.foodeqc.Activity.MainActivity;
import com.ecp_project.carriere_eung.foodeqc.Activity.ShowAllMealsActivity;
import com.ecp_project.carriere_eung.foodeqc.R;

import static android.support.v4.app.ActivityCompat.startActivity;

/**
 * Created by Matthieu on 05/06/2016.
 * This class defines a basic menu-handling that is meant to be same for every  activities
 * It allows the user to get rapidly to the main activities whener he wants to do so
 */
public class MenuHandler  {
    public static boolean HandleMenuEvents(MenuItem item, Context context, Activity activity){
        Intent intent;
        switch (item.getItemId()){
            case R.id.menu_main:

                intent = new Intent(context,MainActivity.class);
                activity.startActivity(intent);
                return true;
            case R.id.menu_show_item:

                intent = new Intent(context,DisplayExistingItemActivity.class);
                activity.startActivity(intent);
                return true;

            case R.id.menu_history:

                intent = new Intent(context,ShowAllMealsActivity.class);
                activity.startActivity(intent);
                return true;

            case R.id.menu_new_meal:

                intent = new Intent(context,AddNewRepasTypeActivity.class);
                activity.startActivity(intent);
                return true;
            case R.id.menu_create_item:

                intent = new Intent(context,AddNewItemTypeActivity.class);
                activity.startActivity(intent);
                return true;

            default:
                return false;



        }
    }
}
