package example.web;

import android.app.Activity;
import android.content.Context;
import androidx.databinding.DataBindingUtil;

import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.webkit.JavascriptInterface;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.style.base.BaseDialog;
import com.style.framework.R;
import com.style.framework.databinding.DialogWebViewBinding;

public class WebViewDialog extends BaseDialog {

    //private String url = "file:///android_asset/interact.html";
    private String url = "http://test-wap.wujinpu.cn/aliverify/slidingVerify";
    //private String url = "http://192.168.0.149:8080/aliverify/slidingVerify";
    //private String url = "https://www.hao123.com";

    private OnResultListener mListener;
    private DialogWebViewBinding bd;

    public WebViewDialog(Context context) {
        super(context, R.style.Dialog_General);
        setOwnerActivity((Activity) context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.e("WebViewDialog", "onCreate");
        View contentView = LayoutInflater.from(getContext()).inflate(R.layout.dialog_web_view, null, false);
        bd = DataBindingUtil.bind(contentView);
        setContentView(bd.getRoot());
        bd.webView.setBackgroundColor(0);
        bd.webView.getBackground().setAlpha(0); // 设置填充透明度 范围：0-255
        bd.webView.getSettings().setJavaScriptEnabled(true);
        bd.webView.addJavascriptInterface(new JsInterface(), "control");
        bd.webView.setWebViewClient(new WebViewClient());
        bd.webView.loadUrl(url);

        Window window = getWindow();
        //window.setBackgroundDrawableResource(android.R.color.transparent);// 一句话搞定
        //默认对话框会有边距，宽度不能占满屏幕
        window.getDecorView().setPadding(0, 0, 0, 0);
        window.setGravity(Gravity.CENTER);
        int w = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 270, getContext().getResources().getDisplayMetrics());
        window.setLayout(w, ViewGroup.LayoutParams.WRAP_CONTENT);
        window.setWindowAnimations(R.style.Animations_SlideInFromBottom_OutToBottom);
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.e("WebViewDialog", "onStart");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.e("WebViewDialog", "onStop");
        bd.webView.clearHistory();
        bd.webView.clearCache(true);
        bd.webView.removeAllViews();
        bd.webView.destroy();
    }

    public void setOnResultListener(OnResultListener mListener) {
        if (mListener != null)
            this.mListener = mListener;
    }

    public interface OnResultListener {
        void onResult(String token, String sessionId, String sig, String scene);
    }

    public class JsInterface {

        @JavascriptInterface
        public void show(String toast) {
            showToast(toast);
        }

        @JavascriptInterface
        public void onVerifyResult(String nc_token, String csessionid, String sig, String scene) {
            Log.e("onVerifyResult", nc_token + "--" + csessionid + "--" + sig + "--" + scene);
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (mListener != null)
                        mListener.onResult(nc_token, csessionid, sig, scene);
                }
            });
        }
    }

    private void showToast(String str) {
        Toast.makeText(getOwnerActivity(), str, Toast.LENGTH_SHORT).show();
    }


}
