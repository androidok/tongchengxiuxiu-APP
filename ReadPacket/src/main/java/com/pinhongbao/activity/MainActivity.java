package com.pinhongbao.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.pinhongbao.R;
import com.pinhongbao.fragmet.InvitationFragment;
import com.pinhongbao.fragmet.LockListFragment;
import com.pinhongbao.fragmet.MeFragment;
import com.pinhongbao.fragmet.RedhallFragment;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    @InjectView(R.id.image_red_hall)
    ImageView imageRedHall;
    @InjectView(R.id.tv_red_hall)
    TextView tvRedHall;
    @InjectView(R.id.layout_red_hall)
    LinearLayout layoutRedHall;
    @InjectView(R.id.image_lock_list)
    ImageView imageLockList;
    @InjectView(R.id.tv_lock_list)
    TextView tvLockList;
    @InjectView(R.id.layout_lock_list)
    LinearLayout layoutLockList;
    @InjectView(R.id.image_invitation)
    ImageView imageInvitation;
    @InjectView(R.id.tv_invitation)
    TextView tvInvitation;
    @InjectView(R.id.layout_invitation)
    LinearLayout layoutInvitation;
    @InjectView(R.id.image_me)
    ImageView imageMe;
    @InjectView(R.id.tv_me)
    TextView tvMe;
    @InjectView(R.id.layout_me)
    LinearLayout layoutMe;
    @InjectView(R.id.activity_main)
    LinearLayout activityMain;
    private RedhallFragment redhallFragment;
    private LockListFragment lockListFragment;
    private InvitationFragment invitationFragment;
    private MeFragment meFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.inject(this);
        selector(0);
    }

    @OnClick({R.id.layout_red_hall,R.id.layout_lock_list,R.id.layout_invitation,R.id.layout_me})
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.layout_red_hall://红包大厅
                selector(0);
                break;
            case R.id.layout_lock_list://手气榜
                selector(1);
                break;
            case R.id.layout_invitation://邀请赚钱
                selector(2);
                break;
            case R.id.layout_me://我的
                selector(3);
                break;
        }
    }
    private void selector(int index) {
        resetImage();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        hideFragment(transaction);
        switch (index) {
            case 0:
                if (redhallFragment == null) {
                    redhallFragment = new RedhallFragment();
                    transaction.add(R.id.contonfragment, redhallFragment);
                } else {
                    transaction.show(redhallFragment);
                }
                imageRedHall.setImageResource(R.mipmap.red_hall_selector);
                tvRedHall.setTextColor(getResources().getColor(R.color.color_homepage));
                break;
            case 1:

                if (lockListFragment == null) {
                    lockListFragment = new LockListFragment();
                    transaction.add(R.id.contonfragment, lockListFragment);
                } else {
                    transaction.show(lockListFragment);
                }
                imageLockList.setImageResource(R.mipmap.luck_list_selector);
                tvLockList.setTextColor(getResources().getColor(R.color.color_homepage));
                break;
            case 2:
                if (invitationFragment == null) {
                    invitationFragment = new InvitationFragment();
                    transaction.add(R.id.contonfragment, invitationFragment);
                } else {
                    transaction.show(invitationFragment);
                }
                imageInvitation.setImageResource(R.mipmap.invitation_selector);
                tvInvitation.setTextColor(getResources().getColor(R.color.color_homepage));
                break;
            case 3:
                if (meFragment == null) {
                    meFragment = new MeFragment();
                    transaction.add(R.id.contonfragment, meFragment);
                } else {
                    transaction.show(meFragment);
                }
                imageMe.setImageResource(R.mipmap.me_selector);
                tvMe.setTextColor(getResources().getColor(R.color.color_homepage));
                break;
        }
        transaction.commit();

    }
    private void hideFragment(FragmentTransaction transaction) {
        if (redhallFragment != null) {
            transaction.hide(redhallFragment);
        }
        if (lockListFragment != null) {
            transaction.hide(lockListFragment);
        }
        if (invitationFragment !=null){
            transaction.hide(invitationFragment);
        }
        if (meFragment !=null){
            transaction.hide(meFragment);
        }
    }

    private void resetImage() {
        imageRedHall.setImageResource(R.mipmap.red_hall_default);
        tvRedHall.setTextColor(getResources().getColor(R.color.color_gray));
        imageLockList.setImageResource(R.mipmap.luck_list_default);
        tvLockList.setTextColor(getResources().getColor(R.color.color_gray));
        imageInvitation.setImageResource(R.mipmap.invitation_default);
        tvInvitation.setTextColor(getResources().getColor(R.color.color_gray));
        imageMe.setImageResource(R.mipmap.me_default);
        tvMe.setTextColor(getResources().getColor(R.color.color_gray));
    }
}
