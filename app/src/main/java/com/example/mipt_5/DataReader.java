package com.example.mipt_5;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class DataReader {
    public static String getValuesFromApi(String apiCode) throws IOException{
        InputStream apiContentStream = null;
        String result ="";
        try {
            apiContentStream = downloadURLContent("https://www.floatrates.com/daily/eur.xml");
            result  = Parser.getCurrencyRatesBaseEur(apiContentStream);

        } finally {
            if (apiContentStream != null){
                apiContentStream.close();
            }
        }
        return result;
    }

    private static InputStream downloadURLContent(String urlString) throws IOException{
        URL url = new URL(urlString);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setReadTimeout(10000);
        conn.setConnectTimeout(15000);
        conn.setRequestMethod("GET");
        conn.setDoInput(true);
        conn.connect();
        return conn.getInputStream();
    }
}
