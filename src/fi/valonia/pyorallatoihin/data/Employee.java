package fi.valonia.pyorallatoihin.data;

import java.io.Serializable;

public class Employee implements Serializable {
    private static final long serialVersionUID = -1532134864300446357L;
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

    @Override
    public boolean equals(Object other) {
        // check for self-comparison
        if (this == other) {
            return true;
        }
        if (!(other instanceof Employee)) {
            return false;
            // Alternative to the above line :
            // if ( aThat == null || aThat.getClass() != this.getClass() )
            // return false;
        }

        // cast to native object is now safe
        Employee otherEmployee = (Employee) other;

        // now a proper field-by-field evaluation can be made
        return getName().equals(otherEmployee.getName());
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }
}
