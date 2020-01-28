package com.liwenpeng.textimage.view.main.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;

import com.liwenpeng.textimage.view.MainActivity;
import com.liwenpeng.textimage.view.main.module.image.ImageFragment;
import com.liwenpeng.textimage.view.main.module.main.MainFragment;
import com.liwenpeng.textimage.view.main.module.me.MeFragment;

import java.util.ArrayList;
import java.util.List;

public class MainAdapter extends FragmentPagerAdapter {

    private Context context;

    private ImageFragment mImageFragment;
    private MainFragment mMainFragment;
    private MeFragment mMeFragment;

    private List<Fragment> mFragment = new ArrayList<>();

    public MainAdapter(MainActivity mainActivity) {
        super(mainActivity.getSupportFragmentManager());
        this.context = mainActivity;
        mImageFragment = new ImageFragment();
        mMainFragment = new MainFragment();
        mMeFragment = new MeFragment();
        mFragment.add(mImageFragment);
        mFragment.add(mMeFragment);
        mFragment.add(mMainFragment);
    }

    @Override
    public Fragment getItem(int i) {
        return mFragment.get(i);
    }

    @Override
    public int getCount() {
        return mFragment.size();
    }
}
