package com.pinhongbao.fragmet;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.pinhongbao.R;
import com.pinhongbao.Util.Indicator;
import com.pinhongbao.fragmet.childfragment.AmoutFragment;
import com.pinhongbao.fragmet.childfragment.NumkFragment;
import com.pinhongbao.fragmet.childfragment.TimeFragment;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * 未参与
 */
public class NoJoinFragment extends Fragment {


    @InjectView(R.id.tab_time)
    TextView tabTime;
    @InjectView(R.id.tab_num)
    TextView tabNum;
    @InjectView(R.id.tab_amount)
    TextView tabAmount;
    @InjectView(R.id.indicator)
    Indicator indicator;
    @InjectView(R.id.ViewPager)
    android.support.v4.view.ViewPager ViewPager;
    private ArrayList<Fragment> fragmentList;
    private FragmentPagerAdapter pagerAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View inflate = inflater.inflate(R.layout.fragment_no_join, container, false);
        ButterKnife.inject(this, inflate);
        init();
        return inflate;
    }

    /**
     * 初始化
     */
    private void init() {
        fragmentList = new ArrayList<>();
        fragmentList.add(new TimeFragment());
        fragmentList.add(new NumkFragment());
        fragmentList.add(new AmoutFragment());
        pagerAdapter = new FragmentPagerAdapter(getActivity().getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return fragmentList.get(position);
            }

            @Override
            public int getCount() {
                return fragmentList.size();
            }
        };
        ViewPager.setOnPageChangeListener(new android.support.v4.view.ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                indicator.scroll(position, positionOffset);
            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        ViewPager.setAdapter(pagerAdapter);
        ViewPager.setCurrentItem(0);

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }

    @OnClick({R.id.tab_time,R.id.tab_num,R.id.tab_amount})
    public void onClick(View view) {
    switch (view.getId()){
        case R.id.tab_time://时间
            ViewPager.setCurrentItem(0);
            break;
        case R.id.tab_num://人数
            ViewPager.setCurrentItem(1);
            break;
        case R.id.tab_amount://金额
            ViewPager.setCurrentItem(2);
            break;
    }
    }
}
