package com.example.tut_slz.da_client;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Toast;

import java.net.InetAddress;
import java.net.Socket;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutionException;

/**
 * Created by tut_slz on 19/11/2014.
 */
public class Food extends Activity {
    public static int iSelect=-1;
    public static Context Fcontext;
    public static Item[] aItem;
    private static WebView wvFood;
    public static boolean IsOnline=true;
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        Fcontext=Food.this;
        wvFood=new WebView(this);
        final WebSettings wsFood=wvFood.getSettings();
        wsFood.setJavaScriptEnabled(true);
        wvFood.addJavascriptInterface(new JSInterface(this), "pFood");
        String sHTML="<HTML><HEAD><LINK href=\"StyleCSS.css\" type=\"text/css\" rel=\"stylesheet\"/><script src=\"JScript.js\" type=\"text/javascript\"></script></HEAD><body background=\"background.jpg\">";
        aItem= LoadData("http://"+ClientDA.IPServer+":8080/rest-webapp/rest/store/Restaurant");
        sHTML+=MakeHTML(aItem,(byte)0).toString()+"</body></HTML>";
        wvFood.loadDataWithBaseURL("file:///android_asset/", sHTML, "text/html", "utf-8", null);
        this.setContentView(wvFood);
    }

    public static StringBuilder MakeHTML(Item[] aItem,byte bContext)
    {
        StringBuilder sbHtml = new StringBuilder();
        if (null != aItem)
        {
            for (int i=0;i<aItem.length;++i) {
                sbHtml.append("<div id=\"Food" + i + "\" class=\"divparent\" onclick=\"SelectContext("+i+","+i+","+bContext+")\" >");
                sbHtml.append("<image class=\"Image\" src=\"" + aItem[i].getImg_url() + "\"/>");
                sbHtml.append("<h1>" + aItem[i].getName() + "</h1>");
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

    public static Item[] LoadData(String URL)
    {
        Item[] aItem;
        try {
            AsyncTask<String, Void, Item[]> cnt=new ConnectServer().execute(URL);
            aItem=cnt.get();
            return aItem;
        } catch (InterruptedException e) {
            Toast.makeText(Fcontext,"Cannot connect to Server.",Toast.LENGTH_SHORT).show();
        } catch (ExecutionException e) {
            Toast.makeText(Fcontext,"Cannot connect to Server.",Toast.LENGTH_SHORT).show();
        }
        return null;
    }

    public static void Refresh()
    {
        aItem = LoadData("http://"+ClientDA.IPServer+":8080/rest-webapp/rest/store/Restaurant");
        String sHTML="<HTML><HEAD><LINK href=\"StyleCSS.css\" type=\"text/css\" rel=\"stylesheet\"/><script src=\"JScript.js\" type=\"text/javascript\"></script></HEAD><body background=\"background.jpg\">";
        sHTML+=MakeHTML(aItem,(byte)0).toString()+"</body></HTML>";
        wvFood.loadDataWithBaseURL("file:///android_asset/",sHTML , "text/html", "utf-8", null);
    }


}
