package edu.mci.rtloop;

/**
 * Convenience class for creating a RealTimeLoopActivity using a FixedRateLooper
 */
public abstract class FixedRateLoopActivity extends RealTimeLoopActivity {

    private FixedPeriodLooper fixedPeriodLooper = new FixedPeriodLooper() {

        @Override
        public void setup() {
            loopSetup();
        }

        @Override
        protected void loopFixed() {
            loopIteration();
        }

        @Override
        public void tearDown(boolean error) {
            loopTearDown(error);
        }
    };

    @Override
    public IRealTimeLooper createRTLooper() {
        return fixedPeriodLooper;
    }

    /**
     * called once when creating the looper
     */
    protected void loopSetup() {
        // do nothing
    }

    /**
     * called once when destroying the looper
     * @param error
     */
    protected void loopTearDown(boolean error) {
        // do nothing
    }

    /**
     * called at every iteration of the looper
     */
    protected void loopIteration() {
        // do nothing
    }

    protected void setLoopPeriod(long period) {
        fixedPeriodLooper.setLoopPeriod(period);
    }

    protected void setLoopFrequency(long frequency) {
        fixedPeriodLooper.setLoopPeriod(1000.0/frequency);
    }
}
