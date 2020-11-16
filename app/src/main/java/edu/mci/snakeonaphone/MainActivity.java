package edu.mci.snakeonaphone;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import edu.mci.rtloop.FixedRateLoopActivity;
import edu.mci.rtloop.IRealTimeLooper;
import edu.mci.rtloop.LoopState;
import edu.mci.snake.Direction;
import edu.mci.snake.GameState;
import edu.mci.snake.views.SnakeView;
import edu.mci.util.AbstractOnSeekBarChangeListener;

public class MainActivity extends FixedRateLoopActivity {

    //region Properties
    int score = 0;
    TextView txtCounter;
    TextView txtFreq;
    Button btnStart;
    Button btnStop;
    SeekBar skbFreq;
    SnakeView snakeView;
    //endregion

    //region Activity life-cycle methods
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        txtCounter = findViewById(R.id.txtCounter);
        txtCounter.setText("");

        txtFreq = findViewById(R.id.txtFreq);

        skbFreq = findViewById(R.id.seekBar);
        skbFreq.setOnSeekBarChangeListener(new AbstractOnSeekBarChangeListener() {
            final double minValue = 1;
            final double maxValue = 50;
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                int freq = (int) (minValue + ((maxValue-minValue)*(progress/100.0)));
                setLoopFrequency(freq);
                setWidgetText(txtFreq, String.format("%d Hz", freq));
            }
        });
        skbFreq.setProgress(5);

        btnStart = findViewById(R.id.btnStart);
        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (getLoopState()) {
                    case CREATED:
                    case STOPPED:
                        loopStart();
                        break;
                    case RUNNING:
                        loopPause();
                        break;
                    case PAUSED:
                        loopResume();
                        break;
                }
            }
        });

        btnStop = findViewById(R.id.btnStop);
        btnStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loopStop();
            }
        });

        snakeView = findViewById(R.id.snakeView);

    }
    //endregion

    //region RTloop definition
    @Override
    protected void loopSetup() {
        score = 0;
        snakeView.setup(20,30,5,true, 10, 20);
        snakeView.start();
    }

    @Override
    protected void loopIteration() {
        score++;
        setWidgetText(txtCounter, String.valueOf(snakeView.getScore()));

        GameState state = snakeView.update();
        if(state == GameState.GameOver) {
            loopStop();
        }
    }

    @Override
    protected void loopTearDown(boolean error) {
        snakeView.stop();
    }
    //endregion

    //region RTloop life-cycle methods
    @Override
    protected void onLoopStart() {
        setWidgetText(btnStart, "Pause");
    }

    @Override
    protected void onLoopPause() {
        setWidgetText(btnStart, "Resume");
    }

    @Override
    protected void onLoopResume() {
        setWidgetText(btnStart, "Pause");
    }

    @Override
    protected void onLoopStop() {
        setWidgetText(btnStart, "Start");
    }
    //endregion
}
