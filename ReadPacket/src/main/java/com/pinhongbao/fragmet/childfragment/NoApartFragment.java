package com.pinhongbao.fragmet.childfragment;


import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.pinhongbao.Adapter.HomeAdapter;
import com.pinhongbao.Model.homeInfo;
import com.pinhongbao.R;
import com.pinhongbao.serviceTool.ApiService;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * 未拆开
 */
public class NoApartFragment extends Fragment {


    @InjectView(R.id.pull_refresh_list)
    PullToRefreshListView mPullRefreshListView;
    List<homeInfo> homeInfoList = new ArrayList<>();
    private HomeAdapter homeAdapter;
    private ProgressDialog progressDialog;
    private int page = 1;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View inflate = inflater.inflate(R.layout.fragment_no_apart, container, false);
        ButterKnife.inject(this, inflate);
        initView();
        return inflate;
    }
    /**
     * 初始化
     */
    private void initView() {
        mPullRefreshListView.setMode(PullToRefreshBase.Mode.BOTH);
        // 初始化数据和数据源
        homeAdapter = new HomeAdapter(getActivity(), homeInfoList);
        mPullRefreshListView.setAdapter(homeAdapter);
        progressDialog = ProgressDialog.show(getActivity(), "", "加载中...");
        progressDialog.setCancelable(true);
        getData(page);
        mPullRefreshListView
                .setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
                    @Override
                    public void onPullDownToRefresh(
                            PullToRefreshBase<ListView> refreshView) {
                        Log.d("TAG", "onPullDownToRefresh");
                        //这里写下拉刷新的任务
                        homeInfoList.clear();
                        page=1;
                        getData(page);
                    }

                    @Override
                    public void onPullUpToRefresh(
                            PullToRefreshBase<ListView> refreshView) {
                        Log.d("TAG", "onPullUpToRefresh");
                        //这里写上拉加载更多的任务
                        ++page;
                        getData(page);
                    }
                });
    }

    /**
     * 加载数据
     */
    private void getData(int page) {
        progressDialog.show();
        ApiService.gethomeInfo(getActivity(), "2", "22", page, new ApiService.ParsedRequestListener<List<homeInfo>>() {
            @Override
            public void onResponseResult(List<homeInfo> homeInfo) {
                Log.e("加载数据",""+homeInfo.toString());
                for (int i = 0; i < homeInfo.size(); i++) {
                    homeInfoList.add(homeInfo.get(i));
                }
                homeAdapter.notifyDataSetChanged();


            }

            @Override
            public void _OnError(String errormessage) {

            }
        });
        progressDialog.dismiss();
        mPullRefreshListView.onRefreshComplete();
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }
}
