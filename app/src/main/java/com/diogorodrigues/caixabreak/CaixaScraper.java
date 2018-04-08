package com.diogorodrigues.caixabreak;

import android.annotation.SuppressLint;

import com.diogorodrigues.caixabreak.Entities.Card;
import com.diogorodrigues.caixabreak.Entities.Movement;
import com.diogorodrigues.caixabreak.Entities.Transaction;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.helper.HttpConnection;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Diogo Rodrigues on 24/03/2018.
 */

public class CaixaScraper {
    public final static String USER_AGENT = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/64.0.3282.186 Safari/537.36";
    public final static String BASE_URL = "https://portalprepagos.cgd.pt/portalprepagos/";
    public final static String WELCOME_PAGE = "login.seam?sp_param=PrePago";
    public final static String LOGIN_PAGE = "auth/forms/login.fcc";
    public final static String MANAGEMENT_PAGE = "private/saldoMovimentos.seam";
    public final static String TARGET_HOME = "/portalprepagos/private/home.seam";
    private static final String contentType = "application/x-www-form-urlencoded;charset=UTF-8";

    public CaixaScraper() {
    }


    public void parseBalance(Document doc, Card c) {
        Element ele;
        if (doc != null) {
            ele = doc.selectFirst("#consultaMovimentosCartoesPrePagos > div:nth-child(7) > div > div > div > div > p.valor > label:nth-child(1)");

            if (ele != null && !ele.text().isEmpty()) {
                String db = ele.text();
                c.setBalance(db + "€");
            }
        } else
            System.out.println("O parsing do saldo falhou!");
    }

    public void parseTransactions(Document doc, Card c) throws ParseException {
        Element table;
        Element totalsTable;
        if (doc != null) {
            //select and parse the transactions table
            table = doc.selectFirst("#consultaMovimentosCartoesPrePagos > div:nth-child(10) > table > tbody");
            Elements rows = table.select("tr");

            Movement mv = new Movement();
            List<Transaction> trs = new ArrayList<Transaction>();

            for (int i = 1; i < rows.size(); i++) { //first row is the col names so skip it.
                Element row = rows.get(i);
                Elements cols = row.select("td");
               // System.out.println("Row i=" + i + "---------");

                Transaction t = new Transaction();

                if (cols != null && cols.size() > 0 && !cols.text().equals("")) {
                    SimpleDateFormat dtf = new SimpleDateFormat("dd-MM-yyyy");

                    Date dt1 = dtf.parse(cols.get(0).text());
                    Date dt2 = dtf.parse(cols.get(1).text());

                    t.setlData(dt1);
                    t.setlDataValor(dt2);

                    t.setDescription(cols.get(2).text());
                    if (cols.get(3) != null)
                        t.setCredit(cols.get(3).text());
                    else
                        t.setCredit("");

                    if (cols.get(4) != null)
                        t.setDebit(cols.get(4).text());
                    else
                        t.setDebit("");

                    trs.add(t);
                }
            }
            mv.setTransactions(trs);

            totalsTable = doc.selectFirst("#consultaMovimentosCartoesPrePagos > div:nth-child(10) > div.mgtop > table > tbody");
            Element totalsRow = totalsTable.selectFirst("tr");
            Elements totalsCols = totalsRow.select("td");

            //total credit & total debit
            mv.setTotalCredit(totalsCols.get(3).text());
            mv.setTotalDebit(totalsCols.get(4).text());

            c.setMovement(mv);
        } else
            System.out.println("O parsing da transação falhou!");
    }

    public Document connect2CaixaBreak(Card c) throws IOException {
        String loginFormSubmit = "Entrar";


        HashMap<String, String> formData1 = new HashMap<>();
        HashMap<String, String> formData2 = new HashMap<>();
        Map<String, String> cookies;

        formData1.put("USERNAME", c.getUser());
        formData1.put("login_btn_1PPP", "OK");

        Connection.Response loginForm = null;
        try {
            loginForm = Jsoup.connect(BASE_URL + WELCOME_PAGE).
                    method(Connection.Method.GET).
                    header("Content-Type", contentType).
                    userAgent(USER_AGENT).
                    data(formData1).
                    ignoreHttpErrors(true).
                    timeout(0).
                    execute();
        } catch (IOException e) {
            e.printStackTrace();
        }

        formData2.put("target", TARGET_HOME);
        formData2.put("username", c.getUsername());
        formData2.put("userInput", c.getUser());
        formData2.put("passwordInput", "*****");
        formData2.put("loginForm:submit", loginFormSubmit);
        formData2.put("password", c.getPwd());

        cookies = loginForm.cookies();

        System.out.println(loginForm.statusMessage());
        //this closes the body?
        loginForm = null;

        Connection.Response loginForm2 = null;
        try {
            loginForm2 = Jsoup.connect(BASE_URL + LOGIN_PAGE).
                    header("Content-Type", contentType).
                    cookies(cookies).
                    method(Connection.Method.POST).
                    userAgent(USER_AGENT).
                    data(formData2).
                    timeout(0).
                    ignoreHttpErrors(true).
                    execute();
        } catch (IOException e) {
            e.printStackTrace();
        }

        cookies.putAll(loginForm2.cookies());// save the cookies, this will be passed on to next request

        System.out.println(loginForm2.statusMessage());
        //this closes the body?
        loginForm2 = null;

        Connection.Response loginForm3 = null;
        try {
            loginForm3 = Jsoup.connect(BASE_URL + MANAGEMENT_PAGE).
                    header("Content-Type", contentType).
                    cookies(cookies).
                    userAgent(USER_AGENT).
                    method(Connection.Method.GET).
                    timeout(0).
                    ignoreHttpErrors(true).
                    execute();
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println(loginForm3.statusMessage());

        Document loginDoc = null; // this is the document that contains response html
        try {
            String body = loginForm3.body();
            loginDoc = Jsoup.parse(body);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return loginDoc;
    }


}
