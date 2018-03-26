package com.diogorodrigues.caixabreak;

import com.diogorodrigues.caixabreak.Entities.Card;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Diogo Rodrigues on 24/03/2018.
 */

public class CaixaScraper {
    public final static String USER_AGENT = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/64.0.3282.186 Safari/537.36";
    public final static String  BASE_URL = "https://portalprepagos.cgd.pt/portalprepagos/";
    public final static String  WELCOME_PAGE ="login.seam?sp_param=PrePago";
    public final static String  LOGIN_PAGE = "auth/forms/login.fcc";
    public final static String  MANAGEMENT_PAGE = "private/saldoMovimentos.seam";
    public final static String  TARGET_HOME = "/portalprepagos/private/home.seam";
    private static final String contentType = "application/x-www-form-urlencoded;charset=UTF-8";

    public CaixaScraper() {
    }


    public void parseBalance(Document doc, Card c) {
        Element ele = doc.selectFirst("#consultaMovimentosCartoesPrePagos > div:nth-child(7) > div > div > div > div > p.valor > label:nth-child(1)");
        if(ele!=null && !ele.text().isEmpty()){
            String db = ele.text();
            c.setBalance(db+"€");
            }
        else
            System.out.println("O parsing do saldo falhou!");
    }

    public Document connect2CaixaBreak(Card c) {
        String loginFormSubmit = "Entrar";


        HashMap<String, String> formData1 = new HashMap<>();
        HashMap<String, String> formData2 = new HashMap<>();
        Map<String, String> cookies;

        formData1.put("USERNAME", c.getUser());
        formData1.put("login_btn_1PPP","OK");

        Connection.Response loginForm = null;
        try {
            loginForm = Jsoup.connect(BASE_URL+WELCOME_PAGE).
                    method(Connection.Method.GET).
                    header("Content-Type",contentType).
                    userAgent(USER_AGENT).
                    data(formData1).
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

        Connection.Response loginForm2 = null;
        try {
            loginForm2 = Jsoup.connect(BASE_URL+LOGIN_PAGE).
                    header("Content-Type",contentType).
                    cookies(cookies).
                    method(Connection.Method.POST).
                    userAgent(USER_AGENT).
                    timeout(0).
                    data(formData2).
                    execute();
        } catch (IOException e) {
            e.printStackTrace();
        }

        cookies.putAll(loginForm2.cookies());// save the cookies, this will be passed on to next request

        System.out.println(loginForm2.statusMessage());

        Connection.Response loginForm3 = null;
        try {
            loginForm3 = Jsoup.connect(BASE_URL+MANAGEMENT_PAGE).
                    header("Content-Type",contentType).
                    cookies(cookies).
                    userAgent(USER_AGENT).
                    method(Connection.Method.GET).
                    execute();
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println(loginForm3.statusMessage());
        Document loginDoc = null; // this is the document that contains response html
        try {
            loginDoc = loginForm3.parse();

        } catch (IOException e) {
            e.printStackTrace();
        }
        return loginDoc;
    }


}
