package com.example.macmine.apiasynctaskserverconnector.async;

import android.app.Activity;
import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

//import org.apache.http.NameValuePair;
//import org.apache.http.client.HttpClient;
//import org.apache.http.client.ResponseHandler;
//import org.apache.http.client.entity.UrlEncodedFormEntity;
//import org.apache.http.client.methods.HttpPost;
//import org.apache.http.impl.client.BasicResponseHandler;
//import org.apache.http.impl.client.DefaultHttpClient;


/**
 * Created by root on 21/11/15.
 */
public class ServerConnector extends AsyncTask<String, String, String> {
    String urlParameters;
    boolean isGET = false;
    private onAsyncTaskComplete mListener;
    //    private List<NameValuePair> nameValuePairs;
    private Activity activity;

    public ServerConnector(Activity activity, String urlParameters) {
        this.activity = activity;
        this.urlParameters = urlParameters;
    }

    public boolean isGET() {
        return isGET;
    }

    public void setGET(boolean GET) {
        isGET = GET;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();


    }

    @Override
    protected String doInBackground(String... urls) {
        String response = "";
        try {


            response = excutePost(urls[0], urlParameters);

        } catch (Exception e) {
            e.printStackTrace();
        }


        return response;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        mListener.OnSucess(s);
    }

    public void setDataDowmloadListner(onAsyncTaskComplete dataDowmloadListner) {
        mListener = dataDowmloadListner;
    }

    public String excutePost(String targetURL, String urlParameters) {
        URL url;
        HttpURLConnection connection = null;
        try {
            //Create connection
            url = new URL(targetURL);
            connection = (HttpURLConnection) url.openConnection();
//            if (!isGET)
            {
                connection.setRequestMethod("POST");
                connection.setRequestProperty("Content-Type",
                        "application/x-www-form-urlencoded");
//                connection.addRequestProperty("Content-Type", "application/json");
                connection.setRequestProperty("Content-Length", "" +
                        Integer.toString(urlParameters.getBytes().length));
                connection.setRequestProperty("Content-Language", "en-US");
            }
//            else {
//                connection.setRequestMethod("GET");
//            }
//            connection.addRequestProperty("access_token", AppUtils.getAccesstoken(activity));


            connection.setUseCaches(false);
            connection.setDoInput(true);
            connection.setDoOutput(true);

            //Send request
            DataOutputStream wr = new DataOutputStream(
                    connection.getOutputStream());
            wr.writeBytes(urlParameters);
            wr.flush();
            wr.close();

            //Get Response
            InputStream is = connection.getInputStream();
            BufferedReader rd = new BufferedReader(new InputStreamReader(is));
            String line;
            StringBuffer response = new StringBuffer();
            while ((line = rd.readLine()) != null) {
                response.append(line);
                response.append('\r');
            }
            rd.close();
            return response.toString();

        } catch (Exception e) {

            e.printStackTrace();
            return null;

        } finally {

            if (connection != null) {
                connection.disconnect();
            }
        }
    }

    public interface onAsyncTaskComplete {
        public void OnSucess(String string);


    }
}
