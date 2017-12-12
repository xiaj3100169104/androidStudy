package example.activity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.style.base.BaseToolBarActivity;
import com.style.framework.R;
import com.style.view.progressbar.HorizontalProgressBar;


public class UserAgreeActivity extends BaseToolBarActivity {

    private WebView webView;
    private String url = "file:///android_asset/useragree.html";
    private HorizontalProgressBar progressBar;

    @Override
    protected void onCreate(Bundle arg0) {
        mLayoutResID = R.layout.activity_h5_remote;
        super.onCreate(arg0);
    }

    @Override
    public void initData() {
        setToolbarTitle("");

        progressBar = (HorizontalProgressBar) findViewById(R.id.MaterialProgressBar);

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
                Toast.makeText(UserAgreeActivity.this, "load finish", Toast.LENGTH_SHORT).show();
                setToolbarTitle(view.getTitle());
            }

            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                Toast.makeText(UserAgreeActivity.this, "start load", Toast.LENGTH_SHORT).show();
                super.onPageStarted(view, url, favicon);
            }
        });
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
