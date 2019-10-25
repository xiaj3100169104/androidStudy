package example.web;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.webkit.JavascriptInterface;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.Toast;

import com.dmcbig.mediapicker.utils.ScreenUtils;
import com.style.framework.R;
import com.style.framework.databinding.DialogWebViewBinding;
import com.style.utils.DeviceInfoUtil;

public class WebViewDialog extends Dialog {

    //private String url = "file:///android_asset/interact.html";
    //private String url = "http://test-wap.wujinpu.cn/aliverify/slidingVerify";
    private String url = "http://192.168.0.149:8080/aliverify/slidingVerify";
    //private String url = "https://www.hao123.com";

    private OnItemClickListener mListener;
    private DialogWebViewBinding bd;

    public WebViewDialog(Context context) {
        super(context, R.style.Dialog_General);
        setOwnerActivity((Activity) context);
        init(context);
    }

    public void init(Context context) {
        int w = (int) (DeviceInfoUtil.getScreenWidth(context) * 0.8);
        int h = (int) (w * 0.6);
        View contentView = LayoutInflater.from(context).inflate(R.layout.dialog_web_view, null, false);
        bd = DataBindingUtil.bind(contentView);
        setContentView(bd.getRoot());
        bd.webView.getSettings().setJavaScriptEnabled(true);
        bd.webView.addJavascriptInterface(new JsInterface(), "control");
        bd.webView.setWebViewClient(new WebViewClient());
        bd.webView.loadUrl(url);

        Window window = getWindow();
        //默认对话框会有边距，宽度不能占满屏幕
        window.getDecorView().setPadding(0, 0, 0, 0);
        window.setGravity(Gravity.CENTER);
        DisplayMetrics dm = new DisplayMetrics();
        ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(dm);
        window.setLayout(w, h);
        window.setWindowAnimations(R.style.Animations_SlideInFromBottom_OutToBottom);
    }

    @Override
    public void dismiss() {
        super.dismiss();
        bd.webView.clearHistory();
        bd.webView.clearCache(true);
        bd.webView.removeAllViews();
        bd.webView.destroy();
    }

    public void setOnItemClickListener(OnItemClickListener mListener) {
        if (mListener != null)
            this.mListener = mListener;
    }

    public interface OnItemClickListener {
        void OnClickCamera();
    }

    public class JsInterface {

        @JavascriptInterface
        public void show(String toast) {
            showToast(toast);
        }

        @JavascriptInterface
        public void onVerifyResult(String nc_token, String csessionid, String sig) {
            Log.e("onVerifyResult", nc_token + "--" + csessionid + "--" + sig);
        }
    }

    private void showToast(String str) {
        Toast.makeText(getOwnerActivity(), str, Toast.LENGTH_SHORT).show();
    }


}
