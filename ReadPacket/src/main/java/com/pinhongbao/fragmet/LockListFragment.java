package com.pinhongbao.fragmet;


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
import com.pinhongbao.Adapter.LockListAdapter;
import com.pinhongbao.Model.lockListInfo;
import com.pinhongbao.R;
import com.pinhongbao.Util.UtilTool;
import com.pinhongbao.serviceTool.ApiService;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * 手气榜
 */
public class LockListFragment extends Fragment {


    @InjectView(R.id.pull_refresh_list)
    PullToRefreshListView mPullRefreshListView;
    List<lockListInfo> lockListInfoslist=new ArrayList<>();
    private LockListAdapter lockListAdapter;
    private ProgressDialog progressDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View inflate = inflater.inflate(R.layout.fragment_lock_list, container, false);
        ButterKnife.inject(this, inflate);
        initview();
        return inflate;
    }

    private void initview() {
        mPullRefreshListView.setMode(PullToRefreshBase.Mode.PULL_FROM_START);//设置了就没有上拉刷新更多的
        // 初始化数据和数据源
        lockListAdapter = new LockListAdapter(getActivity(), lockListInfoslist);
        mPullRefreshListView.setAdapter(lockListAdapter);
        progressDialog = ProgressDialog.show(getActivity(), "", "加载中...");
        progressDialog.setCancelable(true);
        getData();
        mPullRefreshListView
                .setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
                    @Override
                    public void onPullDownToRefresh(
                            PullToRefreshBase<ListView> refreshView) {
                        Log.d("TAG", "onPullDownToRefresh");
                        //这里写下拉刷新的任务
                        lockListInfoslist.clear();
                        getData();
                    }

                    @Override
                    public void onPullUpToRefresh(
                            PullToRefreshBase<ListView> refreshView) {
                        Log.d("TAG", "onPullUpToRefresh");
                        //这里写上拉加载更多的任务
                        //
                    }
                });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }
    /**
     * 加载数据
     */
    private void getData() {
        progressDialog.show();
        ApiService.getLockListInfo(getActivity(),   new ApiService.ParsedRequestListener<List<lockListInfo>>() {
            @Override
            public void onResponseResult(List<lockListInfo> lockListInfos) {
                for (int i=0;i<lockListInfos.size();i++){
                    lockListInfo lockListInfo = lockListInfos.get(i);
                    lockListInfo.setRanking("NO: "+(i+1));
                    if (i==lockListInfos.size()-1){
                        lockListInfo lockListInfo1 = lockListInfos.get(i);
                        lockListInfo1.setRanking("NO: 10");
                        lockListInfos.set(i,lockListInfo1);
                    }
                    lockListInfos.set(i,lockListInfo);
                }
                for (int j=0;j<lockListInfos.size();j++){
                    lockListInfoslist.add(lockListInfos.get(j));
                }
                Log.e("日志",""+lockListInfoslist.toString());
                lockListAdapter.notifyDataSetChanged();
            }

            @Override
            public void _OnError(String errormessage) {
                UtilTool.ShowToast(getActivity(),errormessage);

            }
        });
        progressDialog.dismiss();
        mPullRefreshListView.onRefreshComplete();
    }
}
