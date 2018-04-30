package com.diogorodrigues.caixabreak;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.diogorodrigues.caixabreak.Adapters.CardAdapter;
import com.diogorodrigues.caixabreak.Adapters.RecipeAdapter;
import com.diogorodrigues.caixabreak.Entities.Card;
import com.diogorodrigues.caixabreak.Entities.Movement;
import com.diogorodrigues.caixabreak.Entities.Transaction;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.yarolegovich.discretescrollview.DSVOrientation;
import com.yarolegovich.discretescrollview.DiscreteScrollView;
import com.yarolegovich.discretescrollview.InfiniteScrollAdapter;
import com.yarolegovich.discretescrollview.transform.ScaleTransformer;


import org.jsoup.nodes.Document;

import java.io.IOException;
import java.lang.reflect.Type;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements DiscreteScrollView.OnItemChangedListener,
        View.OnClickListener {

    private List<Card> cardList;
    private CaixaScraper cxs;
    public static final int TEXT_REQUEST = 1;

    private RecipeAdapter recipeAdapter;
    private ListView currentList;
    private TextView currentItemName;
    private ImageView currentOverflow;
    private View currentHeader;

    private DiscreteScrollView itemPicker;
    private InfiniteScrollAdapter infiniteAdapter;

    SwipeRefreshLayout mSwipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        cxs = new CaixaScraper();
        cardList = new ArrayList<>();
        //arrayAdapter = new ArrayAdapter<>(this, R.layout.row_item, new ArrayList<Transaction>());

        // myToolbar is defined in the layout file
        Toolbar myToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);

        // SwipeRefreshLayout
        mSwipeRefreshLayout = findViewById(R.id.swiperefresh);
        //PULL TO REFRESH

        mSwipeRefreshLayout.setOnRefreshListener(
                new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        // Refresh items
                        new Refresh().execute();
                    }
                });

        // VIEWS to VARIABLES
        currentItemName = (TextView) findViewById(R.id.saldo_disp);
        currentList = (ListView) findViewById(R.id.current_list);
    //TODO
        //currentHeader = LayoutInflater.inflate(R.layout.list_group, parent, false);


        //Retrieve data from SplashActivity
        Intent intent = getIntent();
        String gsonCards = intent.getStringExtra("gsonCards");
        Type type = new TypeToken<List<Card>>() {}.getType();

        //parse intent from splashactivity
        Gson gson = new Gson();
        cardList = gson.fromJson(gsonCards, type);

        itemPicker = (DiscreteScrollView) findViewById(R.id.item_picker);
        itemPicker.setOrientation(DSVOrientation.HORIZONTAL);
        itemPicker.addOnItemChangedListener(this);
        infiniteAdapter = InfiniteScrollAdapter.wrap(new CardAdapter(cardList));
        itemPicker.setAdapter(infiniteAdapter);
        itemPicker.setItemTransitionTimeMillis(150);
        itemPicker.setItemTransformer(new ScaleTransformer.Builder()
                .setMinScale(0.8f)
                .build());

        onItemChanged(cardList.get(0));

        //findViewById(R.id.item_btn_rate).setOnClickListener(this);
        //findViewById(R.id.item_btn_buy).setOnClickListener(this);
        //findViewById(R.id.item_btn_comment).setOnClickListener(this);
        //currentOverflow.setOnClickListener(this);




        //findViewById(R.id.btn_smooth_scroll).setOnClickListener(this);
        //findViewById(R.id.btn_transition_time).setOnClickListener(this);
        mSwipeRefreshLayout.setRefreshing(true);
        new Refresh().execute();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            //case R.id.item_btn_rate:
            //int realPosition = infiniteAdapter.getRealPosition(itemPicker.getCurrentItem());
            //Card currentCard = cardList.get(realPosition);
            //shop.setRated(current.getId(), !shop.isRated(current.getId()));
            //changeRateButtonState(current);
            //break;
            case R.id.home:
                finish();
                break;
            case R.id.overflow:
                Toast.makeText(this, "overflow", Toast.LENGTH_LONG).show();
                break;
            /*case R.id.btn_transition_time:
                DiscreteScrollViewOptions.configureTransitionTime(itemPicker);
                break;
            case R.id.btn_smooth_scroll:
                DiscreteScrollViewOptions.smoothScrollToUserSelectedPosition(itemPicker, v);
                break;
            */default:
                showUnsupportedSnackBar();
                break;
        }
    }

    private void onItemChanged(Card card) {
        if(card!=null){
            currentItemName.setText("Movimentos do cartão de "+card.getNome());
            loadTransactions(card.getMovement());
        }
    }

    private void loadTransactions(Movement movement){
        if(movement!=null) {
            List<Transaction> trs = movement.getTransactions();
            recipeAdapter = new RecipeAdapter(this,trs );
            currentList.setAdapter(recipeAdapter);

        }
    }

    @Override
    public void onCurrentItemChanged(RecyclerView.ViewHolder viewHolder, int position) {
        int positionInDataSet = infiniteAdapter.getRealPosition(position);
        onItemChanged(cardList.get(positionInDataSet));
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == TEXT_REQUEST) {
            if (resultCode == RESULT_OK) {

                Card nCard = (Card) data.getSerializableExtra(NewCardActivity.SER_KEY);
                Toast.makeText(this, "Recebido cartão: "+nCard.getNome(), Toast.LENGTH_LONG).show();
                cardList.add(nCard);

                infiniteAdapter.notifyItemInserted(cardList.indexOf(nCard));

                mSwipeRefreshLayout.setRefreshing(true);
                new Refresh().execute();
            }
        }
    }

    // Refresh AsyncTask
    public class Refresh extends AsyncTask<Void, Void, Void> {
        Document dc;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... params) {
            for (Card c : cardList){
                try {
                    dc =  cxs.connect2CaixaBreak(c);
                    cxs.parseBalance(dc, c);
                    cxs.parseTransactions(dc, c);
                } catch (ParseException | IOException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {

            infiniteAdapter.notifyDataSetChanged();

            if(mSwipeRefreshLayout.isRefreshing())
                mSwipeRefreshLayout.setRefreshing(false);

            // Update the adapter and notify data set changed
            System.out.println("Refresh executado com sucesso! ");
            Snackbar.make(itemPicker, R.string.refresh_success, Snackbar.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_mainactivity_add_card, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.add_card:
                // Click action
                Intent intent = new Intent(MainActivity.this, NewCardActivity.class);
                startActivityForResult(intent, TEXT_REQUEST);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    //helper
    private void showUnsupportedSnackBar() {
        Snackbar.make(itemPicker, "UNSUPPORTED_OPERATION", Snackbar.LENGTH_SHORT).show();
    }





    /**
     * Showing popup menu when tapping on 3 dots
     */
    private void showPopupMenu(View view) {
        // inflate menu
        PopupMenu popup = new PopupMenu(this, view);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.menu_card, popup.getMenu());
        popup.setOnMenuItemClickListener(new MyMenuItemClickListener());

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
                    Snackbar.make(itemPicker, "Delete position: "+itemPicker.getCurrentItem(), Snackbar.LENGTH_SHORT).show();
                    //Toast.makeText(mContext, "delete position: " + itemPicker.getCurrentItem(), Toast.LENGTH_SHORT).show();
                    cardList.remove(itemPicker.getCurrentItem());
                    infiniteAdapter.notifyDataSetChanged();
                    recipeAdapter.notifyDataSetChanged();
                    return true;

                case R.id.action_add_widget:
                    Snackbar.make(itemPicker, "Add Widget", Snackbar.LENGTH_SHORT).show();
                    return true;
                default:

            }
            return false;
        }
    }
}