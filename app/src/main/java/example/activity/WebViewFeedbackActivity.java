package example.activity;

import android.os.Bundle;
import android.view.View;
import android.webkit.ConsoleMessage;
import android.webkit.JavascriptInterface;
import android.webkit.JsPromptResult;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.Button;

import com.style.base.BaseToolBarActivity;
import com.style.framework.R;

import butterknife.Bind;

public class WebViewFeedbackActivity extends BaseToolBarActivity {

    @Bind(R.id.btn_java_to_js)
    Button btnJavaToJs;
    private WebView webView;
    private String url = "file:///android_asset/user_feedback.html";

    @Override
    protected void onCreate(Bundle arg0) {
        mLayoutResID = R.layout.activity_web_view;
        super.onCreate(arg0);
    }

    @Override
    public void initData() {
        setToolbarTitle("");

        webView = (WebView) findViewById(R.id.webView);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.addJavascriptInterface(new JsInterface(), "control");

        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int progress) {
                // Activity和Webview根据加载程度决定进度条的进度大小

            }

            @Override
            public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
                showToast("onJsAlert");
                result.confirm();
                return true;
            }

            @Override
            public boolean onJsConfirm(WebView view, String url, String message, JsResult result) {
                return super.onJsConfirm(view, url, message, result);
            }

            @Override
            public boolean onJsPrompt(WebView view, String url, String message, String defaultValue, JsPromptResult result) {
                return super.onJsPrompt(view, url, message, defaultValue, result);
            }

            @Override
            public boolean onConsoleMessage(ConsoleMessage consoleMessage) {
                showToast(consoleMessage.message());
                return true;//拦截log
            }
        });

        btnJavaToJs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                webView.loadUrl("javascript:javaCallJavascript('javacalljs参数')");
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

    public class JsInterface {

        @JavascriptInterface
        public void JavaCommitFeedback(String toast) {
            showToast(toast);
        }

    }
}
