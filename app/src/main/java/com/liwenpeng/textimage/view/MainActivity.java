package com.liwenpeng.textimage.view;

import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;

import com.liwenpeng.textimage.R;
import com.liwenpeng.textimage.databinding.ActivityMainBinding;
import com.liwenpeng.textimage.view.main.adapter.MainAdapter;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding mActivityMainBinding;
    private MainAdapter mMainAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivityMainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        mMainAdapter = new MainAdapter(this);
        mActivityMainBinding.mainViewPager.setAdapter(mMainAdapter);
        mActivityMainBinding.mainTabLayout.setupWithViewPager(mActivityMainBinding.mainViewPager);
        mActivityMainBinding.mainViewPager.setOffscreenPageLimit(3);
        initTab();
    }

    private void initTab(){
        mActivityMainBinding.mainTabLayout.getTabAt(0).setCustomView(R.layout.layout_tab_main);
        mActivityMainBinding.mainTabLayout.getTabAt(1).setCustomView(R.layout.layout_tab_image);
        mActivityMainBinding.mainTabLayout.getTabAt(2).setCustomView(R.layout.layout_tab_me);
        mActivityMainBinding.mainTabLayout.getTabAt(0).getCustomView().setSelected(true);
    }

}
    