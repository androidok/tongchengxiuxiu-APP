package com.pinhongbao.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.pinhongbao.Adapter.ApprenticeAdapter;
import com.pinhongbao.Model.ApprenticeInfo;
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
 * 下线徒弟
 */
public class ApprenticeActivity extends Activity {

    @InjectView(R.id.tv_title)
    TextView tvTitle;
    @InjectView(R.id.pull_refresh_list)
    PullToRefreshListView mPullRefreshListView;
    List<ApprenticeInfo> apprenticeInfoList = new ArrayList<>();
    @InjectView(R.id.image_back)
    ImageView imageBack;
    private ApprenticeAdapter apprenticeAdapter;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apprentice);
        ButterKnife.inject(this);
        initview();
    }

    /**
     * ui
     */
    private void initview() {
        tvTitle.setText("推广明细");
        mPullRefreshListView.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
        // 初始化数据和数据源
        apprenticeAdapter = new ApprenticeAdapter(this, apprenticeInfoList);
        mPullRefreshListView.setAdapter(apprenticeAdapter);
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
                        apprenticeInfoList.clear();
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
     * 数据
     */
    private void getdata() {
        progressDialog.show();
        ApiService.getApprenticeInfo(this, SPUtils.getUid(this), new ApiService.ParsedRequestListener<String>() {
            @Override
            public void onResponseResult(String onpaser) {
                try {
                    JSONObject jsonObject = new JSONObject(onpaser);
                    if (jsonObject.getString("code").equalsIgnoreCase(commonParme.API_SERVICE_SUCCESSFUL)) {
                        if (!jsonObject.isNull("data")) {
                            JSONArray data = jsonObject.getJSONArray("data");
                            List<ApprenticeInfo> apprenticeInfos = JSON.parseArray(data.toString(), ApprenticeInfo.class);
                            for (int i = 0; i < apprenticeInfos.size(); i++) {
                                apprenticeInfoList.add(apprenticeInfos.get(i));
                            }
                            apprenticeAdapter.notifyDataSetChanged();
                        } else {
                            UtilTool.ShowToast(ApprenticeActivity.this, "没数据");
                        }
                    } else {
                        UtilTool.ShowToast(ApprenticeActivity.this, jsonObject.getString("msg"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                progressDialog.dismiss();
                mPullRefreshListView.onRefreshComplete();
            }

            @Override
            public void _OnError(String errormessage) {
                progressDialog.dismiss();
                UtilTool.ShowToast(ApprenticeActivity.this, errormessage);
                mPullRefreshListView.onRefreshComplete();
            }
        });

    }

    @OnClick(R.id.image_back)
    public void onClick() {
        finish();
    }
}
