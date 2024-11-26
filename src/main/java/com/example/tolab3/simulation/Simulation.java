package com.example.tolab3.simulation;

import com.example.tolab3.person.Person;
import com.example.tolab3.person.state.HealthyState;
import com.example.tolab3.person.state.NoSignsOfInfectionState;
import com.example.tolab3.person.state.SignsOfInfectionState;
import javafx.scene.layout.Pane;

import java.util.ArrayList;
import java.util.List;

public class Simulation {
    private Pane pane;
    private List<Person> population;


    public Simulation(Pane pane, int countOfStartPopulation) {
        this.pane = pane;
        this.population = new ArrayList<>();

        for(int i = 0; i < countOfStartPopulation; i++) {
            spawnNewPerson(false, 1);
        }
    }


    public void simulateOneStep() {
        spawnNewPerson(true, 0.1);
        for(Person person : this.population) {
            person.infectNearbyIndividuals(this.population);
            person.checkIfIsHealed();
            person.move();
        }
    }

    private void spawnNewPerson(boolean borderPosition, double chanceToSpawn) {
        if(Math.random() < chanceToSpawn) {
            if (Math.random() < 0.1f) {
                if (Math.random() < 0.5f) {
                    population.add(new Person(pane, new SignsOfInfectionState(), borderPosition));
                } else {
                    population.add(new Person(pane, new NoSignsOfInfectionState(), borderPosition));
                }
            } else {
                //if(Math.random() < 0.1) {
                //    population.add(new Person(pane, new ResistantState(), borderPosition));
                //} else {
                    population.add(new Person(pane, new HealthyState(), borderPosition));
                //}
            }
        }
    }

    public Momento takeSnapshot() {
        return new Momento(this.population, pane);
    }
    public void restore(Momento momento) {
        this.population = momento.getPopulation();
    }

    public static class Momento {
        private final List<Person.Momento> populationMomento;
        private final Pane pane;

        private Momento(List<Person> population, Pane pane) {
            this.populationMomento = population.stream().map(Person::takeSnapshot).toList();
            this.pane = pane;
        }

        private List<Person> getPopulation() {
            List<Person> personList = new ArrayList<>();
            populationMomento.stream().forEach(personMomento -> {
                Person person = new Person(this.pane);
                person.restore(personMomento);
                personList.add(person);
            });
            return personList;
        }
    }

}
