package com.diogorodrigues.caixabreak.Adapters;

import android.content.Context;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.diogorodrigues.caixabreak.Entities.Transaction;
import com.diogorodrigues.caixabreak.R;

import java.util.ArrayList;

/**
 * Created by Diogo Rodrigues on 21/04/2018.
 */

public class CustomAdapter extends ArrayAdapter<Transaction> implements View.OnClickListener{

    private ArrayList<Transaction> dataSet;
    private Context mContext;

    // View lookup cache
    private static class ViewHolder {
        TextView txtName;
        TextView txtType;
        TextView txtVersion;
        ImageView info;
    }

    public CustomAdapter(ArrayList<Transaction> data, Context context) {
        super(context, R.layout.row_item, data);
        this.dataSet = data;
        this.mContext=context;

    }

    @Override
    public void onClick(View v) {

        int position=(Integer) v.getTag();
        Object object= getItem(position);
        Transaction dataModel=(Transaction)object;

        switch (v.getId())
        {
            case R.id.t_description:
                Snackbar.make(v, "Description " +dataModel.getDescription(), Snackbar.LENGTH_LONG)
                        .setAction("No action", null).show();
                break;
        }
    }

    private int lastPosition = -1;

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        Transaction dataModel = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        ViewHolder viewHolder; // view lookup cache stored in tag

        final View result;

        if (convertView == null) {

            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.row_item, parent, false);
            viewHolder.txtName = (TextView) convertView.findViewById(R.id.t_description);
            viewHolder.txtType = (TextView) convertView.findViewById(R.id.t_date_transaction);
            viewHolder.txtVersion = (TextView) convertView.findViewById(R.id.t_value);
            //viewHolder.info = (ImageView) convertView.findViewById(R.id.item_info);

            result=convertView;

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
            result=convertView;
        }

      //  Animation animation = AnimationUtils.loadAnimation(mContext, (position > lastPosition) ? R.anim.up_from_bottom : R.anim.down_from_top);
       // result.startAnimation(animation);
        lastPosition = position;

        viewHolder.txtName.setText(dataModel.getDescription());
        viewHolder.txtType.setText(dataModel.getlDataValor().toString());
        viewHolder.txtVersion.setText(dataModel.getDebit());
        //falta o get credit
        viewHolder.info.setOnClickListener(this);
        viewHolder.info.setTag(position);
        // Return the completed view to render on screen
        return convertView;
    }
}
