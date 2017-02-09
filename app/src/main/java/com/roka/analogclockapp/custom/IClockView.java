package com.roka.analogclockapp.custom;

import android.support.annotation.Nullable;

import java.util.Calendar;

/**
 * Created by Roka on 2/8/2017.
 */

public interface IClockView {
    void startClock(@Nullable Calendar calendar);

    void startClock();

    void setStaticClockTime(Calendar c);

}
