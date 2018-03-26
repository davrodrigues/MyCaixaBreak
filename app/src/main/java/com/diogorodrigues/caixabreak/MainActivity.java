package com.diogorodrigues.caixabreak;
import android.app.ProgressDialog;
import android.content.res.Resources;
import android.graphics.Rect;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.View;

import com.diogorodrigues.caixabreak.Adapters.CardAdapter;
import com.diogorodrigues.caixabreak.Entities.Album;
import com.diogorodrigues.caixabreak.Entities.Card;

import org.jsoup.nodes.Document;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private CardAdapter adapter;
    private List<Album> albumList;
    private List<Card> cardList;
    private CaixaScraper cxs;
    ProgressDialog mProgressDialog;
    String url = "http://www.androidbegin.com";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);

       // albumList = new ArrayList<>();
       // adapter = new AlbumsAdapter(this, albumList);

        cardList = new ArrayList<>();
        adapter = new CardAdapter(this, cardList);
        cxs = new CaixaScraper();

        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this, 1);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(1, dpToPx(10), true));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
        prepareCards();

       // prepareAlbums();
    }
    private void prepareCards(){

            Card card = new Card();
            cardList.add(card);



        new Description().execute();
    }


    /**
     * RecyclerView item decoration - give equal margin around grid item
     */
    public class GridSpacingItemDecoration extends RecyclerView.ItemDecoration {

        private int spanCount;
        private int spacing;
        private boolean includeEdge;

        public GridSpacingItemDecoration(int spanCount, int spacing, boolean includeEdge) {
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

    // Description AsyncTask
    private class Description extends AsyncTask<Void, Void, Void> {
        String desc;
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

            dc =  cxs.connect2CaixaBreak(cardList.get(0));
            cxs.parseBalance(dc, cardList.get(0));
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {


            System.out.println("novo saldo: "+cardList.get(0).getBalance());
            mProgressDialog.dismiss();
            adapter.notifyDataSetChanged();
        }
    }
}