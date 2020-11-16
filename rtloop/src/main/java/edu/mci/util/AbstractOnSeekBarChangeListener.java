package edu.mci.util;

import android.widget.SeekBar;

/**
 * Created by thhausberger on 12.12.2016.
 */
public abstract class AbstractOnSeekBarChangeListener implements SeekBar.OnSeekBarChangeListener {
    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        // do nothing by default
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
        // do nothing by default
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        // do nothing by default
    }
}
