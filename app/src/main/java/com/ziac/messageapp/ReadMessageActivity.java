package com.ziac.messageapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.Telephony;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class ReadMessageActivity extends AppCompatActivity {

    private ArrayList<String> smslist=new ArrayList<>();
    private ListView listView;
    private  static final int READ_SMS_PERMISSION_CODE=1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read_message);

        listView=findViewById(R.id.listview);
        ArrayAdapter<String> adapter=new ArrayAdapter<>(this, android.R.layout.simple_list_item_1,smslist);
        listView.setAdapter(adapter);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_SMS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_SMS}, READ_SMS_PERMISSION_CODE);
        } else {
            readSms();
        }


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode==READ_SMS_PERMISSION_CODE){
            if (grantResults.length>0 && grantResults[0]==PackageManager.PERMISSION_GRANTED){

                readSms();
                ArrayAdapter<String> adapter=(ArrayAdapter<String>) listView.getAdapter();
                adapter.notifyDataSetChanged();
            }
        }
    }
    private void readSms(){
        ContentResolver contentResolver=getContentResolver();
        Cursor cursor=contentResolver.query(
                Telephony.Sms.CONTENT_URI,
                null,
                null,
                null,
                null
        );

        if (cursor != null && cursor.moveToFirst()){
            do {
                @SuppressLint("Range") String address=cursor.getString(cursor.getColumnIndex(Telephony.Sms.ADDRESS));
                @SuppressLint("Range") String body=cursor.getString(cursor.getColumnIndex(Telephony.Sms.BODY));
                if (body.startsWith("RC ")) { // Check if the message starts with "This"
                    smslist.add("From: "+address+"\nMessage: "+body);
                }
            }while (cursor.moveToNext());
        }
        if (cursor !=null){
            cursor.close();
        }
    }

}