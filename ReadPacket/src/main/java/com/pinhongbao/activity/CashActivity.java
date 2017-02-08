package com.pinhongbao.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.pinhongbao.Model.cashInfo;
import com.pinhongbao.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * 余额兑现
 */
public class CashActivity extends Activity {

    @InjectView(R.id.image_back)
    ImageView imageBack;
    @InjectView(R.id.tv_title)
    TextView tvTitle;
    @InjectView(R.id.tv_balance)
    TextView tvBalance;
    @InjectView(R.id.gridView)
    GridView gridView;
    String[] amount = new String[]{"5", "10", "15", "20", "50", "100", "500", "1000"};
    int[] image = new int[]{R.mipmap.one, R.mipmap.two, R.mipmap.three, R.mipmap.four, R.mipmap.five, R.mipmap.six, R.mipmap.seven, R.mipmap.eight};
    List<cashInfo> cashInfoList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cash);
        ButterKnife.inject(this);
        initview();

    }

    /**
     * UI
     */
    private void initview() {
        tvTitle.setText("兑现");
        gedata();
        gridView.setAdapter(new MyGridView());


    }

    /**
     * 数据
     */
    private void gedata() {
        for (int i = 0; i < 8; i++) {
            cashInfo info = new cashInfo();
            info.setAmount(amount[i]);
            info.setImage(image[i]);
            cashInfoList.add(info);
        }

    }

    @OnClick({R.id.image_back})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.image_back:
                finish();
                break;
        }
    }

    class MyGridView extends BaseAdapter {



        @Override
        public int getCount() {
            return cashInfoList.size();
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
             ViewHolder viewHolder;
            if (view==null){
                 view = getLayoutInflater().inflate(R.layout.layout_cash_gridview, null);
                viewHolder = new ViewHolder(view);
                view.setTag(viewHolder);
            }else {
                 viewHolder = (ViewHolder) view.getTag();
            }
            viewHolder.image.setImageResource(cashInfoList.get(i).getImage());

            return view;
        }

         class ViewHolder {
            @InjectView(R.id.image)
            ImageView image;
            @InjectView(R.id.btn_cash)
            Button btnCash;

            ViewHolder(View view) {
                ButterKnife.inject(this, view);
            }
        }
    }
}
