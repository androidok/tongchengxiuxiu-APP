package com.pinhongbao.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.pinhongbao.Adapter.RedPackRecodAdapter;
import com.pinhongbao.Model.redPackRecordInfo;
import com.pinhongbao.R;
import com.pinhongbao.Util.SPUtils;
import com.pinhongbao.Util.SelecPopuview;
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
 * 红包记录
 */
public class RedPackRecordActivity extends Activity {

    @InjectView(R.id.image_back)
    ImageView imageBack;
    @InjectView(R.id.tv_title)
    TextView tvTitle;
    @InjectView(R.id.pull_refresh_list)
    PullToRefreshListView mPullRefreshListView;
    @InjectView(R.id.tv_choose)
    TextView tvChoose;
    @InjectView(R.id.tv_all)
    TextView tvAll;
    @InjectView(R.id.tv_hong)
    Button tvHong;
    @InjectView(R.id.tv_tou)
    Button tvTou;
    private RedPackRecodAdapter redPackRecodAdapter;
    List<redPackRecordInfo> redPackRecordInfoList = new ArrayList<>();
    private ProgressDialog progressDialog;
    private int page = 1;
    private int type=-1;// -1全部   0  进行中   2已完成   3已退款

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_red_pack_record);
        ButterKnife.inject(this);
        initview();
    }

    private void initview() {
        tvTitle.setText("拼手气");
        mPullRefreshListView.setMode(PullToRefreshBase.Mode.BOTH);
        // 初始化数据和数据源
        redPackRecodAdapter = new RedPackRecodAdapter(this, redPackRecordInfoList);
        mPullRefreshListView.setAdapter(redPackRecodAdapter);
        progressDialog = ProgressDialog.show(this, "", "加载中...");
        progressDialog.setCancelable(true);
        getdata(page);
        mPullRefreshListView
                .setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
                    @Override
                    public void onPullDownToRefresh(
                            PullToRefreshBase<ListView> refreshView) {
                        Log.d("TAG", "onPullDownToRefresh");
                        //这里写下拉刷新的任务
                        redPackRecordInfoList.clear();
                        page = 1;
                        getdata(page);
                    }

                    @Override
                    public void onPullUpToRefresh(
                            PullToRefreshBase<ListView> refreshView) {
                        Log.d("TAG", "onPullUpToRefresh");
                        //这里写上拉加载更多的任务
                        ++page;
                        getdata(page);
                    }
                });
    }

    /**
     *
     */
    private void getdata(int page) {
        progressDialog.show();
        ApiService.getRedPackcoredInfo(this, SPUtils.getUid(this), type+"", page + "", new ApiService.ParsedRequestListener<String>() {
            @Override
            public void onResponseResult(String onpase) {
                try {
                    JSONObject jsonObject = new JSONObject(onpase);
                    if (jsonObject.getString("code").equalsIgnoreCase(commonParme.API_SERVICE_SUCCESSFUL)) {
                        JSONObject uinfo = jsonObject.getJSONObject("uinfo");
                        String backmoney = uinfo.getString("backmoney");
                        tvAll.setText(backmoney);
                        tvHong.setText("活动红包"+backmoney);
                        tvTou.setText("投入红包"+uinfo.getString("paymoney"));
                        if (!jsonObject.isNull("data")) {
                            JSONArray data = jsonObject.getJSONArray("data");
                            List<redPackRecordInfo> redPackRecordInfos = JSON.parseArray(data.toString(), redPackRecordInfo.class);
                            for (int i = 0; i < redPackRecordInfos.size(); i++) {
                                redPackRecordInfoList.add(redPackRecordInfos.get(i));
                            }
                            redPackRecodAdapter.notifyDataSetChanged();
                        } else {
                            UtilTool.ShowToast(RedPackRecordActivity.this, "没有数据");
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                mPullRefreshListView.onRefreshComplete();
                progressDialog.dismiss();
            }

            @Override
            public void _OnError(String errormessage) {
                progressDialog.dismiss();
                mPullRefreshListView.onRefreshComplete();
                UtilTool.ShowToast(RedPackRecordActivity.this, errormessage);
            }
        });


    }

    @OnClick({R.id.image_back, R.id.tv_choose})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.image_back:
                finish();
                break;
            case R.id.tv_choose://选择
                 SelecPopuview selecPopuview = new SelecPopuview(this, new SelecPopuview.setonListener() {
                    @Override
                    public void setonlistfener(View view, int index) {
                        redPackRecordInfoList.clear();
                        type=index;
                        page=1;
                        getdata(page);
                    }
                });
                selecPopuview.showAsDropDown(findViewById(R.id.tv_choose));
                break;
        }
    }
}
