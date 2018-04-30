package com.diogorodrigues.caixabreak.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.diogorodrigues.caixabreak.Entities.Transaction;
import com.diogorodrigues.caixabreak.R;

import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Created by Diogo Rodrigues on 22/04/2018.
 */

public class RecipeAdapter extends BaseAdapter {

    private Context mContext;
    private LayoutInflater mInflater;
    private List<Transaction> mDataSource;

    //constructor
    public RecipeAdapter(Context context, List<Transaction> items) {
        mContext = context;
        mDataSource = items;
        mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    //1
    @Override
    public int getCount() {
        return mDataSource.size();
    }

    //2
    @Override
    public Object getItem(int position) {
        return mDataSource.get(position);
    }

    //3
    @Override
    public long getItemId(int position) {
        return position;
    }

    //4
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get view for row item
        View rowView;
        rowView = mInflater.inflate(R.layout.row_item, parent, false);

        // Get title element
        TextView titleTextView =
                (TextView) rowView.findViewById(R.id.t_description);

        // Get subtitle element
        TextView subtitleTextView =
                (TextView) rowView.findViewById(R.id.t_date_transaction);

        // Get detail element
        TextView detailTextView =
                (TextView) rowView.findViewById(R.id.t_value);

        // 1
        Transaction recipe = (Transaction) getItem(position);

        // 2
        titleTextView.setText(recipe.getDescription());

        //3
        //formatting date in Java using SimpleDateFormat
       String d1 = new SimpleDateFormat("dd-MMM").format(recipe.getlDataValor());
        subtitleTextView.setText(d1);

        //4
        if(recipe.getCredit()!=null && !recipe.getCredit().equals(""))
            detailTextView.setText("+"+recipe.getCredit());
        else
            detailTextView.setText("-"+recipe.getDebit());


        return rowView;
    }
}