package com.ecp_project.carriere_eung.foodeqc.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.ecp_project.carriere_eung.foodeqc.Activity.DisplayExistingItemActivity;
import com.ecp_project.carriere_eung.foodeqc.Entity.Repas;
import com.ecp_project.carriere_eung.foodeqc.R;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;

/**
 * Created by eung on 08/06/16.
 */
public class ItemAdapter extends ArrayAdapter<HashMap<String,String>> {

    public ItemAdapter(Context context,List<HashMap<String, String>> objects) {
        super(context, 0, objects);
    }

    public class ItemViewHolder {
        public TextView textViewItemType;
        public TextView textViewItemName;
        public TextView textViewItemCO2Equivalent;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView =
                    LayoutInflater.from(getContext()).inflate(R.layout.display_item, parent, false);
        }
        ItemViewHolder viewHolder = (ItemViewHolder) convertView.getTag();
        if (viewHolder == null) {
            viewHolder = new ItemViewHolder();
            viewHolder.textViewItemType = (TextView) convertView.findViewById(R.id.textViewDisplayItemType);
            viewHolder.textViewItemName = (TextView) convertView.findViewById(R.id.textViewDisplayItemName);
            viewHolder.textViewItemCO2Equivalent = (TextView) convertView.findViewById(R.id.textViewDisplayItemEquivalent);
            convertView.setTag(viewHolder);
        }
        HashMap<String,String> item = getItem(position);
        //il ne reste plus qu'à remplir notre vue
        viewHolder.textViewItemType.setText(item.get(DisplayExistingItemActivity.TAG_ITEMTYPE));
        viewHolder.textViewItemName.setText(item.get(DisplayExistingItemActivity.TAG_IIEM));
        viewHolder.textViewItemCO2Equivalent.setText(item.get(DisplayExistingItemActivity.TAG_EQUIVALENT));

        return convertView;
    }
}
