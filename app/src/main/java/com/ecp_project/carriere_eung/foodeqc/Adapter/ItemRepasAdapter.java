package com.ecp_project.carriere_eung.foodeqc.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.ecp_project.carriere_eung.foodeqc.Entity.ItemRepas;
import com.ecp_project.carriere_eung.foodeqc.R;

import java.util.List;

/**
 * Created by eung on 25/05/16.
 *
 * Adapter personnalisé affichant les items selectionnés par l'utilisateur pour son repas
 */
public class ItemRepasAdapter extends ArrayAdapter<ItemRepas> {


    public ItemRepasAdapter(Context context, List<ItemRepas> objects) {
        super(context, 0, objects);
    }

    public class ItemRepasViewHolder {
        public TextView textViewItemName;
        public TextView textViewItemWeight;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView =
                    LayoutInflater.from(getContext()).inflate(R.layout.display_repas_item, parent, false);
        }
        ItemRepasViewHolder viewHolder = (ItemRepasViewHolder) convertView.getTag();
        if (viewHolder == null) {
            viewHolder = new ItemRepasViewHolder();
            viewHolder.textViewItemName = (TextView) convertView.findViewById(R.id.textViewRepasItemName);
            viewHolder.textViewItemWeight = (TextView) convertView.findViewById(R.id.textViewRepasItemWeight);
            convertView.setTag(viewHolder);
        }
//getItem(position) va récupérer l'item [position] de la List<Tweet> tweets
        ItemRepas itemRepas = getItem(position);
        //il ne reste plus qu'à remplir notre vue
        viewHolder.textViewItemName.setText(itemRepas.getItem().getName());
        viewHolder.textViewItemWeight.setText(Integer.toString(itemRepas.getPoids())+" g");

        return convertView;
    }
}
