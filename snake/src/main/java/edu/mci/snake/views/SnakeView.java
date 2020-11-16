package edu.mci.snake.views;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;

import java.util.Collection;
import java.util.Iterator;

import edu.mci.snake.Arena;
import edu.mci.snake.Direction;
import edu.mci.snake.GameState;
import edu.mci.snake.Position;
import edu.mci.snake.R;

/**
 * Created by thhausberger on 11.12.2016.
 */
public class SnakeView extends View {

    private final static int BORDER_WIDTH = 4;
    private Arena arena;

    // initialize variables for paint operations
    private final Bitmap bitmapBG;
    private final Paint paintBitmap;
    private final Paint paintBackground;
    private final Paint paintSnakeBody;
    private final Paint paintSnakeHead;
    private final Paint paintFood;
    private final Paint paintRock;
    private final Paint paintBorder;
    private final Paint paintGrid;
    private final RectF rect;
    private float xStep, yStep;

    public SnakeView(Context context, AttributeSet attrs) {
        super(context, attrs);

        paintBitmap = new Paint();
        paintBitmap.setAntiAlias(true);
        paintBitmap.setFilterBitmap(true);
        paintBitmap.setDither(true);

        paintBackground = new Paint();
        paintBackground.setColor(context.getResources().getColor(R.color.colorBackground));

        paintBorder = new Paint();
        paintBorder.setStrokeWidth(BORDER_WIDTH);
        paintBorder.setStyle(Paint.Style.STROKE);
        paintBorder.setColor(context.getResources().getColor(R.color.colorRock));

        paintGrid = new Paint();
        paintGrid.setColor(context.getResources().getColor(R.color.colorGrid));

        paintSnakeBody = new Paint();
        paintSnakeBody.setColor(context.getResources().getColor(R.color.colorSnakeBody));

        paintSnakeHead = new Paint();
        paintSnakeHead.setColor(context.getResources().getColor(R.color.colorSnakeHead));

        paintFood = new Paint();
        paintFood.setColor(context.getResources().getColor(R.color.colorFood));

        paintRock = new Paint();
        paintRock.setColor(context.getResources().getColor(R.color.colorRock));

        int bgPicId = getResourceIdByName(context, "snakebw");
        if(bgPicId != 0) {
            // Disable dpi scaling
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inScaled = false;
            bitmapBG = BitmapFactory.decodeResource(getResources(),bgPicId , null);
        } else {
            bitmapBG = null;
        }

        rect = new RectF();
    }

    private int getResourceIdByName(Context context, String name) {
        int resId = getResources().getIdentifier(name, "drawable", context.getPackageName());
        return resId;
    }

    public void setup(int width, int height, int startLength, boolean wrapAround, int foodInterval, int rockInterval) {
        arena = new Arena(width, height, startLength, wrapAround, foodInterval, rockInterval);
        postInvalidate();
    }

    public void start() {
        arena.start();
    }

    public void stop() {

    }

    public void turnSnake(Direction dir) {
        arena.turnSnake(dir);
    }

    public GameState update() {
        GameState state = arena.update();
        postInvalidate();
        return state;
    }

    public int getScore() {
        return arena.getScore();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (arena.getState() == GameState.Running) {
            Direction dir = directionFromMotionEvent(event);

            if (dir != Direction.Forward) {
                turnSnake(dir);
                return true;
            }
        }
        return false;
    }

    //region Input methods
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (arena.getState() == GameState.Running) {
            Direction dir = directionFromKeyEvent(event);

            if (dir != Direction.Forward) {
                turnSnake(dir);
                return true;
            }
        }
        return false;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawBackground(canvas);

        if (arena != null) {
            // update values
            xStep = (float) canvas.getWidth() / arena.getWidth();
            yStep = (float) canvas.getHeight() / arena.getHeight();

            if(!arena.isWrapAround()) {
                drawBorder(canvas);
            }
            drawGrid(canvas);
            drawSnake(canvas, arena.getSnake().getPositions());
            drawFoods(canvas, arena.getFoods());
            drawRocks(canvas, arena.getRocks());
        }
    }

    private void drawBorder(Canvas canvas) {
        rect.set(BORDER_WIDTH/2, BORDER_WIDTH/2, canvas.getWidth()-BORDER_WIDTH/2, canvas.getHeight()-BORDER_WIDTH/2);
        canvas.drawRect(rect, paintBorder);
    }

    private void drawBackground(final Canvas canvas) {
        rect.set(0, 0, canvas.getWidth(), canvas.getHeight());
        if(bitmapBG != null) {
            canvas.drawBitmap(bitmapBG, null, rect, paintBitmap);
        } else {
            canvas.drawRect(rect, paintBackground);
        }
    }

    private void drawFoods(Canvas canvas, Collection<Position> foods) {
        for (Position p :
                foods) {
            rect.set(p.getX() * xStep, p.getY() * yStep, (p.getX() + 1) * xStep, (p.getY() + 1) * yStep);
            canvas.drawRoundRect(rect, 25f, 25f, paintFood);
        }
    }

    private void drawRocks(Canvas canvas, Collection<Position> rocks) {
        for (Position p :
                rocks) {
            rect.set(p.getX() * xStep, p.getY() * yStep, (p.getX() + 1) * xStep, (p.getY() + 1) * yStep);
            canvas.drawRoundRect(rect, 12f, 12f, paintRock);
        }
    }

    private void drawSnake(final Canvas canvas, final Collection<Position> snake) {
        Iterator<Position> it = snake.iterator();

        Position p;
        // draw head
        if(it.hasNext()) {
            p = it.next();
            rect.set(p.getX() * xStep, p.getY() * yStep, (p.getX() + 1) * xStep, (p.getY() + 1) * yStep);
            canvas.drawRoundRect(rect, 8f, 8f, paintSnakeHead);
        }

        // draw body
        while(it.hasNext()) {
            p = it.next();
            rect.set(p.getX() * xStep, p.getY() * yStep, (p.getX() + 1) * xStep, (p.getY() + 1) * yStep);
            canvas.drawRoundRect(rect, 12f, 12f, paintSnakeBody);
        }
    }

    private void drawGrid(final Canvas canvas) {
        for (int x = 0; x < arena.getWidth(); x++) {
            canvas.drawLine(x * xStep, 0, x * xStep, canvas.getHeight(), paintGrid);
        }
        for (int y = 0; y < arena.getHeight(); y++) {
            canvas.drawLine(0, (y * yStep), canvas.getWidth(), (y * yStep), paintGrid);
        }
    }

    private Direction directionFromMotionEvent(MotionEvent event) {
        Direction dir = Direction.Forward; // neutral direction

        if (event.getAction() == MotionEvent.ACTION_DOWN) { // only when first pressed
            if (event.getX() <= ((getWidth() / 2.0))) { // left side of @SnakeView
                dir = Direction.Left;
            } else { // right side of @SnakeView
                dir = Direction.Right;
            }
        }

        return dir;
    }

    private Direction directionFromKeyEvent(KeyEvent event) {
        Direction dir = Direction.Forward; // neutral direction

        if (event.getAction() == KeyEvent.ACTION_DOWN) { // only when first pressed
            switch (event.getKeyCode()) {
                case KeyEvent.KEYCODE_A:
                    dir = Direction.Left;
                    break;
                case KeyEvent.KEYCODE_D:
                    dir = Direction.Right;
                    break;
            }
        }

        return dir;
    }
}
