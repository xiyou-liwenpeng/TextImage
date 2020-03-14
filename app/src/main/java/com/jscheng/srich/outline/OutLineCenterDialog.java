package com.jscheng.srich.outline;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;

import com.jscheng.srich.model.NoteModel;
import com.liwenpeng.textimage.R;
import com.liwenpeng.textimage.view.main.module.main.INotify;

/**
 * Created By Chengjunsen on 2019/3/19
 */
public class OutLineCenterDialog extends Dialog implements View.OnClickListener{

    private OutLinePresenter mPresenter;
    private String noteid;
    private INotify mINotify;

    public OutLineCenterDialog(Context context, OutLinePresenter mPresenter) {
        super(context);
        setContentView(R.layout.outline_center_dialog);
        this.mPresenter = mPresenter;

        getWindow().setGravity(Gravity.CENTER);
        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        findViewById(R.id.delete_view).setOnClickListener(this);
    }

    public OutLineCenterDialog(Context context, OutLinePresenter mPresenter, INotify iNotify) {
        super(context);
        setContentView(R.layout.outline_center_dialog);
        this.mPresenter = mPresenter;

        getWindow().setGravity(Gravity.CENTER);
        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        findViewById(R.id.delete_view).setOnClickListener(this);
        mINotify = iNotify;
    }

    public void show(String noteid) {
        this.noteid = noteid;
        super.show();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.delete_view:
                if (mPresenter == null && mINotify != null){
                    NoteModel.deleteNote(getContext(), noteid);
                    mINotify.upDate();
                }else if (mPresenter != null){
                    mPresenter.tapDelete(noteid);
                }
                super.dismiss();
                break;
            default:
                break;
        }
    }
}
