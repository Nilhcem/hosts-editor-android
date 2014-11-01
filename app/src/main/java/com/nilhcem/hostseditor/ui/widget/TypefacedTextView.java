package com.nilhcem.hostseditor.ui.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

import com.nilhcem.hostseditor.R;

/**
 * TextView using a custom font.
 *
 * @see "http://stackoverflow.com/questions/4395309/android-want-to-set-custom-fonts-for-whole-application-not-runtime/9199258#9199258"
 */
public final class TypefacedTextView extends TextView {

    public TypefacedTextView(Context context, AttributeSet attrs) {
        super(context, attrs);

        // Typeface.createFromAsset doesn't work in the layout editor. Skipping...
        if (isInEditMode()) {
            return;
        }

        TypefacedTextView.applyFont(context, attrs, this);
    }

    static void applyFont(Context context, AttributeSet attrs, TextView view) {
        TypedArray styledAttrs = context.obtainStyledAttributes(attrs, R.styleable.TypefacedTextView);
        if (styledAttrs != null) {
            String fontName = styledAttrs.getString(R.styleable.TypefacedTextView_typeface);
            styledAttrs.recycle();

            if (fontName != null) {
                Typeface typeface = Typeface.createFromAsset(context.getAssets(), fontName);
                view.setTypeface(typeface);
            }
        }
    }
}
