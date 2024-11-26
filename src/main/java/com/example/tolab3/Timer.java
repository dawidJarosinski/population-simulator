package com.example.tolab3;

import com.example.tolab3.simulation.Simulation;
import com.example.tolab3.simulation.SimulationCareTaker;
import javafx.animation.AnimationTimer;
import javafx.scene.control.Label;

public class Timer extends AnimationTimer {
    private final long  fps = 25;
    private final long interval = 1000000000 / fps;
    private long last = 0;
    private double time = 0.0;
    private int tick = 0;
    private Simulation simulation;
    private Label timerLabel;
    private SimulationCareTaker simulationCareTaker;

    public Timer(Simulation simulation, Label timerLabel, SimulationCareTaker simulationCareTaker) {
        this.simulation = simulation;
        this.timerLabel = timerLabel;
        this.simulationCareTaker =simulationCareTaker;
    }

    @Override
    public void handle(long now) {
        if(now - last > interval) {
            this.simulation.simulateOneStep();
            last = now;
            time += 1.0 / fps;
            tick++;
            timerLabel.setText(String.format("%.2f", time));
            if(tick % 25 == 0) {
                simulationCareTaker.save(tick/25);
            }
        }
    }
    public void resetTime(){
        time = 0.0;
        tick = 0;
        timerLabel.setText(String.format("%.2f", time));
    }

    public double getTime() {
        return time;
    }

    public void setTime(double time){
        this.time = time;
    }

    public void setSimulation(Simulation simulation) {
        this.simulation = simulation;
    }

    public void setSimulationCareTaker(SimulationCareTaker simulationCareTaker) {
        this.simulationCareTaker = simulationCareTaker;
    }
}
