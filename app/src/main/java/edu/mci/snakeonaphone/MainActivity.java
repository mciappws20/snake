package edu.mci.snakeonaphone;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import edu.mci.rtloop.FixedRateLoopActivity;
import edu.mci.rtloop.IRealTimeLooper;
import edu.mci.rtloop.LoopState;
import edu.mci.snake.Direction;
import edu.mci.snake.GameState;
import edu.mci.snake.views.SnakeView;
import edu.mci.util.AbstractOnSeekBarChangeListener;

public class MainActivity extends AppCompatActivity {

    //region Activity life-cycle methods
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
    //endregion
}
