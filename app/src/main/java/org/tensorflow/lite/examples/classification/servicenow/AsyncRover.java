package org.tensorflow.lite.examples.classification.servicenow;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.*;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.json.JSONArray;
import org.tensorflow.lite.examples.classification.fragments.HomeViewModel;
import org.tensorflow.lite.examples.classification.model.RoverDataObject;
import org.tensorflow.lite.examples.classification.rover.RoverParams;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.Authenticator;
import java.net.HttpURLConnection;
import java.net.PasswordAuthentication;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.xml.transform.Result;

public class AsyncRover extends AsyncTask<Void, Void, String> {

    private final String USER_AGENT = "Mozilla/5.0";
    private final String user = "dqt5211@psu.edu";
    private final String pwd = "Abington1";

    private final String urlString = "https://emplkasperpsu1.service-now.com/api/now/table/u_rover_table";

    String SNGet;

    String u_number;

    String u_name;
    String i_team;

    private final RoverDataObject roverDataObject;
    RoverParams rover = new RoverParams();

    public AsyncRover(RoverDataObject roverDataObject) {
        this.roverDataObject = roverDataObject;
    }

    @Override
    protected String doInBackground(Void... params) {

        try
        {
            URL url = new URL(urlString);
            HttpURLConnection con2 = (HttpURLConnection) url.openConnection();

            con2.setRequestMethod("GET");
            con2.setRequestProperty("User-Agent", USER_AGENT);

            Authenticator.setDefault(new Authenticator() {
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(user, pwd.toCharArray());
                }
            });


            int responseCode = con2.getResponseCode();
            System.out.println("nSending 'GET' request to URL : " + url);
            System.out.println("Response Code : " + responseCode);

            // Reading response from input Stream
            BufferedReader in = new BufferedReader(new InputStreamReader(con2.getInputStream()));
            String output;
            StringBuffer response = new StringBuffer();

            while ((output = in.readLine()) != null) {
                response.append(output);
            }
            in.close();
            con2.disconnect();

            i_team = roverDataObject.getRover_Name();
            rover.setRoverId(roverDataObject.getRover_Name());


            //covert response to string
            SNGet = response.toString();

            //Converts SNGet to JSON from String
            JSONObject json = new JSONObject(SNGet);


            JSONArray jsonArray = json.getJSONArray("result");
            for (int i = 0; i < jsonArray.length(); i++) {
                //Get object info in JSON Array
                JSONObject info = jsonArray.getJSONObject(i);

                String u_name = info.get("u_name").toString();
                //Matches input with name
                if (i_team.equals(u_name)){
                    u_number = info.get("u_number").toString();
                    System.out.println(u_number);

                }
            }


        } catch (Exception e){
            Log.e(AsyncRover.class.getSimpleName(), e.getMessage(), e);
        }
        return u_number;
    }
    @Override
    protected void  onPostExecute(String Result){
        super.onPostExecute(Result);

    }


}
