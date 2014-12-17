package com.example.tut_slz.da_client;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Toast;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Objects;
import java.util.concurrent.ExecutionException;

import Items.Item;

/**
 * Created by tut_slz on 19/11/2014.
 */
public class Food extends Activity {
    public static int iSelect=-1;
    public static Context Fcontext;
    public static Item[] aItem;
    private static WebView wvFood;
    //public static boolean IsOnline=true;
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        Fcontext=Food.this;
        wvFood=new WebView(this);
        final WebSettings wsFood=wvFood.getSettings();
        wsFood.setJavaScriptEnabled(true);
        wvFood.addJavascriptInterface(new JSInterface(this), "pFood");
        String sHTML="<HTML><HEAD><LINK href=\"StyleCSS.css\" type=\"text/css\" rel=\"stylesheet\"/><script src=\"JScript.js\" type=\"text/javascript\"></script></HEAD><body background=\"background.jpg\">";
        LoadData("http://"+ClientDA.rsInfo.getIPAddr()+":8080"+ClientDA.rsInfo.getUrlHotFood(),"0");
        //LoadData("http://"+ClientDA.rsInfo.getIPAddr()+":8080/rest-webapp/rest/store/Restaurant");
        sHTML+=MakeHTML(aItem,(byte)0).toString()+"</body></HTML>";
        wvFood.loadDataWithBaseURL("file:///android_asset/", sHTML, "text/html", "utf-8", null);
        this.setContentView(wvFood);
    }

    public static StringBuilder MakeHTML(Item[] aItem,byte bContext)
    {
        StringBuilder sbHtml = new StringBuilder();
        if (ClientDA.IsOnline)
        {
            for (int i=0;i<aItem.length;++i) {
                sbHtml.append("<div id=\"Food" + i + "\" class=\"divparent\" onclick=\"SelectContext("+i+","+i+","+bContext+")\" >");
                sbHtml.append("<image class=\"Image\" style=\"float:none;\" src=\"" + aItem[i].getImg_url() + "\"/>");
                sbHtml.append("<h2>" + aItem[i].getName() + "</h2>");
                sbHtml.append("<p class=\"Detail\">Price: " + aItem[i].getPrice() +"</p>");
                sbHtml.append("</div>");
            }
        }
        else
        {
            sbHtml.append("<h1 class=\"Error\">Cannot connect to server :(</h1>");
        }
        return sbHtml;
    }

    public static void LoadData(String URL,String sCaterory)
    {
        Item[] aItem;
        try {
            AsyncTask<String, Void, Boolean> cnt=new ConnectServer().execute(URL,sCaterory);
            //aItem= Arrays.copyOf(cnt.get(),cnt.get().length,Item[].class);
            //return aItem;
            ClientDA.IsOnline=cnt.get();
        } catch (InterruptedException e) {
            Toast.makeText(Fcontext,"Cannot connect to Server.",Toast.LENGTH_SHORT).show();
        } catch (ExecutionException e) {
            Toast.makeText(Fcontext,"Cannot connect to Server.",Toast.LENGTH_SHORT).show();
        }
    }

    public static void Refresh()
    {
        LoadData("http://"+ClientDA.rsInfo.getIPAddr()+":8080"+ClientDA.rsInfo.getUrlHotFood(),"0");
        String sHTML="<HTML><HEAD><LINK href=\"StyleCSS.css\" type=\"text/css\" rel=\"stylesheet\"/><script src=\"JScript.js\" type=\"text/javascript\"></script></HEAD><body background=\"background.jpg\">";
        sHTML+=MakeHTML(aItem,(byte)0).toString()+"</body></HTML>";
        wvFood.loadDataWithBaseURL("file:///android_asset/",sHTML , "text/html", "utf-8", null);
    }


}
