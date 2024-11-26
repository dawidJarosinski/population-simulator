package com.example.tolab3.person;

public class Position {
    private double x;
    private double y;

    public Position() {
    }

    public double countDistance(Position position) {
        return Math.abs(
                Math.sqrt((position.getX() - this.x) * (position.getX() - this.x) + (position.getY() - this.y) * (position.getY() - this.y)));
    }


    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }
}
