package com.custom.view.project.webview;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.webkit.JavascriptInterface;
import android.webkit.JsPromptResult;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.custom.view.project.R;

import java.io.ByteArrayInputStream;

/**
 * 加载assets目录下的test.html文件
 * webView.loadUrl("file:///android_asset/test.html");
 * 加载网络资源（需要上网权限）
 * webView.loadUrl("http://blog.csdn.net");
 *  setWebViewClient（如果用户设置了WebViewClient，则在点击新的链接以后就不会跳转到系统浏览器了，而是在本WebView中显示。注意：并不需要覆盖 shouldOverrideUrlLoading 方法，同样可以实现所有的链接都在 WebView 中打开。）
 */
public class WebViewActivity extends AppCompatActivity {

    private WebView mWebView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);
        mWebView = (WebView) findViewById(R.id.webView);
        // 这种方式加载 默认是调用系统自带的浏览器来打开连接
        mWebView.loadUrl("http://www.baidu.com");

        // 通过WebView代为处理这个动作 ，那么需要通WebViewClient。
        mWebView.setWebViewClient(mWebViewClient);

        WebSettings webSettings = mWebView.getSettings();
        // 将JavaScript设置为可用
        webSettings.setJavaScriptEnabled(true);
        mWebView.addJavascriptInterface(new JsInterface(), "control");

        //Android中处理JS的警告，对话框等
        mWebView.setWebChromeClient(mWebChromeClient);
    }

    /**
     * JS 脚本调用Android  showToast() 方法
     * function showToast(toast){
     * javascript:control.showToast(toast);
     * }
     */

    public class JsInterface {
        @JavascriptInterface
        public void showToast(String text) {
            toastMsg(text);

            Log.d("html", "show toast success");
        }

        /**
         * function log(msg){
         * consolse.log(msg);
         * }
         *
         * @param msg
         */
        public void log(final String msg) {
            mWebView.post(new Runnable() {
                @Override
                public void run() {
                    mWebView.loadUrl("javascript log(" + "'" + msg + "'" + ")");
                }
            });
        }
    }

    private WebViewClient mWebViewClient = new WebViewClient() {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            Log.d("info", "----------getHost = " + Uri.parse(url).getHost());
            if (Uri.parse(url).getHost().equals("www.baidu.com")) {
                return false;
            }
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            startActivity(intent);

//            view.loadUrl(url);
            return true;
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
            toastMsg("开始加载");
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            toastMsg("加载完成");
        }

        // 拦截请求
        // 运行在非UI线程
        @Override
        public WebResourceResponse shouldInterceptRequest(WebView view, WebResourceRequest request) {
            String response = "<html>\n"+"<title>零度</title>\n"+ "<body>\n"+
                    "<a href=\"www.taobao.com \" >哈哈哈哈</a>\n"+"</body>\n"+"</html>\n";

            WebResourceResponse resourceResponse = new WebResourceResponse("text/html","utf-8",new ByteArrayInputStream(response.getBytes()));

            return resourceResponse;
        }
    };
    /**
     * Android中处理JS的警告，对话框等
     * 在Android中处理JS的警告，对话框等需要对WebView设置WebChromeClient对象,并复写其中的onJsAlert，onJsConfirm，onJsPrompt方法可以处理javascript的常用对话框。
     */
    private WebChromeClient mWebChromeClient = new WebChromeClient() {
        //处理javascript中的alert（弹框）
        @Override
        public boolean onJsAlert(WebView view, String url, String message, final JsResult result) {
            // 构建一个builder来显示网页中的对话框
            AlertDialog.Builder builder = new AlertDialog.Builder(WebViewActivity.this);
            builder.setTitle("提示对话框").setMessage(message)
                    .setPositiveButton("", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            result.confirm();
                        }
                    }).setNegativeButton("", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    result.cancel();
                }
            }).create();
            builder.create();
            builder.setCancelable(false);
            builder.show();
            return true;
        }

        @Override
        //处理JavaScript中的confirm
        public boolean onJsConfirm(WebView view, String url, String message, JsResult result) {
            return super.onJsConfirm(view, url, message, result);
        }

        @Override
        // 处理JavaScript中的prompt
        public boolean onJsPrompt(WebView view, String url, String message, String defaultValue, JsPromptResult result) {
            return super.onJsPrompt(view, url, message, defaultValue, result);
        }
    };

    public void toastMsg(String text) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onBackPressed() {
        if (mWebView.canGoBack()) {
            mWebView.goBack();
            return;
        }
        super.onBackPressed();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d("info", "-------------onDestroy()");
        if (mWebViewClient != null) {
            mWebViewClient = null;
        }
        if (mWebView != null) {
            mWebView.setWebViewClient(null);
            mWebView = null;
        }
    }
}
