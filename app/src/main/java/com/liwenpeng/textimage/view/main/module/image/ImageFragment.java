package com.liwenpeng.textimage.view.main.module.image;

import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.liwenpeng.textimage.R;
import com.liwenpeng.textimage.databinding.FragmentIamgeBinding;
import com.liwenpeng.textimage.view.main.module.image.activity.GetImageActivity;


public class ImageFragment extends Fragment {

    private FragmentIamgeBinding iamgeBinding;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        iamgeBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_iamge, container, false);
        iamgeBinding.setEventListener(new EventListener());

        return iamgeBinding.getRoot();
    }

    public class EventListener {
        public void clickImage(View view) {
            GetImageActivity.startGetImageActivity(getContext(), new GetImageActivity.IGetImageTxt() {
                @Override
                public void setImageTxt(String txt) {
                    Log.e("lwp","输出："+txt);
                    iamgeBinding.tabImageEditText.setText(txt);
                }
            });
        }
    }

}
