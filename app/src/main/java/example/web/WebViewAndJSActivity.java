package example.web;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.View;
import android.webkit.ConsoleMessage;
import android.webkit.JavascriptInterface;
import android.webkit.JsPromptResult;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebView;

import com.style.base.activity.BaseTitleBarActivity;
import com.style.framework.R;
import com.style.framework.databinding.ActivityWebViewBinding;

import org.jetbrains.annotations.Nullable;


public class WebViewAndJSActivity extends BaseTitleBarActivity {

    ActivityWebViewBinding bd;
    private String url = "file:///android_asset/interact.html";
    private String url_feedback = "file:///android_asset/user_feedback.html";

    @Override
    protected void onCreate(@Nullable Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.activity_web_view);
        bd = getBinding();
        setTitleBarTitle("与js交互测试");
        bd.webView.getSettings().setJavaScriptEnabled(true);
        bd.webView.addJavascriptInterface(new JsInterface(), "control");

        bd.webView.setWebChromeClient(new WebChromeClient() {
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
        bd.webView.loadUrl(url);
        bd.btnJavaToJs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bd.webView.loadUrl("javascript:javaCallJavascript('javacalljs参数')");
            }
        });
        bd.btnFeedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bd.webView.loadUrl(url_feedback);
            }
        });
        bd.btnDialogWebview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showWebViewDialog();
            }
        });
    }

    private void showWebViewDialog() {
        WebViewDialog dialog = new WebViewDialog(this);
        dialog.setOnResultListener(new WebViewDialog.OnResultListener() {

            @Override
            public void onResult(String token, String sessionId, String sig, String scene) {
                dialog.dismiss();
                showToast(scene);
            }
        });
        dialog.show();
    }

    @Override
    public void onBackPressed() {
        if (bd.webView.canGoBack()) {
            bd.webView.goBack();
            return;
        }
        super.onBackPressed();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        bd.webView.clearHistory();
        bd.webView.clearCache(true);
        bd.webView.removeAllViews();
        bd.webView.destroy();
    }

    public class JsInterface {

        @JavascriptInterface
        public void show(String toast) {
            showToast(toast);
        }

        @JavascriptInterface
        public void JavaCommitFeedback(String toast) {
            showToast(toast);
        }
    }
}
