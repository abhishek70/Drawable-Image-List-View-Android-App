package abhishek.customlistview.Helper;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.drawable.ColorDrawable;

/**
 * Created by abhishek on 9/13/14.
 */
public class CustomDrawable extends ColorDrawable {

    private final char character;
    private final Paint textPaint;
    private final Paint borderPaint;
    private static final int STROKE_WIDTH = 0;
    private static final float SHADE_FACTOR = 0.9f;

    public CustomDrawable(char character, int color) {
        super(color);
        this.character = character;
        this.textPaint = new Paint();
        this.borderPaint = new Paint();

        // text paint settings
        textPaint.setColor(Color.WHITE);
        textPaint.setAntiAlias(true);
        textPaint.setFakeBoldText(true);
        textPaint.setStyle(Paint.Style.FILL);
        textPaint.setTextAlign(Paint.Align.CENTER);


        // border paint settings
        borderPaint.setColor(getDarkerShade(color));
        borderPaint.setStyle(Paint.Style.STROKE);
        borderPaint.setStrokeWidth(STROKE_WIDTH);
    }

    private int getDarkerShade(int color) {
        return Color.rgb((int)(SHADE_FACTOR * Color.red(color)),
                (int)(SHADE_FACTOR * Color.green(color)),
                (int)(SHADE_FACTOR * Color.blue(color)));
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);

        // draw border
        canvas.drawRect(getBounds(), borderPaint);

        // draw text
        int width = canvas.getWidth();
        int height = canvas.getHeight();
        textPaint.setTextSize(height / 2);
        canvas.drawText(String.valueOf(character), width/2, height/2 - ((textPaint.descent() + textPaint.ascent()) / 2) , textPaint);
    }

    @Override
    public void setAlpha(int alpha) {
        textPaint.setAlpha(alpha);
    }

    @Override
    public void setColorFilter(ColorFilter cf) {
        textPaint.setColorFilter(cf);
    }

    @Override
    public int getOpacity() {
        return PixelFormat.TRANSLUCENT;
    }
}
