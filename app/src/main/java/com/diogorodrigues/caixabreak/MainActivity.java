package com.diogorodrigues.caixabreak;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Rect;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.Toast;

import com.diogorodrigues.caixabreak.Adapters.CardAdapter;
import com.diogorodrigues.caixabreak.Adapters.ExpandableListAdapter;
import com.diogorodrigues.caixabreak.Entities.Card;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.jsoup.nodes.Document;

import java.io.IOException;
import java.lang.reflect.Type;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private CardAdapter adapter;
    private List<Card> cardList;
    private CaixaScraper cxs;
    ProgressDialog mProgressDialog;
    public static final int TEXT_REQUEST = 1;

    ExpandableListAdapter listAdapter;
    ExpandableListView expListView;

    SwipeRefreshLayout mSwipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        cxs = new CaixaScraper();
        cardList = new ArrayList<>();

        // myToolbar is defined in the layout file
        Toolbar myToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);

        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        // SwipeRefreshLayout
        mSwipeRefreshLayout = findViewById(R.id.swiperefresh);
        //PULL TO REFRESH
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Refresh items
                refreshItems();
                // Stop refresh animation
                mSwipeRefreshLayout.setRefreshing(false);
            }
        });

        //Retrieve data from SplashActivity
        Intent intent = getIntent();
        String gsonCards = intent.getStringExtra("gsonCards");
        Type type = new TypeToken<List<Card>>() {}.getType();

        //parse response from splashactivity
        Gson gson = new Gson();
        cardList = gson.fromJson(gsonCards, type);
        adapter = new CardAdapter(this, cardList);

        LinearLayoutManager horizontalLayoutManager =
                new LinearLayoutManager(MainActivity.this, LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(horizontalLayoutManager);

        recyclerView.addItemDecoration(new GridSpacingItemDecoration(2, dpToPx(10), true));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);

        //DEPRECATED FILLS IN CARDLIST
        //adapter.prepareCards();

        //expandableView Adapter  -----------------

        // get the listview
        expListView = findViewById(R.id.exp_view);

        listAdapter = new ExpandableListAdapter(this, cardList);
        // setting list adapter
        expListView.setAdapter(listAdapter);

        //end list view --------------------------

        new Refresh().execute();
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == TEXT_REQUEST) {
            if (resultCode == RESULT_OK) {

                Card nCard = (Card) data.getSerializableExtra(NewCardActivity.SER_KEY);
                Toast.makeText(this, "Recebido cartão: "+nCard.getNome(), Toast.LENGTH_LONG).show();
                cardList.add(nCard);
                //adapter.notifyDataSetChanged();
                adapter.notifyItemInserted(cardList.indexOf(nCard));
                listAdapter.notifyDataSetChanged();
                new Refresh().execute();
            }
        }
    }

    /**
     * RecyclerView item decoration - give equal margin around grid item
     */
    public class GridSpacingItemDecoration extends RecyclerView.ItemDecoration {

        private int spanCount;
        private int spacing;
        private boolean includeEdge;

        GridSpacingItemDecoration(int spanCount, int spacing, boolean includeEdge) {
            this.spanCount = spanCount;
            this.spacing = spacing;
            this.includeEdge = includeEdge;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            int position = parent.getChildAdapterPosition(view); // item position
            int column = position % spanCount; // item column

            if (includeEdge) {
                outRect.left = spacing - column * spacing / spanCount; // spacing - column * ((1f / spanCount) * spacing)
                outRect.right = (column + 1) * spacing / spanCount; // (column + 1) * ((1f / spanCount) * spacing)

                if (position < spanCount) { // top edge
                    outRect.top = spacing;
                }
                outRect.bottom = spacing; // item bottom
            } else {
                outRect.left = column * spacing / spanCount; // column * ((1f / spanCount) * spacing)
                outRect.right = spacing - (column + 1) * spacing / spanCount; // spacing - (column + 1) * ((1f /    spanCount) * spacing)
                if (position >= spanCount) {
                    outRect.top = spacing; // item top
                }
            }
        }
    }

    /**
     * Converting dp to pixel
     */
    private int dpToPx(int dp) {
        Resources r = getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
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

            adapter.notifyDataSetChanged();
            listAdapter.notifyDataSetChanged();

            // Update the adapter and notify data set changed
            System.out.println("Refresh executado com sucesso! ");
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

    void refreshItems() {
        // Load items
        new Refresh().execute();
        // Load complete
    }

}