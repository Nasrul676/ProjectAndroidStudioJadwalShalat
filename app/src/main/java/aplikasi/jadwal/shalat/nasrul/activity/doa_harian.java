package aplikasi.jadwal.shalat.nasrul.activity;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import info.blogbasbas.jadwalshalat.R;

public class doa_harian extends AppCompatActivity {

    private WebView webView1;
    private SwipeRefreshLayout swipe1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doa_harian);

        swipe1=findViewById(R.id.swipe1);
        swipe1.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadWeb();
            }
        });
        webView1 = findViewById(R.id.web_doa);
        webView1.getSettings().setJavaScriptEnabled(true);
        webView1.getSettings().setAppCacheEnabled(true);
        webView1.setWebViewClient(new WebViewClient());
        webView1.loadUrl("http://www.doaharianislami.com/2017/06/kumpulan-doa-sehari-hari-lengkap-dalam-bahasa-arab-latin-dan-artinya.html");
    }
    private void loadWeb() {
        webView1 = findViewById(R.id.web_doa);
        webView1.getSettings().setJavaScriptEnabled(true);
        webView1.getSettings().setAppCacheEnabled(true);
        webView1.loadUrl("http://www.doaharianislami.com/2017/06/kumpulan-doa-sehari-hari-lengkap-dalam-bahasa-arab-latin-dan-artinya.html");
        webView1.setWebViewClient(new WebViewClient(){
            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                webView1.loadUrl("file:///android_asset/error.html");
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                swipe1.setRefreshing(true);
            }
        });

        webView1.setWebChromeClient(new WebChromeClient(){
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                if (webView1.getProgress()== 90){
                    swipe1.setRefreshing(false);
                }else {
                    swipe1.setRefreshing(true);
                }
            }
        });
    }
    @Override
    public void onBackPressed(){
        if (webView1.canGoBack()){
            webView1.goBack();
        }else {
            finish();
        }
    }
}

