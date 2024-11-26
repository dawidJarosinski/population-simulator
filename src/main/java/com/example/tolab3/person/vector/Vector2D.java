package com.example.tolab3.person.vector;

public class Vector2D implements Vector {
    private double x;
    private double y;

    public Vector2D(double x, double y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public double abs() {
        return Math.sqrt(this.x * this.x + this.y * this.y);
    }

    @Override
    public double cdot(Vector vector) {
        return this.x * vector.getComponents()[0] + this.y * vector.getComponents()[1];
    }

    @Override
    public double getAngle() {
        return Math.atan2(this.getComponents()[1], this.getComponents()[0]);
    }

    @Override
    public double[] getComponents() {
        return new double[]{this.x, this.y};
    }

    @Override
    public void convertToUnitVector() {
        double abs = abs();
        this.x = x / abs;
        this.y = y / abs;
    }
}
