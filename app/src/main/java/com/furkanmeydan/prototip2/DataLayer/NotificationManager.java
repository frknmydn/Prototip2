package com.furkanmeydan.prototip2.DataLayer;

import android.util.Log;

import com.furkanmeydan.prototip2.Models.Post;
import com.furkanmeydan.prototip2.Models.Request;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

public class NotificationManager {
    String id;
    String dateTime;
    String message;
    String heading;

    public NotificationManager(String id, String dateTime, String message, String heading) {
        this.id = id;
        this.dateTime = dateTime;
        this.message = message;
        this.heading = heading;
    }

    public NotificationManager(String id, String message, String heading) {
        this.id = id;
        this.message = message;
        this.heading = heading;
    }


    public void NotificationForNow() throws IOException {

        String jsonResponse;

        URL url = new URL("https://onesignal.com/api/v1/notifications");
        HttpURLConnection con = (HttpURLConnection)url.openConnection();
        con.setUseCaches(false);
        con.setDoOutput(true);
        con.setDoInput(true);

        con.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
        con.setRequestProperty("Authorization", "Basic ODkxMDQwZTQtNDJiMS00Y2IzLTgwNGYtMjExNTFhZGEwNWFl");
        con.setRequestMethod("POST");

        String strJsonBody = "{"
                +   "\"app_id\": \"f374fb5a-1e58-45e9-9f0e-acf96f92c166\","
                +   "\"include_external_user_ids\": [\""+id+"\"],"
                +   "\"channel_for_external_user_ids\": \"push\","
                +   "\"data\": {\"foo\": \"bar\"},"
                +   "\"contents\": {\"en\": \""+message+"\"},"
                +   "\"headings\": {\"en\": \""+heading+"\"}"
                + "}";

        Log.d("Tag","strJsonBody:\n" + strJsonBody);

        byte[] sendBytes = strJsonBody.getBytes("UTF-8");
        con.setFixedLengthStreamingMode(sendBytes.length);

        OutputStream outputStream = con.getOutputStream();
        outputStream.write(sendBytes);

        int httpResponse = con.getResponseCode();
        Log.d("Tag","httpResponse: " + httpResponse);

        if (  httpResponse >= HttpURLConnection.HTTP_OK
                && httpResponse < HttpURLConnection.HTTP_BAD_REQUEST) {
            Scanner scanner = new Scanner(con.getInputStream(), "UTF-8");
            jsonResponse = scanner.useDelimiter("\\A").hasNext() ? scanner.next() : "";
            scanner.close();
        }
        else {
            Scanner scanner = new Scanner(con.getErrorStream(), "UTF-8");
            jsonResponse = scanner.useDelimiter("\\A").hasNext() ? scanner.next() : "";
            scanner.close();
        }
        Log.d("Tag","jsonResponse:\n" + jsonResponse);


    }

    public void NotificationForFuture() throws IOException {
        String jsonResponse;

        URL url = new URL("https://onesignal.com/api/v1/notifications");
        HttpURLConnection con = (HttpURLConnection)url.openConnection();
        con.setUseCaches(false);
        con.setDoOutput(true);
        con.setDoInput(true);

        con.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
        con.setRequestProperty("Authorization", "Basic ODkxMDQwZTQtNDJiMS00Y2IzLTgwNGYtMjExNTFhZGEwNWFl");
        con.setRequestMethod("POST");

        String strJsonBody = "{"
                +   "\"app_id\": \"f374fb5a-1e58-45e9-9f0e-acf96f92c166\","
                +   "\"include_external_user_ids\": [\""+id+"\"],"
                +   "\"channel_for_external_user_ids\": \"push\","
                +   "\"data\": {\"foo\": \"bar\"},"
                +   "\"contents\": {\"en\": \""+message+"\"},"
                +   "\"headings\": {\"en\": \""+heading+"\"},"
                +   "\"send_after\": \""+dateTime+"\""
                + "}";

        Log.d("Tag","strJsonBody:\n" + strJsonBody);

        byte[] sendBytes = strJsonBody.getBytes("UTF-8");
        con.setFixedLengthStreamingMode(sendBytes.length);

        OutputStream outputStream = con.getOutputStream();
        outputStream.write(sendBytes);

        int httpResponse = con.getResponseCode();
        Log.d("Tag","httpResponse: " + httpResponse);

        if (  httpResponse >= HttpURLConnection.HTTP_OK
                && httpResponse < HttpURLConnection.HTTP_BAD_REQUEST) {
            Scanner scanner = new Scanner(con.getInputStream(), "UTF-8");
            jsonResponse = scanner.useDelimiter("\\A").hasNext() ? scanner.next() : "";
            scanner.close();
        }
        else {
            Scanner scanner = new Scanner(con.getErrorStream(), "UTF-8");
            jsonResponse = scanner.useDelimiter("\\A").hasNext() ? scanner.next() : "";
            scanner.close();
        }
        Log.d("Tag","jsonResponse:\n" + jsonResponse);


    }


}
