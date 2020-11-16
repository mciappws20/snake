package edu.mci.rtloop;

/**
 * Created by thhausberger on 11.12.2016.
 */
public interface IRealTimeLooper {
    void setup() throws InterruptedException;
    void loop() throws InterruptedException;
    void tearDown(final boolean error);
}
