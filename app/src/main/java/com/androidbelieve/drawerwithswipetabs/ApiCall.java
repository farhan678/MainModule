package com.androidbelieve.drawerwithswipetabs;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

import org.apache.http.NameValuePair;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import urllink.Url;
public class ApiCall {

    Context mContext;
    //public static String kServer = " http://rajsscl.com/ws/Execution/index.php";
//    public static String user_id = "EMP16012500001";
//    public  static String sub_id = "SUB15112700002";
    public static String user_id = "EMP";
    public  static String sub_id = "SUB";

    public  static String user_type = "user_type";
    public ArrayList<NameValuePair> nameValuePairs;
    public ApiCall(Context ctx) {
         mContext = ctx;
    }
    public  static SharedPreferences userDetails;

    public void varAssign() {
        userDetails = mContext.getSharedPreferences("cms", mContext.MODE_PRIVATE);
        user_id = userDetails.getString("subid", "");
        sub_id = userDetails.getString("usersubid", "");
        user_type=userDetails.getString("usertype", "");
       // Toast.makeText(mContext.getApplicationContext(),sub_id  , Toast.LENGTH_LONG).show();
    }

    public String doGetLabourListing() {

        String url = Url.server + "?action=Lab-listing&user_id="+user_id+"&sub_id="+sub_id+"&user_type="+user_type;
        Log.v("Test", ""+"URL : " + url);
       /* nameValuePairs = new ArrayList<NameValuePair>();
        nameValuePairs.add(new BasicNameValuePair("user_id", user_id));
        nameValuePairs.add(new BasicNameValuePair("sub_id", sub_id));*/

        String result = doGetServerRequest(url);
        return result;
    }
    public String doGetSCListing() {

        String url = Url.server + "?action=SC-listing&user_id="+user_id+"&sub_id="+sub_id+"&user_type="+user_type;
        Log.v("Test", ""+"URL : " + url);
       /* nameValuePairs = new ArrayList<NameValuePair>();
        nameValuePairs.add(new BasicNameValuePair("user_id", user_id));
        nameValuePairs.add(new BasicNameValuePair("sub_id", sub_id));*/

        String result = doGetServerRequest(url);
        return result;
    }


    public String doGetProjectListing(){

        String url = Url.server + "?action=projects&user_id="+user_id+"&sub_id="+sub_id+"&user_type="+user_type;
        Log.v("Test",""+"PListing URL : "+ url);
        String result = doGetServerRequest(url);
        return result;
    }


    public String doGetReportedName(){

 //       String dummyUserId="USR16022800001";
        String url = Url.server + "?action=emp-name&user_id="+user_id+"&sub_id="+sub_id+"&user_type="+user_type;
        Log.v("Test",""+"PListing URL : "+ url);
        String result = doGetServerRequest(url);
        return result;
    }



    //http://www.rajsscl.com/ws/Execution/index.php?action=getAll&user_id=EMP16012500001&sub_id=SUB15112700002&user_type=SUB15112700002
    public String doGetAllSCProjectData(){

        String url = Url.server + "?action=SC-getAll&user_id="+user_id+"&sub_id="+sub_id+"&user_type="+user_type;
        Log.v("Test",""+"getall URL : "+ url);
        String result = doGetServerRequest(url);
        return result;
    }
    //http://www.rajsscl.com/ws/Execution/index.php?action=getAll&user_id=EMP16012500001&sub_id=SUB15112700002&user_type=SUB15112700002
    public String doGetAllProjectData(){

        String url = Url.server + "?action=Lab-getAll&user_id="+user_id+"&sub_id="+sub_id+"&user_type="+user_type;
        Log.v("Test",""+"getall URL : "+ url);
        String result = doGetServerRequest(url);
        return result;
    }

    public String doGetContractorListing(String proj_id){

        String url = Url.server + "?action=contractors&user_id="+user_id+"&sub_id="+sub_id+"&prj_id="+proj_id+"&user_type="+user_type;
        Log.v("Test",""+"PListing URL : "+ url);
        String result = doGetServerRequest(url);
        return result;
    }

    public String doGetContractListing(String proj_id,String Vendor_id){

        String url = Url.server+ "?action=contracts&user_id="+user_id+"&sub_id="+sub_id+"&prj_id="+proj_id+"&ven_id="+Vendor_id+"&user_type="+user_type;
        Log.v("Test",""+"PListing URL : "+ url);
        String result = doGetServerRequest(url);
        return result;
    }

    public String doGetDescriptionstListing(String proj_id,String contract_id){

        String url = Url.server + "?action=Lab-descr&user_id="+user_id+"&sub_id="+sub_id+"&prj_id="+proj_id+"&con_id="+contract_id+"&user_type="+user_type;
        Log.v("Test",""+"PListing URL : "+ url);
        String result = doGetServerRequest(url);
        return result;
    }

    public String doGetDeleteLabour(String batch_id){

        String url = Url.server + "?action=delete-labour&sub_id="+sub_id+"&user_id="+user_id+"&batch_id="+batch_id+"&user_type="+user_type;
        Log.v("Test",""+"PListing URL : "+ url);
        String result = doGetServerRequest(url);
        return result;
    }

    public String doGetDeleteImage(String batch_id){

        String url = Url.server+ "?action=Lab-delete-doc&sub_id="+sub_id+"&user_id="+user_id+"&user_type="+user_type+"&batch_id="+batch_id;
        Log.v("Test",""+"PListing URL : "+ url);
        String result = doGetServerRequest(url);
        return result;
    }


    public String doGetViewLabourDetail(String batch_id){

        String url = Url.server+ "?action=view-labour&sub_id="+sub_id+"&user_id="+user_id+"&batch_id="+batch_id+"&user_type="+user_type;
        Log.v("Test",""+"PListing URL : "+ url);
        String result = doGetServerRequest(url);
        return result;
    }

    public String doGetEditLabourDetail(String batch_id){

        String url = Url.server + "?action=view-labour&sub_id="+sub_id+"&user_id="+user_id+"&batch_id="+batch_id+"&user_type="+user_type;
        Log.v("Test",""+"PListing URL : "+ url);
        String result = doGetServerRequest(url);
        return result;
    }

    // Call the api with Get Method
    public String doGetServerRequest(String Url) {
        URL url;
        HttpURLConnection urlConnection = null;

        try {
            url = new URL(Url);
            urlConnection = (HttpURLConnection) url.openConnection();

            int responseCode = urlConnection.getResponseCode();

            if(responseCode == HttpURLConnection.HTTP_OK){
                String server_response = readStream(urlConnection.getInputStream());
                Log.v("CatalogClient", server_response);
                return server_response;
            }

        } catch (MalformedURLException e) {
            e.printStackTrace();
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        return null;
    }
    private String readStream(InputStream in) {
        BufferedReader reader = null;
        StringBuffer response = new StringBuffer();
        try {
            reader = new BufferedReader(new InputStreamReader(in));
            String line = "";
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return response.toString();
    }

}
