package com.ziac.messageapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class SendSMSActivity extends AppCompatActivity {


    EditText Mobile,Message;
    Button Sendsms;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Mobile=findViewById(R.id.mobileno);
        Message=findViewById(R.id.message);
        Sendsms=findViewById(R.id.sendsms);

        Sendsms.setOnClickListener(v -> {
            if(ContextCompat.checkSelfPermission(SendSMSActivity.this, Manifest.permission.SEND_SMS)
                    == PackageManager.PERMISSION_GRANTED){

                sendsms();
            }else {

                ActivityCompat.requestPermissions(SendSMSActivity.this,new String[]
                        {Manifest.permission.SEND_SMS},100);
            }
        });

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode==100 && grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){

            sendsms();;
        }else {

            Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show();
        }
    }

    private void sendsms() {
        String mobile = Mobile.getText().toString();
        String message = Message.getText().toString();

        if (!mobile.isEmpty() && !message.isEmpty()) { // Check if both mobile and message are not empty
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(mobile, null, message, null, null);
            Toast.makeText(this, "Sms sent successfully", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Please enter mobile and message to send", Toast.LENGTH_SHORT).show();
        }
    }

}