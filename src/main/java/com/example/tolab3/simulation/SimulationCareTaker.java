package com.example.tolab3.simulation;

import java.util.HashMap;
import java.util.Map;

public class SimulationCareTaker {
    private Simulation simulation;
    private Map<Integer, Simulation.Momento> integerMomentoMap;


    public SimulationCareTaker(Simulation simulation) {
        this.simulation = simulation;
        this.integerMomentoMap = new HashMap<>();
    }

    public void save(Integer second) {
        integerMomentoMap.put(second, simulation.takeSnapshot());
    }
    public void load(Integer second) {
        simulation.restore(integerMomentoMap.get(second));
    }

}
