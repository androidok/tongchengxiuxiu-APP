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
import com.pinhongbao.Adapter.withdrawalAdapter;
import com.pinhongbao.Model.withdralInfo;
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
 * 提现记录
 */
public class WithdrawalActivity extends Activity {

    @InjectView(R.id.image_back)
    ImageView imageBack;
    @InjectView(R.id.tv_title)
    TextView tvTitle;
    @InjectView(R.id.pull_refresh_list)
    PullToRefreshListView mPullRefreshListView;
    private ProgressDialog progressDialog;
    private com.pinhongbao.Adapter.withdrawalAdapter withdrawalAdapter;
    List<withdralInfo> withdralInfos=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_withdrawal);
        ButterKnife.inject(this);
        initView();
    }

    private void initView() {
        tvTitle.setText("提现记录");
        mPullRefreshListView.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
        // 初始化数据和数据源
        withdrawalAdapter = new withdrawalAdapter(this, withdralInfos);
        mPullRefreshListView.setAdapter(withdrawalAdapter);
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
                        withdralInfos.clear();
                        getdata();
                    }

                    @Override
                    public void onPullUpToRefresh(
                            PullToRefreshBase<ListView> refreshView) {
                        Log.d("TAG", "onPullUpToRefresh");
                        //这里写上拉加载更多的任务
                    }
                });
    }
    private void getdata(){
        progressDialog.show();
        ApiService.getwithdrawalInfo(this, SPUtils.getUid(this), new ApiService.ParsedRequestListener<String>() {
            @Override
            public void onResponseResult(String onpause) {
                try {
                    JSONObject jsonObject=new JSONObject(onpause);
                    String code = jsonObject.getString("code");
                    if (commonParme.API_SERVICE_SUCCESSFUL.equalsIgnoreCase(code)){
                        if (!jsonObject.isNull("data")){
                            JSONArray data = jsonObject.getJSONArray("data");
                            List<withdralInfo> withdralInfoList = JSON.parseArray(data.toString(), withdralInfo.class);
                            for (int i=0;i<withdralInfoList.size();i++){
                                withdralInfos.add(withdralInfoList.get(i));
                            }
                            withdrawalAdapter.notifyDataSetChanged();
                        }else {
                            UtilTool.ShowToast(WithdrawalActivity.this,"没数据");
                        }
                    }else {
                        UtilTool.ShowToast(WithdrawalActivity.this,jsonObject.getString("msg"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                progressDialog.dismiss();
                mPullRefreshListView.onRefreshComplete();
            }

            @Override
            public void _OnError(String errormessage) {
                mPullRefreshListView.onRefreshComplete();
                UtilTool.ShowToast(WithdrawalActivity.this,errormessage);
                progressDialog.dismiss();
            }
        });

    }

    @OnClick(R.id.image_back)
    public void onClick() {
        finish();
    }
}
