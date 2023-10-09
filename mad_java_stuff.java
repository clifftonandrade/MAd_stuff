<?xml version="1.0" encoding="utf-8"?>
<manifest xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:tools="http://schemas.android.com/tools">
    <uses-feature
        android:name="android.hardware.telephony"
        android:required="true"/>
    <uses-permission android:name="android.permission.SEND_SMS"/>

    <application
        android:allowBackup="true"
        android:dataExtractionRules="@xml/data_extraction_rules"
        android:fullBackupContent="@xml/backup_rules"
        android:icon="@mipmap/ic_launcher"
        android:label="@string/app_name"
        android:roundIcon="@mipmap/ic_launcher_round"
        android:supportsRtl="true"
        android:theme="@style/Theme.Expt13"
        tools:targetApi="31">
        <activity
            android:name=".MainActivity"
            android:exported="true">
            <intent-filter>
                <action android:name="android.intent.action.MAIN" />

                <category android:name="android.intent.category.LAUNCHER" />
            </intent-filter>
        </activity>
    </application>

</manifest>
package com.example.expt13;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    EditText phone,messageSubject,messageText;
    Button sendBtn;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        phone = findViewById(R.id.Phone);
        messageText = findViewById(R.id.messageText);
        messageSubject = findViewById(R.id.subject);
        sendBtn = findViewById(R.id.sendBtn);
        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendEmail();
            }
        });
    }
    public void sendEmail(){
        String to = phone.getText().toString();
        String subject = messageSubject.getText().toString();
        String message = messageText.getText().toString();
        Intent emailIntent = new Intent(Intent.ACTION_SEND);
        emailIntent.setType("message/rfc822");
        emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{to});
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, subject);
        emailIntent.putExtra(Intent.EXTRA_TEXT, message);
        try {
            startActivity(Intent.createChooser(emailIntent,"Choose and Email app"));
        }catch (Exception e){
            Toast.makeText(this, "No Email app Found", Toast.LENGTH_SHORT).show();
        }
       


    }
}
package com.example.read_sms;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    private static final int SMS_PERMISSION_CODE = 102;
    private TextView messageTextView;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        messageTextView = findViewById(R.id.txtSms);
        if(ContextCompat.checkSelfPermission(this,Manifest.permission.READ_SMS)!= PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.READ_SMS},SMS_PERMISSION_CODE);
        }
        else{
            //call the function to display
            readMessages();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == SMS_PERMISSION_CODE){
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                readMessages();
            }
            else{
                messageTextView.setText("Permission Failed!!");
            }
        }
    }
    //function to read sms
    public void readMessages(){
        Uri uri = Uri.parse("content://sms/inbox");
        ContentResolver contentResolver = getContentResolver();
        String[]  projection = new String[]{"_id","address","body","date"};
        Cursor cursor = contentResolver.query(uri,projection,null,null,"date DESC");
        if(cursor != null && cursor.moveToFirst()){
            StringBuilder messageBuilder = new StringBuilder();
            do{
                 @SuppressLint("Range") String sender = cursor.getString(cursor.getColumnIndex("address"));
                @SuppressLint("Range") String message = cursor.getString(cursor.getColumnIndex("body"));
                @SuppressLint("Range") long dateMills = cursor.getLong(cursor.getColumnIndex("date"));
                String dateFormat = formatDateString(dateMills);
                messageBuilder.append("From:").append(sender).append("\n");
                messageBuilder.append("Message:").append(message).append("\n");
                messageBuilder.append("Date:").append(dateFormat).append("\n");


            }
            while (cursor.moveToNext());
            messageTextView.setText(messageBuilder.toString());
        }
        else{
            messageTextView.setText("No messages found");
        }

    }
    public String formatDateString(long mills){
        return new SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.getDefault()).format(new Date(mills));
    }
}
<?xml version="1.0" encoding="utf-8"?>
<manifest xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:tools="http://schemas.android.com/tools">
    <uses-feature
        android:name="android.hardware.telephony"
        android:required="true"/>
    <uses-permission android:name="android.permission.READ_SMS"/>

    <application
        android:allowBackup="true"
        android:dataExtractionRules="@xml/data_extraction_rules"
        android:fullBackupContent="@xml/backup_rules"
        android:icon="@mipmap/ic_launcher"
        android:label="@string/app_name"
        android:roundIcon="@mipmap/ic_launcher_round"
        android:supportsRtl="true"
        android:theme="@style/Theme.Read_sms"
        tools:targetApi="31">
        <activity
            android:name=".MainActivity"
            android:exported="true">
            <intent-filter>
                <action android:name="android.intent.action.MAIN" />

                <category android:name="android.intent.category.LAUNCHER" />
            </intent-filter>
        </activity>
    </application>

</manifest>