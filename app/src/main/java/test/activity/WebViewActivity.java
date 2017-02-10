package test.activity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.view.ViewConfigurationCompat;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.style.base.BaseToolBarActivity;
import com.style.framework.R;

import me.zhanghai.android.materialprogressbar.MaterialProgressBar;

public class WebViewActivity extends BaseToolBarActivity {

    private WebView webView;
    private String url = "http://www.baidu.com";
    ViewConfigurationCompat ee;
    private MaterialProgressBar progressBar;

    @Override
    protected void onCreate(Bundle arg0) {
        mLayoutResID = R.layout.activity_web_view;
        super.onCreate(arg0);
    }

    @Override
    public void initData() {
        setToolbarTitle("网页测试");

        progressBar = (MaterialProgressBar) findViewById(R.id.MaterialProgressBar);
        progressBar.setMax(100);
        progressBar.setIndeterminate(false);


        webView = (WebView) findViewById(R.id.webView);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebChromeClient(new WebChromeClient() {
            public void onProgressChanged(WebView view, int progress) {
                // Activity和Webview根据加载程度决定进度条的进度大小
                setWebViewProgress(progress);

            }
        });
        //设置WebViewClient
        webView.setWebViewClient(new WebViewClient() {

            public void onPageFinished(WebView view, String url) {
                Toast.makeText(WebViewActivity.this, "load finish", Toast.LENGTH_SHORT).show();
                super.onPageFinished(view, url);
            }

            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                Toast.makeText(WebViewActivity.this, "start load", Toast.LENGTH_SHORT).show();
                super.onPageStarted(view, url, favicon);
            }
        });
        //webView.loadUrl("file:///android_asset/index.html");//加载本地网页文件
        webView.loadUrl(url);
    }

    @Override
    public void onBackPressed() {
        if (webView.canGoBack()) {
            webView.goBack();
            return;
        }
        super.onBackPressed();
    }

    public void setWebViewProgress(int progress) {
        progressBar.setProgress(progress);
        // 当加载到100%的时候 进度条自动消失
        if (progress == 100)
            progressBar.setVisibility(View.GONE);
    }
}
