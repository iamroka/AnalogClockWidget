package com.roka.analogclockapp.analogclock;

import android.os.Bundle;

import com.roka.analogclockapp.R;
import com.roka.analogclockapp.base.BaseActivity;
import com.roka.analogclockapp.custom.ClockView;

import java.util.Calendar;
import java.util.GregorianCalendar;

public class MainActivity extends BaseActivity {
    ClockView myAnalogClock;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        myAnalogClock = (ClockView) findViewById(R.id.analog_clock);

       // myAnalogClock.startClock(); //This starts the clock from the current time

        /*  Calendar cal = GregorianCalendar.getInstance();
        cal.set(Calendar.MINUTE,35);  //sample fixed time. Uncomment to test the below methods
        cal.set(Calendar.HOUR,5);
        cal.set(Calendar.AM_PM,0);*/

        //myAnalogClock.startClock(cal); //uncomment this to start clock from a fixed time
        // myAnalogClock.setStaticClockTime(cal)); //uncomment this to set a fixed time

    }
}
