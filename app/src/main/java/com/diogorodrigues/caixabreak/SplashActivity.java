package com.diogorodrigues.caixabreak;

/**
 * Created by Diogo Rodrigues on 16/04/2018.
 */
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.diogorodrigues.caixabreak.Entities.Card;
import com.diogorodrigues.caixabreak.Entities.Movement;
import com.diogorodrigues.caixabreak.Entities.Transaction;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class SplashActivity extends AppCompatActivity {
    private List<Card> cardList;
    private String gsonCards;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //read/write cards to file
        String FILENAME = "storage.json";
        try {
            Context context= getApplicationContext();
            //READ FROM INTERNAL
           if(isFilePresent(context,FILENAME)){
             gsonCards = read(context,FILENAME);
               System.out.println("Data Read from Internal Storage");
           }
           else {
               //DUMMY CARDLIST FILLER
               populateCards();

               //CONVERT CARDLIST -> JSON
               convert();

               //WRITE TO INTERNAL
                Boolean b = create(context,FILENAME,gsonCards);
               System.out.println("Write Status: "+b);
           }
            //CREATE INTENT
            Intent intent = new Intent(this, MainActivity.class);
            intent.putExtra("gsonCards", gsonCards);

            //SEND INTENT TO NEXT ACTIVITY
            startActivity(intent);
            finish();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //convert cardList to JSON and Prints
    public void convert(){
        Gson gson = new Gson();
        Type type = new TypeToken<List<Card>>() {}.getType();
        gsonCards = gson.toJson(cardList, type);
        System.out.println(gsonCards);
    }

    public void populateCards(){
        List<Card> list = new ArrayList<>();

        for (int i = 0; i < 3; i++) {
            Card card = new Card();
            card.setNome("diogo"+i);
            card.setUser("6272293");
            card.setUsername();

            Movement mv = new Movement();
            mv.setTransactions(new ArrayList<Transaction>());

            card.setMovement(mv);
            list.add(card);

            cardList = list;
        }
    }


    private String read(Context context, String fileName) {
        try {
            FileInputStream fis = context.openFileInput(fileName);
            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader bufferedReader = new BufferedReader(isr);
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                sb.append(line);
            }
            return sb.toString();
        } catch (IOException ioException) {
            return null;
        }
    }

    private boolean create(Context context, String fileName, String jsonString){

        try {
            FileOutputStream fos = openFileOutput(fileName,Context.MODE_PRIVATE);
                fos.write(jsonString.getBytes());
            fos.close();
            return true;
        } catch (IOException ioException) {
            return false;
        }

    }
    public boolean isFilePresent(Context context, String fileName) {
        String path = context.getFilesDir().getAbsolutePath() + "/" + fileName;
        File file = new File(path);
        return file.exists();
    }
}