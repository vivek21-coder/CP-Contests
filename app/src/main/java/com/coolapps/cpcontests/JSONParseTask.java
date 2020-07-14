package com.coolapps.cpcontests;

import android.os.AsyncTask;

import org.json.JSONObject;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class JSONParseTask extends AsyncTask<String, Void, JSONObject> {
    @Override
    protected JSONObject doInBackground(final String... strings) {

        StringBuilder result = new StringBuilder();
        URL url;
        HttpURLConnection urlConnection;

        try {
            url = new URL(strings[0]);
            urlConnection = (HttpURLConnection) url.openConnection();
            InputStream inputStream = urlConnection.getInputStream();
            InputStreamReader reader = new InputStreamReader(inputStream);

            int data = reader.read();

            while (data != -1) {

                char ch = (char) data;
                result.append(ch);

                data = reader.read();
            }

            return new JSONObject(result.toString());

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}
