package com.diogorodrigues.caixabreak.Adapters;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.diogorodrigues.caixabreak.Entities.Card;
import com.diogorodrigues.caixabreak.R;

import java.util.List;

/**
 * Created by Diogo Rodrigues on 22/03/2018.
 */

public class CardAdapter extends RecyclerView.Adapter<CardAdapter.ViewHolder> {
    private List<Card> cardList;

    public CardAdapter(List<Card> cardList) {
        this.cardList = cardList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v = inflater.inflate(R.layout.album_card, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Card card = cardList.get(position);
        holder.cardNameView.setText(card.getNome());
        holder.cardNumberView.setText(card.getUser());
        holder.cardBalanceView.setText(card.getBalance());
    }


    /**
     * Showing popup menu when tapping on 3 dots

     private void showPopupMenu(View view) {
     // inflate menu
     PopupMenu popup = new PopupMenu(mContext, view);
     MenuInflater inflater = popup.getMenuInflater();
     inflater.inflate(R.menu.menu_card, popup.getMenu());
     popup.setOnMenuItemClickListener(new CardAdapter.MyMenuItemClickListener());

     popup.show();
     }
     */
    /**
     * Click listener for popup menu items
     *
     class MyMenuItemClickListener implements PopupMenu.OnMenuItemClickListener {

     public MyMenuItemClickListener() {

     }

     @Override
     public boolean onMenuItemClick(MenuItem menuItem) {
     switch (menuItem.getItemId()) {
     case R.id.action_delete_card:
     Toast.makeText(mContext, "delete position: " + getCurrentPos(), Toast.LENGTH_SHORT).show();
     cardList.remove(getCurrentPos());
     notifyDataSetChanged();
     return true;

     case R.id.action_add_widget:
     Toast.makeText(mContext, "Play next", Toast.LENGTH_SHORT).show();
     return true;
     default:

     }
     return false;
     }
     }
     */

    @Override
    public int getItemCount() {
        return cardList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private CardView cardView;
        private TextView cardNameView, cardNumberView, cardBalanceView;
        private ImageView overflow;


        public ViewHolder(View view) {
            super(view);
            cardView = view.findViewById(R.id.card_view);

            cardNameView = view.findViewById(R.id.cartao_nome);
            cardNumberView = view.findViewById(R.id.card_number);
            cardBalanceView = view.findViewById(R.id.card_balance);
            overflow = view.findViewById(R.id.overflow);
        }
    }
}