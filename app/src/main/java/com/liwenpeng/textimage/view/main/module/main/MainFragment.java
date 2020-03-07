package com.liwenpeng.textimage.view.main.module.main;

import android.arch.lifecycle.LifecycleObserver;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.hjq.bar.OnTitleBarListener;
import com.jscheng.srich.model.Note;
import com.jscheng.srich.model.NoteModel;
import com.jscheng.srich.outline.OutLineCenterDialog;
import com.jscheng.srich.outline.OutLinesAdapter;
import com.jscheng.srich.route.Router;
import com.liwenpeng.textimage.R;
import com.liwenpeng.textimage.databinding.FragmentMainBinding;
import com.wyt.searchbox.SearchFragment;
import com.wyt.searchbox.custom.IOnSearchClickListener;
import com.youth.banner.BannerConfig;
import com.youth.banner.Transformer;
import com.youth.banner.listener.OnBannerListener;
import com.youth.banner.loader.ImageLoader;

import org.w3c.dom.Node;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class MainFragment extends Fragment implements OnBannerListener , IShowLongView, INotify{

    private FragmentMainBinding mFragmentMainBinding;
    private SearchFragment mSearchFragment;
    private int mState = 0;
    private int mLastState = 0;

    private RecyclerView mRecyclerView;
    private LinearLayoutManager mLayoutManager;
    private OutLinesAdapter mRecyclerAdapter ;
    private List<Note> mNodes = new ArrayList<>();
    private OutLineCenterDialog mCenterDialog;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initSearch();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mFragmentMainBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_main, container, false);

        initOutLin();
        initTitleBar();
        initBanner();
        initRecyc();
        initAppbarLayout();

        return mFragmentMainBinding.getRoot();
    }

    private void initRecyc(){
        this.mRecyclerView = mFragmentMainBinding.outlineRecyclerview;
        this.mLayoutManager = new LinearLayoutManager(getContext(),
                LinearLayoutManager.VERTICAL, false);
        this.mRecyclerAdapter = new OutLinesAdapter(getContext(), mRecyclerView, mLayoutManager, this);
        this.mRecyclerView.setLayoutManager(mLayoutManager);
        this.mRecyclerView.setAdapter(mRecyclerAdapter);
        this.mRecyclerView.addOnScrollListener(new ScrollChangeListener());
    }

    private void gotoNode(){
        Router.with(getContext()).route("editnote").go();
    }

    @Override
    public void onResume() {
        super.onResume();
        reload();
    }

    public void reload() {
        Observable.create(new ObservableOnSubscribe<List<Note>>() {
            @Override
            public void subscribe(ObservableEmitter<List<Note>> emitter) throws Exception {
                List<Note> notes = NoteModel.getNotes(getContext());
                emitter.onNext(notes);
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<Note>>() {
                    @Override
                    public void accept(List<Note> notes) throws Exception {
                        mNodes = notes;
                        mRecyclerAdapter.setData(notes);
                    }
                });
    }

    @Override
    public void showCenterDialog(String id) {
        if (mCenterDialog == null) {
            mCenterDialog = new OutLineCenterDialog(getContext(), null, this);
        }
        mCenterDialog.show(id);
    }

    @Override
    public void upDate() {
        reload();
    }

    private class ScrollChangeListener extends RecyclerView.OnScrollListener {
        @Override
        public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
        }
    }

    private void initOutLin(){
        reload();
        mFragmentMainBinding.newFloatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoNode();
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        mFragmentMainBinding.mainBanner.startAutoPlay();
    }

    @Override
    public void onStop() {
        super.onStop();
        mFragmentMainBinding.mainBanner.stopAutoPlay();
    }

    private void initAppbarLayout(){
        mFragmentMainBinding.mainAppBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int i) {
                if (i == 0){
                    //展开
                    mState = 0;
                    if (mState != mLastState) {
                        mFragmentMainBinding.mainCollapsingTool.setTitle("");
                        mFragmentMainBinding.mainTitleBar.setTitle(getContext().getString(R.string.app_name));
                        mFragmentMainBinding.mainBanner.setVisibility(View.VISIBLE);
                        mFragmentMainBinding.mainBanner.startAutoPlay();
                        mLastState = 0;
                    }
                }else {
                    //非展开状态
                    mState = 1;
                    if (mState != mLastState) {
                        mFragmentMainBinding.mainTitleBar.setTitle("");
                        mFragmentMainBinding.mainCollapsingTool.setTitle(getContext().getString(R.string.app_name));
                        mFragmentMainBinding.mainBanner.setVisibility(View.GONE);
                        mFragmentMainBinding.mainBanner.stopAutoPlay();
                        mLastState = 1;
                    }
                }
            }
        });
    }

    private void initBanner(){

        int[] imageResourceID = new int[]{R.mipmap.main_banner_1, R.mipmap.main_banner_2, R.mipmap.main_banner_3, R.mipmap.main_banner_4};
        List<Integer> imgeList = new ArrayList<>();
        //轮播标题
        String[] mtitle = new String[]{"文档合并", "图片转文字", "文档瘦身", "全文翻译"};
        List<String> titleList = new ArrayList<>();

        for (int i = 0; i < imageResourceID.length; i++) {
            imgeList.add(imageResourceID[i]);//把图片资源循环放入list里面
            titleList.add(mtitle[i]);//把标题循环设置进列表里面
            //设置图片加载器，通过Glide加载图片
            mFragmentMainBinding.mainBanner.setImageLoader(new ImageLoader() {
                @Override
                public void displayImage(Context context, Object path, ImageView imageView) {
                    imageView.setBackgroundResource((Integer) path);
                }
            });
            mFragmentMainBinding.mainBanner.setBannerAnimation(Transformer.Accordion);
            mFragmentMainBinding.mainBanner.setImages(imgeList);//设置图片资源
            mFragmentMainBinding.mainBanner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR_TITLE_INSIDE);//设置banner显示样式（带标题的样式）
            mFragmentMainBinding.mainBanner.setBannerTitles(titleList); //设置标题集合（当banner样式有显示title时）
            //设置指示器位置（即图片下面的那个小圆点）
            mFragmentMainBinding.mainBanner.setIndicatorGravity(BannerConfig.CENTER);
            mFragmentMainBinding.mainBanner.setDelayTime(3000);//设置轮播时间3秒切换下一图
            mFragmentMainBinding.mainBanner.setOnBannerListener(this);//设置监听
            mFragmentMainBinding.mainBanner.start();//开始进行banner渲染
        }
    }

    private void initTitleBar(){
        mFragmentMainBinding.mainTitleBar.setTitle(getContext().getString(R.string.app_name));
        mFragmentMainBinding.mainTitleBar.setOnTitleBarListener(new OnTitleBarListener() {
            @Override
            public void onLeftClick(View v) {

            }

            @Override
            public void onTitleClick(View v) {

            }

            @Override
            public void onRightClick(View v) {
                if (mSearchFragment == null){
                    initSearch();
                }
                if (mRecyclerAdapter != null){
                    mRecyclerAdapter.setData(mNodes);
                }
                mSearchFragment.show(getChildFragmentManager(),SearchFragment.TAG);
            }
        });
    }

    private void initSearch(){
        if (mSearchFragment == null) {
            mSearchFragment = SearchFragment.newInstance();
        }
        mSearchFragment.setOnSearchClickListener(new IOnSearchClickListener() {
            @Override
            public void OnSearchClick(String keyword) {
                //这里处理逻辑
                search(keyword);
            }
        });
    }

    private void search(String keyword){
        if (TextUtils.isEmpty(keyword)){
            reload();
        }else {
            List<Note> nodeList = new ArrayList<>();
            for (Note note : mNodes){
                if (note == null || TextUtils.isEmpty(note.getTitle())){
                    continue;
                }
                if (note.getTitle().contains(keyword)){
                    nodeList.add(note);
                }
            }
            mRecyclerAdapter.setData(nodeList);
        }
    }

    @Override
    public void OnBannerClick(int position) {

    }
}
