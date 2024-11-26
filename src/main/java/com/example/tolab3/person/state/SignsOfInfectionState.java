package com.example.tolab3.person.state;

import javafx.scene.paint.Color;

public class SignsOfInfectionState implements State{

    @Override
    public Color getState() {
        return Color.RED;
    }
}
