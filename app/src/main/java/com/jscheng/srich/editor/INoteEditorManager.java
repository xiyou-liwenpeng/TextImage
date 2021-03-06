package com.jscheng.srich.editor;

import android.net.Uri;

import com.jscheng.srich.model.Note;
import com.jscheng.srich.model.NoteSnap;

/**
 * Created By Chengjunsen on 2019/3/11
 */
public interface INoteEditorManager {

    void commandImage(Uri uri, boolean draw);

    void commandImage(String url, boolean draw);

    void commandColor(boolean isSelected, boolean draw);

    void commandUnderline(boolean isSelected, boolean draw);

    void commandItalic(boolean isSelected, boolean draw);

    void commandBold(boolean isSelected, boolean draw);
    void commandSize(boolean isSelected, boolean draw);
    void commandSubColor(boolean isSelected, boolean draw);

    void commandSuperscript(boolean isSelected, boolean draw);

    void commandSubscript(boolean isSelected, boolean draw);

    void commandStrikeThrough(boolean isSelected, boolean draw);

    void commandDividingLine(boolean draw);

    void commandBulletList(boolean isSelected, boolean draw);

    void commandNumList(boolean isSelected, boolean draw);

    void commandCheckBox(boolean isSelected, boolean draw);

    void commandIndentation(boolean draw);

    void commandReduceIndentation(boolean draw);

    void commandDeleteSelection(boolean draw);

    void commandDelete(boolean draw);

    void commandDelete(int num, boolean draw);

    void commandPaste(String content, boolean draw);

    void commandEnter(boolean draw);

    void commandInput(CharSequence content, boolean draw);

    NoteSnap commandRetroke();

    NoteSnap commandRecover();

    void addSelectionChangeListener(NoteEditorSelectionListener listener);

    void removeSelectionChangeListener(NoteEditorSelectionListener listener);

    void addClickListener(NoteEditorClickListener listener);

    void requestDraw();

    void apply(Note mNote, int selectionStart, int selectionEnd);
}
