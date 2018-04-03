package com.diogorodrigues.caixabreak;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.diogorodrigues.caixabreak.Entities.Card;

public class NewCardActivity extends AppCompatActivity {
    TextView newUserNameView;
    TextView newUserNumberView;
    TextView newPasswordView;
    public  final static String SER_KEY = "com.easyinfogeek.objectPass.ser";

    // Unique tag for the intent reply.
    public static final String EXTRA_REPLY =
            "com.example.android.twoactivities.extra.REPLY";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_card);


         newUserNameView = (TextView) findViewById(R.id.new_card_name);
         newUserNumberView = (TextView) findViewById(R.id.new_card_user);
         newPasswordView = (TextView) findViewById(R.id.password);


        // myToolbar is defined in the layout file
        Toolbar myToolbar = (Toolbar) findViewById(R.id.card_toolbar);
        setSupportActionBar(myToolbar);

        // Get a support ActionBar corresponding to this toolbar
        ActionBar ab = getSupportActionBar();

        // Enable the Up button
        if (ab != null) {
            ab.setDisplayHomeAsUpEnabled(true);
        }


    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {


        switch (item.getItemId()) {

            case R.id.card_accept:
                String username = newUserNameView.getText().toString();
                String password = newPasswordView.getText().toString();
                String usernumber = newUserNumberView.getText().toString();

                if(validate(username, password, usernumber)) {
                    Card nCard = new Card();
                    nCard.setNome(username);
                    nCard.setPwd(password);
                    nCard.setUser(usernumber);
                    nCard.setUsername();
                    Toast.makeText(this.getApplicationContext(), "Cartão criado!", Toast.LENGTH_SHORT).show();

                    //Intent mIntent = new Intent(this,MainActivity.class);
                    Intent mIntent = new Intent();
                    Bundle mBundle = new Bundle();
                    mBundle.putSerializable(SER_KEY,nCard);
                    mIntent.putExtra(EXTRA_REPLY, "12345");
                    setResult(RESULT_OK,mIntent);
                    mIntent.putExtras(mBundle);


                    finish();
                    //startActivity(mIntent);
                    return true;

                }
                else{
                    Toast.makeText(this.getApplicationContext(), "Erro na validação!", Toast.LENGTH_SHORT).show();
                    return false;
                }

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                //Toast.makeText(this.getApplicationContext(), "Default selecionado!", Toast.LENGTH_SHORT).show();
                //return super.onOptionsItemSelected(item);
                return false;

        }
    }

    private boolean validate(String username, String password, String usernumber) {

        if(username.isEmpty()) return false;
        if(password.isEmpty() || password.length()!=5) return false;
        if(usernumber.isEmpty() || usernumber.length()!=7) return false;

        else
            return true;

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_new_card, menu);
        return true;
    }






}
