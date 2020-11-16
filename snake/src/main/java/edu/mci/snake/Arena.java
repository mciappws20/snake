package edu.mci.snake;

import java.util.Collection;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

/**
 * Created by thhausberger on 11.12.2016.
 */
public class Arena {

    private static final int MAX_FOOD = 10;
    private static final int MAX_ROCKS = 20;

    private final int width;
    private final int height;
    private final boolean wrapAround;
    private final int foodInterval;
    private final int rockInterval;
    private final Random rng;

    private long gameSteps;
    private GameState state;
    private int score;

    private Snake snake;
    private Set<Position> foods;
    private Set<Position> rocks;

    public Arena(int width, int height, int startLength, boolean wrapAround, int foodInterval, int rockInterval) {
        this.width = width;
        this.height = height;
        this.wrapAround = wrapAround;
        this.foodInterval = foodInterval;
        this.rockInterval = rockInterval;

        // initialize
        this.rng = new Random(System.currentTimeMillis());
        state = GameState.Created;
        foods = new HashSet<>(MAX_FOOD);
        rocks = new HashSet<>(MAX_ROCKS);
        gameSteps = 0;
        score = 0;
        snake = new Snake(randomRockPosition(), startLength);
    }

    public void start() {
        state = GameState.Running;
    }

    private void gameOver() {
        state = GameState.GameOver;
    }

    public GameState update() {
        snake.move();
        if (wrapAround) {
            handleWrapAround();
        } else {
            handleBorder();
        }
        if (snake.handleBody() || snake.handleRocks(rocks)) {
            gameOver();
        }

        if (snake.handleFood(foods)) {
            score++;
        }

        if (((gameSteps % foodInterval) == 0) && foods.size() < MAX_FOOD) {
            foods.add(randomFoodPosition());
        }
        if (((gameSteps % rockInterval) == 0) && rocks.size() < MAX_ROCKS) {
            rocks.add(randomRockPosition());
        }
        gameSteps++;
        return state;
    }

    private Position randomFoodPosition() {
        Position p;
        do {
            int x = rng.nextInt(width);
            int y = rng.nextInt(height);
            p = Position.of(x, y, Heading.None);
        } while (rocks.contains(p));

        return p;
    }

    private Position randomRockPosition() {
        Position p;
        do {
            int x = rng.nextInt(width);
            int y = rng.nextInt(height);
            p = Position.of(x, y, Heading.None);
        } while (foods.contains(p));

        return p;
    }

    private void handleWrapAround() {
        if (snake.getHead().getX() > width - 1) {
            snake.getHead().setX(0);
        } else if (snake.getHead().getX() < 0) {
            snake.getHead().setX(width - 1);
        } else if (snake.getHead().getY() > height - 1) {
            snake.getHead().setY(0);
        } else if (snake.getHead().getY() < 0) {
            snake.getHead().setY(height - 1);
        }
    }

    private void handleBorder() {
        if ((snake.getHead().getX() > width - 1) || (snake.getHead().getX() < 0) || (snake.getHead().getY() > height - 1) || (snake.getHead().getY() < 0)) {
            gameOver();
        }
    }

    public void turnSnake(Direction dir) {
        snake.turn(dir);
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public boolean isWrapAround() {
        return wrapAround;
    }

    public Snake getSnake() {
        return snake;
    }

    public GameState getState() {
        return state;
    }

    public Collection<Position> getFoods() {
        return foods;
    }

    public Collection<Position> getRocks() {
        return rocks;
    }

    public int getScore() {
        return score;
    }
}
