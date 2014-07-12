package com.nilhcem.hostseditor.ui.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.Button;

/**
 * Button using a custom font.
 *
 * @see TypefacedTextView
 */
public final class TypefacedButton extends Button {

    public TypefacedButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypefacedTextView.applyFont(context, attrs, this);
    }
}
