package com.liwenpeng.textimage.view.main.module.image;

import android.arch.lifecycle.LifecycleOwner;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jscheng.srich.editor.NoteEditorClickListener;
import com.jscheng.srich.editor.NoteEditorText;
import com.jscheng.srich.model.Note;
import com.jscheng.srich.note_edit.EditNoteFormatDialog;
import com.jscheng.srich.note_edit.EditNoteInputDialog;
import com.jscheng.srich.note_edit.EditNotePresenter;
import com.jscheng.srich.note_edit.EditNoteToolbar;
import com.jscheng.srich.note_edit.FloatEditButton;
import com.jscheng.srich.note_edit.NoteEditorBar;
import com.jscheng.srich.utils.KeyboardUtil;
import com.jscheng.srich.utils.PermissionUtil;
import com.jscheng.srich.widget.CircularProgressView;
import com.liwenpeng.textimage.R;
import com.liwenpeng.textimage.view.main.module.image.activity.GetImageActivity;

import java.util.List;



public class ImageFragment extends Fragment  implements EditNotePresenter.EditNoteView, NoteEditorClickListener, LifecycleOwner {

    private final static int ACTION_PICK_CODE = 1;
    private final static int PERMISSION_CODE = 2;

    private EditNoteToolbar mToolbar;
    private EditNotePresenter mPresenter;
    private FloatEditButton mEditButton;
    private NoteEditorText mEditorText;
    private NoteEditorBar mEditorBar;
    private EditNoteFormatDialog mFormatDialog;
    private CircularProgressView mLoadingView;
    private View rootView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_iamge, container, false);

        init();

        return rootView;
    }

    private void init(){
        mEditorBar = rootView.findViewById(R.id.edit_note_bar);
        mToolbar = rootView.findViewById(R.id.edit_note_toolbar);
        mLoadingView = rootView.findViewById(R.id.circular_progress);
        mEditButton = rootView.findViewById(R.id.float_edit_button);
        mEditorText = rootView.findViewById(R.id.edit_note_editor);
        mPresenter = new EditNotePresenter(getActivity().getIntent());
        mPresenter.onCreateNoteView(this, getContext());

        mEditButton.setPresenter(mPresenter);

        mLoadingView.setVisibility(View.GONE);

        mEditorText.getStyleManager().addClickListener(this);

        mToolbar.setPresenter(mPresenter);
        mToolbar.setStyleManager(mEditorText.getStyleManager());

        mEditorBar.setStyleManager(mEditorText.getStyleManager());
        mEditorBar.hide();
        requestPermission();
    }

    @Override
    public void showLoading() {
        mLoadingView.setVisibility(View.VISIBLE);
    }


    @Override
    public void hideLoading() {
        mLoadingView.setVisibility(View.GONE);
    }

    @Override
    public void apply(Note mNote, int selectionStart, int selectionEnd) {
        mEditorText.getStyleManager().apply(mNote, selectionStart, selectionEnd);
    }

    @Override
    public void writingMode(boolean isEditorBarEnable) {
        mEditorText.writingMode();
        mToolbar.writingMode();
        mEditorBar.setVisibility(isEditorBarEnable ? View.VISIBLE : View.GONE);
        mToolbar.setFormatEnable(isEditorBarEnable);
        mEditButton.hide();
        mLoadingView.setVisibility(View.GONE);
    }

    @Override
    public void readingMode() {
        mEditorText.readingMode();
        mToolbar.readingMode();
        mEditButton.show();
        mEditorBar.setVisibility(View.GONE);
        mLoadingView.setVisibility(View.GONE);
    }

    @Override
    public void loadingMode() {
        mEditorText.readingMode();
        mToolbar.readingMode();
        mEditButton.setVisibility(View.GONE);
        mEditorBar.setVisibility(View.GONE);
        mLoadingView.setVisibility(View.VISIBLE);
    }

    public void onBackPressed() {
        mPresenter.tapBack();
    }

    @Override
    public void finish() {
        getActivity().finish();
    }

    @Override
    public void setEditorbar(boolean isEnable) {
        mEditorBar.setVisibility(isEnable ? View.VISIBLE : View.GONE);
        mToolbar.setFormatEnable(isEnable);
    }

    @Override
    public void showFormatDialog() {
        if (mFormatDialog == null) {
            mFormatDialog = new EditNoteFormatDialog(getContext(), mPresenter);
        }
        KeyboardUtil.hideSoftInput(getContext(), mEditorText);
        mFormatDialog.show();
    }

    @Override
    public void showAlbumDialog() {
        GetImageActivity.startGetImageActivity(getContext(), new GetImageActivity.IGetImageTxt() {
            @Override
            public void setImageTxt(String txt) {
                Log.e("lwp","输出："+txt);
            }

            @Override
            public void getImageInfor(String s) {
                insertImage(s);
            }
        }, false);
    }

    @Override
    public void showNetworkDialog() {
        EditNoteInputDialog dialog = new EditNoteInputDialog(getContext(), mPresenter);
        dialog.show();
    }

    @Override
    public void showWenzi() {
        GetImageActivity.startGetImageActivity(getContext(), new GetImageActivity.IGetImageTxt() {
            @Override
            public void setImageTxt(String txt) {
                Log.e("lwp","输出："+txt);
                mEditorText.appent(txt);
            }

            @Override
            public void getImageInfor(String s) {

            }
        }, true);
    }

    @Override
    public void insertImage(String url) {
        mEditorText.getStyleManager().commandImage(url, true);
    }

    private void requestPermission() {
        PermissionUtil.checkPermissionsAndRequest(getContext(), PermissionUtil.STORAGE, PERMISSION_CODE, "请求访问SD卡权限被拒绝");
    }

    @Override
    public void onClickImage(List<String> urls, int index) {
        mPresenter.tapImage(urls, index);
    }


}
