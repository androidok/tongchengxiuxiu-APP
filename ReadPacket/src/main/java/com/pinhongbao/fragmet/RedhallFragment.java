package com.pinhongbao.fragmet;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.pinhongbao.R;
import com.pinhongbao.Util.SPUtils;
import com.pinhongbao.activity.LoginActivity;
import com.pinhongbao.activity.SendRedEnvelopeActivity;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * 红包大厅
 */
public class RedhallFragment extends Fragment {


    @InjectView(R.id.inti)
    TextView inti;
    @InjectView(R.id.btn_nojoin)
    Button btnNojoin;
    @InjectView(R.id.btn_join)
    Button btnJoin;
    private NoJoinFragment noJoinFragment;
    private JoinFragment joinFragment;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View inflate = inflater.inflate(R.layout.fragment_redhall, container, false);
        ButterKnife.inject(this, inflate);
        selector(0);
        return inflate;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }

    @OnClick({R.id.btn_nojoin, R.id.btn_join,  R.id.inti})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_join://已经参与
                selector(1);
                break;
            case R.id.btn_nojoin://未参与
                selector(0);
                break;
            case R.id.inti://发起
                if (TextUtils.isEmpty(SPUtils.getUid(getActivity()))){
                    startActivity(new Intent(getActivity(), LoginActivity.class));
                }else {

                    startActivity(new Intent(getActivity(), SendRedEnvelopeActivity.class));
                }
                break;
        }
    }

    private void selector(int index) {
        restBtn();
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        hideFragment(transaction);
        switch (index) {
            case 0:
                if (noJoinFragment == null) {
                    noJoinFragment = new NoJoinFragment();
                    transaction.add(R.id.redhall_famelayout, noJoinFragment);
                } else {
                    transaction.show(noJoinFragment);
                }
                btnNojoin.setBackgroundResource(R.drawable.shape_no_join_selector);
                btnNojoin.setTextColor(getResources().getColor(R.color.color_while));
                break;
            case 1:

                if (joinFragment == null) {
                    joinFragment = new JoinFragment();
                    transaction.add(R.id.redhall_famelayout, joinFragment);
                } else {
                    transaction.show(joinFragment);
                }
                btnJoin.setBackgroundResource(R.drawable.shape_join_selector);
                btnJoin.setTextColor(getResources().getColor(R.color.color_while));
                break;
        }
        transaction.commit();

    }

    private void hideFragment(FragmentTransaction transaction) {
        if (noJoinFragment != null) {
            transaction.hide(noJoinFragment);
        }
        if (joinFragment != null) {
            transaction.hide(joinFragment);
        }
    }

    private void restBtn() {
        btnJoin.setBackgroundResource(R.drawable.shape_join_default);
        btnJoin.setTextColor(getResources().getColor(R.color.color_jnon_selector));
        btnNojoin.setBackgroundResource(R.drawable.shape_no_join_defaute);
        btnNojoin.setTextColor(getResources().getColor(R.color.color_jnon_selector));
    }
}
