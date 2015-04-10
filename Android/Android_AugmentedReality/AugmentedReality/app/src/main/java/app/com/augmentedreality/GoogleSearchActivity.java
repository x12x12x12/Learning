package app.com.augmentedreality;

/**
 * Created by HeavyRain on 4/6/2015.
 */
import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Arrays;

public class GoogleSearchActivity extends  Activity{
    /** Called when the activity is first created. */

    /*Define search item for google search*/
    public String search_item;

    public Button btnSearch;
    public EditText txtInput;
    public WebView webView;
    public Boolean bSearching=false;

    /*
   *START: Function for google search
   * */
    public void jsonSearch(String keyForSearch){
        new JsonSearchTask(keyForSearch).execute();
    }

    private class JsonSearchTask extends AsyncTask<Void, Void, Void> {

        String searchResult = "";
        String search_url = "http://ajax.googleapis.com/ajax/services/search/web?v=1.0&rsz=8&q=";
        String search_query;

        JsonSearchTask(String item){

            try {
                search_item = URLEncoder.encode(item, "utf-8");
            } catch (UnsupportedEncodingException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            search_query = search_url + search_item;
        }

        @Override
        protected Void doInBackground(Void... arg0) {

            try {
                searchResult = ParseResult(sendQuery(search_query));
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPreExecute() {

            btnSearch.setEnabled(false);
            btnSearch.setText("Waiting....");
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(Void result) {
//            webView.loadData(searchResult,
//                    "text/html",
//                    "UTF-8");
            webView.loadDataWithBaseURL(null,
                    searchResult,
                    "text/html",
                    "UTF-8",
                    null);

            btnSearch.setEnabled(true);
            btnSearch.setText("Search");
            super.onPostExecute(result);
        }

    }

    private String sendQuery(String query) throws IOException{
        String result = "";

        URL searchURL = new URL(query);

        HttpURLConnection httpURLConnection = (HttpURLConnection) searchURL.openConnection();

        if(httpURLConnection.getResponseCode() == HttpURLConnection.HTTP_OK){
            InputStreamReader inputStreamReader = new InputStreamReader(httpURLConnection.getInputStream());
            BufferedReader bufferedReader = new BufferedReader(
                    inputStreamReader,
                    8192);

            String line = null;
            while((line = bufferedReader.readLine()) != null){
                result += line;
            }

            bufferedReader.close();
        }

        return result;
    }

    private String ParseResult(String json) throws JSONException{
        String parsedResult = "";

        JSONObject jsonObject = new JSONObject(json);
        JSONObject jsonObject_responseData = jsonObject.getJSONObject("responseData");
        JSONArray jsonArray_results = jsonObject_responseData.getJSONArray("results");

        parsedResult += "Google Search APIs (JSON) for : <b>" + search_item + "</b><br/>";
        parsedResult += "Number of results returned = <b>" + jsonArray_results.length() + "</b><br/><br/>";

        /*Define new array of object for log result*/
        String[] searchResults= new String[jsonArray_results.length()];


        for(int i = 0; i < jsonArray_results.length(); i++){

            JSONObject jsonObject_i = jsonArray_results.getJSONObject(i);

            String iTitle = jsonObject_i.getString("title");
            String iContent = jsonObject_i.getString("content");
            String iUrl = jsonObject_i.getString("url");

            parsedResult += "<a href='" + iUrl + "'>" + iTitle + "</a><br/>";
            parsedResult += iContent + "<br/><br/>";

            searchResults[i]=iTitle+"//"+iUrl+"//"+iContent;

        }
        /*Log the searchResults*/
        Log.i("My Activities:", Arrays.deepToString(searchResults));

        /*return parseResult for display in Webview*/
        return parsedResult;
    }
    /*
    *END: Function for google search
    * */

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.google_search);

        btnSearch = (Button)findViewById(R.id.btnSearch);
        txtInput = (EditText)findViewById(R.id.txtInput);
        webView = (WebView)findViewById(R.id.webView);

        Intent i = getIntent();
        bSearching = Boolean.valueOf(i.getStringExtra("searching"));
        if(bSearching){
            txtInput.setText(i.getStringExtra("stringForSearch"));
            bSearching=false;
            jsonSearch(i.getStringExtra("stringForSearch"));
        }

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                jsonSearch(txtInput.getText().toString());
            }
        });

    }


}
