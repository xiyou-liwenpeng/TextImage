package com.liwenpeng.textimage.view.main.module.main;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.liwenpeng.textimage.R;
import com.liwenpeng.textimage.databinding.FragmentIamgeBinding;
import com.liwenpeng.textimage.databinding.FragmentMainBinding;

public class MainFragment extends Fragment {

    private FragmentMainBinding mFragmentMainBinding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mFragmentMainBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_main, container, false);

        return mFragmentMainBinding.getRoot();
    }
}
