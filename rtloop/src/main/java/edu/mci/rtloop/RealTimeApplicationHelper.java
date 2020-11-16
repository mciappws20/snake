package edu.mci.rtloop;

import android.util.Log;

import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Created by thhausberger on 11.12.2016.
 */
class RealTimeApplicationHelper implements Thread.UncaughtExceptionHandler {
    private static final String TAG = "RTAppHelper";
    private final IRealTimeLooperProvider looperProvider_;
    private RTThread thread_;
    private LoopState loopState;

    public interface LoopStateChangeListener {
        void onStateChange(LoopState oldState, LoopState newState);
    }

    private final CopyOnWriteArrayList<LoopStateChangeListener> stateChangeListeners = new CopyOnWriteArrayList<>();

    private void notifyStateChangedListeners(final LoopState oldState, final LoopState newState) {
        for (LoopStateChangeListener l :
                stateChangeListeners) {
            l.onStateChange(oldState, newState);
        }
    }

    public boolean addStateChangedListener(LoopStateChangeListener l) {
        return stateChangeListeners.add(l);
    }

    public boolean removeStateChangedListener(LoopStateChangeListener l) {
        return stateChangeListeners.remove(l);
    }

    public RealTimeApplicationHelper(IRealTimeLooperProvider provider) {
        this.looperProvider_ = provider;
    }

    private void changeState(final LoopState newState) {
        LoopState oldState = loopState;
        loopState = newState;
        notifyStateChangedListeners(oldState, newState);
    }

    private void createThread() {
        IRealTimeLooper looper = this.looperProvider_.createRTLooper();
        thread_ = new RTThread(looper);
        thread_.setUncaughtExceptionHandler(this);
        changeState(LoopState.CREATED);
    }

    private void startThread() {
        if(loopState == LoopState.STOPPED) {
            createThread();
        }

        if(loopState == LoopState.CREATED) {
            thread_.start();
            changeState(LoopState.RUNNING);
        }
    }

    private void pauseThread() {
        if(loopState == LoopState.RUNNING) {
            thread_.pauseLoop();
            changeState(LoopState.PAUSED);
        }
    }

    private void resumeThread() {
        if(loopState == LoopState.PAUSED) {
            thread_.resumeLoop();
            changeState(LoopState.RUNNING);
        }
    }

    private void stopThread() {
        if(loopState == LoopState.RUNNING || loopState == LoopState.PAUSED)  {
            thread_.abort();
            try {
                thread_.join(1000);
            } catch (InterruptedException e) {
            }
            changeState(LoopState.STOPPED);
        }
    }

    private void destroyThread() {
        // do nothing
    }

    private void restartThread() {
        stopThread();
        destroyThread();
        createThread();
        startThread();
    }

    static class RTThread extends Thread {
        private static final String TAG = "RTThread";
        private boolean abort_ = false;
        private boolean pause_ = false;
        private boolean connected_ = false;
        private boolean error_ = false;
        private final IRealTimeLooper looper_;

        RTThread(IRealTimeLooper looper) {
            this.looper_ = looper;
        }

        public final void run() {
            super.run();

            while(!this.abort_) {
                try {
                    this.connected_ = true;
                    this.looper_.setup();

                    while(!this.abort_) {
                        if(!this.pause_) {
                            this.looper_.loop();
                        } else {
                            Thread.sleep(100);
                        }
                    }
                } catch (InterruptedException ie) {
                    // do nothing
                } catch (Exception e) {
                    Log.e(TAG, "Unknown exception", e);
                    error_ = true;
                    break;
                } finally {

                    if(this.connected_) {
                        this.looper_.tearDown(error_);
                        this.connected_ = false;
                    }

                }
            }

            Log.d(TAG, "RTThread is exiting");
        }

        public final synchronized void abort() {
            this.abort_ = true;

            if(this.connected_) {
                this.interrupt();
            }
        }

        public final synchronized void pauseLoop() {
            this.pause_ = true;
        }

        public final synchronized void resumeLoop() {
            this.pause_ = false;
        }
    }

    public void create() {
        createThread();
    }

    public void destroy() {
        destroyThread();
    }

    public void start() {
        startThread();
    }

    public void pause() {
        pauseThread();
    }

    public void resume() {
        resumeThread();
    }

    public void stop() {
        stopThread();
    }

    public void restart() {
        restartThread();
    }

    @Override
    public void uncaughtException(Thread t, Throwable e) {
        Log.e(TAG, "Unexpected exception caught", e);
        changeState(LoopState.STOPPED);
    }

    public LoopState getLoopState() {
        return loopState;
    }
}
