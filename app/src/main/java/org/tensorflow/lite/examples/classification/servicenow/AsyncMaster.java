package org.tensorflow.lite.examples.classification.servicenow;

import android.os.AsyncTask;
import android.util.Log;

import com.google.gson.Gson;

import org.tensorflow.lite.examples.classification.model.MissionDataObject;
import org.tensorflow.lite.examples.classification.model.RoverDataObject;
import org.tensorflow.lite.examples.classification.model.SensorDataObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.Authenticator;
import java.net.HttpURLConnection;
import java.net.PasswordAuthentication;
import java.net.URL;


public class AsyncMaster extends AsyncTask<Void, Void, String> {
    private final String USER_AGENT = "Mozilla/5.0";
    private final String user = "pmp5207@psu.edu";
    private final String pwd = "Shakshi122";

    private final String urlString = "https://emplkasperpsu1.service-now.com/api/now/table/u_master_test_table";

    private final SensorDataObject dataObject;
    private final RoverDataObject roverDataObject;
    private final MissionDataObject missionDataObject;

    public AsyncMaster(SensorDataObject dataObject, RoverDataObject roverDataObject, MissionDataObject missionDataObject){
        this.dataObject = dataObject;
        this.roverDataObject = roverDataObject;
        this.missionDataObject =missionDataObject;
    }

    @Override
    protected String doInBackground(Void... params) {
        try
        {
            URL url = new URL(urlString);
            HttpURLConnection con2 = (HttpURLConnection) url.openConnection();


            Authenticator.setDefault(new Authenticator() {
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(user, pwd.toCharArray());
                }
            });

            con2.setRequestMethod("POST");
            con2.setRequestProperty("User-Agent", USER_AGENT);
            con2.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
            con2.setRequestProperty("Content-Type","application/json");

            String postJsonData = new Gson().toJson(dataObject);
            String missionPostData = new Gson().toJson(missionDataObject);
            String roverPostData = new Gson().toJson(roverDataObject);


            String testMerge = postJsonData + missionPostData + roverPostData;

            String a = testMerge.replace("{","");
            String b = a.replace("}", ",");
            String d = b.substring(0, b.length()-1);
            String c = "{" + d + "}";
            System.out.println(d);

         /*   JSONObject SenseJson = new JSONObject(postJsonData);
            JSONObject missJson = new JSONObject(missionPostData);
            JSONObject rovJson = new JSONObject(roverPostData);

            JSONObject finalMerger = new JSONObject(();*/


/*            for (int i = 0; i < missJson.length(); i++)
            {
                String k;
                String val;
                k = missJson.getJSONObject("");
            }*/



/*            for (int i = 0; i < SenseJson.length(); i++) {
                //Get object info in JSON Array
                JSONObject info = jsonArray.getJSONObject(i);

                String u_number = info.get("u_number").toString();
                //Matches input with name
                if (u_number.equals(i_number)){
                    u_name = info.get("u_name").toString();
                    System.out.println(u_name);

                }
            }*/



            // Send post request
            con2.setDoOutput(true);
            DataOutputStream wr = new DataOutputStream(con2.getOutputStream());
            wr.writeBytes(c);
            wr.flush();
            wr.close();

            int responseCode = con2.getResponseCode();
            System.out.println("nSending 'POST' request to URL : " + url);
            System.out.println("Post Data : " + c);
            System.out.println("Response Code : " + responseCode);

            BufferedReader in = new BufferedReader(new InputStreamReader(con2.getInputStream()));
            StringBuffer response = new StringBuffer();

            String output;
            while ((output = in.readLine()) != null) {
                response.append(output);
            }
            in.close();
            con2.disconnect();

            //printing result from response
            System.out.println(response.toString());

        } catch (Exception e) {
            Log.e(AsyncTaskTableTes4.class.getSimpleName(), e.getMessage(), e);
        }

        return null;
    }
}
