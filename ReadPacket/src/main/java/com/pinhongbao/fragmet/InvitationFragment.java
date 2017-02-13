package com.pinhongbao.fragmet;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.pinhongbao.R;
import com.pinhongbao.Util.SPUtils;
import com.pinhongbao.Util.commonParme;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * 邀请赚钱
 */
public class InvitationFragment extends Fragment {


    @InjectView(R.id.webview)
    WebView webview;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View inflate = inflater.inflate(R.layout.fragment_invitation, container, false);
        ButterKnife.inject(this, inflate);
        initview();
        return inflate;
    }

    private void initview() {
        WebSettings webSettings = webview.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setAllowContentAccess(true);
        webSettings.setAppCacheEnabled(false);
        webSettings.setBuiltInZoomControls(false);
        webSettings.setUseWideViewPort(true);
        webSettings.setLoadWithOverviewMode(true);
        webview.setWebViewClient(new WebViewClient());
        webview.loadUrl(commonParme.apiservice.url_initation+ SPUtils.getUid(getActivity())+".html");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }
}
