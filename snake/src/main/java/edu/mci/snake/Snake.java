package edu.mci.snake;

import java.util.Collection;
import java.util.Deque;
import java.util.LinkedList;

/**
 * Created by thhausberger on 11.12.2016.
 */
public class Snake {

    private int length;
    private final Deque<Position> positions;
    private Heading heading = Heading.None;

    public Snake(Position startPosition, int startLength) {
        this.length = startLength;
        this.positions = new LinkedList<>();
        this.positions.add(startPosition);
    }

    public void setHeading(Heading heading) {
        this.heading = heading;
    }

    public void turn(Direction dir) {
        switch (dir) {
            case Left:
                switch (heading) {
                    case North:
                        this.heading = Heading.West;
                        break;
                    case East:
                        this.heading = Heading.North;
                        break;
                    case South:
                        this.heading = Heading.East;
                        break;
                    case West:
                        this.heading = Heading.South;
                        break;
                    default:
                        this.heading = Heading.West;
                        break;
                }
                break;
            case Right:
                switch (heading) {
                    case North:
                        this.heading = Heading.East;
                        break;
                    case East:
                        this.heading = Heading.South;
                        break;
                    case South:
                        this.heading = Heading.West;
                        break;
                    case West:
                        this.heading = Heading.North;
                        break;
                    default:
                        this.heading = Heading.East;
                        break;
                }
                break;
            default:
                // do nothing on forward direction
        }
    }

    public void move() {
        if (heading != Heading.None) {
            Position oldHead = positions.getFirst();
            Position newHead = Position.move(oldHead, heading);
            positions.addFirst(newHead);
            if (positions.size() > length) {
                positions.removeLast();
            }
        }
    }

    public Position getHead() {
        return positions.getFirst();
    }

    public Deque<Position> getPositions() {
        return positions;
    }

    public boolean handleFood(Collection<Position> foods) {
        boolean eaten = false;
        if (foods.contains(getHead())) {
            foods.remove(getHead());
            length++;
            eaten = true;
        }
        return eaten;
    }

    public boolean handleRocks(Collection<Position> rocks) {
        boolean collision = false;
        if (rocks.contains(getHead())) {
            collision = true;
        }
        return collision;
    }

    public boolean handleBody() {
        boolean collision = false;

        // if the same position as the head is present in the list -> collision
        Position head = positions.removeFirst();
        if (positions.contains(head)) {
            collision = true;
        }
        positions.addFirst(head);
        return collision;
    }
}
