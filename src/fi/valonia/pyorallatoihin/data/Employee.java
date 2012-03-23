package fi.valonia.pyorallatoihin.data;

import java.io.Serializable;

public class Employee implements Serializable {

    private int id;
    private String name = "";
    private Sport sport;
    private double distance;
    private final boolean[] days = new boolean[8];

    public Employee() {
        for (int i = 0; i < 8; i++) {
            days[i] = false;
        }
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Sport getSport() {
        return sport;
    }

    public void setSport(Sport sport) {
        this.sport = sport;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public boolean[] getDays() {
        return days;
    }

    public int getTotalMarkers() {
        int total = 0;
        for (int i = 0; i < 8; i++) {
            if (days[i] == true) {
                total++;
            }
        }
        if (total > 5) {
            total = 5;
        }
        return total;
    }
}
