package com.tectoro.emm.browser;

import androidx.appcompat.app.AppCompatActivity;
import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import com.tectoro.emm.browser.util.Util;
import com.tectoro.emm.browser.webview.MyBrowser;
import java.util.ArrayList;
import java.util.List;

public class SecondActivity extends AppCompatActivity {

    private static EditText url;
    private ImageView webBack, webForward, webRefresh, webHome;
    private ProgressBar progressBar;
    private WebView webView;
    boolean allAllow;
    List<String> exceptionList = new ArrayList<>();


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity2);
        getSupportActionBar().hide();

        url = (EditText) findViewById(R.id.url_input);
        progressBar = findViewById(R.id.progress_bar);
        webHome = findViewById(R.id.web_home);
        webBack = findViewById(R.id.web_back);
        webForward = findViewById(R.id.web_forward);
        webRefresh = findViewById(R.id.web_refresh);
        webView = findViewById(R.id.web_view);

        allAllow = MainActivity.getBool();
        exceptionList = MainActivity.getList();


        webView.setWebViewClient(new MyBrowser(SecondActivity.this));

        // when enter the go in soft keyboard this method will call
        url.setOnEditorActionListener(new TextView.OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                String urlString = url.getText().toString();
                hitOnSearch(urlString, webView);

                return true;
            }
        });

        // this is progress code
        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                progressBar.setProgress(newProgress);
                super.onProgressChanged(view, newProgress);
//                progressBar.setProgress(View.GONE);
            }
        });

        // when click on home it will call
        webHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        // when click on back it will call
        webBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (webView.canGoBack()) {
                    webView.goBack();
                }
            }
        });

        // when click on forward it will call
        webForward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (webView.canGoForward()) {
                    webView.goForward();
                }
            }
        });

        // when click on refresh it will call
        webRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                webView.reload();
            }
        });

        // when click on cross (close) it will call
        findViewById(R.id.web_clear).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                url.getText().clear();
            }
        });
    }

    private void hitOnSearch(String urlString, WebView webView) {
        String host = Util.getUri(urlString);
        if (host != null) {
            // this is uri
            host = Util.addingHttps(host);
            webView.loadUrl(host);
        } else {

            webView.getSettings().setJavaScriptEnabled(true);
//            webView.loadUrl(urlString);

            if (urlString.contains(".")){

                int flag = domainIsExistInList(urlString);

                if (allAllow) {
                    // list of sites are white and remaining block
                    if (flag == 1) {
                        Util.allowtheDomain(urlString,webView);

                    } else if (flag == 0) {
                        new Util().blocktheSites(this);
                    }
                } else if (!allAllow) {
                    // list of sites are blocked and remaining white
                    if (flag == 1) {
                        new Util().blocktheSites(this);
                    } else if (flag == 0) {
                        Util.allowtheDomain(urlString,webView);
                    }
                }

            }else {

                int flag = domainIsExistInList("google.com");
                int domainFlag =  domainIsExistInListWithoutExt(urlString);

                if (allAllow){
                    if(flag ==1 && domainFlag == 1){
                        Util.allowtheGooglrSearch(urlString,webView);
                    }else {
                        new Util().blocktheSites(this);
                    }
                }else if (!allAllow){
                    if(flag ==1 && domainFlag == 1){
                        new Util().blocktheSites(this);
                    }else {
                        Util.allowtheGooglrSearch(urlString,webView);
                    }
                }
            }
        }
    }

    private int domainIsExistInList(String urlString) {
        int flag = 0;
        if (exceptionList != null) {
            for (String exception : exceptionList) {
                if (exception.contains(urlString)) {
                    flag = 1;
                }
            }
        } else {
            Toast.makeText(this, "Exception list is empty", Toast.LENGTH_SHORT).show();
        }
        return flag;
    }

    private int domainIsExistInListWithoutExt(String urlString) {
        int flag = 0;
        if (exceptionList != null) {
            for (String exceptionStrings : exceptionList) {

                String[] splitArray = exceptionStrings.split("[.]");
                String splitString = splitArray[0];
                if (splitString.contains(urlString)) {
                    flag = 1;
                }
            }
        } else {
            Toast.makeText(this, "Exception list is empty", Toast.LENGTH_SHORT).show();
        }
        return flag;
    }

    public static void setingUrl(String u) {
        url.setText(u);
    }

}