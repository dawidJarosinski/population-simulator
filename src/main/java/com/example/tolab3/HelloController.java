package com.example.tolab3;


import com.example.tolab3.simulation.Simulation;
import com.example.tolab3.simulation.SimulationCareTaker;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;

import javafx.scene.control.*;
public class HelloController {
    @FXML
    private Pane pane;

    @FXML
    private Button startButton, stopButton, stepButton, resetButton, backButton;

    @FXML
    private TextField backField;

    @FXML
    private Label timerLabel, statusLabel;

    private Simulation simulation;
    private Timer timer;
    private SimulationCareTaker simulationCareTaker;

    @FXML
    public void initialize() {
        simulation = new Simulation(pane, 60);
        simulationCareTaker = new SimulationCareTaker(simulation);
        timer = new Timer(simulation, timerLabel, simulationCareTaker);
    }

    @FXML
    private void startSimulation() {
        timer.start();
        statusLabel.setText("Simulation started");
    }

    @FXML
    private void stopSimulation() {
        timer.stop();
        statusLabel.setText("Simulation stopped");
    }

    @FXML
    private void stepSimulation() {
        simulation.simulateOneStep();
        timer.setTime(timer.getTime() + 0.04f);
        statusLabel.setText("Simulation step executed");
    }

    @FXML
    private void resetSimulation() {
        stopSimulation();
        pane.getChildren().clear();
        simulation = new Simulation(pane, 60);
        timer.resetTime();
        simulationCareTaker = new SimulationCareTaker(simulation);
        timer.setSimulation(simulation);
        timer.setSimulationCareTaker(simulationCareTaker);

        timerLabel.setText("0.0");
        statusLabel.setText("Simulation reset");
    }

    @FXML
    private void setBackValue() {
        String value = backField.getText();
        statusLabel.setText("Back value set to: " + value);

        pane.getChildren().clear();
        simulationCareTaker.load(Integer.valueOf(value));
        timer.setTime(Double.parseDouble(value));
    }
}