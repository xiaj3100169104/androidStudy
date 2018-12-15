package example.web;

import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.style.base.BaseDefaultTitleBarActivity;
import com.style.framework.R;
import com.style.framework.databinding.ActivityH5RemoteBinding;

import org.jetbrains.annotations.Nullable;


public class WebViewActivity extends BaseDefaultTitleBarActivity {

    ActivityH5RemoteBinding bd;
    //private String url = "http://192.168.1.200:8082/content/app/#/healthknowledge?id=5160&name=test";
    private String url = "http://wap.gd.10086.cn/nwap/wlw/wlwrealName/index.jsps";

    //private String urlLocal = "file:///android_asset/useragree.html";
    private String urlLocal = "https://watch.lemonnc.com/Content/wap-guardian";

    @Override
    protected void onCreate(@Nullable Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.activity_h5_remote);
    }


    @Override
    public void initData() {
        bd = getBinding();
        setToolbarTitle("");
        bd.btnLocal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bd.webView.loadUrl(urlLocal);
            }
        });
        bd.webView.getSettings().setJavaScriptEnabled(true);
        bd.webView.getSettings().setDomStorageEnabled(true);

        // 设置支持本地存储
        /*mWebView.getSettings().setDatabaseEnabled(true);
        //取得缓存路径
        String path = getActivity().getApplicationContext().getDir("cache", Context.MODE_PRIVATE).getPath();
        //设置路径
        mWebView.getSettings().setDatabasePath(path);
        //设置支持DomStorage
        mWebView.getSettings().setDomStorageEnabled(true);
        //设置存储模式
        mWebView.getSettings().setCacheMode(WebSettings.LOAD_DEFAULT);
        //设置适应屏幕
        mWebView.getSettings().setUseWideViewPort(true);
        mWebView.getSettings().setLoadWithOverviewMode(true);
        mWebView.getSettings().setSupportZoom(true);
        mWebView.getSettings().setBuiltInZoomControls(true);
        mWebView.getSettings().setDisplayZoomControls(false);
        //设置缓存
        mWebView.getSettings().setAppCacheEnabled(true);*/
        bd.webView.setWebChromeClient(new WebChromeClient() {
            public void onProgressChanged(WebView view, int progress) {
                // Activity和Webview根据加载程度决定进度条的进度大小
                setWebViewProgress(progress);
            }
        });
        String name = "25225272";
        String number = "1111";
        final String js = "javascript:document.getElementById('iccid').value = '" + name + "';document.getElementById('puk').value='" + number + "';";
        //设置WebViewClient就不会调用系统浏览器
        bd.webView.setWebViewClient(new WebViewClient() {

            public void onPageFinished(WebView view, String url) {
                Toast.makeText(WebViewActivity.this, "load finish", Toast.LENGTH_SHORT).show();
                setToolbarTitle(view.getTitle());
                if (Build.VERSION.SDK_INT >= 19) {
                    bd.webView.evaluateJavascript(js, new ValueCallback<String>() {
                        @Override
                        public void onReceiveValue(String s) {
                            logE(getTAG(), s);
                        }
                    });
                } else {
                    bd.webView.loadUrl(js);
                }
            }

            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                Toast.makeText(WebViewActivity.this, "start load", Toast.LENGTH_SHORT).show();
                super.onPageStarted(view, url, favicon);
            }
        });
        bd.webView.loadUrl(url);


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        bd.webView.clearHistory();
        bd.webView.clearCache(true);
        bd.webView.removeAllViews();
        bd.webView.destroy();
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
