package com.tectoro.emm.browser.webview;

import android.content.Context;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;
import com.tectoro.emm.browser.MainActivity;
import com.tectoro.emm.browser.SecondActivity;
import com.tectoro.emm.browser.util.Util;
import java.util.ArrayList;
import java.util.List;

public class MyBrowser extends WebViewClient {
    private boolean allAllow;
    private List<String> exceptionList = new ArrayList<>();
    private Context context;

    public MyBrowser(Context context) {
        this.context = context;
    }



    @Override
    public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
        SecondActivity.setingUrl(request.getUrl().toString());

        view.loadUrl(request.getUrl().toString());

        allAllow = MainActivity.getBool();
        exceptionList = MainActivity.getList();

      /*  String urlString = request.getUrl().toString();
        urlString = Util.addingHttps(urlString);
        String URI = Util.getUri(urlString);*/

        SecondActivity.setingUrl(request.getUrl().toString());

        String d = request.getUrl().toString();
        System.out.println(d);
        String uri = Util.getUri(d);
        System.out.println(uri);
        int flag = domainIsExistInList(Util.getUri(request.getUrl().toString()));
        if (allAllow) {
            if (flag == 1) {
                view.getSettings().setAppCacheEnabled(true);
                view.getSettings().setDomStorageEnabled(true);
                view.loadUrl(request.getUrl().toString());
            } else {
                new Util().blocktheSites(context);
                view.stopLoading();
            }
        } else {
            if (flag == 1) {
                new Util().blocktheSites(context);
                view.stopLoading();
            } else {
                view.getSettings().setAppCacheEnabled(true);
                view.getSettings().setDomStorageEnabled(true);
                view.loadUrl(request.getUrl().toString());
            }
        }

        return true;
    }

    private int domainIsExistInList(String urlString) {
        System.out.println(urlString);
        int flag = 0;
        if (exceptionList != null) {
            for (String exception : exceptionList) {
                if (urlString.contains(exception)) {
                    flag = 1;
                }
            }
        } else {
            Toast.makeText(context, " Exception list is empty ", Toast.LENGTH_SHORT).show();
        }
        return flag;
    }

}
