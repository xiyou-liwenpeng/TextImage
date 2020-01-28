package com.liwenpeng.textimage.view.main.module.image;

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

public class ImageFragment extends Fragment {

    private FragmentIamgeBinding iamgeBinding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        iamgeBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_iamge, container, false);
        return iamgeBinding.getRoot();
    }
}
