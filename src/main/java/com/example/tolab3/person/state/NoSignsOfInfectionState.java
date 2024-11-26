package com.example.tolab3.person.state;

import javafx.scene.paint.Color;

public class NoSignsOfInfectionState implements State{
    @Override
    public Color getState() {
        return Color.ORANGE;
    }
}
