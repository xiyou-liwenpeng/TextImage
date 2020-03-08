package com.jscheng.srich.editor.spans;

import android.graphics.Typeface;
import android.text.style.StyleSpan;

/**
 * Created By Chengjunsen on 2019/2/25
 */
public class SizeSpan extends StyleSpan{

    public SizeSpan() {
        super(Typeface.ITALIC);
    }

    public static SizeSpan create() {
        return new SizeSpan();
    }
}
