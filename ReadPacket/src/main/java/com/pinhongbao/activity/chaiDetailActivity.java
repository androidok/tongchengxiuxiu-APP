package com.pinhongbao.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.pinhongbao.Model.RedPackInfo;
import com.pinhongbao.R;
import com.pinhongbao.Util.GlideCircleTransform;
import com.pinhongbao.Util.SPUtils;
import com.pinhongbao.Util.UtilTool;
import com.pinhongbao.Util.chairedSelecPopuview;
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
 * 拆红包
 */
public class chaiDetailActivity extends Activity {

    @InjectView(R.id.image_back)
    ImageView imageBack;
    @InjectView(R.id.tv_right)
    TextView tvRight;
    @InjectView(R.id.tv_detaile)
    TextView tvDetaile;
    @InjectView(R.id.tv_price)
    TextView tvPrice;
    @InjectView(R.id.tv_num)
    TextView tvNum;
    @InjectView(R.id.tv_name)
    TextView tvName;
    @InjectView(R.id.tv_money)
    TextView tvMoney;
    @InjectView(R.id.pull_refresh_list)
    PullToRefreshListView mPullRefreshListView;
    List<RedPackInfo> redPackInfoList = new ArrayList<>();
    private chaiDetailActivity.myadapter myadapter;
    private ProgressDialog progressDialog;
    private String rid;
    private String back;
    private boolean isFirst;
    private com.pinhongbao.Util.chairedSelecPopuview chairedSelecPopuview;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chai_detail);
        ButterKnife.inject(this);
        initview();
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
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
        chai_redpack();
        mPullRefreshListView
                .setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
                    @Override
                    public void onPullDownToRefresh(
                            PullToRefreshBase<ListView> refreshView) {
                        Log.d("TAG", "onPullDownToRefresh");
                        //这里写下拉刷新的任务
                    }

                    @Override
                    public void onPullUpToRefresh(
                            PullToRefreshBase<ListView> refreshView) {
                        Log.d("TAG", "onPullUpToRefresh");
                        //这里写上拉加载更多的任务
                    }
                });
        chairedSelecPopuview = new chairedSelecPopuview(this, new chairedSelecPopuview.setonListener() {
            @Override
            public void setonlistfener(View view, ImageView imageView, TextView tv_gong, TextView tv_money) {
                if (isFirst) {
                    chairedSelecPopuview.dismiss();
                    isFirst = false;
                } else {
                    imageView.setImageDrawable(getResources().getDrawable(R.mipmap.ic_chaired_two));
                    isFirst = true;
                    tv_gong.setVisibility(View.VISIBLE);
                    tv_money.setVisibility(View.VISIBLE);
                    tv_money.setText(back + "元");
                }

            }
        });
        myHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                chairedSelecPopuview.showAtLocation(findViewById(R.id.tv_right), Gravity.CENTER, 0, 0);
            }
        },1000);

    }

    Handler myHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:

                    break;
            }
            super.handleMessage(msg);
        }
    };

    @OnClick({R.id.image_back, R.id.tv_right})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.image_back:
                finish();
                break;
            case R.id.tv_right:
                finish();

                break;
        }
    }

    private void getData() {
        progressDialog.show();
        ApiService.getRedPackInfo(this, SPUtils.getUid(this), rid, new ApiService.ParsedRequestListener<String>() {
            @Override
            public void onResponseResult(String onpase) {
                try {
                    JSONObject jsonObject = new JSONObject(onpase);
                    String code = jsonObject.getString("code");
                    if (commonParme.API_SERVICE_SUCCESSFUL.equalsIgnoreCase(code)) {
                        JSONArray paylogList = jsonObject.getJSONArray("paylogList");
                        JSONObject red = jsonObject.getJSONObject("red");
                        int pay = Integer.parseInt(red.getString("pay"));
                        int num = Integer.parseInt(red.getString("num"));
                        double price = pay * num - (num - 1) * 0.01;
                        tvPrice.setText(pay + "元参与,最高可得" + price);
                        tvNum.setText(num + "/" + num + "人");
                        tvDetaile.setText(red.getString("title"));
                        tvName.setText(SPUtils.getnicname(chaiDetailActivity.this));
                        List<RedPackInfo> redPackInfos = JSON.parseArray(paylogList.toString(), RedPackInfo.class);
                        for (int i = 0; i < redPackInfos.size(); i++) {
                            redPackInfoList.add(redPackInfos.get(i));
                        }
                        myadapter.notifyDataSetChanged();
                    } else {
                        UtilTool.ShowToast(chaiDetailActivity.this, jsonObject.getString("msg"));
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
                UtilTool.ShowToast(chaiDetailActivity.this, errormessage);
            }
        });
    }

    /**
     * 拆红包
     */
    private void chai_redpack() {
        ApiService.chai_redpack(this, rid, SPUtils.getUid(this), new ApiService.ParsedRequestListener<String>() {
            @Override
            public void onResponseResult(String onpase) {
                try {
                    JSONObject jsonObject = new JSONObject(onpase);
                    back = jsonObject.getString("back");
                    tvMoney.setText(back + "元");
                } catch (JSONException e) {
                    e.printStackTrace(
                    );
                }

            }

            @Override
            public void _OnError(String errormessage) {
                UtilTool.ShowToast(chaiDetailActivity.this, errormessage);

            }
        });
    }

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    public Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("chaiDetail Page") // TODO: Define a title for the content shown.
                // TODO: Make sure this auto-generated URL is correct.
                .setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]"))
                .build();
        return new Action.Builder(Action.TYPE_VIEW)
                .setObject(object)
                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
                .build();
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        AppIndex.AppIndexApi.start(client, getIndexApiAction());
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        AppIndex.AppIndexApi.end(client, getIndexApiAction());
        client.disconnect();
    }

    class myadapter extends BaseAdapter {

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
            TextView tv_name = (TextView) inflate.findViewById(R.id.tv_name);
            TextView tv_time = (TextView) inflate.findViewById(R.id.tv_time);
            RedPackInfo redPackInfo = redPackInfoList.get(i);
            Glide.with(chaiDetailActivity.this)
                    .load(redPackInfo.getAvatar())
                    .placeholder(R.mipmap.logo)
                    .error(R.mipmap.logo)
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .bitmapTransform(new GlideCircleTransform(chaiDetailActivity.this))
                    .into(image_head);
            tv_name.setText(redPackInfo.getUser_nicename());
            tv_time.setText(UtilTool.timedate(redPackInfo.getPaytime()));
            return inflate;
        }
    }
}
