package com.pinhongbao.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.pinhongbao.Model.RedPackInfo;
import com.pinhongbao.R;
import com.pinhongbao.Util.GlideCircleTransform;
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
 * 详情
 */
public class DetailActivity extends Activity {

    @InjectView(R.id.tv_contanct)
    TextView tvContanct;
    @InjectView(R.id.btn_join)
    Button btnJoin;
    @InjectView(R.id.btn_back)
    Button btnBack;
    @InjectView(R.id.pull_refresh_list)
    PullToRefreshListView mPullRefreshListView;
    private String rid;
    List<RedPackInfo> redPackInfoList=new ArrayList<>();
    private DetailActivity.myadapter myadapter;
    private ProgressDialog progressDialog;
    private boolean isJson=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        ButterKnife.inject(this);
        initview();
    }

    private void initview() {
        Bundle extras = getIntent().getExtras();
        rid = extras.getString("rid");
        mPullRefreshListView.setMode(PullToRefreshBase.Mode.DISABLED);
        // 初始化数据和数据源
        myadapter = new myadapter();
        mPullRefreshListView.setAdapter(myadapter);
        progressDialog = ProgressDialog.show(this, "", "加载中...");
        progressDialog.setCancelable(true);
        getData();
        mPullRefreshListView
                .setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
                    @Override
                    public void onPullDownToRefresh(
                            PullToRefreshBase<ListView> refreshView) {
                        Log.d("TAG", "onPullDownToRefresh");
                        //这里写下拉刷新的任务
//                        homeInfoList.clear();
//                        page=1;.
//                        getData(page);
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
        mPullRefreshListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                startActivity(new Intent(DetailActivity.this, DetailActivity.class));
            }
        });
    }

    private void getData() {
        progressDialog.show();
        ApiService.getRedPackInfo(this, SPUtils.getUid(this), rid, new ApiService.ParsedRequestListener<String>() {
            @Override
            public void onResponseResult(String onpase) {
                try {
                    JSONObject jsonObject=new JSONObject(onpase);
                    String code = jsonObject.getString("code");
                    if (commonParme.API_SERVICE_SUCCESSFUL.equalsIgnoreCase(code)){
                        JSONArray paylogList = jsonObject.getJSONArray("paylogList");
                        JSONObject red = jsonObject.getJSONObject("red");
                        int pay = Integer.parseInt(red.getString("pay"));
                        int num = Integer.parseInt(red.getString("num"));
                        double price = pay * num - (num - 1) * 0.01;
                        tvContanct.setText("支付" + pay + "人,已参与" + red.getString("numed")+ "人,满" + num + "人开奖" + ",最高分得" + price + "元");
                        List<RedPackInfo> redPackInfos = JSON.parseArray(paylogList.toString(), RedPackInfo.class);
                        String status = red.getString("status");//是1的话就是可以拆了
                        for (int i=0;i<redPackInfos.size();i++){
                            redPackInfoList.add(redPackInfos.get(i));
                        }
                        if (jsonObject.isNull("paylog")){
                            isJson=false;
                            btnJoin.setText("戳我参与");
                        }else {
                            //已经参与了
                            isJson=true;
                            btnJoin.setText("已参与");

                        }
                        if ("1".equalsIgnoreCase(status)){
                            Intent intent=new Intent(DetailActivity.this,chaiDetailActivity.class);
                            intent.putExtra("rid",rid);
                            startActivity(intent);
                            finish();

                        }
                        myadapter.notifyDataSetChanged();
                    }else {
                        UtilTool.ShowToast(DetailActivity.this,jsonObject.getString("msg"));
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
                UtilTool.ShowToast(DetailActivity.this,errormessage);
            }
        });
    }

    @OnClick({R.id.btn_join,R.id.btn_back})
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_join:
                if (!isJson){

                }
                break;
            case R.id.btn_back:
                finish();
                break;
        }
    }
    class myadapter extends BaseAdapter{

        @Override
        public int getCount() {
            return redPackInfoList.size();
        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            View inflate = getLayoutInflater().inflate(R.layout.item_listview_redpack, null);
            ImageView image_head = (ImageView) inflate.findViewById(R.id.image_head);
            TextView tv_name= (TextView) inflate.findViewById(R.id.tv_name);
            TextView tv_time= (TextView) inflate.findViewById(R.id.tv_time);
            RedPackInfo redPackInfo = redPackInfoList.get(i);
            Glide.with(DetailActivity.this)
                    .load(redPackInfo.getAvatar())
                    .placeholder(R.mipmap.logo)
                    .error(R.mipmap.logo)
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .bitmapTransform(new GlideCircleTransform(DetailActivity.this))
                    .into(image_head);
            tv_name.setText(redPackInfo.getUser_nicename());
            tv_time.setText(UtilTool.timedate(redPackInfo.getPaytime()));
            return inflate;
        }
    }
}
