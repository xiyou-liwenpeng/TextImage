package com.jscheng.srich.editor;

import android.content.Context;
import android.support.v7.widget.AppCompatEditText;
import android.text.Editable;
import android.text.Layout;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputConnection;
import com.jscheng.srich.editor.spans.NoteClickSpan;
import com.jscheng.srich.image_loader.ImageGlobalListener;
import com.jscheng.srich.image_loader.ImageLoader;
import com.jscheng.srich.utils.ClipboardUtil;
import com.jscheng.srich.utils.KeyboardUtil;
/**
 * Created By Chengjunsen on 2019/2/21
 */
public class NoteEditorText extends AppCompatEditText implements ImageGlobalListener {
    private static final String TAG = "NoteEditorText";
    private NoteEditorManager mStyleManager;
    private NoteEditorInputConnection mInputConnection;
    private boolean isReadMode;
    public NoteEditorText(Context context) {
        super(context, null);
        init();
    }

    public NoteEditorText(Context context, AttributeSet attrs) {
        super(context, attrs, 0);
        init();
    }

    public NoteEditorText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        this.mStyleManager = new NoteEditorManager(this);
        this.mInputConnection = new NoteEditorInputConnection(mStyleManager);
        this.setOverScrollMode(OVER_SCROLL_ALWAYS);
        this.setScrollBarStyle(SCROLLBARS_INSIDE_INSET);

        this.setInputType(EditorInfo.TYPE_CLASS_TEXT
                | EditorInfo.TYPE_TEXT_FLAG_MULTI_LINE
                | EditorInfo.TYPE_TEXT_FLAG_NO_SUGGESTIONS);
        this.setFocusableInTouchMode(true);
        this.setBackground(null);
        this.setHighlightColor(NoteEditorConfig.HighLightColor);
        this.setSingleLine(false);
        this.setTextSize(TypedValue.COMPLEX_UNIT_SP, NoteEditorConfig.TextSizeSp);
        this.setLetterSpacing(NoteEditorConfig.LetterSpacing);
        this.setLineSpacing(NoteEditorConfig.LineSpacing, 1f);

        this.readingMode();
    }

    // 写模式
    public void writingMode() {
        this.isReadMode = false;
        this.setCursorVisible(true);
        this.setSelected(true);
        this.showSoftKeyboard();
        this.requestFocus();
        this.moveCursorToLastSpan();
    }

    // 读模式
    public void readingMode() {
        this.isReadMode = true;
        this.setCursorVisible(false);
        this.setSelected(false);
        this.hideSoftKeyboard();
    }

    private void moveCursorToLastSpan() {
        Editable editable = getText();
        if (editable != null) {
            this.setSelection(editable.length());
        }
    }

    private void hideSoftKeyboard() {
        post(new Runnable() {
            @Override
            public void run() {
                clearFocus();
                KeyboardUtil.configSoftInput(NoteEditorText.this, false);
                KeyboardUtil.hideSoftInput(getContext(), NoteEditorText.this);
            }
        });
    }

    private void showSoftKeyboard() {
        post(new Runnable() {
            @Override
            public void run() {
                KeyboardUtil.configSoftInput(NoteEditorText.this, true);
                KeyboardUtil.showSoftInput(getContext(), NoteEditorText.this);
                requestFocus();
            }
        });
    }

    @Override
    public InputConnection onCreateInputConnection(EditorInfo outAttrs) {
        mInputConnection.setTarget(super.onCreateInputConnection(outAttrs));
        return mInputConnection;
    }

    @Override
    protected void onSelectionChanged(int selStart, int selEnd) {
        super.onSelectionChanged(selStart, selEnd);
        if (mStyleManager != null) {
            mStyleManager.onSelectionChanged(selStart, selEnd);
        }
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        ImageLoader.with(getContext()).addGlobalImageListener(this);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        ImageLoader.with(getContext()).removeGlobalImageListener(this);
    }

    public INoteEditorManager getStyleManager() {
        return mStyleManager;
    }

    @Override
    public boolean onTextContextMenuItem(int id) {
        switch (id) {
            case android.R.id.paste:
                paste();
                return true;
            case android.R.id.cut:
                cut();
                return true;
            case android.R.id.copy:
                copy();
                return true;
            default:
                return super.onTextContextMenuItem(id);
        }
    }

    private void copy() {
        String copyContent = mStyleManager.getSelectionText();
        ClipboardUtil.copy(copyContent, getContext());
    }

    private void cut() {
        String cutContent = mStyleManager.getSelectionText();
        ClipboardUtil.copy(cutContent, getContext());
        if (!isReadMode) {
            mStyleManager.commandDeleteSelection(true);
        }
    }

    private void paste() {
        if (!isReadMode) {
            String pasteContent = ClipboardUtil.paste(getContext());
            mStyleManager.commandPaste(pasteContent, true);
        }
    }

    public void appent(String s){
        mStyleManager.commandPaste(s, true);
    }

    float lastX = 0;
    float lastY = 0;
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                lastX = event.getX();
                lastY = event.getY();
                return super.onTouchEvent(event);
            case MotionEvent.ACTION_UP:
                if (onTouchUpSpan(event) && !isReadMode) {
                    return true;
                }
                return super.onTouchEvent(event);
            default:
                break;
        }
        return super.onTouchEvent(event);
    }

    private boolean onTouchUpSpan(MotionEvent event) {
        float deltaX = Math.abs(event.getX() - lastX);
        float deltaY = Math.abs(event.getY() - lastY);
        int off = getPos(event);
        NoteClickSpan[] spans = getText().getSpans(off, off, NoteClickSpan.class);
        if (deltaX < 10 && deltaY < 10 && spans.length > 0 && mStyleManager.onSpanTouchUp(off)) {
            return true;
        }
        return false;
    }

    private int getPos(MotionEvent event) {
        int x = (int)event.getX();
        int y = (int)event.getY();
        x -= getTotalPaddingLeft();
        y -= getTotalPaddingTop();

        x += getScrollX();
        y += getScrollY();

        Layout layout = getLayout();
        int line = layout.getLineForVertical(y);
        int off = layout.getOffsetForHorizontal(line, x);
        return off;
    }

    @Override
    public void onImageLoadSuccess(String url) {
        mStyleManager.requestDraw();
    }

    @Override
    public void onImageLoadFailed(String url, String err) {

    }
}
