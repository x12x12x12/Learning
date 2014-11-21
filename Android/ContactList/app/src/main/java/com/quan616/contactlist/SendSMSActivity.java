package com.quan616.contactlist;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.telephony.SmsManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.net.Uri;

import java.io.Console;


public class SendSMSActivity extends Activity {

    public Button buttonSend;
    public Button buttonCall;
    public EditText textPhoneNo;
    public EditText textSMS;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_sms);

        buttonSend  = (Button) findViewById(R.id.buttonSend);
        buttonCall  = (Button) findViewById(R.id.buttonCall);
        textPhoneNo = (EditText) findViewById(R.id.editTextPhoneNo);
        textSMS     = (EditText) findViewById(R.id.editTextSMS);

        Intent intent=getIntent();
        if(intent!=null){
            String data=intent.getStringExtra("contact");
//            textSMS.setText(data);
//            Uri contactData=intent.getData();
//            Cursor cursor=getContentResolver().query(contactData, null, null, null, null);
//            cursor.moveToFirst();
//            String phone=cursor.getString(cursor.getColumnIndexOrThrow(ContactsContract.Contacts.DISPLAY_NAME_PRIMARY));
//            cursor.close();
        }

        buttonSend.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                String phoneNo = textPhoneNo.getText().toString();
                String sms = textSMS.getText().toString();

                try {
                    SmsManager smsManager = SmsManager.getDefault();
                    smsManager.sendTextMessage(phoneNo, null, sms, null, null);
                    Toast.makeText(getApplicationContext(), "SMS Sent!",
                            Toast.LENGTH_LONG).show();
                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(),
                            "SMS faild, please try again later!",
                            Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }

            }
        });

        buttonCall.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                String call_number=textPhoneNo.getText().toString();
                Intent callIntent = new Intent(Intent.ACTION_CALL);
                callIntent.setData(Uri.parse("tel:"+call_number));
                startActivity(callIntent);

            }

        });
    }


}
