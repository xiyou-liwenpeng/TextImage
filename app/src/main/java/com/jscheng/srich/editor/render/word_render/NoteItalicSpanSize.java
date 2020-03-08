package com.jscheng.srich.editor.render.word_render;

import android.text.style.TextAppearanceSpan;

import com.jscheng.srich.editor.render.NoteWordSpanRender;
import com.jscheng.srich.editor.spans.NoteItalicSpan;
import com.jscheng.srich.editor.spans.SizeSpan;
import com.jscheng.srich.model.Style;
import com.liwenpeng.textimage.R;
import com.liwenpeng.textimage.base.BaseApplication;

/**
 * Created By Chengjunsen on 2019/3/4
 */
public class NoteItalicSpanSize extends NoteWordSpanRender<TextAppearanceSpan> {

    @Override
    protected TextAppearanceSpan createSpan() {
        return new TextAppearanceSpan(BaseApplication.getAppliction(), R.style.style_size);
    }

    @Override
    protected int getStyle() {
        return Style.SubScriptSize;
    }

    @Override
    protected boolean isStyle(int style) {
        return Style.isWordStyle(style, getStyle());
    }
}
