package com.diogorodrigues.caixabreak.Adapters;

/**
 * Created by Diogo Rodrigues on 04/04/2018.
 */

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;

import android.content.Context;
import android.graphics.Typeface;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.diogorodrigues.caixabreak.Entities.Card;
import com.diogorodrigues.caixabreak.Entities.Movement;
import com.diogorodrigues.caixabreak.Entities.Transaction;
import com.diogorodrigues.caixabreak.R;

public class ExpandableListAdapter extends BaseExpandableListAdapter {

    private Context _context;
    private List<Card> cardList;

    //constructor
    public ExpandableListAdapter(Context context, List<Card> cardList){
        this._context = context;
        this.cardList = cardList;
    }

    @Override
    public Object getChild(int groupPosition, int childPosititon) {
        return this.cardList.get(groupPosition).getMovement().getTransactions().get(childPosititon);
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {

        Toast.makeText(this._context, "getChildId: "+childPosition, Toast.LENGTH_SHORT).show();
        return childPosition;
    }

    @Override
    public View getChildView(int groupPosition, final int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {


        if (convertView == null) {
            LayoutInflater inf = LayoutInflater.from(parent.getContext());
            convertView = inf.inflate(R.layout.transaction_item, parent, false);
        }

        Transaction tr = (Transaction) getChild(groupPosition, childPosition);

      //  TextView tDateProcessedView = convertView.findViewById(R.id.t_date_processed);
        TextView tDateTransactionView = convertView.findViewById(R.id.t_date_transaction);
        TextView tDescriptionView = convertView.findViewById(R.id.t_description);
        TextView tValueView = convertView.findViewById(R.id.t_value);

        //formatting date in Java using SimpleDateFormat
        SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd-MMM");
      //  String d1 = DATE_FORMAT.format(tr.getlData());
        String d2 = DATE_FORMAT.format(tr.getlDataValor());

       // tDateProcessedView.setText(d1);
        tDateTransactionView.setText(d2);
        tDescriptionView.setText(tr.getDescription());

        if(tr.getCredit()!=null && !tr.getCredit().equals(""))
        tValueView.setText("-"+tr.getCredit());
        else
            tValueView.setText("+"+tr.getDebit());
        return convertView;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return cardList.get(groupPosition).getMovement().getTransactions().size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return this.cardList.get(groupPosition);
    }

    @Override
    public int getGroupCount() {
        return cardList.size();
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {

        Card groupCard = (Card) getGroup(groupPosition);

        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this._context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.list_group, null);
        }
            // ROW HEADERS
      //  TextView hDateProcessed = convertView.findViewById(R.id.h_date_processed);
      //  TextView hDateTransaction = convertView.findViewById(R.id.h_date_transaction);
      //  TextView hDescription = convertView.findViewById(R.id.h_description);
      //  TextView hValue = convertView.findViewById(R.id.h_value);
            TextView hCard = convertView.findViewById(R.id.h_card);

        hCard.setTypeface(null, Typeface.BOLD);
        hCard.setText("Cartão de "+groupCard.getNome());

        return convertView;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}