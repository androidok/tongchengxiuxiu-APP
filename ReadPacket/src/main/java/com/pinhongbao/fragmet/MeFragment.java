package com.pinhongbao.fragmet;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.pinhongbao.R;
import com.pinhongbao.activity.CashActivity;
import com.pinhongbao.activity.LoginActivity;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Me
 */
public class MeFragment extends Fragment {


    @InjectView(R.id.tv_id)
    TextView tvId;
    @InjectView(R.id.tv_banlen)
    TextView tvBanlen;
    @InjectView(R.id.readpacket_init)
    RelativeLayout readpacketInit;
    @InjectView(R.id.layout_pash)
    RelativeLayout layoutPash;
    @InjectView(R.id.layout_with)
    RelativeLayout layoutWith;
    @InjectView(R.id.layout_envelope)
    RelativeLayout layoutEnvelope;
    @InjectView(R.id.layout_promote)
    RelativeLayout layoutPromote;
    @InjectView(R.id.layout_service)
    RelativeLayout layoutService;
    @InjectView(R.id.layout_layout)
    RelativeLayout layoutLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View inflate = inflater.inflate(R.layout.fragment_me, container, false);
        ButterKnife.inject(this, inflate);
        return inflate;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }

    @OnClick({R.id.layout_pash,R.id.layout_layout,R.id.layout_promote,R.id.layout_service,R.id.layout_with})
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.layout_layout://退出
                startActivity(new Intent(getActivity(), LoginActivity.class));
                break;
            case R.id.layout_pash://余额兑现
                startActivity(new Intent(getActivity(), CashActivity.class));
                break;
            case R.id.layout_promote://推广明细
                break;
            case R.id.layout_service://联系客服
                break;
            case R.id.layout_with://提现记录
                break;
            case R.id.layout_envelope://红包记录
                break;
        }
    }
}
