package com.jscheng.srich.editor.render.line_render;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.View;

import com.liwenpeng.textimage.R;
import com.jscheng.srich.editor.render.NoteLineSpanRender;
import com.jscheng.srich.editor.spans.NoteCheckBoxSpan;
import com.jscheng.srich.model.Paragraph;
import com.jscheng.srich.model.Style;

/**
 * Created By Chengjunsen on 2019/3/6
 */
public class NoteUncheckBoxSpanRender extends NoteLineSpanRender<NoteCheckBoxSpan> {

    private View mView;
    private Bitmap mBitmap;

    public NoteUncheckBoxSpanRender(View view) {
        this.mView = view;
        this.mBitmap = BitmapFactory.decodeResource(view.getContext().getResources(), R.mipmap.ic_note_edit_uncheck);
    }

    @Override
    protected boolean isLineStyle(Paragraph paragraph) {
        return paragraph.isUnCheckbox();
    }

    @Override
    protected NoteCheckBoxSpan createSpan(int num, int level, String url) {
        return NoteCheckBoxSpan.create(mBitmap);
    }
}
