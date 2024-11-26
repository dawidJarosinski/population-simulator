package com.example.tolab3.person.state;

import javafx.scene.paint.Color;

public class HealthyState implements State{
    @Override
    public Color getState() {
        return Color.GREEN;
    }
}
