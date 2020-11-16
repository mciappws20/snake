package edu.mci.snake;

/**
 * Created by thhausberger on 11.12.2016.
 */
public class Position {

    private int x;
    private int y;
    private Heading heading;

    private Position(final int x, final int y, final Heading heading) {
        this.x = x;
        this.y = y;
        this.heading = heading;
    }

    public static Position move(Position p, Heading heading) {
        int x = p.x;
        int y = p.y;
        switch (heading) {
            case North:
                y--;
                break;
            case East:
                x++;
                break;
            case South:
                y++;
                break;
            case West:
                x--;
                break;
            default:
                // do nothing
        }
        return Position.of(x, y, heading);
    }

    public static Position of(final int x, final int y, final Heading dir) {
        return new Position(x, y, dir);
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    @Override
    public int hashCode() {
        return String.format("%d,%d", x, y).hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return !(obj == null || !(obj instanceof Position)) && this.hashCode() == obj.hashCode();
    }
}
