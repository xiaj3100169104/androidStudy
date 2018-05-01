package example.activity;

import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.style.base.BaseActivity;
import com.style.framework.R;
import com.style.framework.databinding.ActivityH5RemoteBinding;
import com.style.view.progressbar.HorizontalProgressBar;


public class UserAgreeActivity extends BaseActivity {

    ActivityH5RemoteBinding bd;
    private String url = "file:///android_asset/useragree.html";

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        bd = DataBindingUtil.setContentView(this, R.layout.activity_h5_remote);
        super.setContentView(bd.getRoot());
    }

    @Override
    public void initData() {
        setToolbarTitle("");

        bd.webView.getSettings().setJavaScriptEnabled(true);
        bd.webView.setWebChromeClient(new WebChromeClient() {
            public void onProgressChanged(WebView view, int progress) {
                // Activity和Webview根据加载程度决定进度条的进度大小
                setWebViewProgress(progress);

            }
        });
        //设置WebViewClient
        bd.webView.setWebViewClient(new WebViewClient() {

            public void onPageFinished(WebView view, String url) {
                Toast.makeText(UserAgreeActivity.this, "load finish", Toast.LENGTH_SHORT).show();
                setToolbarTitle(view.getTitle());
            }

            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                Toast.makeText(UserAgreeActivity.this, "start load", Toast.LENGTH_SHORT).show();
                super.onPageStarted(view, url, favicon);
            }
        });
        bd.webView.loadUrl(url);
    }

    @Override
    public void onBackPressed() {
        if (bd.webView.canGoBack()) {
            bd.webView.goBack();
            return;
        }
        super.onBackPressed();
    }

    public void setWebViewProgress(int progress) {
        bd.progress.MaterialProgressBar.setProgress(progress);
        // 当加载到100%的时候 进度条自动消失
        if (progress == 100)
            bd.progress.MaterialProgressBar.setVisibility(View.GONE);
    }
}
