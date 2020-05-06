package com.example.divinebrowser;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebViewClient;
import android.webkit.URLUtil;
import android.util.Patterns;

import com.example.divinebrowser.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private boolean pressed;
    private int steps = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Init activity
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setActionBar(binding.toolbar);
        getActionBar().setDisplayShowTitleEnabled(false);

        initWebView();
        initSearchBar();
        initButtons();
    }

    private void connect(String url) {
        steps = 0;
        binding.buttonLang.setVisibility(View.INVISIBLE);

        if (url.equals("index.html")) {
            binding.webview.loadUrl("file:///android_asset/index.html");
            binding.buttonLang.setVisibility(View.VISIBLE);
            return;
        }

        String parsedUrl = url;
        parsedUrl = parsedUrl.replaceAll(" ", "");
        // Try given url without parsing
        if (checkURL(parsedUrl))
            binding.webview.loadUrl(parsedUrl);
        else
            parsedUrl = "http://" + url;

        // Try given url with https:// prefix
        if (checkURL(parsedUrl))
            binding.webview.loadUrl(parsedUrl);
        else
            parsedUrl = "https://" + url;

        // Try given url with http:// prefix
        if (checkURL(parsedUrl))
            binding.webview.loadUrl(parsedUrl);
        else
            binding.webview.loadUrl("file:///android_asset/site_not_found.html");
        checkPageViewButtonStages();
        System.out.println("_LOG: Connecting to: "+ binding.webview.getUrl());
    }

    private void checkPageViewButtonStages() {
        if (!binding.webview.canGoBack() || steps == 10)
            binding.buttonBack.setActivated(false);
        else
            binding.buttonBack.setActivated(true);

        if (!binding.webview.canGoForward() || steps == 0)
            binding.buttonForward.setActivated(false);
        else
            binding.buttonForward.setActivated(true);
        System.out.println("_LOG: "+steps);
    }

    private boolean checkURL(String url) {
        try {
            return URLUtil.isValidUrl(url) && Patterns.WEB_URL.matcher(url).matches();
        } catch (Exception e) {
            System.out.println("_LOG: URL FAILED: " + e);
        }
        return false;
    }

    private void initWebView() {
        binding.webview.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                if (!binding.etSearch.isFocused())
                    binding.etSearch.setText(binding.webview.getUrl());
                checkPageViewButtonStages();
            }
        });
        binding.webview.setWebViewClient(new WebViewClient());
        binding.webview.getSettings().setJavaScriptEnabled(true);
        binding.webview.canGoBackOrForward(2);
        connect("http://www.google.fi");
    }

    private void initSearchBar() {
        binding.etSearch.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    connect(binding.etSearch.getText().toString());
                    checkPageViewButtonStages();
                    return true;
                }
                return false;
            }
        });
    }

    private void initButtons() {
        binding.buttonSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (binding.etSearch.getText().toString().isEmpty())
                    binding.etSearch.setText(binding.webview.getUrl());
                connect(binding.etSearch.getText().toString());
                checkPageViewButtonStages();
            }
        });

        binding.buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!binding.buttonBack.isActivated())
                    return;
                steps++;
                binding.webview.goBack();
                checkPageViewButtonStages();
            }
        });

        binding.buttonForward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!binding.buttonForward.isActivated())
                    return;
                steps--;
                binding.webview.goForward();
                checkPageViewButtonStages();
            }
        });

        binding.buttonLang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pressed = !pressed;
                switchLang();
            }
        });
    }

    private void switchLang() {
        String jsFunc = pressed ? "shoutOut()" : "initialize()";
        binding.webview.evaluateJavascript(jsFunc, null);
    }
}
