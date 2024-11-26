package com.example.tolab3.person;

import com.example.tolab3.person.state.*;
import com.example.tolab3.person.vector.Vector;
import com.example.tolab3.person.vector.Vector2D;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

import java.util.*;

public class Person {
    private Pane pane;
    private State state;
    private Vector direction;
    private double velocity;
    private Position position;
    private Circle circle;
    private int infectedTime;
    private Map<Person, Integer> infectedIndividuals;


    public Person(Pane pane) {
        this.circle = new Circle();
        this.position = new Position();
        this.pane = pane;
        pane.getChildren().add(circle);
    }

    public Person(Pane pane, State state, boolean borderPosition) {
        this.pane = pane;
        this.state = state;
        this.direction = new Vector2D(Math.cos(Math.random() * 2f * Math.PI), Math.sin(Math.random() * 2f * Math.PI));
        this.velocity = Math.random() * 2.5f;
        this.circle = new Circle();
        this.infectedTime = 0;

        if(state instanceof SignsOfInfectionState || state instanceof NoSignsOfInfectionState) {
            infectedIndividuals = new HashMap<>();
        }

        updateCircle();
        pane.getChildren().add(circle);

        this.position = new Position();
        if(!borderPosition) {
            setPosition(Math.random() * pane.getPrefWidth(), Math.random() * pane.getPrefHeight());
        } else {
            switch((int)(Math.random() * 4f) + 1) {
                case 1:
                    setPosition(Math.random() * pane.getPrefWidth(), pane.getPrefHeight());
                    break;
                case 2:
                    setPosition(Math.random() * pane.getPrefWidth(), 0);
                    break;
                case 3:
                    setPosition(0, Math.random() * pane.getPrefHeight());
                    break;
                case 4:
                    setPosition(pane.getPrefWidth(), Math.random() * pane.getPrefHeight());
                    break;
            }
        }
    }

    public boolean move() {
        if(this.state instanceof DeadState) {
            return false;
        }

        double newX, newY;

        direction.convertToUnitVector();
        double[] components = direction.getComponents();
        newX = this.position.getX() + (components[0] * velocity * 0.04);
        newY = this.position.getY() + (components[1] * velocity * 0.04);

        if(newX >= this.pane.getPrefWidth() || newX <= 0 || newY >= this.pane.getPrefHeight() || newY <= 0) {
            if(Math.random() < 0.5f) {
                if(newX >= pane.getPrefWidth()) {
                    newX = pane.getPrefWidth() - (newX - pane.getPrefWidth());
                }
                if(newX <= 0) {
                    newX = 0 + (0 - newX);
                }
                if(newY >= pane.getPrefHeight()) {
                    newY = pane.getPrefHeight() - (newY - pane.getPrefHeight());
                }
                if(newY <= 0) {
                    newY = 0 + (0 - newY);
                }
            }
            else {
                updateState(new DeadState());
                return false;
            }
        }
        setPosition(newX, newY);

        this.direction = setDirectionWithDeviation(1.0);
        this.velocity = Math.random() * 2.5f;
        return true;
    }

    public void infectNearbyIndividuals(List<Person> population) {
        if(!(this.state instanceof NoSignsOfInfectionState || this.state instanceof SignsOfInfectionState)) {
            return;
        }

        List<Person> nearbyIndividuals = getNearbyHealthyIndividuals(population);

        for(Person person : nearbyIndividuals) {
            infectedIndividuals.put(person, infectedIndividuals.getOrDefault(person, 0) + 1);
            if(infectedIndividuals.get(person) >= 75) {
                if(this.state instanceof SignsOfInfectionState) {
                    if(Math.random() < 0.5f) {
                        person.updateState(new SignsOfInfectionState());
                    } else {
                        person.updateState(new NoSignsOfInfectionState());
                    }
                }
                else {
                    if(Math.random() < 0.5f) {
                        if(Math.random() < 0.5f) {
                            person.updateState(new SignsOfInfectionState());
                        } else {
                            person.updateState(new NoSignsOfInfectionState());
                        }
                    }
                }
                infectedIndividuals.remove(person);
            }
        }

        Iterator<Person> iterator = infectedIndividuals.keySet().iterator();
        while (iterator.hasNext()) {
            Person person = iterator.next();
            if (!nearbyIndividuals.contains(person)) {
                person.setInfectedTime(0);
                iterator.remove();
            }
        }
    }

    public void checkIfIsHealed() {
        if(this.state instanceof NoSignsOfInfectionState || this.state instanceof SignsOfInfectionState) {
            this.infectedTime++;
            if (this.infectedTime >= 625) {
                updateState(new ResistantState());
            }
        }
    }

    private List<Person> getNearbyHealthyIndividuals(List<Person> population) {
        return population
                .stream()
                .filter(person -> this.position.countDistance(person.getPosition()) <= 2f && person.getState() instanceof HealthyState)
                .toList();
    }

    private void updateState(State state) {
        if(state instanceof SignsOfInfectionState || state instanceof NoSignsOfInfectionState) {
            this.infectedIndividuals = new HashMap<>();
        }
        this.state = state;
        updateCircle();
    }

    private void updateCircle() {
        if(this.state instanceof DeadState) {
            circle.setStroke(null);
            circle.setRadius(0);
        }
        else {
            circle.setRadius(0.25f);
            circle.setStrokeWidth(0.05);
            circle.setStroke(Color.BLACK);
            circle.setFill(this.state.getState());
        }
    }

    private Vector setDirectionWithDeviation(double maxDeviationRadians) {
        double randomDeviation = (Math.random() * 2 - 1) * maxDeviationRadians;
        double newAngle = this.direction.getAngle() + randomDeviation;

        return new Vector2D(Math.cos(newAngle), Math.sin(newAngle));
    }

    private void setPosition(double x, double y) {
        this.position.setX(x);
        this.position.setY(y);
        this.circle.setTranslateX(this.position.getX());
        this.circle.setTranslateY(this.position.getY());
    }

    public Pane getPane() {
        return pane;
    }

    public void setPane(Pane pane) {
        this.pane = pane;
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

    public Vector getDirection() {
        return direction;
    }

    public void setDirection(Vector direction) {
        this.direction = direction;
    }

    public Double getVelocity() {
        return velocity;
    }

    public void setVelocity(Double velocity) {
        this.velocity = velocity;
    }

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    public Circle getCircle() {
        return circle;
    }

    public void setCircle(Circle circle) {
        this.circle = circle;
    }

    public int getInfectedTime() {
        return infectedTime;
    }

    public void setInfectedTime(int infectedTime) {
        this.infectedTime = infectedTime;
    }

    public Map<Person, Integer> getInfectedIndividuals() {
        return infectedIndividuals;
    }

    public void setInfectedIndividuals(Map<Person, Integer> infectedIndividuals) {
        this.infectedIndividuals = infectedIndividuals;
    }

    public Momento takeSnapshot() {
        return new Momento(
                this.state,
                this.direction,
                this.velocity,
                this.position.getX(),
                this.position.getY(),
                this.infectedTime,
                this.infectedIndividuals);
    }

    public void restore(Momento momento) {
        updateState(momento.getState());
        this.direction = momento.getDirection();
        this.velocity = momento.getVelocity();
        this.setPosition(momento.getPositionX(), momento.getPositionY());
        this.infectedTime = momento.infectedTime;
        this.infectedIndividuals = momento.infectedIndividuals;
    }

    public static class Momento {
        private final State state;
        private final Vector direction;
        private final double velocity;
        private final double positionX;
        private final double positionY;
        private final int infectedTime;
        private final Map<Person, Integer> infectedIndividuals;

        private Momento(State state, Vector direction, double velocity, double positionX, double positionY, int infectedTime, Map<Person, Integer> infectedIndividuals) {
            this.state = state;
            this.direction = direction;
            this.velocity = velocity;
            this.positionX = positionX;
            this.positionY = positionY;
            this.infectedTime = infectedTime;
            this.infectedIndividuals = infectedIndividuals;
        }

        private State getState() {
            return state;
        }

        private Vector getDirection() {
            return direction;
        }

        private double getVelocity() {
            return velocity;
        }

        private double getPositionX() {
            return positionX;
        }

        private double getPositionY() {
            return positionY;
        }

        private int getInfectedTime() {
            return infectedTime;
        }

        private Map<Person, Integer> getInfectedIndividuals() {
            return infectedIndividuals;
        }
    }
}
