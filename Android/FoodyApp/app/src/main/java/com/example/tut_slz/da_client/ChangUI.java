package com.example.tut_slz.da_client;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.webkit.WebView;
import android.widget.Toast;

import Items.Item;

/**
 * Created by tut_slz on 20/11/2014.
 */
public class ChangUI extends AsyncTask<Integer,Void,Boolean> {
    int iInterface;
    int Arg1;
    @Override
    protected Boolean doInBackground(Integer... params) {
        try {
            iInterface = params[0];
            //Arg1=params[1];
            return true;
        }
        catch (Exception e)
        {
            return false;
        }
    }

    private void SetOrder(Item item)
    {
        for (int i=0;i<Order.FOrder.size();++i)
        {
            if (Order.FOrder.get(i).Itemcmp(item))
            {
                Order.FOrder.get(i).IncCount();
                return;
            }
        }
        OrderFood of=new OrderFood();
        of.setItem(item);
        of.setCount(1);
        Order.FOrder.add(of);
    }

    @Override
    protected void onPostExecute(Boolean b)
    {
        if(true == b)
        {
            WebView wvItem;
            final Context context;
            final Item sItem;
            StringBuilder sbHtml=new StringBuilder();
            if (2 == iInterface){
                //context=Order.OContext;
                Order.Refresh();
            }
            else {
                if (3 == iInterface) {
                    sbHtml.append("<HTML><HEAD><LINK href=\"StyleCSS.css\" type=\"text/css\" rel=\"stylesheet\"/><script src=\"JScript.js\" type=\"text/javascript\"></script><script src=\"jquery-1.11.1.min.js\"></script></HEAD><body background=\"background.jpg\">");
                    sbHtml.append("<div style=\"position: absolute;left:30%;top:30%\"><h1>Let us get your Information</h1><table style=\"margin:auto;\"><tr><td>Your Name: </td><td><input id=\"username\" type=\"text\" /></td></tr><tr><td>Your phone number: </td><td><input id=\"phone\" type=\"text\" onkeypress='return event.charCode >= 48 && event.charCode <= 57'/></td></tr><tr><td>Your Address: </td><td><input id=\"addr\" type=\"text\" /></td></tr><tr><td></td><td><button onclick=\"ReadySend()\">Send</button></td></tr><table></div>");
                    //sbHtml.append("<button onclick=\"Stest()\">Start</button>");
                    //sbHtml.append("Your name: <input id=\"0\" type=\"text\" onblur=\"SetInfo(0)\"/></br>Phone number: <input id=\"1\" type=\"text\" onblur=\"SetInfo(1)\"/></br>Your Address: <input id=\"2\" type=\"text\" onblur=\"SetInfo(2)\" /></body></HTML>");
                    Order.wvOrder.loadDataWithBaseURL("file:///android_asset/", sbHtml.toString(), "text/html", "utf-8", null);

                } else {
                    sbHtml.append("<HTML><HEAD><LINK href=\"StyleCSS.css\" type=\"text/css\" rel=\"stylesheet\"/></HEAD><body>");
                    if (0 == iInterface) {
                        context = Food.Fcontext;
                        sItem = Food.aItem[Food.iSelect];
                    } else {
                        context = Restaurant.Rcontext;
                        sItem = Restaurant.ChildItem[Restaurant.RSelect][Restaurant.RCSeclect];
                    }
                    wvItem = new WebView(context);
                    sbHtml.append("<image class=\"Image\" src=\"" + sItem.getImg_url() + "\"/>");
                    sbHtml.append("<h1>" + sItem.getName() + "</h1>");
                    sbHtml.append("<p class=\"Detail Resname\">Restaurant: " + sItem.getRestaurant_name() + "</p>");
                    sbHtml.append("<p class=\"Detail\"> Date Create: " + sItem.getCreateDate().toString() + "<br>Date Update: " + sItem.getUpdateDate().toString() + "<br>Price: " + sItem.getPrice() + "<br>Status:  " + sItem.getStatus() + "</p>");
                    sbHtml.append("</body></HTML>");
                    wvItem.loadDataWithBaseURL("file:///android_asset/", sbHtml.toString(), "text/html", "utf-8", null);
                    AlertDialog.Builder adb = new AlertDialog.Builder(context).setView(wvItem).setTitle("Your Food").setPositiveButton("Buy", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            SetOrder(sItem);
                            Order.Changed = true;
                            Toast.makeText(context, "Buy Sucess", Toast.LENGTH_SHORT).show();
                        }
                    }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
                    adb.create().show();
                }
            }
        }
        else
        {
            Toast.makeText(Food.Fcontext,"Fail",Toast.LENGTH_SHORT).show();
        }
    }
}
