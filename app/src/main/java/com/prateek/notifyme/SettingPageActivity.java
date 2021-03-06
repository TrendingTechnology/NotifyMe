package com.prateek.notifyme;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.Toast;

import com.prateek.notifyme.commons.utils;
import com.prateek.notifyme.service.SQLiteHelper;

import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;

public class SettingPageActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    List<String> dataForSetting = new ArrayList<>();

    @Override
    protected void onStart() {
        super.onStart();
        SQLiteHelper sqLiteHelper = new SQLiteHelper(SettingPageActivity.this);
        Cursor dto  =  sqLiteHelper.getEnableStatusForAppsDB();
        while (dto.moveToNext()) {
            Log.i(utils.TAG, dto.getString(0) + " " + dto.getString(1) );
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_page);
        init();
        mAuth = FirebaseAuth.getInstance();
        ArrayAdapter adapter = new ArrayAdapter<String>(this, R.layout.activity_listview, dataForSetting);
        ListView listView = findViewById(R.id.settingDataView);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String name = adapterView.getItemAtPosition(i).toString();
                routeToRightPage(name);
            }
        });



    }

    // method to rate the app
    public void rateMe() {
        try {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + "com.whatsapp")));
        } catch (android.content.ActivityNotFoundException e) {
            startActivity(new Intent(Intent.ACTION_VIEW,
                    Uri.parse("http://play.google.com/store/apps/details?id=" + "com.whatsapp")));
        }
    }

    private void routeToRightPage(String name) {

        switch (name) {
            case "App Configure":
                startActivity(new Intent(SettingPageActivity.this, SubSettingPageActivity.class));
                overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
                break;
            case "Archive Notifications":
                startActivity(new Intent(SettingPageActivity.this, ArchiveActivity.class));
                overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
                break;
            case "Rate my app":
                rateMe();
                break;
            case "Share":
                invokeShare();
                break;
            case "Version":
                Toast.makeText(getApplicationContext(), "Version 0.0.1 SNAPSHOT ", Toast.LENGTH_SHORT).show();
                break;
            case "Sign out":
                Toast.makeText(getApplicationContext(), "Signed out !! ", Toast.LENGTH_SHORT).show();
                mAuth.signOut();
                Intent intent = new Intent(getApplicationContext(), SignIn.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
                overridePendingTransition(R.anim.slide_to_right, R.anim.slide_from_left);
//                overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);

                break;
        }
    }

    private void invokeShare() {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        String body = "Share this app, make it great again !!";
        String shareSub = "Wanna share this app";
        intent.putExtra(Intent.EXTRA_SUBJECT, shareSub);
        intent.putExtra(Intent.EXTRA_TEXT, body);
        startActivity(Intent.createChooser(intent, "Share Using"));
    }

    private void init() {
        dataForSetting.add("App Configure");
        dataForSetting.add("Archive Notifications");
        dataForSetting.add("Rate my app");
        dataForSetting.add("Share");
        dataForSetting.add("Sign out");
        dataForSetting.add("Version");
    }

    @Override
    public void onBackPressed() {
        finish();
        overridePendingTransition(R.anim.slide_to_right, R.anim.slide_from_left);
    }
}
