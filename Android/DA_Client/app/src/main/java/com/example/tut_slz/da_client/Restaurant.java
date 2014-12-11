package com.example.tut_slz.da_client;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Toast;

import java.util.List;

/**
 * Created by tut_slz on 19/11/2014.
 */
public class Restaurant extends Activity{
    public static int RSelect=-1;
    public static int RCSeclect=-1;
    public static Context Rcontext;
    public static Item[] aItem;
    public static Item[][] ChildItem;
    private static WebView wvRes;
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        wvRes=new WebView(this);
        Rcontext=this;
        wvRes=new WebView(this);
        final WebSettings wsRes=wvRes.getSettings();
        wsRes.setJavaScriptEnabled(true);
        wvRes.addJavascriptInterface(new JSInterface(this), "pRes");
        String sHTML="";
        aItem = Food.LoadData("http://"+ClientDA.IPServer+":8080/rest-webapp/rest/store/Restaurant");
        if(null != aItem){
            ChildItem=new Item[aItem.length][];
        }
        sHTML=MakeHTML().toString();
        wvRes.loadDataWithBaseURL("file:///android_asset/", sHTML, "text/html", "utf-8", null);
        try {
            setContentView(wvRes);
        }catch (Exception e)
        {

        }

    }

    private static StringBuilder MakeHTML()
    {
        StringBuilder sbHtml = new StringBuilder();
        sbHtml.append("<HTML><HEAD><LINK href=\"StyleCSS.css\" type=\"text/css\" rel=\"stylesheet\"/><script src=\"JScript.js\" type=\"text/javascript\"></script></HEAD><body background=\"background.jpg\">");
        if (null != aItem)
        {
            for (int i=0;i<aItem.length;++i) {
                sbHtml.append("<div id=\"Res" + i + "\" class=\"Rdiv\" onclick=\"jsRes(" + i + ")\">");
                sbHtml.append("<image class=\"RImage\" src=\"" + aItem[i].getImg_url() + "\"/>");
                sbHtml.append("<h1>" + aItem[i].getRestaurant_name() + "</h1>");
                //sbHtml.append("<p class=\"Detail\">Price: " + aItem[i].getPrice() + "<br>Status:  " + aItem[i].getStatus() + "</p>");
                sbHtml.append("</div>");
                sbHtml.append("<div id=\"sdiv"+i+"\" class=\"Rdiv Rcdiv\"></div>");
                sbHtml.append("</br>");
            }
        }
        else
        {
            sbHtml.append("<h1 class=\"Error\">Cannot connect to server :(</h1>");
        }
        sbHtml.append("</body></HTML>");
        return sbHtml;
    }

    public static void Refresh()
    {
        aItem = Food.LoadData("http://"+ClientDA.IPServer+":8080/rest-webapp/rest/store/Restaurant");
        if(null != aItem){
            ChildItem=new Item[aItem.length][];
        }
        wvRes.loadDataWithBaseURL("file:///android_asset/", MakeHTML().toString(), "text/html", "utf-8", null);
    }
}
