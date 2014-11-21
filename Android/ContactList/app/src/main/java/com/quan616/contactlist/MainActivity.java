package com.quan616.contactlist;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.ContactsContract.Contacts;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.view.View;
import android.view.Menu;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import java.util.ArrayList;


public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        ListView listView=(ListView)findViewById(R.id.listView1);

        final Cursor cursor=getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null, null);
        startManagingCursor(cursor);
        String[] from  = new String[] {ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME, ContactsContract.CommonDataKinds.Phone.NUMBER};
        int[] to=new int[] {R.id.textView1,R.id.textView2};
        final SimpleCursorAdapter adapter=new SimpleCursorAdapter(this, R.layout.listitem, cursor, from, to);
        listView.setAdapter(adapter);
        listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        listView.setOnItemClickListener(new ListView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int pos, long id) {
                cursor.moveToPosition(pos);
                cursor.getString(cursor.getColumnIndex("_id"));
                Intent outData= new Intent(MainActivity.this,SendSMSActivity.class);
                outData.putExtra("contact",cursor.getString(cursor.getColumnIndex("_id")));
                startActivity(outData);
            }

        });
    }

}
