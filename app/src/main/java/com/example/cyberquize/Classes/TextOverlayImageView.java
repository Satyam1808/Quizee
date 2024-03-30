package com.example.cyberquize.Classes;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.ImageView;

public class TextOverlayImageView extends ImageView {

    private String mText;
    private Paint mPaint;

    public TextOverlayImageView(Context context) {
        super(context);
        init();
    }

    public TextOverlayImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public TextOverlayImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mText = ""; // Default text
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setColor(Color.BLACK);
        mPaint.setTextSize(55);
        mPaint.setTypeface(Typeface.create(Typeface.SANS_SERIF, Typeface.NORMAL));
    }

    public void setText(String text) {
        mText = text;
        invalidate(); // Redraw the view with the new text
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (mText != null && !mText.isEmpty()) {
            // Calculate text width to center it horizontally
            float textWidth = mPaint.measureText(mText);
            float x = (getWidth() - textWidth) / 2;

            // Calculate text height to center it vertically
            Paint.FontMetrics metrics = mPaint.getFontMetrics();
            float y = getHeight() / 2 - (metrics.ascent + metrics.descent) / 2 - metrics.descent-20;

            // Draw the text in the center
            canvas.drawText(mText, x, y, mPaint);
        }
    }


}
