package com.diogorodrigues.caixabreak.Adapters;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.diogorodrigues.caixabreak.Entities.Card;
import com.diogorodrigues.caixabreak.R;

import java.util.List;

/**
 * Created by Diogo Rodrigues on 22/03/2018.
 */

public class CardAdapter extends RecyclerView.Adapter<CardAdapter.MyViewHolder> {
    private Context mContext;
    private List<Card> cardList;

    private int currentPos = 0;



    public CardAdapter(Context mContext, List<Card> cardList) {
        super();
        this.mContext = mContext;
        this.cardList = cardList;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView cardNameView, cardNumberView, cardBalanceView;
        public ImageView overflow;

        public CardView cardView;

        public MyViewHolder(View view) {
            super(view);
            cardView = (CardView) view.findViewById(R.id.card_view);

            cardNameView = (TextView) view.findViewById(R.id.cartao_nome);
            cardNumberView = (TextView) view.findViewById(R.id.card_number);
            cardBalanceView = (TextView) view.findViewById(R.id.card_balance);
            overflow = (ImageView) view.findViewById(R.id.overflow);
        }

    }


    @Override
    public CardAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.album_card, parent, false);

        return new CardAdapter.MyViewHolder(itemView);

    }

    @Override
    public void onBindViewHolder(final CardAdapter.MyViewHolder holder, int position) {

        Card card = cardList.get(position);
        holder.cardNameView.setText(card.getNome());
        holder.cardNumberView.setText(card.getUser());
        holder.cardBalanceView.setText(card.getBalance());

        setCurrentPos(position);

        holder.overflow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //setCurrentPos(holder.getAdapterPosition());
                showPopupMenu(holder.overflow);
            }
        });

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Toast.makeText(mContext,
                        "Card selected: " + holder.getAdapterPosition(), Toast.LENGTH_SHORT).show();
                Card nCard = cardList.get(holder.getAdapterPosition());

                notifyDataSetChanged();
            }
        });

    }

    /**
     * Showing popup menu when tapping on 3 dots
     */
    private void showPopupMenu(View view) {
        // inflate menu
        PopupMenu popup = new PopupMenu(mContext, view);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.menu_card, popup.getMenu());
        popup.setOnMenuItemClickListener(new CardAdapter.MyMenuItemClickListener());

        popup.show();
    }

    /**
     * Click listener for popup menu items
     */
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
                    return false;
            }
            //return false;
        }
    }

    @Override
    public int getItemCount() {
        return cardList.size();
    }

    public int getCurrentPos() {
        return currentPos;
    }

    public void setCurrentPos(int currentPos) {
        this.currentPos = currentPos;
    }

    public void prepareCards() {
        for (int i = 0; i < 1; i++) {
            Card card = new Card();
            card.setNome("diogo");
            card.setUser("6272293");
            card.setUsername();
            cardList.add(card);
        }
    }





}