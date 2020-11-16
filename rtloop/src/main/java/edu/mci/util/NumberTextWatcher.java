package edu.mci.util;

import android.text.Editable;
import android.text.TextWatcher;

/**
 * Created by thhausberger on 11.12.2016.
 */
public abstract class NumberTextWatcher implements TextWatcher {
    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        // NOP
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        // NOP
    }

    @Override
    public void afterTextChanged(Editable s) {
        if (s.length() > 0) {
            try {
                numberEntered(Integer.parseInt(s.toString()));
            } catch (NumberFormatException nfe) {
                // Do nothing
            }
        }
    }

    public abstract void numberEntered(int num);
}
