package com.roka.analogclockapp.custom;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

import com.roka.analogclockapp.Constants;

import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * Created by Roka on 2/7/2017.
 */

public class ClockView extends View implements IClockView {

    private int height, width = 0;
    private int padding = 0;
    private int fontSize = 0;
    private int handTruncation, hourHandTruncation = 0;
    private int radius = 0;
    private Paint paint;
    private boolean isInit;
    private int[] numbers = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12};
    private Rect rect = new Rect();
    private boolean isStaticTime = true;
    private Calendar calendar = GregorianCalendar.getInstance();
    private Long timeOffset;

    public ClockView(Context context) {
        this(context, null);

    }

    public ClockView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);

    }

    public ClockView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        calendar = GregorianCalendar.getInstance();
        calendar.set(2000, 0, 1, 0, 0, 0);

    }

    private void initClock() {
        height = getHeight();
        width = getWidth();
        padding = 50;
        fontSize = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 13,
                getResources().getDisplayMetrics());
        int min = Math.min(height, width);
        radius = min / 2 - padding;
        handTruncation = min / 20;
        hourHandTruncation = min / 7;
        paint = new Paint();
        isInit = true;

    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (!isInit) {
            initClock();
        }

        canvas.drawColor(Color.TRANSPARENT);
        drawCircle(canvas);
        drawNumeral(canvas);
        drawHands(canvas);
        drawCenter(canvas);

        if (!isStaticTime) {
            postInvalidateDelayed(500);
            invalidate();
        }
    }

    private void drawHand(Canvas canvas, double loc, int handCode) {
        double angle = Math.PI * loc / 30 - Math.PI / 2;
        int handRadius = handCode == Constants.handCode.HOUR ? radius - handTruncation - hourHandTruncation : radius - handTruncation;
        switch (handCode) {
            case Constants.handCode.HOUR:
                paint.setColor(getResources().getColor(android.R.color.darker_gray));
                paint.setStrokeWidth(8);
                break;
            case Constants.handCode.MINUTE:
                paint.setColor(getResources().getColor(android.R.color.darker_gray));
                paint.setStrokeWidth(6);
                break;
            case Constants.handCode.SECOND:
                paint.setColor(getResources().getColor(android.R.color.holo_blue_light));
                paint.setStrokeWidth(4);
                break;
        }
        canvas.drawLine(width / 2, height / 2,
                (float) (width / 2 + Math.cos(angle) * handRadius),
                (float) (height / 2 + Math.sin(angle) * handRadius),
                paint);
    }

    private void drawHands(Canvas canvas) {
        if (!isStaticTime) {
            calendar = GregorianCalendar.getInstance();
            if (timeOffset != null)
                calendar.setTimeInMillis(calendar.getTimeInMillis() + timeOffset);
        }
        float hour = calendar.get(Calendar.HOUR_OF_DAY);
        hour = hour > 12 ? hour - 12 : hour;
        drawHand(canvas, (hour + calendar.get(Calendar.MINUTE) / 60.0f) * 5f, Constants.handCode.HOUR);
        drawHand(canvas, calendar.get(Calendar.MINUTE), Constants.handCode.MINUTE);
        drawHand(canvas, calendar.get(Calendar.SECOND), Constants.handCode.SECOND);
    }

    /**
     * private method to place the numbers in a circle
     * @param canvas
     */
    private void drawNumeral(Canvas canvas) {
        paint.reset();
        paint.setTextSize(fontSize);
        paint.setColor(getResources().getColor(android.R.color.black));
        for (int number : numbers) {
            String tmp = String.valueOf(number);
            paint.getTextBounds(tmp, 0, tmp.length(), rect);
            double angle = Math.PI / 6 * (number - 3);
            int x = (int) (width / 2 + Math.cos(angle) * radius - rect.width() / 2);
            int y = (int) (height / 2 + Math.sin(angle) * radius + rect.height() / 2);
            canvas.drawText(tmp, x, y, paint);
        }
    }
    /**
     * Private method to draw the center circle
     * @param canvas
     */
    private void drawCenter(Canvas canvas) {
        paint.reset();
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(getResources().getColor(android.R.color.darker_gray));
        canvas.drawCircle(width / 2, height / 2, 12, paint);
    }

    /**
     * private method to draw the background
     * @param canvas
     */
    private void drawCircle(Canvas canvas) {
        paint.reset();
        paint.setColor(getResources().getColor(android.R.color.holo_blue_light));
        paint.setStrokeWidth(16);
        paint.setStyle(Paint.Style.STROKE);
        paint.setAntiAlias(true);
        canvas.drawCircle(width / 2, height / 2, radius + padding - 10, paint);

        paint.reset();
        paint.setColor(getResources().getColor(android.R.color.white));
        paint.setStrokeWidth(16);
        paint.setStyle(Paint.Style.FILL_AND_STROKE);
        paint.setAntiAlias(true);
        canvas.drawCircle(width / 2, height / 2, radius + padding - 26, paint);
    }

    /**
     * This method is used if you just want to display a fixed time and don't want the clock to update or change
     *
     * @param c
     */
    @Override
    public void setStaticClockTime(Calendar c) {
        calendar = c;
        isStaticTime = true;
        invalidate();
    }

    /**
     * This method is called to start the clock from the system time
     */
    @Override
    public void startClock() {
        this.startClock(null);
    }

    /**
     * This is called if the clock needs to start with a custom time.
     *
     * @param calendar
     */
    @Override
    public void startClock(@Nullable Calendar calendar) {
        isStaticTime = false;
        if (calendar != null) {
            this.calendar = GregorianCalendar.getInstance();
            timeOffset = calendar.getTimeInMillis() - this.calendar.getTimeInMillis();
            this.calendar = calendar;
        } else
            this.calendar = GregorianCalendar.getInstance();
    }
}
