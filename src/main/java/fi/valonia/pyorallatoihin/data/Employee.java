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
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		long temp;
		temp = Double.doubleToLongBits(distance);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((sport == null) ? 0 : sport.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Employee other = (Employee) obj;
		if (Double.doubleToLongBits(distance) != Double
				.doubleToLongBits(other.distance))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (sport != other.sport)
			return false;
		return true;
	}
}
