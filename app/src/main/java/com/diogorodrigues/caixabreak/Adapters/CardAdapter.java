    package com.diogorodrigues.caixabreak.Adapters;

    import android.content.Context;
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


     public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView cardNumberView, cardBalanceView;
        public ImageView overflow;

        public MyViewHolder(View view) {
            super(view);
            cardNumberView = (TextView) view.findViewById(R.id.card_number);
            cardBalanceView = (TextView) view.findViewById(R.id.card_balance);
            overflow = (ImageView) view.findViewById(R.id.overflow);
        }
    }
        public CardAdapter(Context mContext, List<Card> cardList) {
            super();
            this.mContext = mContext;
            this.cardList = cardList;
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
            holder.cardNumberView.setText(card.getUser());
            holder.cardBalanceView.setText(card.getBalance());


            //holder.cardBalanceView.setText(album.getNumOfSongs() + " songs");

            holder.overflow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    showPopupMenu(holder.overflow);
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
            inflater.inflate(R.menu.menu_album, popup.getMenu());
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
                case R.id.action_add_favourite:
                    Toast.makeText(mContext, "Add to favourite", Toast.LENGTH_SHORT).show();
                    return true;
                case R.id.action_play_next:
                    Toast.makeText(mContext, "Play next", Toast.LENGTH_SHORT).show();
                    return true;
                default:
            }
            return false;
        }
    }

        @Override
        public int getItemCount() {
            return cardList.size();
        }

    }