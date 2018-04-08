package com.diogorodrigues.caixabreak;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Rect;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.Toast;

import com.diogorodrigues.caixabreak.Adapters.CardAdapter;
import com.diogorodrigues.caixabreak.Adapters.ExpandableListAdapter;
import com.diogorodrigues.caixabreak.Entities.Card;
import com.diogorodrigues.caixabreak.Entities.Movement;

import org.jsoup.nodes.Document;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private CardAdapter adapter;
    private List<Card> cardList;
    private CaixaScraper cxs;
    ProgressDialog mProgressDialog;
    public static final int TEXT_REQUEST = 1;

    ExpandableListAdapter listAdapter;
    ExpandableListView expListView;
    List<String> listDataHeader;
    HashMap<String, List<String>> listDataChild;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view);

        //FAB
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.floatingActionButton);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Click action
                Intent intent = new Intent(MainActivity.this, NewCardActivity.class);
                //startActivity(intent);
                startActivityForResult(intent, TEXT_REQUEST);
            }
        });

        cardList = new ArrayList<>();
        adapter = new CardAdapter(this, cardList);

        cxs = new CaixaScraper();

        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this, 1);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(1, dpToPx(10), true));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);

        adapter.prepareCards();

        //expandableView Adapter  -----------------

        // get the listview
        expListView = (ExpandableListView) findViewById(R.id.exp_view);

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
                String reply =
                        data.getStringExtra(NewCardActivity.EXTRA_REPLY);

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

            mProgressDialog = new ProgressDialog(MainActivity.this);
            mProgressDialog.setTitle("CaixaBreak");
            mProgressDialog.setMessage("A carregar Informação...");
            mProgressDialog.setIndeterminate(false);
            mProgressDialog.show();
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

            System.out.println("Done.");
            mProgressDialog.dismiss();
            adapter.notifyDataSetChanged();
            listAdapter.notifyDataSetChanged();
        }
    }

}