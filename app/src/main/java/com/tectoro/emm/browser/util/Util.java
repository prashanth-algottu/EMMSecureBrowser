package com.tectoro.emm.browser.util;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

import com.tectoro.emm.browser.SecondActivity;
import com.tectoro.emm.browser.R;

import java.net.URI;
import java.net.URISyntaxException;

public class Util extends AppCompatActivity {


    // It will take url and returns uri
    public static String getUri(String url) {
        URI uri = null;
        String host=null;
        try {
            uri = new URI(url);
            host = uri.getHost();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        return host;
    }

    public static String addingHttps(String host) {
        if (!host.startsWith("http")) {
            host = "https://" + host;
            SecondActivity.setingUrl(host);
        }else if (host.startsWith("http://")){
            StringBuffer stringBuffer = new StringBuffer(host);
            stringBuffer.insert(4,"s");
            host = String.valueOf(stringBuffer);
            System.out.println(host+" String buffer");
        }
        return host;
    }

    /*public static String lowerCaseUrl(String urlString) {
        String[] url = urlString.split("[.]");
        String url1 = url[0].toLowerCase();
        urlString = url1 + "." + url[1];
        return urlString;
    }*/

    public static void allowtheGooglrSearch(String urlString, WebView webView) {

        urlString = "https://google.com/search?q=" + urlString;
        SecondActivity.setingUrl(urlString);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.loadUrl(urlString);

    }

    public static void allowtheDomain(String urlString, WebView webView) {
        SecondActivity.setingUrl(urlString);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().getDomStorageEnabled();
        webView.loadUrl(urlString);
    }

    public void blocktheSites(Context context) {
        final Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.blocked_pop_up);
        Button button = dialog.findViewById(R.id.btn_ok);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        dialog.show();

    }
}
