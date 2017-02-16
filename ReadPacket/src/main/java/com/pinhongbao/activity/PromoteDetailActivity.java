package com.pinhongbao.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.pinhongbao.Adapter.PromotedadailAdapter;
import com.pinhongbao.Model.promotedatailInfo;
import com.pinhongbao.R;
import com.pinhongbao.Util.SPUtils;
import com.pinhongbao.Util.UtilTool;
import com.pinhongbao.Util.commonParme;
import com.pinhongbao.serviceTool.ApiService;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * 推广明细
 */
public class PromoteDetailActivity extends Activity {

    @InjectView(R.id.tv_title)
    TextView tvTitle;
    @InjectView(R.id.tv_profit)
    TextView tvProfit;
    @InjectView(R.id.pull_refresh_list)
    PullToRefreshListView mPullRefreshListView;
    @InjectView(R.id.image_back)
    ImageView imageBack;
    List<promotedatailInfo> promotedatailInfoList = new ArrayList<>();
    @InjectView(R.id.layout_apprentice)
    RelativeLayout layoutApprentice;
    private PromotedadailAdapter promotedadailAdapter;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_promote_detail);
        ButterKnife.inject(this);
        initview();
    }

    private void initview() {
        tvTitle.setText("推广明细");
        mPullRefreshListView.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
        // 初始化数据和数据源
        promotedadailAdapter = new PromotedadailAdapter(this, promotedatailInfoList);
        mPullRefreshListView.setAdapter(promotedadailAdapter);
        progressDialog = ProgressDialog.show(this, "", "加载中...");
        progressDialog.setCancelable(true);
        getdata();
        mPullRefreshListView
                .setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
                    @Override
                    public void onPullDownToRefresh(
                            PullToRefreshBase<ListView> refreshView) {
                        Log.d("TAG", "onPullDownToRefresh");
                        //这里写下拉刷新的任务
                        promotedatailInfoList.clear();
                        getdata();
                    }

                    @Override
                    public void onPullUpToRefresh(
                            PullToRefreshBase<ListView> refreshView) {
                        Log.d("TAG", "onPullUpToRefresh");
                        //这里写上拉加载更多的任务
//                        ++page;
//                        getData(page);
                    }
                });
    }

    /**
     *
     */
    private void getdata() {
        progressDialog.show();
        ApiService.getPromotedetailInfo(this, SPUtils.getUid(this), new ApiService.ParsedRequestListener<String>() {
            @Override
            public void onResponseResult(String onpanse) {
                try {
                    JSONObject jsonObject = new JSONObject(onpanse);
                    String code = jsonObject.getString("code");
                    String profit = jsonObject.getString("profit");
                    tvProfit.setText("代理总收益:" + profit);
                    if (commonParme.API_SERVICE_SUCCESSFUL.equalsIgnoreCase(code)) {
                        if (!jsonObject.isNull("data")) {
                            JSONArray data = jsonObject.getJSONArray("data");
                            List<promotedatailInfo> promotedatailInfo = JSON.parseArray(data.toString(), promotedatailInfo.class);
                            if (promotedatailInfo != null && promotedatailInfo.size() > 0) {
                                for (int i = 0; i < promotedatailInfo.size(); i++) {
                                    promotedatailInfoList.add(promotedatailInfo.get(i));
                                }
                                promotedadailAdapter.notifyDataSetChanged();

                            }
                        }
                    } else {
                        UtilTool.ShowToast(PromoteDetailActivity.this, jsonObject.getString("msg"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                progressDialog.dismiss();

            }

            @Override
            public void _OnError(String errormessage) {
                progressDialog.dismiss();

            }
        });
        mPullRefreshListView.onRefreshComplete();
    }

    @OnClick({R.id.image_back, R.id.layout_apprentice})
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.image_back:

                finish();
                break;
            case R.id.layout_apprentice:
                startActivity(new Intent(this,ApprenticeActivity.class));
                break;
        }
    }
}
