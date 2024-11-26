package com.example.tolab3.person.vector;

public interface Vector {
    double abs();
    double cdot(Vector vector);
    double getAngle();
    double[] getComponents();
    void convertToUnitVector();
}
