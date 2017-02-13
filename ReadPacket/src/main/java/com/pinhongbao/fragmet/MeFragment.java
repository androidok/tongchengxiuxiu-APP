package com.pinhongbao.fragmet;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.pinhongbao.R;
import com.pinhongbao.Util.GlideCircleTransform;
import com.pinhongbao.Util.SPUtils;
import com.pinhongbao.Util.UtilTool;
import com.pinhongbao.activity.CashActivity;
import com.pinhongbao.activity.LoginActivity;
import com.pinhongbao.activity.PromoteDetailActivity;
import com.pinhongbao.activity.RedPackRecordActivity;
import com.pinhongbao.activity.RuleActivity;
import com.pinhongbao.activity.SendRedEnvelopeActivity;
import com.pinhongbao.activity.WithdrawalActivity;
import com.pinhongbao.activity.contactServiceActivity;

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
    @InjectView(R.id.image_head)
    ImageView imageHead;
    @InjectView(R.id.tv_niname)
    TextView tvNiname;
    @InjectView(R.id.layout_rule)
    RelativeLayout layoutRule;
    private boolean isNicname = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View inflate = inflater.inflate(R.layout.fragment_me, container, false);
        ButterKnife.inject(this, inflate);
        initView();
        return inflate;
    }

    private void initView() {
//        EventBus.getDefault().register(getActivity());
        setimage_heard();


        String getnicname = SPUtils.getnicname(getActivity());
        if (TextUtils.isEmpty(getnicname)) {
            isNicname = false;
        } else {
            isNicname = true;
            tvNiname.setText(getnicname);
        }
        tvId.setText(SPUtils.getUid(getActivity()));

    }

    private void setimage_heard() {
        String icon = SPUtils.getIcon(getActivity());
        if (TextUtils.isEmpty(icon) || "".equalsIgnoreCase(icon)) {
            Glide.with(getActivity())
                    .load(R.mipmap.logo)
                    .placeholder(R.mipmap.logo)
                    .error(R.mipmap.logo)
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .bitmapTransform(new GlideCircleTransform(getActivity()))
                    .into(imageHead);
        } else {
            Glide.with(getActivity())
                    .load(icon)
                    .placeholder(R.mipmap.logo)
                    .error(R.mipmap.logo)
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .bitmapTransform(new GlideCircleTransform(getActivity()))
                    .into(imageHead);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
//        EventBus.getDefault().unregister(getActivity());
    }

//    @Subscribe
//    public void onEventMainThread(EvenbusInfo info) {
//        if (info.isSuccessful()) {
//            setimage_heard();
//            tvId.setText(SPUtils.getUid(getActivity()));
//        }
//    }

    @Override
    public void onResume() {
        super.onResume();

    }

    private void resetInfo() {
        setimage_heard();
        tvId.setText(SPUtils.getUid(getActivity()));
        String getnicname = SPUtils.getnicname(getActivity());
        if (TextUtils.isEmpty(getnicname)) {
            isNicname = false;
        } else {
            isNicname = true;
            tvNiname.setText(getnicname);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }

    @OnClick({R.id.layout_pash, R.id.layout_layout, R.id.readpacket_init, R.id.layout_promote, R.id.layout_envelope, R.id.layout_service, R.id.layout_with, R.id.tv_niname, R.id.layout_rule})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.layout_layout://退出
                startActivity(new Intent(getActivity(), LoginActivity.class));
                break;
            case R.id.layout_pash://余额兑现
                if (TextUtils.isEmpty(SPUtils.getUid(getActivity()))) {
                    UtilTool.ShowToast(getActivity(),"请先登录");
                    startActivity(new Intent(getActivity(),LoginActivity.class));
                } else {
                    startActivity(new Intent(getActivity(), CashActivity.class));
                }
                break;
            case R.id.layout_promote://推广明细
                if (TextUtils.isEmpty(SPUtils.getUid(getActivity()))) {
                    UtilTool.ShowToast(getActivity(),"请先登录");
                    startActivity(new Intent(getActivity(),LoginActivity.class));
                } else {
                    startActivity(new Intent(getActivity(), PromoteDetailActivity.class));
                }
                break;
            case R.id.layout_service://联系客服
                startActivity(new Intent(getActivity(), contactServiceActivity.class));
                break;
            case R.id.layout_with://提现记录
                if (TextUtils.isEmpty(SPUtils.getUid(getActivity()))) {
                    UtilTool.ShowToast(getActivity(),"请先登录");
                    startActivity(new Intent(getActivity(),LoginActivity.class));
                } else {
                    startActivity(new Intent(getActivity(), WithdrawalActivity.class));
                }
                break;
            case R.id.layout_envelope://红包记录
                if (TextUtils.isEmpty(SPUtils.getUid(getActivity()))) {
                    UtilTool.ShowToast(getActivity(),"请先登录");
                    startActivity(new Intent(getActivity(),LoginActivity.class));
                } else {
                    startActivity(new Intent(getActivity(), RedPackRecordActivity.class));
                }

                break;
            case R.id.tv_niname://昵称
                if (!isNicname) {
                    startActivity(new Intent(getActivity(), LoginActivity.class));
                }
                break;
            case R.id.layout_rule:
                if (TextUtils.isEmpty(SPUtils.getUid(getActivity()))) {
                    UtilTool.ShowToast(getActivity(),"请先登录");
                    startActivity(new Intent(getActivity(),LoginActivity.class));
                } else {
                    startActivity(new Intent(getActivity(), RuleActivity.class));
                }

                break;
            case R.id.readpacket_init://发起红包
                if (TextUtils.isEmpty(SPUtils.getUid(getActivity()))) {
                    UtilTool.ShowToast(getActivity(),"请先登录");
                    startActivity(new Intent(getActivity(),LoginActivity.class));
                } else {
                    startActivity(new Intent(getActivity(),SendRedEnvelopeActivity.class));
                }
                break;
        }
    }
}
