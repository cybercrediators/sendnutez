package send.nutez.Overlays;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;

import java.sql.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import send.nutez.Prediction.Box;
import send.nutez.Prediction.Predictor;

/**
 * create a rectangle overlay view
 * for the predicted bounding boxes
 */
public class RectOverlay extends View {

    private Paint boxPaint;
    private List<Box> rects;
    private int width;

    /**
     * overlay view constructor
     * @param context
     * @param attrs
     */
    public RectOverlay(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        rects = new ArrayList<Box>();
        boxPaint = new Paint();
        boxPaint.setAlpha(200);
        boxPaint.setStyle(Paint.Style.STROKE);
    }

    /**
     * draw the rectangles on the given canvas
     * @param canvas
     */
    @Override
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (rects.size() > 0) {
            for (Box b : rects) {
                boxPaint.setStrokeWidth(4 * width / 800.0f);
                boxPaint.setTextSize(30 * width / 800.0f);
                boxPaint.setColor(b.getColor());
                boxPaint.setStyle(Paint.Style.FILL);
                canvas.drawText(b.getLabel() + String.format(Locale.US, " %.3f", b.getScore()), b.x0 + 3, b.y0 + 30 * width / 1000.0f, boxPaint);
                boxPaint.setStyle(Paint.Style.STROKE);
                canvas.drawRect(b.getRect(), boxPaint);
            }
        }
    }

    /**
     * add the box rectangles to the canvas
     * @param results
     * @param width
     */
    public void drawBoxRects(Box[] results, int width) {
        if (results == null)
            return;
        rects.clear();
        for (Box b: results)
            rects.add(b);
        this.width = width;
        invalidate();
    }
}
