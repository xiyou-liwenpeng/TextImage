package com.liwenpeng.textimage.view.main.module.me;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.jscheng.srich.note_edit.EditNoteActivity;
import com.jscheng.srich.note_edit.EditNotePresenter;
import com.jscheng.srich.outline.OutLinesActivity;
import com.liwenpeng.textimage.R;
import com.liwenpeng.textimage.databinding.FragmentMeBinding;

public class MeFragment extends Fragment {

    private FragmentMeBinding meBinding;
    private int mState = 0;
    private int mLastState = 0;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        meBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_me, container, false);
        meBinding.mainTitleBar.setTitle(getContext().getString(R.string.app_name));
        initAppbarLayout();
        init();
        return meBinding.getRoot();
    }

    private void init(){
        meBinding.linearJilu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), OutLinesActivity.class);
                startActivity(intent);
            }
        });

        meBinding.linearWenzi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), EditNoteActivity.class);
                intent.putExtra("iword", true);
                startActivity(intent);
            }
        });

        meBinding.linearXiangce.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toastShow();
            }
        });

        meBinding.linearYuyan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toastShow();
            }
        });

        meBinding.linearShezhi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toastShow();
            }
        });

    }

    private void toastShow(){
        Toast.makeText(getContext(),"正在努力开发中...",Toast.LENGTH_SHORT).show();
    }

    private void initAppbarLayout() {
        meBinding.mainAppBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int i) {
                if (i == 0) {
                    //展开
                    mState = 0;
                    if (mState != mLastState) {
                        meBinding.mainCollapsingTool.setTitle("");
                        meBinding.mainTitleBar.setTitle(getContext().getString(R.string.tab_me_string_name));
                        meBinding.mainBanner.setVisibility(View.VISIBLE);
                        mLastState = 0;
                    }
                } else {
                    //非展开状态
                    mState = 1;
                    if (mState != mLastState) {
                        meBinding.mainTitleBar.setTitle("");
                        meBinding.mainCollapsingTool.setTitle(getContext().getString(R.string.tab_me_string_name));
                        meBinding.mainBanner.setVisibility(View.GONE);
                        mLastState = 1;
                    }
                }
            }
        });
    }
}
