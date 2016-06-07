package com.ecp_project.carriere_eung.foodeqc.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.ecp_project.carriere_eung.foodeqc.Entity.ItemRepas;
import com.ecp_project.carriere_eung.foodeqc.Entity.Repas;
import com.ecp_project.carriere_eung.foodeqc.R;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

/**
 * Created by eung on 02/06/16.
 * Adapter pour la listview des repas
 */
public class RepasAdapter extends ArrayAdapter<Repas> {


    public RepasAdapter(Context context, List<Repas> objects) {
        super(context, 0, objects);
    }

    public class RepasViewHolder {
        public TextView textViewRepasType;
        public TextView textViewRepasDate;
        public TextView textViewRepasCO2Equivalent;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView =
                    LayoutInflater.from(getContext()).inflate(R.layout.display_meal, parent, false);
        }
        RepasViewHolder viewHolder = (RepasViewHolder) convertView.getTag();
        if (viewHolder == null) {
            viewHolder = new RepasViewHolder();
            viewHolder.textViewRepasType = (TextView) convertView.findViewById(R.id.textViewShowMealType);
            viewHolder.textViewRepasDate = (TextView) convertView.findViewById(R.id.textViewShowMealDate);
            viewHolder.textViewRepasCO2Equivalent = (TextView) convertView.findViewById(R.id.textViewShowMealCO2Equivalent);
            convertView.setTag(viewHolder);
        }
        Repas repas = getItem(position);
        //il ne reste plus qu'à remplir notre vue
        viewHolder.textViewRepasType.setText(repas.getRepasType().toString() + String.valueOf(repas.getId()) + " " + String.valueOf(repas.getDate().getTimeInMillis()));
        viewHolder.textViewRepasDate.setText(new StringBuilder().append("Le ").append(String.format("%02d",repas.getDate().get(Calendar.DAY_OF_MONTH))).append("/").append(String.format("%02d",repas.getDate().get(Calendar.MONTH)+1)).append("/").append(repas.getDate().get(GregorianCalendar.YEAR)).append(" à ").append(repas.getDate().get(GregorianCalendar.HOUR_OF_DAY)).append(":").append(repas.getDate().get(GregorianCalendar.MINUTE)));
        viewHolder.textViewRepasCO2Equivalent.setText(String.format("%.2f",repas.getCo2Equivalent()));

        return convertView;
    }
}
