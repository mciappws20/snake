package edu.mci.rtloop;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

/**
 * Created by thhausberger on 11.12.2016.
 */
public abstract class RealTimeLoopActivity extends AppCompatActivity implements IRealTimeLooperProvider, RealTimeApplicationHelper.LoopStateChangeListener {

    private final RealTimeApplicationHelper helper_ = new RealTimeApplicationHelper(this);
    private boolean autoStart = false;

    public final void loopStart() {
        this.helper_.start();
    }

    public final void loopStop() {
        this.helper_.stop();
    }

    public final void loopPause() {
        this.helper_.pause();
    }

    public final void loopResume() {
        this.helper_.resume();
    }

    public final void loopReset() {
        this.helper_.stop();
        this.helper_.destroy();
        this.helper_.create();
    }

    public boolean isLoopAutoStart() {
        return autoStart;
    }

    public void setLoopAutoStart(boolean autoStart) {
        this.autoStart = autoStart;
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.helper_.addStateChangedListener(this);
        this.helper_.create();
    }

    protected void onDestroy() {
        this.helper_.destroy();
        this.helper_.removeStateChangedListener(this);
        super.onDestroy();
    }

    protected void onStart() {
        super.onStart();
        if (autoStart)
            loopStart();
    }

    protected void onStop() {
        loopStop();
        super.onStop();
    }

    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if ((intent.getFlags() & 268435456) != 0) {
            this.helper_.restart();
        }
    }

    public abstract IRealTimeLooper createRTLooper();

    @Override
    public void onStateChange(LoopState oldState, LoopState newState) {
        switch (newState) {
            case CREATED:
                onLoopCreate();
                break;
            case RUNNING:
                if(oldState == LoopState.PAUSED) {
                    onLoopResume();
                } else {
                    onLoopStart();
                }
                break;
            case PAUSED:
                onLoopPause();
                break;
            case STOPPED:
                onLoopStop();
                break;
        }
    }

    protected void onLoopCreate() {
        // do nothing
    }

    protected void onLoopResume() {
        // do nothing
    }

    protected void onLoopStart() {
        // do nothing
    }

    protected void onLoopPause() {
        // do nothing
    }

    protected void onLoopStop() {
        // do nothing
    }

    protected LoopState getLoopState() {
        return this.helper_.getLoopState();
    }

    protected void setWidgetText(final TextView widget, final String text) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                widget.setText(text);
            }
        });
    }

    protected void setWidgetEnabled(final View widget, final boolean enabled) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                widget.setEnabled(enabled);
            }
        });
    }
}
