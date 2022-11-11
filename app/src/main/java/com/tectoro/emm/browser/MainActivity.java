package com.tectoro.emm.browser;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.RelativeLayout;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.tectoro.emm.browser.db.Config;
import com.tectoro.emm.browser.db.FireStore;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class MainActivity extends AppCompatActivity {
    RelativeLayout searchLayout;
    WebView webView;
    private static boolean allAllow;
    private static List<String> exceptionList = new ArrayList<>();


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity1);
        getSupportActionBar().hide();
        webView = findViewById(R.id.web_view);
        View decorview = findViewById(R.id.ovrlay);
        decorview.setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                | View.SYSTEM_UI_FLAG_FULLSCREEN);

        dbConnection();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        searchLayout = findViewById(R.id.searchMainLay);
        searchLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, SecondActivity.class));
            }
        });
    }

    private void dbConnection() {
        //loader start
        FireStore f = new FireStore(this);

        ExecutorService service = Executors.newSingleThreadExecutor();
        Future<Config> future = service.submit(
                () -> {
                    Config config = f.load();
                    //loader end
                    allAllow = config.isAllowByDefault();
                    exceptionList = config.getExceptionlist();
//                    Config c = new Config(allAllow,exceptionList);
                    return config;
                }
        );
        service.shutdown();
    }

    public static boolean getBool() {
        return allAllow;
    }

    public static List<String> getList() {
        return exceptionList;
    }


}
