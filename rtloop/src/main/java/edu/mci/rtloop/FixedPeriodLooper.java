package edu.mci.rtloop;

import android.util.Log;

/**
 * Created by thhausberger on 11.12.2016.
 */
public abstract class FixedPeriodLooper implements IRealTimeLooper {

    private static final String TAG = "FixedPeriodLooper";
    private static final int DEFAULT_PERIOD_MS = 1000;

    // Loop period in milliseconds
    private long loopPeriod;

    /**
     * uses DEFAULT_PERIOD_MS
     */
    public FixedPeriodLooper() {
        this(DEFAULT_PERIOD_MS);
    }

    /**
     *
     * @param loopPeriod loop period in milliseconds
     */
    public FixedPeriodLooper(final long loopPeriod) {
        this.loopPeriod = loopPeriod;
    }

    @Override
    public void loop() throws InterruptedException {
        long tStart = System.nanoTime() / 1000000; // reset timer

        loopFixed();

        // warn if execution time is longer than period
        if(isOverTime(tStart, loopPeriod)) {
            Log.w(TAG, String.format("Loop execution time is longer than period! (%dms)", (System.nanoTime() / 1000000)-tStart));
        }
        // wait until the end of our refresh period. Ensures more precise timings
        while (!isOverTime(tStart, loopPeriod)) {
            Thread.sleep(loopPeriod/10);
        }
    }

    /**
     * Infinite loop at fixed rate
     * @throws InterruptedException
     */
    protected abstract void loopFixed() throws InterruptedException;

    /**
     * Gets the loop period
     * @return loop period in milliseconds
     */
    public long getLoopPeriod() {
        return loopPeriod;
    }

    /**
     * Gets the loop period
     * @param loopPeriod loop period in milliseconds
     */
    public void setLoopPeriod(long loopPeriod) {
        this.loopPeriod = loopPeriod;
    }

    public void setLoopPeriod(double loopPeriod) {
        this.loopPeriod = (long) loopPeriod;
    }

    /**
     * TRUE if loop time is longer than period
     * @param tStart
     * @param tPeriod
     * @return
     */
    private static boolean isOverTime(long tStart, long tPeriod) {
        return (tStart + tPeriod) < (System.nanoTime() / 1000000);
    }
}
